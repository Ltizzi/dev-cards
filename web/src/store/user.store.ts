import { defineStore } from "pinia";
import { AuthRequest, AuthResponse, User } from "../utils/types";
import { logout, saveToken } from "../utils/auth.utils";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { AxiosRequestConfig } from "axios";

const NEW_uSER_TIME = 1000 * 60 * 1;

const apiCall = useApiCall();

export const useUserStore = defineStore("auth", {
  state: () => ({
    logged: false,
    offlineMode: false,
    offlineSelf: {},
    self: {} as User,
    user: {} as User,
    users: [] as Array<User>,
    newUser: false,
  }),
  actions: {
    setOfflineMode(onoff: boolean) {
      this.offlineMode = onoff;
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
      const apiCall = useApiCall();
      // const response = (await apiCall.get(EndpointType.USER_GET_BY_ID, {
      //   params: { user_id: this.self.user_id },
      // })) as User;
      const response = (await apiCall.get(
        EndpointType.USER_REFRESH
      )) as AuthResponse;
      if (response.user.user_id == this.self.user_id) {
        // this.self = response.user;
        // localStorage.setItem("user", JSON.stringify(response));
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
    updatedToken(user: User) {
      this.setSelf(user);
      localStorage.setItem("user", JSON.stringify(user));
    },
    getSelf() {
      if (this.offlineMode) return this.offlineSelf;
      else {
        if (!this.self.user_id && localStorage.getItem("user")) {
          return JSON.parse(localStorage.getItem("user") as string);
        }
        return this.self;
      }
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
      return await apiCall.get(EndpointType.USER_MEMBER, {
        params: { user_id: id },
      });
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
      this.self = {} as User;
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
  },
});
