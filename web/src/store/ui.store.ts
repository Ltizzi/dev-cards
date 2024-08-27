import { defineStore } from "pinia";
import { checkThemeIsDark, isDarkerCardsActive } from "../utils/client.utils";

export const useUIStore = defineStore("uiStore", {
  state: () => ({
    darkTheme: true,
    theme: "dracula",
    darkerCard: false,
    justUpdated: false,
  }),
  actions: {
    setTheme(theme: string) {
      this.theme = theme;
      const htmlElement = document.documentElement;
      htmlElement.setAttribute("data-theme", theme);
      localStorage.setItem("theme", theme);
      localStorage.setItem("darkTheme", checkThemeIsDark().toString());
      this.darkTheme = checkThemeIsDark();
      this.darkerCard = isDarkerCardsActive();
      this.setJustUpdate();
      return this.theme;
    },
    setDarkerCards(value: boolean) {
      this.darkerCard = value;
      localStorage.setItem("darkerCards", value.toString());
      this.setJustUpdate();
      return this.darkerCard;
    },

    setJustUpdate() {
      this.justUpdated = true;
      setTimeout(() => {
        this.justUpdated = false;
      }, 1000);
    },
  },
});
