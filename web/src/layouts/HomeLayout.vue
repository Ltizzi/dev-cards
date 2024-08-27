<template lang="">
  <div class="flex flex-col justify-center w-full" v-if="isLoaded">
    <div class="flex flex-row justify-between max-w-full ml-24 pt-10 max-h-40">
      <div class="w-full">
        <h1 class="text-2xl">Home page</h1>
      </div>

      <div
        class="flex flex-row justify-end gap-5 w-4/5 mr-10 max-h-12 my-auto align-middle"
      >
        <div class="form-control w-40 mt-2" v-if="isDark">
          <label class="label cursor-pointer">
            <span class="label-text">Darker Cards</span>
            <input
              type="checkbox"
              class="toggle toggle-primary"
              :checked="darkerCards"
              v-model="darkerCards"
            />
          </label>
        </div>
        <div class="dropdown mb-72">
          <div tabindex="0" role="button" class="btn m-1">
            Theme
            <svg
              width="12px"
              height="12px"
              class="inline-block h-2 w-2 fill-current opacity-60"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 2048 2048"
            >
              <path
                d="M1799 349l242 241-1017 1017L7 590l242-241 775 775 775-775z"
              ></path>
            </svg>
          </div>
          <ul
            tabindex="0"
            class="dropdown-content dropdown-right bg-base-300 rounded-box z-[1] w-52 p-2 shadow-2xl"
          >
            <li
              v-for="(theme, index) in themes"
              class="flex flex-row gap-5 bg-base-200"
            >
              <div
                :data-theme="theme"
                class="flex flex-row justify-center w-full bg-base-300"
              >
                <input
                  type="radio"
                  name="theme-dropdown"
                  class="theme-controller btn btn-sm btn-block justify-start active:text-primary-content relative"
                  :aria-label="capitalizeString(theme)"
                  :value="theme"
                  v-model="selected_theme"
                />
                <div class="flex flex-row gap-0.5 absolute right-3 mt-2">
                  <div
                    class="size-4 rounded-full bg-primary"
                    :data-theme="theme"
                  ></div>

                  <div
                    class="size-4 rounded-full bg-secondary"
                    :data-theme="theme"
                  ></div>
                  <div
                    class="size-4 rounded-full bg-accent"
                    :data-theme="theme"
                  ></div>
                  <div
                    class="size-4 rounded-full bg-base-100"
                    :data-theme="theme"
                  ></div>
                  <div
                    class="size-4 rounded-full bg-neutral"
                    :data-theme="theme"
                  ></div>
                </div>
              </div>
            </li>
          </ul>
        </div>

        <button
          class="btn btn-outline btn-secondary"
          @click="createProject"
          v-if="isLoggedIn"
        >
          Create Project
        </button>

        <button
          class="btn btn-outline btn-success"
          @click="login"
          v-if="!isLoggedIn"
        >
          Log in
        </button>
        <button
          class="btn btn-outline btn-error"
          @click="signoff"
          v-if="isLoggedIn"
        >
          Logout
        </button>
      </div>
    </div>
    <DesignatedView
      class="ml-10"
      :isDark="isDark"
      :darkerCards="darkerCards"
      :isLoggedIn="isLoggedIn"
    />
  </div>
</template>
<script setup lang="ts">
  import { useRouter } from "vue-router";
  import { onBeforeMount, onMounted, ref, watch } from "vue";
  import { useUserStore } from "../store/user.store";
  import DesignatedView from "./DesignatedView.vue";
  import { isDarkerCardsActive } from "../utils/client.utils";
  import { useUIStore } from "../store/ui.store";

  const router = useRouter();

  const userStore = useUserStore();
  const UIStore = useUIStore();

  const isLoggedIn = ref(false);
  const isLoaded = ref(false);

  const loadedDesignated = ref(false);

  const selected_theme = ref<string>();

  const isDark = ref<boolean>();

  const darkerCards = ref<boolean>();

  const themes = [
    "dracula",
    "light",
    "dark",
    "cupcake",
    "retro",
    "emerald",
    "synthwave",
    "valentine",
    "lofi",
    "fantasy",
    "autumn",
    "acid",
    "night",
    "nord",
    "dim",
    "forest",
    "aqua",
    "winter",
    "lemonade",
    "pastel",
    "garden",
    "corporate",
    "wireframe",
  ];

  const darkThemes = [
    "dracula",
    "dark",
    "synthwave",
    "night",
    "dim",
    "forest",
    "aqua",
  ];

  watch(
    () => selected_theme.value,
    (newValue, oldValue) => {
      changeTheme(newValue as string);
    }
  );

  watch(
    () => darkerCards.value,
    (newValue, oldValue) => {
      const falsie = false;
      if (newValue != oldValue) {
        darkerCards.value = UIStore.setDarkerCards(newValue as boolean);
        //localStorage.setItem("darkerCards", newValue.toString());
      } //else localStorage.setItem("darkerCards", falsie.toString());
    }
  );

  function changeTheme(theme: string) {
    UIStore.setTheme(theme);
    isDark.value = darkThemes.includes(theme as string);
  }

  function isDarkTheme(theme: string): String {
    return darkThemes.includes(theme).toString();
  }

  function capitalizeString(str: string) {
    return str[0].toUpperCase() + str.slice(1);
  }

  function login() {
    router.push("/login");
  }

  function signoff() {
    console.log("logout!");
    isLoggedIn.value = false;
    userStore.logoutUser();
  }

  function createProject() {
    router.push("/newproject");
  }

  function setLoadedDesignated() {
    loadedDesignated.value = true;
  }

  onBeforeMount(() => {
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    } else {
      if (UIStore.theme || localStorage.getItem("theme")) {
        changeTheme(localStorage.getItem("theme") as string);
      } else {
        changeTheme("dracula");
      }
      isLoggedIn.value = true;
      isLoaded.value = true;
    }
    darkerCards.value = isDarkerCardsActive();
  });
</script>
