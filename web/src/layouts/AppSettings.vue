<template lang="">
  <div class="h-screen w-5/6 mx-auto">
    <h1 class="pt-5 text-3xl text-center">Project Settings</h1>
    <div
      class="flex flex-row border-2 border-opacity-60 border-secondary bg-gradient-to-br from-base-200 to-transparent h-5/6 my-4"
    >
      <SettingsLateralMenu
        class="w-1/6"
        @menuOption="changeMenu"
        :options="state.options"
        :title="'App Settings'"
      />
      <div
        class="divider lg:divider-horizontal divider-secondary opacity-60"
      ></div>

      <div class="w-5/6 h-full overflow-y-hidden">
        <ThemeControl v-show="state.selected == 'theme_setup'" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useUIStore } from "../store/ui.store";
  import { useUserStore } from "../store/user.store";
  import { reactive } from "vue";
  import { MenuOptionUI } from "../utils/types";
  import ThemeControl from "../components/settings/ThemeControl.vue";
  import SettingsLateralMenu from "../components/settings/SettingsLateralMenu.vue";

  const UIStore = useUIStore();
  const userStore = useUserStore();

  const state = reactive({
    selected: "theme_setup",
    options: [
      {
        title: "User Profile",
        path: "user_profile",
        needOwner: false,
      },
      {
        title: "Theme Settings",
        path: "theme_setup",
        needOwner: false,
      },
    ],
  });

  function changeMenu(option: MenuOptionUI) {
    state.selected = option.path;
  }
</script>
