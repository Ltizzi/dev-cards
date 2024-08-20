<template lang="">
  <div class="mt-5">
    <h1 class="text-4xl text-center">Project Settings</h1>
    <div class="my-10 flex flex-col gap-5 justify-center">
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
    </div>
    <BaseDeleteModal
      :id="modal_id"
      :type="modal_type"
      :showModal="showModal"
      @cancel="closeModal"
      @delete="deleteAsMod"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { useProjectStore } from "../../store/project.store";
  import { UserLite, Workspace } from "../../utils/types";
  import { useApiCall } from "../../composables/useAPICall";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import { EndpointType } from "../../utils/endpoints";
  import { checkIsOwner } from "../../utils/auth.utils";

  const projectStore = useProjectStore();

  const user_list = ref<UserLite[]>();
  const mod_list = ref<UserLite[]>();

  const apiCall = useApiCall();

  const modal_id = ref<number>();
  const modal_type = ref<string>();
  const showModal = ref<boolean>(false);

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

  onBeforeMount(() => {
    user_list.value = projectStore.current.users;
    mod_list.value = projectStore.current.moderators;
    isOwner.value = checkIsOwner(projectStore.current.workspace_id);
  });
</script>
