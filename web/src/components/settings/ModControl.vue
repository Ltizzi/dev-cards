<template lang="">
  <div class="my-14 flex flex-col gap-5 justify-center">
    <h2 class="text-3xl text-start font-bold mb-14">Add/Remove Mods</h2>
    <h3 class="text-xl pt-3">User List:</h3>
    <ul class="py-3 flex flex-row justify-start gap-5">
      <li
        class="flex flex-row gap-2 hover:cursor-pointer"
        v-for="user in user_list"
        @click="addMod(user.user_id)"
      >
        <!-- <img :src="user.avatar" class="avatar size-7 rounded-full" />
        <p>{{ user.username }} ,</p> -->
        <BaseUserAvatarItem
          :avatar="user.avatar"
          :username="user.username"
          :id="user.user_id"
          :disableLink="true"
        />
      </li>
    </ul>
    <h3 class="text-xl pt-3">Moderators list:</h3>
    <ul class="py-3 flex flex-row justify-start gap-5">
      <li
        class="flex flex-row gap-2 hover:cursor-pointer"
        v-for="user in mod_list"
        @click="removeMod(user.user_id)"
      >
        <!-- <img :src="user.avatar" class="avatar size-7 rounded-full" />
        <p>{{ user.username }} ,</p> -->
        <BaseUserAvatarItem
          :avatar="user.avatar"
          :username="user.username"
          :id="user.user_id"
          :disableLink="true"
        />
      </li>
    </ul>

    <BaseDeleteModal
      :id="modal_id"
      :type="'user as moderator'"
      :showModal="showModal"
      @cancel="closeModal"
      @delete="deleteAsMod"
    />
  </div>
</template>
<script setup lang="ts">
  import { useApiCall } from "../../composables/useAPICall";
  import { onBeforeMount, ref } from "vue";
  import { UserLite } from "../../utils/types";
  import { EndpointType } from "../../utils/endpoints";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import BaseUserAvatarItem from "../common/BaseUserAvatarItem.vue";

  const props = defineProps<{
    workspace_id: number;
    users_list: UserLite[];
    mod_list: UserLite[];
  }>();

  //const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const emit = defineEmits(["update"]);

  const user_list = ref<UserLite[]>();
  const mod_list = ref<UserLite[]>();

  const modal_id = ref<number>();
  const modal_type = ref<string>();
  const showModal = ref<boolean>(false);

  async function addMod(id: number) {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_ADD_MOD,
      {},
      {
        params: {
          ws_id: props.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite[];
    if (response.length > 0) {
      mod_list.value = response;
      //await projectStore.updateCurrent();
      emit("update");
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
          ws_id: props.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite[];
    if (mod_list.value && response.length < mod_list.value?.length) {
      mod_list.value = response;
      emit("update");
    }
    setTimeout(() => {
      showModal.value = false;
      //mod_list.value = mod_list.value?.filter((u: UserLite) => u.user_id != id);
    }, 500);
  }

  function closeModal() {
    showModal.value = false;
  }

  onBeforeMount(() => {
    user_list.value = props.users_list;
    mod_list.value = props.mod_list;
  });
</script>
