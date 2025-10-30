<template lang="">
  <div>
    <div class="dropdown" v-if="isDropdown">
      <div
        tabindex="0"
        role="button"
        class="btn m-0.5 lg:m-1 btn-sm lg:btn-base text-xs lg:text-base"
      >
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
        class="dropdown-content dropdown-right bg-base-300 rounded-box z-[100] w-52 p-2 shadow-2xl"
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
              class="theme-controller btn btn-xs lg:btn-sm btn-block justify-start active:text-primary-content relative"
              :aria-label="capitalizeString(theme)"
              :value="theme"
              v-model="selected_theme"
            />
            <div class="flex flex-row gap-0.5 absolute right-3 mt-2">
              <div
                class="size-2 lg:size-4 rounded-full bg-primary"
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
    <div v-else>
      <ul class="flex flex-row flex-wrap gap-2 lg:gap-5 w-full">
        <li
          v-for="(theme, index) in themes"
          class="w-5/6 lg:w-60 hover:cursor-pointer"
          @click="selectTheme(index)"
        >
          <div
            :data-theme="theme"
            class="flex flex-row gap-2 justify-evenly bg-base-100 my-auto align-middle py-0.5 rounded-xl"
          >
            <p class="text-sm lg:text-base">{{ capitalizeString(theme) }}</p>
            <div class="flex flex-row gap-0.5 mt-2">
              <div
                class="size-3 lg:size-4 rounded-full bg-primary"
                :data-theme="theme"
              ></div>

              <div
                class="size-3 lg:size-4 rounded-full bg-secondary"
                :data-theme="theme"
              ></div>
              <div
                class="size-3 lg:size-4 rounded-full bg-accent"
                :data-theme="theme"
              ></div>
              <div
                class="size-3 lg:size-4 rounded-full bg-base-100"
                :data-theme="theme"
              ></div>
              <div
                class="size-3 lg:size-4 rounded-full bg-neutral"
                :data-theme="theme"
              ></div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { watch, ref, onBeforeMount } from "vue";
  import { useUIStore } from "../../store/ui.store";

  const props = defineProps<{ dropdown: boolean }>();

  const UIStore = useUIStore();

  const selected_theme = ref<string>();

  const isDropdown = ref<boolean>();

  //const isDark = ref<boolean>();

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

  function selectTheme(id: number) {
    changeTheme(themes[id]);
  }

  function changeTheme(theme: string) {
    UIStore.setTheme(theme);
    //isDark.value = darkThemes.includes(theme as string);
  }

  function capitalizeString(str: string) {
    return str[0].toUpperCase() + str.slice(1);
  }

  onBeforeMount(() => {
    if (UIStore.theme || localStorage.getItem("theme")) {
      changeTheme(localStorage.getItem("theme") as string);
    } else {
      changeTheme("dracula");
    }
    isDropdown.value = props.dropdown;
  });
</script>
<style lang=""></style>
