import { defineStore } from "pinia";
import { AuthResponse, User } from "../utils/types";
import { logout, saveToken } from "../utils/auth.utils";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";

const NEW_uSER_TIME = 1000 * 60 * 1;

export const useUserStore = defineStore("auth", {
  state: () => ({
    logged: false,
    self: {} as User,
    user: {} as User,
    users: [] as Array<User>,
    newUser: false,
  }),
  actions: {
    async refreshSelf() {
      const apiCall = useApiCall();
      // const response = (await apiCall.get(EndpointType.USER_GET_BY_ID, {
      //   params: { user_id: this.self.user_id },
      // })) as User;
      const response = (await apiCall.get(
        EndpointType.USER_REFRESH
      )) as AuthResponse;
      if (response.user.user_id == this.self.user_id) {
        this.self = response.user;
        localStorage.setItem("user", JSON.stringify(response));
        saveToken(response.token);
        return this.self;
      }
    },
    setSelf(user: User) {
      this.logged = true;
      this.self = user;
    },
    setUser(user: User) {
      this.user = user;
    },
    setUsers(users: Array<User>) {
      this.users = users;
    },
    getDesignatedTask() {
      if (this.logged) return this.self.designated_tasks;
      else return [];
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
