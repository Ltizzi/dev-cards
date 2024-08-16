<template lang="">
  <div
    class="w-20"
    v-if="isLogged"
    @mouseover="state.isHovering = true"
    @mouseleave="state.isHovering = false"
  >
    <div class="flex flex-col justify-center py-5 px-0.5" v-if="isLoaded">
      <div
        :class="[
          'avatar ',
          state.selected == 0 && state.isHovering
            ? 'border-l-4 border-accent -ml-1'
            : 'border-l-0 border-base',
          state.selected == 0 && !state.isHovering
            ? 'border-r-4 border-accent mr-2'
            : 'border-r-0 border-base',
        ]"
        v-if="isLogged"
      >
        <div
          class="w-16 h-16 rounded-full mx-auto hover:cursor-pointer hover:border-4 border-accent"
          @click="goHome"
        >
          <img :src="user.avatar" />
        </div>
      </div>
      <div class="divider divider-primary"></div>

      <ul
        class="flex flex-col justify-center w-11/12 gap-4 my-2 mx-auto bg-base-100 bg-opacity-40 rounded-md px-4 py-1.5"
      >
        <li
          v-for="project in projects"
          v-if="isLoaded"
          @click="goTo(project)"
          :class="['-ml-3  px-1']"
        >
          <div
            :class="[
              'avatar -ml-2',
              state.selected == project.workspace_id && state.isHovering
                ? 'border-l-4 border-accent px-1. '
                : 'border-l-4 border-l-transparent',
              state.selected == project.workspace_id && !state.isHovering
                ? 'border-r-4 border-accent'
                : 'border-r-4 border-r-transparent',
            ]"
          >
            <div
              class="w-16 h-16 rounded-full border-4 border-transparent mx-auto hover:cursor-pointer 0 transition-all hover:border-4 hover:border-accent p-3 hover:border-opacity-80 duration-300 ease-in-out"
            >
              <img
                :src="project.project_avatar"
                class="w-full h-full hover:scale-125 transition-all duration-200 ease-in-out"
              />
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useUserStore } from "../store/user.store";
  import { useProjectStore } from "../store/project.store";
  import { useRouter } from "vue-router";
  import { Workspace, User } from "../utils/types";
  import { onBeforeMount, ref, watch, reactive } from "vue";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import { isTokenExpired } from "../utils/auth.utils";

  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const router = useRouter();

  const projects = ref<Array<Workspace>>();
  const user = ref<User>();

  const isLoaded = ref(false);
  const isLogged = ref(false);

  const firstLoad = ref(true);

  const state = reactive({
    selected: 0,
    isHome: true,
    justChange: false,
    isHovering: false,
  });

  watch(
    () => userStore.logged,
    async (newValue, oldValue) => {
      if (!newValue) {
        isLogged.value = false;
        projects.value = [] as Array<Workspace>;
      } else {
        isLogged.value = true;
        user.value = userStore.self;
        projects.value = await fetchProjects(user.value.user_id);
      }
    }
  );

  watch(
    () => projectStore.justCreated,
    async (newValue, oldValue) => {
      if (newValue) {
        // user.value = await userStore.refreshSelf();
        // await fetchProjects(user.value?.user_id as number);
        console.log("REFRESHED PROJECTS");
        projects.value = await fetchProjects(user.value?.user_id as number);
      }
    }
  );

  watch(
    () => projectStore.current,
    (newValue, oldValue) => {
      if (newValue.workspace_id != oldValue.workspace_id && !state.justChange) {
        state.selected = newValue.workspace_id;
      }
    }
  );

  function hovering() {
    state.isHovering = true;
  }

  function mouseOut() {
    state.isHovering = false;
  }

  function goHome() {
    state.selected = 0;
    state.isHome = true;
    router.push("/");
  }

  function goTo(project: Workspace) {
    state.selected = project.workspace_id;
    state.isHome = false;
    projectStore.setCurrent(project);
    state.justChange = true;
    setTimeout(() => {
      state.justChange = false;
    }, 200);

    router.push(`/project/info?id=${project.workspace_id}`);
  }

  async function fetchProjects(userId: number) {
    return (await apiCall.get(EndpointType.USER_MEMBER, {
      params: { user_id: userId },
    })) as Array<Workspace>;
  }

  onBeforeMount(async () => {
    if (isTokenExpired()) {
      router.push("/login");
    } else {
      if (!userStore.logged) {
        const savedUser = JSON.parse(localStorage.getItem("user") as string);
        if (savedUser) {
          userStore.setSelf(savedUser);
          user.value = savedUser;
        } else {
          router.push("/login");
        }
      } else {
        user.value = userStore.self;
      }

      // const response = (await apiCall.get(EndpointType.USER_MEMBER, {
      //   params: { user_id: user.value.user_id },
      // })) as Array<Workspace>;
      projects.value = await fetchProjects(user.value?.user_id as number);
      isLogged.value = true;
      isLoaded.value = true;
    }
  });
</script>
