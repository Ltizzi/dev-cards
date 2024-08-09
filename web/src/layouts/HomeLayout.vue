<template lang="">
  <div class="flex flex-col justify-between w-full" v-if="isLoaded">
    <div class="flex flex-row justify-between max-w-full ml-24 pt-10">
      <div class="w-full">
        <h1 class="text-2xl">Home page</h1>
      </div>

      <div class="flex flex-row justify-end gap-5 w-4/5 mr-10">
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
              <div :data-theme="theme" class="flex flex-row w-full">
                <input
                  type="radio"
                  name="theme-dropdown"
                  class="theme-controller btn btn-sm btn-block justify-center active:text-primary-content"
                  :aria-label="themes_name[index]"
                  :value="theme"
                  v-model="selected_theme"
                />
                <div class="flex flex-row gap-0 w-1/3">
                  <div class="h-auto w-5 bg-primary" :data-theme="theme"></div>

                  <div
                    class="h-auto w-5 bg-secondary"
                    :data-theme="theme"
                  ></div>
                  <div class="h-auto w-5 bg-accent" :data-theme="theme"></div>
                  <div class="h-auto w-5 bg-base-100" :data-theme="theme"></div>
                  <div class="h-auto w-5 bg-neutral" :data-theme="theme"></div>
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
  </div>
</template>
<script setup lang="ts">
  import { useRouter } from "vue-router";
  import { onBeforeMount, onMounted, ref, watch } from "vue";
  import { useUserStore } from "../store/user.store";

  const router = useRouter();

  const userStore = useUserStore();

  const isLoggedIn = ref(false);
  const isLoaded = ref(false);

  const selected_theme = ref<string>();

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
  ];

  const themes_name = [
    "Dracula",
    "Light",
    "Dark",
    "Cupcake",
    "Retro",
    "Emerald",
    "Synthwave",
    "Valentine",
    "Lofi",
    "Fantasy",
    "Autumn",
    "Acid",
    "Night",
    "Nord",
  ];

  watch(
    () => selected_theme.value,
    (newValue, oldValue) => {
      changeTheme(newValue as string);
    }
  );

  function changeTheme(theme: string) {
    const htmlElement = document.documentElement;
    htmlElement.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }

  function login() {
    router.push("/login");
  }

  function signoff() {
    userStore.logoutUser();
    isLoggedIn.value = false;
  }

  function createProject() {
    router.push("/newproject");
  }

  onBeforeMount(() => {
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    } else {
      if (localStorage.getItem("theme")) {
        changeTheme(localStorage.getItem("theme") as string);
      }
      isLoggedIn.value = true;
      isLoaded.value = true;
    }
  });
</script>
