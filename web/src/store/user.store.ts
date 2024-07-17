import { defineStore } from "pinia";
import { User } from "../utils/types";
import { logout } from "../utils/auth.utils";

export const useUserStore = defineStore("auth", {
  state: () => ({
    logged: false,
    self: {} as User,
    user: {} as User,
    users: [] as Array<User>,
  }),
  actions: {
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
  },
});
