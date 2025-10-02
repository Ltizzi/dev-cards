<template lang="">
  <div
    class="flex flex-col justify-center w-full bg-gradient-to-b from-base-100 to-base-300 bg-opacity-60"
    v-if="isLoaded"
  >
    <div
      class="flex flex-row lg:flex-nowrap flex-wrap justify-between max-w-full lg:ml-24 ml-5 pt-10 max-h-40 lg:pb-12 2xl:pb-0 pb-20"
    >
      <div class="w-full">
        <h1 class="text-2xl">Home page</h1>
      </div>

      <div
        class="flex flex-row lg:flex-nowrap flex-wrap justify-start lg:justify-end gap-5 lg:w-4/5 w-full mr-10 max-h-12 my-auto align-middle items-center"
      >
        <div class="flex flex-row gap-3" v-if="isDark">
          <BaseToggle
            @update="handleDarker"
            :isActive="darkerCards"
            :title="'Darker Cards'"
            :type="'card'"
          />
          <BaseToggle
            @update="handleDarker"
            :isActive="darkerMiniCards"
            :title="'Darker Minicards'"
            :type="'minicard'"
          />
        </div>

        <ThemeSelector :dropdown="true" />
        <button
          class="btn btn-outline btn-secondary lg:btn-base btn-sm"
          @click="createProject"
          v-if="isLoggedIn"
        >
          Create Project
        </button>

        <button
          class="btn btn-outline btn-success lg:btn-base btn-sm"
          @click="login"
          v-if="!isLoggedIn"
        >
          Log in
        </button>
        <button
          class="btn btn-outline btn-error lg:btn-base btn-sm"
          @click="signoff"
          v-if="isLoggedIn"
        >
          Logout
        </button>
      </div>
    </div>
    <div class="flex flex-col justify-center items-start w-full mt-10">
      <div role="tablist" class="tabs tabs-boxed tabs-sm w-4/12 lg:ml-14">
        <a
          role="tab"
          :class="['tab', state.selectedTab === 0 ? 'tab-active' : '']"
          @click="selectTab(0)"
          >Projects</a
        >
        <a
          role="tab"
          :class="['tab', state.selectedTab === 1 ? 'tab-active' : '']"
          @click="selectTab(1)"
          >Tasks</a
        >
        <!-- <a
          role="tab"
          :class="['tab', state.selectedTab === 2 ? 'tab-active' : '']"
          @click="selectTab(2)"
          >Calendar</a
        > -->
      </div>
      <div class="flex flex-col justify-center items-center w-full mt-0 pt-5">
        <ProjectList
          v-if="isLoggedIn && state.selectedTab === 0"
          class="lg:mt-0 mt-16"
        />
        <DesignatedView
          class="ml-10"
          :isDark="isDark"
          :darkerCards="darkerCards"
          :isLoggedIn="isLoggedIn"
          v-if="isLoaded && state.selectedTab === 1"
        />
        <!-- <ScheduleLayout v-if="state.selectedTab === 2" :isUserCalendar="true" /> -->
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useRouter } from "vue-router";
  import { onBeforeMount, reactive, ref, watch } from "vue";
  import { useUserStore } from "../store/user.store";
  import DesignatedView from "./DesignatedView.vue";
  import { isDarkerCardsActive } from "../utils/client.utils";
  import { useUIStore } from "../store/ui.store";
  import ThemeSelector from "../components/settings/ThemeSelector.vue";
  import BaseToggle from "../components/common/BaseToggle.vue";
  import ProjectList from "../components/project/ProjectList.vue";
  import { taskUtils } from "../utils/task.utils";
  import { useProjectStore } from "../store/project.store";
  import { User, UserLocal, Workspace } from "../utils/types";

  // import ScheduleLayout from "./ScheduleLayout.vue";

  const router = useRouter();

  const userStore = useUserStore();
  const UIStore = useUIStore();

  const isLoggedIn = ref(false);
  const isLoaded = ref(false);

  const loadedDesignated = ref(false);

  const selected_theme = ref<string>();

  const isDark = ref<boolean>();

  const darkerCards = ref<boolean>(false);
  const darkerMiniCards = ref<boolean>(false);

  const state = reactive({
    selectedTab: 0,
  });

  watch(
    () => UIStore.justUpdated,
    (newValue, oldValue) => {
      if (newValue) {
        isDark.value = UIStore.darkTheme;
        darkerCards.value = UIStore.darkerCard || false;
        darkerMiniCards.value = UIStore.darkerMiniCard;
      }
    }
  );

  function selectTab(tab: number) {
    state.selectedTab = tab;
  }

  function handleDarker(value: boolean, type?: string) {
    if (type && type == "card") {
      UIStore.setDarkerCards(value);
    } else if (type && type == "minicard") {
      UIStore.setDarkerMiniCards(value);
    }
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

  onBeforeMount(async () => {
    const user = userStore.getSelf() as User | UserLocal;
    if (!user) {
      router.push("/login");
    } else {
      isLoggedIn.value = true;
      isLoaded.value = true;
    }
    isDark.value = UIStore.darkTheme;
    darkerCards.value = UIStore.darkerCard;
    darkerMiniCards.value = UIStore.darkerMiniCard;
  });
</script>
