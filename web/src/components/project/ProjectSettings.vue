<template lang="">
  <div class="mt-5">
    <h1 class="text-4xl text-center">Project Settings</h1>
    <div class="my-10 flex flex-col gap-5 justify-center" v-if="isOwner">
      <h2 class="text-2xl">Add/Remove Mods</h2>
      <h3 class="text-xl pt-3">User List:</h3>
      <ul class="py-3 flex flex-row justify-start gap-5">
        <li
          class="flex flex-row gap-2 hover:cursor-pointer"
          v-for="user in user_list"
          @click="addMod(user.user_id)"
        >
          <img :src="user.avatar" class="avatar size-7 rounded-full" />
          <p>{{ user.username }} ,</p>
        </li>
      </ul>
      <h3 class="text-xl pt-3">Moderators list:</h3>
      <ul class="py-3 flex flex-row justify-start gap-5">
        <li
          class="flex flex-row gap-2 hover:cursor-pointer"
          v-for="user in mod_list"
          @click="removeMod(user.user_id)"
        >
          <img :src="user.avatar" class="avatar size-7 rounded-full" />
          <p>{{ user.username }} ,</p>
        </li>
      </ul>
      <div class="flex flex-row justify-between gap-5">
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
        :id="modal_id"
        :type="'user as moderator'"
        :showModal="showModal"
        @cancel="closeModal"
        @delete="deleteAsMod"
      />

      <BaseDeleteModal
        :id="projectStore.current.workspace_id"
        :type="'WORKSPACE'"
        :showModal="showDelWsModal"
        @cancel="closeDeleteWsModal"
        @delete="deleteWs"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { useProjectStore } from "../../store/project.store";
  import { APIResponse, UserLite, Workspace } from "../../utils/types";
  import { useApiCall } from "../../composables/useAPICall";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import { EndpointType } from "../../utils/endpoints";
  import { checkIsOwner } from "../../utils/auth.utils";
  import { useRouter } from "vue-router";
  import { useUserStore } from "../../store/user.store";

  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const router = useRouter();

  const user_list = ref<UserLite[]>();
  const mod_list = ref<UserLite[]>();

  const apiCall = useApiCall();

  const modal_id = ref<number>();
  const modal_type = ref<string>();
  const showModal = ref<boolean>(false);

  const showDelWsModal = ref<boolean>();

  const isOwner = ref<boolean>();

  async function addMod(id: number) {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_ADD_MOD,
      {},
      {
        params: {
          ws_id: projectStore.current.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite[];
    if (response.length > 0) {
      mod_list.value = response;
      await projectStore.updateCurrent();
    }
  }

  function removeMod(id: number) {
    modal_id.value = id;
    modal_type.value = "USER AS MODERATOR";
    showModal.value = true;
  }

  async function deleteAsMod(id: number) {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_REMOVE_MOD,
      {},
      {
        params: {
          ws_id: projectStore.current.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite;
    if (response.user_id == id) {
      const ws = (await projectStore.updateCurrentById(
        projectStore.current.workspace_id
      )) as Workspace;
      mod_list.value = ws.moderators;
    }
    setTimeout(() => {
      showModal.value = false;
      mod_list.value = mod_list.value?.filter((u: UserLite) => u.user_id != id);
    }, 500);
  }

  function closeModal() {
    showModal.value = false;
  }

  function showDeleteWsModal() {
    showDelWsModal.value = true;
  }

  function closeDeleteWsModal() {
    showDelWsModal.value = false;
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

  onBeforeMount(() => {
    user_list.value = projectStore.current.users;
    mod_list.value = projectStore.current.moderators;
    isOwner.value = checkIsOwner(projectStore.current.workspace_id);
  });
</script>
