import { defineStore } from "pinia";
import { User } from "../utils/types";

export const useUserStore = defineStore("auth", {
  state: () => ({
    self: {} as User,
    user: {} as User,
    users: [] as Array<User>,
  }),
  actions: {
    setSelf(user: User) {
      this.self = user;
    },
    setUser(user: User) {
      this.user = user;
    },
    setUsers(users: Array<User>) {
      this.users = users;
    },
  },
});
