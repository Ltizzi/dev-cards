import { defineStore } from "pinia";
import {
  AuthRequest,
  AuthResponse,
  JSONWorkspace,
  User,
  UserLocal,
  UserWorkspaceRoles,
  Workspace,
} from "../utils/types";
import { logout, saveToken } from "../utils/auth.utils";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { AxiosRequestConfig } from "axios";
import { useUIStore } from "./ui.store";
import { useProjectStore } from "./project.store";

const NEW_uSER_TIME = 1000 * 60 * 1;

const apiCall = useApiCall();

export const useUserStore = defineStore("auth", {
  state: () => ({
    logged: false,
    offlineMode: false,
    offlineSelf: {},
    self: {} as User,
    local: {} as UserLocal,
    localUsers: [] as UserLocal[],
    user: {} as User,
    users: [] as Array<User>,
    newUser: false,
    localRoles: [] as UserWorkspaceRoles[],
    savedRoles: false,
  }),
  actions: {
    setOfflineMode(onoff: boolean) {
      this.offlineMode = onoff;
    },
    checkOfflineMode() {
      const UIStore = useUIStore();
      this.offlineMode = UIStore.getOfflineMode();
      return this.offlineMode;
    },
    async login(loginReq: AuthRequest) {
      const response = (await apiCall.post(
        EndpointType.USER_LOGIN,
        loginReq
      )) as AuthResponse;
      this.offlineMode = false;
      saveToken(response.token);
      this.setSelf(response.user);
      return response;
    },
    async refreshSelf() {
      if (this.offlineMode) return this.getSelf();
      const response = (await apiCall.get(
        EndpointType.USER_REFRESH
      )) as AuthResponse;
      if (response.user.user_id == this.self.user_id) {
        this.setSelf(response.user);
        saveToken(response.token);
        return this.self;
      }
    },
    setSelf(user: User) {
      localStorage.setItem("user", JSON.stringify(user));
      this.logged = true;
      this.self = user;
    },
    setLocalUser(user: UserLocal) {
      this.local = user;
      this.logged = true;
      this.offlineMode = true;
    },
    saveLocal() {
      localStorage.setItem("localUser", JSON.stringify(this.local));
      let localUsers = this.getLocalUsers() || [];
      if (
        localUsers &&
        !localUsers.find((u: UserLocal) => u.nickname == this.local.nickname)
      ) {
        localUsers.push(this.local);
        localStorage.setItem("localUsers", JSON.stringify(localUsers));
      }
    },
    updatedToken(user: User) {
      this.setSelf(user);
      localStorage.setItem("user", JSON.stringify(user));
    },
    getSelf() {
      this.offlineMode = this.checkOfflineMode();
      if (this.offlineMode) {
        this.offlineSelf = JSON.parse(
          localStorage.getItem("localUser") as string
        ) as UserLocal;
        return this.offlineSelf;
      } else {
        if (!this.self.user_id && localStorage.getItem("user")) {
          return JSON.parse(localStorage.getItem("user") as string);
        }
        return this.self;
      }
    },
    setRoles(uwrs: UserWorkspaceRoles[]) {
      this.localRoles = uwrs;
      this.savedRoles = true;
      this.saveLocalRoles();
    },
    getLocalRoles() {
      if (localStorage.getItem("localRoles")) {
        this.localRoles = JSON.parse(
          localStorage.getItem("localRoles") as string
        );
      }
      return this.localRoles;
    },
    saveLocalRoles() {
      localStorage.setItem("localRoles", JSON.stringify(this.localRoles));
    },

    async registerUser(user: any) {
      return await apiCall.post(EndpointType.USER_REGISTER, user);
    },
    async completeUserProfile(user: User, options: AxiosRequestConfig) {
      return await apiCall.patch(EndpointType.USER_UPDATE, user, options);
    },
    async updateUser(user: User) {
      return await apiCall.patch(EndpointType.USER_UPDATE, user, {
        params: { user_id: user.user_id },
      });
    },

    async fetchUserById(id: number, ws_id: number) {
      const user = this.users.filter((u: User) => u.user_id === id);
      if (user.length > 0) return user[0];
      else {
        const response = (await apiCall.get(EndpointType.USER_GET_BY_ID, {
          params: { user_id: id, ws_id: ws_id },
        })) as User;
        this.users.push(response);
        return response;
      }
    },
    async fetchAllWorkspacesMember(id: number) {
      this.offlineMode = this.checkOfflineMode();
      const projectStore = useProjectStore();

      if (this.offlineMode) {
        const saved_jws = projectStore.getLocalStorageWorkspaces() || [];

        return saved_jws.length > 0
          ? saved_jws.map((jws: JSONWorkspace) => jws.workspace)
          : [];
      } else {
        const workspaces = (await apiCall.get(EndpointType.USER_MEMBER, {
          params: { user_id: id },
        })) as Workspace[];
        projectStore.setMemberOf(workspaces);
        return workspaces;
      }
    },
    async checkUserExists(email: string) {
      return await apiCall.get(EndpointType.USER_CHECK, {
        params: { email: email },
      });
    },
    setUser(user: User) {
      this.user = user;
    },
    setUsers(users: Array<User>) {
      this.users = users;
    },
    getDesignatedTask() {
      if (this.logged) return this.self.designated_tasks;
      else {
        if (localStorage.getItem("user")) {
          const user = JSON.parse(
            localStorage.getItem("user") as string
          ) as User;
          return user.designated_tasks;
        } else return [];
      }
    },
    logoutUser() {
      if (this.offlineMode) {
        this.local = {} as UserLocal;
        this.offlineMode = false;
      } else this.self = {} as User;
      const projectStore = useProjectStore();
      projectStore.clean();
      this.logged = false;
      logout();
    },
    flagAsNewUser() {
      this.newUser = true;
      const now = Date.now();
      localStorage.setItem("registerIn", JSON.stringify(now));
      setTimeout(() => {
        this.newUser = false;
      }, NEW_uSER_TIME);
    },
    checkIfUserIsNew() {
      if (localStorage.getItem("registerIn")) {
        const registerTime = JSON.parse(
          localStorage.getItem("registerIn") as string
        );
        return Date.now() - registerTime < NEW_uSER_TIME;
      } else {
        const created_at = new Date(this.self.created_at).getTime();
        return Date.now() - created_at < NEW_uSER_TIME;
      }
    },
    getLocalUsers() {
      this.localUsers = JSON.parse(
        localStorage.getItem("localUsers") as string
      );
      return this.localUsers;
    },
    getLocalUser() {
      return JSON.parse(localStorage.getItem("localUser") as string);
    },
  },
});
