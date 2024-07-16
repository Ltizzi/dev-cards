<template lang="">
  <div class="flex flex-col justify-center mx-auto py-5 px-2" v-if="isLoaded">
    <div class="avatar">
      <div class="w-14 h-14 rounded-full mx-auto" @click="goHome">
        <img :src="user.avatar" />
      </div>
    </div>
    <div class="divider divider-primary"></div>

    <ul class="flex flex-col gap-4 my-2 mx-auto">
      <li v-for="project in projects" v-if="isLoaded" @click="goTo(project)">
        <div class="avatar">
          <div
            class="w-14 h-14 rounded-full mx-auto hover:cursor-pointer hover:scale-110 transition-all hover:border-2 hover:border-cyan-200 p-1 hover:border-opacity-60"
          >
            <img :src="project.project_avatar" />
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
  import { useUserStore } from "../store/user.store";
  import { useProjectStore } from "../store/project.store";
  import { useRouter } from "vue-router";
  import { Workspace, User } from "../utils/types";
  import { onBeforeMount, ref } from "vue";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";

  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const router = useRouter();

  const projects = ref<Array<Workspace>>();
  const user = ref<User>();

  const isLoaded = ref(false);

  function goHome() {
    router.push("/");
  }

  function goTo(project: Workspace) {
    projectStore.setCurrent(project);
    router.push(`/project/info?id=${project.workspace_id}`);
  }

  onBeforeMount(async () => {
    if (!userStore.self.user_id) {
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

    const response = (await apiCall.get(EndpointType.USER_MEMBER, {
      params: { user_id: user.value.user_id },
    })) as Array<Workspace>;
    projects.value = response;
    isLoaded.value = true;
  });
</script>
