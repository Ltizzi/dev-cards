import { defineStore } from "pinia";
import { checkThemeIsDark, isDarkerCardsActive } from "../utils/client.utils";
import { CustomConfiguration, UserLocal } from "../utils/types";
import { useUserStore } from "./user.store";

export const useUIStore = defineStore("uiStore", {
  state: () => ({
    darkTheme: true,
    theme: "dracula",
    darkerCard: false,
    darkerMiniCard: false,
    justUpdated: false,
    isMobile: false,
    offlineMode: false,
  }),
  actions: {
    setTheme(theme: string) {
      this.theme = theme;
      const htmlElement = document.documentElement;
      htmlElement.setAttribute("data-theme", theme);
      localStorage.setItem("theme", theme);
      localStorage.setItem("darkTheme", checkThemeIsDark(theme).toString());
      this.darkTheme = checkThemeIsDark(theme);
      this.darkerCard = isDarkerCardsActive();
      this.setJustUpdate();
      return this.theme;
    },
    getTHeme() {
      if (this.theme.length > 0) {
        const theme = localStorage.getItem("theme")
          ? localStorage.getItem("theme")
          : "dracula";
        this.setTheme(theme as string);
        return theme;
      } else return this.theme;
    },
    checkIsDarkTheme() {
      return this.darkTheme;
    },
    setDarkerCards(value: boolean) {
      this.darkerCard = value;
      localStorage.setItem("darkerCards", value.toString());
      this.setJustUpdate();
      return this.darkerCard;
    },
    setDarkerMiniCards(value: boolean) {
      this.darkerMiniCard = value;
      localStorage.setItem("darkerMiniCards", value.toString());
      this.setJustUpdate();
      return this.darkerMiniCard;
    },
    setJustUpdate() {
      this.justUpdated = true;
      setTimeout(() => {
        this.justUpdated = false;
      }, 10);
    },
    setIsMobile(value: boolean) {
      this.isMobile = window.innerWidth < 1024;
    },
    getOfflineMode() {
      const userStore = useUserStore();
      const user = userStore.getLocalUser() as UserLocal;
      const isLocal = this.checkOfflineMode();
      if (!this.offlineMode && user && isLocal) {
        this.offlineMode = isLocal;
        userStore.setLocalUser(user);
      }
      return this.offlineMode;
    },
    setOfflineMode(condition: boolean) {
      this.offlineMode = condition;
      localStorage.setItem(
        "offlineMode",
        JSON.stringify({ active: condition })
      );
    },
    checkOfflineMode() {
      return JSON.parse(localStorage.getItem("offlineMode") as string).active;
    },
  },
});
