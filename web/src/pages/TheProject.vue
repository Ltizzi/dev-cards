<template lang="">
  <div class="flex flex-row justify-center w-screen" v-if="isLoaded">
    <ProjectLateralMenu class="h-screen left-4 fixed" @update="updateProject" />

    <div class="ml-52 w-10/12">
      <router-view></router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useProjectStore } from "../store/project.store";
  import { AuthResponse, UserLite, Workspace } from "../utils/types";
  import { ref, onBeforeMount, watch } from "vue";
  import { useRoute } from "vue-router";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import ProjectLateralMenu from "../components/project/ProjectLateralMenu.vue";
  import { useUserStore } from "../store/user.store";
  import { saveToken } from "../utils/auth.utils";
  import { taskUtils } from "../utils/task.utils";

  const project = ref<Workspace>();

  const apiCall = useApiCall();

  const isLoaded = ref<boolean>(false);

  const route = useRoute();

  const projectStore = useProjectStore();

  const userStore = useUserStore();

  const moderators = ref<UserLite[]>();

  const designatedTaskNumber = ref<number>();

  watch(
    () => projectStore.justUpdated,
    async (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        if (
          moderators.value &&
          moderators.value.length < projectStore.current.moderators.length
        ) {
          moderators.value = projectStore.current.moderators;
          moderators.value.forEach(async (mod: UserLite) => {
            if (mod.user_id == userStore.self.user_id) {
              await updateToken();
            }
          });
        }
        if (
          designatedTaskNumber.value &&
          designatedTaskNumber.value < userStore.getDesignatedTask().length
        ) {
          designatedTaskNumber.value = taskUtils.getProjectUserDesignatedTasks(
            projectStore.current.tasks,
            userStore.getDesignatedTask()
          ).length;
          await updateToken();
        }
      }
    }
  );

  async function updateProject() {
    await projectStore.updateCurrent();
  }

  setInterval(async () => {
    await projectStore.updateCurrent();
  }, 1000 * 60);

  setInterval(async () => {
    await updateToken();
  }, 1000 * 60 * 5);

  async function updateToken() {
    const response = (await apiCall.get(
      EndpointType.USER_REFRESH
    )) as AuthResponse;
    if (response.user && response.token) {
      saveToken(response.token);
      userStore.setSelf(response.user);
    }
  }

  onBeforeMount(async () => {
    if (projectStore.current.workspace_id) {
      project.value = projectStore.current;
      moderators.value = project.value.moderators;
      isLoaded.value = true;
    } else {
      const id = route.query.id as unknown as number;
      const response = (await projectStore.updateCurrentById(id)) as Workspace;
      // const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
      //   params: { id: id },
      // })) as Workspace;
      if (response.workspace_id) {
        project.value = response;
        moderators.value = project.value.moderators;
        isLoaded.value = true;
      } else {
        console.error(response);
      }
    }
    if (isLoaded.value) {
      designatedTaskNumber.value = userStore.self.designated_tasks.length;
    }
  });
</script>
