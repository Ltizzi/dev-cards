<template lang="">
  <div class="h-screen lg:w-5/6 lg:mx-auto mx-5">
    <h1
      class="pt-5 text-3xl text-center motion-duration-200 motion-delay-200 motion-preset-focus-lg"
    >
      App Settings
    </h1>
    <div
      class="flex flex-row border-2 border-opacity-60 border-secondary bg-gradient-to-br from-base-200 to-transparent h-5/6 my-4 motion-duration-300 motion-delay-150 motion-opacity-in-0 motion-preset-slide-up-md"
    >
      <SettingsLateralMenu
        class="lg:w-1/6 w-1/12 motion-delay-200 motion-duration-200 motion-preset-fade-lg"
        @menuOption="changeMenu"
        :options="state.options"
        :title="'App Settings'"
      />
      <div
        class="divider lg:divider-horizontal divider-secondary opacity-60"
      ></div>

      <div
        class="lg:w-5/6 w-11/12 h-full overflow-y-hidden motion-delay-500 motion-duration-200 motion-preset-fade-lg"
      >
        <ThemeControl v-show="state.selected == 'theme_setup'" />
        <UserProfileSetup v-show="state.selected == 'user_profile'" />
      </div>
    </div>
    <div class="flex flex-row justify-start">
      <LoadWorkspaceButton />
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
  import UserProfileSetup from "../components/settings/UserProfileSetup.vue";
  import LoadWorkspaceButton from "../components/ui/LoadWorkspaceButton.vue";

  const UIStore = useUIStore();
  const userStore = useUserStore();

  const state = reactive({
    selected: "user_profile",
    options: [
      {
        title: "User Profile",
        path: "user_profile",
        needOwner: false,
        needOnline: false,
      },
      {
        title: "Theme Settings",
        path: "theme_setup",
        needOwner: false,
        needOnline: false,
      },
    ],
  });

  function changeMenu(option: MenuOptionUI) {
    state.selected = option.path;
  }
</script>
