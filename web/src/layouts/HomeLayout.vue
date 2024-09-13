<template lang="">
  <div class="flex flex-col justify-center w-full" v-if="isLoaded">
    <div
      class="flex flex-row lg:flex-nowrap flex-wrap justify-between max-w-full lg:ml-24 ml-5 pt-10 max-h-40 lg:pb-12 2xl:pb-0 pb-20"
    >
      <div class="w-full">
        <h1 class="text-2xl">Home page</h1>
      </div>

      <div
        class="flex flex-row lg:flex-nowrap flex-wrap justify-end gap-5 w-4/5 mr-10 max-h-12 my-auto align-middle"
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
    <ProjectList v-if="isLoggedIn" class="lg:mt-0 mt-16" />
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
  import ThemeSelector from "../components/settings/ThemeSelector.vue";
  import BaseToggle from "../components/common/BaseToggle.vue";
  import ProjectList from "../components/project/ProjectList.vue";
  import { taskUtils } from "../utils/task.utils";

  const router = useRouter();

  const userStore = useUserStore();
  const UIStore = useUIStore();

  const isLoggedIn = ref(false);
  const isLoaded = ref(false);

  const loadedDesignated = ref(false);

  const selected_theme = ref<string>();

  const isDark = ref<boolean>();

  const darkerCards = ref<boolean>();
  const darkerMiniCards = ref<boolean>();

  watch(
    () => UIStore.justUpdated,
    (newValue, oldValue) => {
      if (newValue) {
        isDark.value = UIStore.darkTheme;
        darkerCards.value = UIStore.darkerCard;
        darkerMiniCards.value = UIStore.darkerMiniCard;
      }
    }
  );

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

  onBeforeMount(() => {
    const user = userStore.getSelf(); //JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    } else {
      isLoggedIn.value = true;
      isLoaded.value = true;
    }
    isDark.value = UIStore.darkTheme;
    darkerCards.value = UIStore.darkerCard;
    darkerMiniCards.value = UIStore.darkerMiniCard;
    //taskUtils.saveTagPool(); //usado para crear tagpools para testear
  });
</script>
