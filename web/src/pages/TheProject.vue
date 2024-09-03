<template lang="">
  <div
    class="flex flex-row justify-center w-screen min-h-screen max-h-screen lg:mx-5"
    v-if="isLoaded"
  >
    <ProjectLateralMenu
      class="h-screen left-10 fixed"
      @update="updateProject"
    />

    <div class="lg:ml-5 lg:w-10/12 w-full mx-5 lg:mx-0">
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
  import { useUIStore } from "../store/ui.store";

  const route = useRoute();

  const projectStore = useProjectStore();
  const UIStore = useUIStore();
  const userStore = useUserStore();

  const apiCall = useApiCall();

  const project = ref<Workspace>();
  const moderators = ref<UserLite[]>();
  const designatedTaskNumber = ref<number>();

  const isMobile = ref<boolean>();
  const showMenu = ref<boolean>();
  const isLoaded = ref<boolean>(false);

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
      localStorage.setItem("user", JSON.stringify(response.user));
    }
  }

  onBeforeMount(async () => {
    isMobile.value = UIStore.isMobile;
    showMenu.value = !isMobile.value;
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
        projectStore.setCurrent(response);
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
