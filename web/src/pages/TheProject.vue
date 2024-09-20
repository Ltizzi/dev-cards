<template lang="">
  <div
    class="flex flex-row justify-end w-auto overflow-x-auto lg:overflow-x-hidden lg:w-screen min-h-screen max-h-screen lg:mx-5"
    v-if="isLoaded"
  >
    <ProjectLateralMenu class="h-screen fixed" @update="updateProject" />

    <div
      class="lg:ml-0 2xl:w-10/12 xl:w-10/12 lg:w-9/12 w-auto mx-5 lg:mx-0 2xl:mx-9"
    >
      <router-view></router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useProjectStore } from "../store/project.store";
  import { AuthResponse, UserLite, Workspace } from "../utils/types";
  import { ref, onBeforeMount, watch, onMounted } from "vue";
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
      userStore.updatedToken(response.user);
    }
  }

  onBeforeMount(async () => {
    isMobile.value = UIStore.isMobile;
    showMenu.value = !isMobile.value;
    UIStore.getTHeme();
    if (projectStore.current.workspace_id) {
      project.value = projectStore.getCurrent();
      moderators.value = project.value?.moderators;
      isLoaded.value = true;
    } else {
      const id =
        route.query.id && route.path != "/project/task"
          ? (route.query.id as unknown as number)
          : JSON.parse(localStorage.getItem("current_workspace_id") as string);
      const response = (await projectStore.fetchProjectById(id)) as Workspace;
      if (response.workspace_id) {
        project.value = response;
        projectStore.setCurrent(response);
        moderators.value = project.value.moderators;
        isLoaded.value = true;
      } else {
        console.error(response);
      }
    }
  });

  onMounted(() => {
    if (isLoaded.value) {
      designatedTaskNumber.value = userStore.getSelf().designated_tasks.length;
    }
  });
</script>
