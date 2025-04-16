<template lang="">
  <div class="mt-5 xl:ml-2 lg:-ml-6 mx-auto h-screen">
    <h1
      class="text-3xl text-center motion-duration-200 motion-delay-200 motion-preset-focus-lg"
    >
      Project Settings
    </h1>
    <div
      class="flex flex-row border-2 border-opacity-60 border-secondary bg-gradient-to-br from-base-200 to-transparent h-5/6 my-4 xl:w-full lg:w-11/12 motion-duration-300 motion-delay-150 motion-opacity-in-0 motion-preset-slide-up-md"
    >
      <SettingsLateralMenu
        class="w-1/6 motion-delay-200 motion-duration-200 motion-preset-fade-lg"
        @menuOption="changeMenu"
        :isOwner="isOwner"
        :options="state.options"
        :title="'Project Settings'"
      />
      <div
        class="divider lg:divider-horizontal divider-secondary opacity-60"
      ></div>

      <div
        class="w-5/6 h-fit motion-delay-500 motion-duration-200 motion-preset-fade-lg"
      >
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
        <UsersControl
          :workspace_id="project.workspace_id"
          :user_list="project.users"
          :mod_list="project.moderators"
          :coll_list="project.collaborators"
          :owner_id="state.ownerId"
          @update="updateProject"
          v-show="state.selected == 'users_control'"
        />

        <GlosaryControl
          :ws_id="project.workspace_id"
          v-show="state.selected == 'glosary_setup'"
          @update="updateProject"
        />
        <SpecialTagsControl
          :ws_id="project.workspace_id"
          v-show="state.selected == 'tags_setup'"
        />
      </div>
    </div>
    <div class="flex flex-row justify-between gap-5" v-if="isOwner">
      <button class="btn btn-primary text-white" @click="downloadJson">
        Download JSON
      </button>
      <!-- TODO: activar disabled de nuevo -->
      <button class="btn btn-error text-white" @click="showDeleteWsModal">
        DELETE WORKSPACE
      </button>
    </div>

    <BaseDeleteModal
      :id="state.ws_id"
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
    JSONWorkspace,
    MenuOptionUI,
    UserLite,
    Workspace,
  } from "../utils/types";
  import { useApiCall } from "../composables/useAPICall";
  import BaseDeleteModal from "../components/common/BaseDeleteModal.vue";
  import { EndpointType } from "../utils/endpoints";
  import { checkIsOwner } from "../utils/auth.utils";
  import { dateUtils } from "../utils/date.utils";
  import { useRouter } from "vue-router";
  import { useUserStore } from "../store/user.store";
  import SettingsLateralMenu from "../components/settings/SettingsLateralMenu.vue";
  import ModControl from "../components/settings/ModControl.vue";
  import ProjectBasicControl from "../components/settings/ProjectBasicControl.vue";
  import UsersControl from "../components/settings/UsersControl.vue";
  import GlosaryControl from "../components/settings/GlosaryControl.vue";
  import SpecialTagsControl from "../components/settings/SpecialTagsControl.vue";
  import { SpecialTag } from "../utils/types";

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
    ws_id: 0,
    options: [
      {
        title: "Basic Settings",
        path: "basic",
        needOwner: false,
        needOnline: false,
      },
      {
        title: "Moderators",
        path: "mods",
        needOwner: true,
        needOnline: true,
      },
      {
        title: "Users Control",
        path: "users_control",
        needOwner: false,
        needOnline: true,
      },
      {
        title: "Custom Glosaries",
        path: "glosary_setup",
        needOwner: false,
        needOnline: false,
      },
      {
        title: "Special Tags",
        path: "tags_setup",
        needOwner: false,
        needOnline: false,
      },
    ],
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
    //console.log("UPDATING");
    if (ws) {
      projectStore.setCurrent(ws);
      project.value = projectStore.getCurrent();
    } else project.value = await projectStore.updateCurrent();

    // projectStore.current;
  }

  async function deleteWs() {
    //console.log("DELETED");
    const ws = projectStore.getCurrent() as Workspace;

    const response = (await projectStore.deleteWorkspace(
      ws.workspace_id as number
    )) as APIResponse;
    //  (await apiCall.del(EndpointType.WORKSPACE_DELETE, {
    //   params: {
    //     id: projectStore.current.workspace_id,
    //   },
    // })) as APIResponse;
    if (response.message == "Workspace deleted!") {
      setTimeout(() => {
        showDelWsModal.value = false;
        router.push("/");
      }, 500);
    } else console.error(response.message);
  }

  async function downloadJson() {
    const ws = projectStore.getCurrent() as Workspace;
    console.log("DEBUG");
    console.log(userStore.self.user_id);
    const offlineMode = projectStore.checkOfflineMode();
    let JSONws_blob;
    if (!offlineMode) {
      try {
        const response = (await apiCall.get(EndpointType.WORKSPACE_JSON, {
          params: {
            ws_id: ws.workspace_id,
            user_id: userStore.self.user_id,
          },
          responseType: "blob",
        })) as BlobPart;
        JSONws_blob = new Blob([response], { type: "application/json" });
        // const fileURL = window.URL.createObjectURL(new Blob([response]));
        // const fileLink = document.createElement("a");
        // fileLink.href = fileURL;
        // fileLink.setAttribute(
        //   "download",
        //   `${ws.project_name + "_" + ws.updated_at}.json`
        // );
        // document.body.appendChild(fileLink);
        // fileLink.click();
        // document.body.removeChild(fileLink);
        // window.URL.revokeObjectURL(fileURL);
      } catch (error) {
        console.error(error);
      }
    } else {
      const workspace_data = projectStore.getLocalStorageWorkspaceById(
        ws.workspace_id as number
      ) as JSONWorkspace;
      if (workspace_data) {
        const json_string = JSON.stringify(workspace_data);
        JSONws_blob = new Blob([json_string], { type: "application/json" });
        // const url = URL.createObjectURL(blob);
        // const a = document.createElement("a");
        // a.href = url;
        // a.download = `${workspace_data.workspace.project_name}_${workspace_data.workspace.updated_at}.json`;
        // document.body.appendChild(a);
        // a.click();
        // document.body.removeChild(a);
      } else {
        console.error(
          "Something went wrong, workspace not found in localStorage. ID=",
          ws.workspace_id
        );
        return;
      }
    }
    if (JSONws_blob) {
      const url = URL.createObjectURL(JSONws_blob);
      const a = document.createElement("a");
      a.href = url;

      a.download = `${ws.project_name}_${dateUtils.getActualDate()}.json`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    }
  }

  watch(
    () => projectStore.justUpdated,
    (newValue, oldValue) => {
      if (newValue) {
        project.value = projectStore.getCurrent();
      }
    }
  );

  onBeforeMount(() => {
    const ws = projectStore.getCurrent() as Workspace;
    isOwner.value = checkIsOwner(ws.workspace_id as number);
    project.value = ws;
    state.ownerId = project.value.owner.user_id;
  });
</script>
