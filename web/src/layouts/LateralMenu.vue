<template lang="">
  <div class="w-20">
    <div class="flex flex-col justify-center mx-auto py-5 px-2" v-if="isLoaded">
      <div
        :class="[
          'avatar',
          state.selected == 0 ? 'border-l-4 border-primary -ml-1' : '',
        ]"
        v-if="isLogged"
      >
        <div class="w-14 h-14 rounded-full mx-auto" @click="goHome">
          <img :src="user.avatar" />
        </div>
      </div>
      <div class="divider divider-primary"></div>

      <ul class="flex flex-col gap-4 my-2 mx-auto">
        <li
          v-for="project in projects"
          v-if="isLoaded"
          @click="goTo(project)"
          :class="
            state.selected == project.workspace_id
              ? 'border-l-4 border-primary -ml-1'
              : ''
          "
        >
          <div class="avatar ml-2">
            <div
              class="w-14 h-14 rounded-full mx-auto hover:cursor-pointer hover:scale-110 transition-all hover:border-2 hover:border-cyan-200 p-1 hover:border-opacity-80 duration-300 ease-in-out"
            >
              <img :src="project.project_avatar" />
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

  const state = reactive({
    selected: 0,
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

  function goHome() {
    state.selected = 0;
    router.push("/");
  }

  function goTo(project: Workspace) {
    state.selected = project.workspace_id;
    projectStore.setCurrent(project);
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
