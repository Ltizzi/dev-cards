import { defineStore } from "pinia";
import { User } from "../utils/types";
import { logout } from "../utils/auth.utils";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";

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
      const response = (await apiCall.get(EndpointType.USER_GET_BY_ID, {
        params: { user_id: this.self.user_id },
      })) as User;
      if (response.user_id == this.self.user_id) {
        this.self = response;
        localStorage.setItem("user", JSON.stringify(response));
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
    logoutUser() {
      this.self = {} as User;
      this.logged = false;
      logout();
    },
    flagAsNewUser() {
      this.newUser = true;
      setTimeout(() => {
        this.newUser = false;
      }, 1000 * 60 * 10);
    },
  },
});
