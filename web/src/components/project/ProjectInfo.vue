<template lang="">
  <div
    class="flex flex-col justify-center w-full min-h-screen max-h-screen overflow-auto xl:ml-0 lg:-ml-7 motion-duration-2000 motion-preset-slide-up motion-opacity-in-0"
    v-if="isLoaded"
  >
    <div
      class="flex flex-row justify-center gap-5 align-middle lg:mt-7 xl:mt-0 2xl:mt-0 mt-32"
    >
      <h1 class="text-4xl text-center my-5 font-bold">
        {{ project.project_name }}
      </h1>
      <div class="avatar">
        <div class="w-14 h-14 rounded-full mx-auto my-auto">
          <img :src="project.project_avatar" />
        </div>
      </div>
    </div>

    <div
      class="flex flex-col gap-5 justify-center items-center text-center mt-10 -ml-10"
    >
      <div>
        <h1 class="mb-2 text-base font-bold">Created by:</h1>

        <BaseUserAvatarItem
          :avatar="project.owner.avatar"
          :username="project.owner.username"
          :id="project.owner.user_id"
          :createdAt="project.created_at"
        />
      </div>
      <div>
        <h1 class="mb-2 text-base font-bold">Moderators:</h1>
        <div class="flex flex-row flex-wrap gap-5">
          <p class="text-lg" v-for="mod in project.moderators">
            <BaseUserAvatarItem
              :avatar="mod.avatar"
              :username="mod.username"
              :id="mod.user_id"
            />
          </p>
        </div>
      </div>
      <div>
        <h1 class="mb-2 text-base font-bold">Users:</h1>
        <div
          class="flex flex-row flex-wrap gap-5 justify-center lg:mx-7 xl:mx-0"
        >
          <p class="text-lg" v-for="user in project.users">
            <BaseUserAvatarItem
              :avatar="user.avatar"
              :username="user.username"
              :id="user.user_id"
            />
          </p>
        </div>
      </div>
    </div>

    <div
      class="my-10 xl:mx-72 lg:mx-14 flex flex-col justify-center items-center"
    >
      <BaseEditDescription
        :description="project.description"
        :id="project.workspace_id"
        :isDark="isDark"
        :type="'workspace'"
        :canModify="canModify"
        @update="updateProject"
      />
    </div>

    <SpecialTagNavigation :showTags="true" class="xl:mx-72 pb-14" />

    <TagNavigationPanel
      :ws_id="project.workspace_id"
      :info="true"
      :show="true"
      class="xl:mx-72 pb-14"
    />

    <NewTaskBtn class="mx-auto" />
  </div>
</template>

<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { Workspace } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";
  import { useRoute } from "vue-router";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import BaseEditDescription from "../common/BaseEditDescription.vue";
  import NewTaskBtn from "../task/NewTaskBtn.vue";
  import TaskList from "../task/TaskList.vue";
  import { dateUtils } from "../../utils/date.utils";
  import { checkIsModOrOwner } from "../../utils/auth.utils";
  import TagNavigationPanel from "../ui/TagNavigationPanel.vue";
  import BaseUserAvatarItem from "../common/BaseUserAvatarItem.vue";
  import SpecialTagNavigation from "../ui/customConfiguration/SpecialTagNavigation.vue";
  import { useUIStore } from "../../store/ui.store";

  const projectStore = useProjectStore();
  const UIStore = useUIStore();

  const apiCall = useApiCall();

  const route = useRoute();

  const project = ref<Workspace>();

  const isLoaded = ref(false);

  const isDark = ref<boolean>();

  const canModify = ref<boolean>();

  watch(
    () => projectStore.current.workspace_id,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        project.value = projectStore.current;
      }
    }
  );

  // watch(
  //   () => projectStore.local.workspace_id,
  //   (newValue, oldValue) => {
  //     if (newValue != oldValue) {
  //       project.value = projectStore.local;
  //     }
  //   }
  // );

  async function updateProject(workspace: Workspace) {
    projectStore.setCurrent(workspace);
    project.value = projectStore.getCurrent();
  }

  onBeforeMount(async () => {
    const ws = projectStore.getCurrent() as Workspace;
    if (ws && ws.workspace_id) {
      project.value = ws;
      isLoaded.value = true;
    } else {
      const id = route.query.id as unknown as number;
      const response = (await projectStore.fetchProjectById(id)) as Workspace;
      //  (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
      //   params: { id: id },
      // })) as Workspace;
      if (response.workspace_id) {
        project.value = response;
        projectStore.setCurrent(response);
        isLoaded.value = true;
      } else {
        console.error(response);
      }
    }

    canModify.value = checkIsModOrOwner(project.value?.workspace_id as number);
    // if (localStorage.getItem("darkTheme")) {
    isDark.value = UIStore.checkIsDarkTheme(); //JSON.parse(localStorage.getItem("darkTheme") as string);
    //}
  });
</script>
