<template lang="">
  <div class="mt-5">
    <h1 class="text-3xl text-center">Project Settings</h1>
    <div
      class="flex flex-row border-2 border-secondary bg-gradient-to-br from-base-200 to-transparent min-h-96 my-10"
    >
      <SettingsLateralMenu
        class="w-1/6"
        @menuOption="changeMenu"
        :isOwner="isOwner"
      />
      <div class="divider lg:divider-horizontal divider-secondary"></div>

      <div class="w-5/6 h-fit">
        <ProjectBasicControl
          :project="project"
          v-show="state.selected == 'basic'"
        />
        <ModControl
          :workspace_id="project.workspace_id"
          :users_list="project.users"
          :mod_list="project.moderators"
          v-show="state.selected == 'mods'"
        />
        <RemoveUserControl
          :workspace_id="project.workspace_id"
          :user_list="project.users"
          :mod_list="project.moderators"
          :owner_id="state.ownerId"
          @update="updateProject"
          v-show="state.selected == 'remove_users'"
        />
      </div>
    </div>
    <div class="flex flex-row justify-between gap-5" v-if="isOwner">
      <button class="btn btn-primary text-white" @click="downloadJson">
        Download JSON
      </button>
      <button
        class="btn btn-error text-white"
        disabled
        @click="showDeleteWsModal"
      >
        DELETE WORKSPACE
      </button>
    </div>

    <BaseDeleteModal
      :id="projectStore.current.workspace_id"
      :type="'WORKSPACE'"
      :showModal="showDelWsModal"
      @cancel="closeDeleteWsModal"
      @delete="deleteWs"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, reactive, ref, watch } from "vue";
  import { useProjectStore } from "../store/project.store";
  import {
    APIResponse,
    MenuOptionUI,
    UserLite,
    Workspace,
  } from "../utils/types";
  import { useApiCall } from "../composables/useAPICall";
  import BaseDeleteModal from "../components/common/BaseDeleteModal.vue";
  import { EndpointType } from "../utils/endpoints";
  import { checkIsOwner } from "../utils/auth.utils";
  import { useRouter } from "vue-router";
  import { useUserStore } from "../store/user.store";
  import SettingsLateralMenu from "../components/settings/SettingsLateralMenu.vue";
  import ModControl from "../components/settings/ModControl.vue";
  import ProjectBasicControl from "../components/settings/ProjectBasicControl.vue";
  import RemoveUserControl from "../components/settings/RemoveUserControl.vue";

  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const project = ref<Workspace>();

  const router = useRouter();

  const apiCall = useApiCall();

  const showDelWsModal = ref<boolean>();

  const isOwner = ref<boolean>();

  const state = reactive({
    selected: "basic",
    ownerId: 0,
  });

  function changeMenu(option: MenuOptionUI) {
    state.selected = option.path;
    //console.log("SELECTED: ", state.selected);
  }

  function showDeleteWsModal() {
    showDelWsModal.value = true;
  }

  function closeDeleteWsModal() {
    showDelWsModal.value = false;
  }

  async function updateProject(ws?: Workspace) {
    console.log("UPDATING");
    if (ws) {
      projectStore.setCurrent(ws);
      project.value = projectStore.current;
    } else project.value = await projectStore.updateCurrent();

    projectStore.current;
  }

  async function deleteWs() {
    console.log("DELETED");

    const response = (await apiCall.del(EndpointType.WORKSPACE_DELETE, {
      params: {
        id: projectStore.current.workspace_id,
      },
    })) as APIResponse;
    if (response.message == "Workspace deleted!") {
      setTimeout(() => {
        showDelWsModal.value = false;
        router.push("/");
      }, 500);
    } else console.error(response.message);
  }

  async function downloadJson() {
    try {
      const response = (await apiCall.get(EndpointType.WORKSPACE_JSON, {
        params: {
          ws_id: projectStore.current.workspace_id,
          user_id: userStore.self.user_id,
        },
        responseType: "blob",
      })) as BlobPart;
      const fileURL = window.URL.createObjectURL(new Blob([response]));
      const fileLink = document.createElement("a");
      fileLink.href = fileURL;
      fileLink.setAttribute(
        "download",
        `${
          projectStore.current.project_name +
          "_" +
          projectStore.current.updated_at
        }.json`
      );
      document.body.appendChild(fileLink);
      fileLink.click();
      document.body.removeChild(fileLink);
      window.URL.revokeObjectURL(fileURL);
    } catch (error) {
      console.error(error);
    }
  }

  watch(
    () => projectStore.justUpdated,
    (newValue, oldValue) => {
      if (newValue) {
        project.value = projectStore.current;
      }
    }
  );

  onBeforeMount(() => {
    isOwner.value = checkIsOwner(projectStore.current.workspace_id);
    project.value = projectStore.current;
    state.ownerId = project.value.owner.user_id;
  });
</script>
