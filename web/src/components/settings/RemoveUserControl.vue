import { UserLite } from '../../utils/types';
<template lang="">
  <div class="flex flex-col justify-center gap-10">
    <h1 class="text-start text-xl">Workspace's users</h1>
    <div class="flex flex-col gap-5">
      <ul class="flex flex-col gap-5">
        <li
          v-for="user in user_list"
          class="w-full flex flex-row gap-5 my-auto"
        >
          <div class="avatar">
            <div class="w-8 rounded-full border-primary border-2">
              <img :src="user.avatar" />
            </div>
          </div>
          <p class="w-5">{{ user.user_id }}</p>
          <p class="w-40">{{ user.username }}</p>
          <p class="w-96">{{ user.email }}</p>
          <div class="w-12">
            <font-awesome-icon
              :icon="['fas', 'hammer']"
              class="text-lg text-white"
              v-if="checkIsMod(user.user_id)"
            />
            <font-awesome-icon
              :icon="['fas', 'crown']"
              class="text-lg text-white"
              v-if="user.user_id == props.owner_id"
            />
          </div>

          <font-awesome-icon
            :icon="['fas', 'trash']"
            class="size-6 text-error mt-1 hover:cursor-pointer hover:scale-110 transition-all ease-in-out duration-100"
            @click="openModal(user.user_id)"
            v-if="!checkIsMod(user.user_id) && user.user_id != props.owner_id"
          />
        </li>
      </ul>
    </div>
    <BaseDeleteModal
      :id="modal_user_id_to_delete"
      :type="'user from workspace'"
      :showModal="showModal"
      @cancel="closeModal"
      @delete="removeUser"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { UserLite } from "../../utils/types";
  import { EndpointType } from "../../utils/endpoints";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";

  const props = defineProps<{
    workspace_id: number;
    user_list: UserLite[];
    mod_list: UserLite[];
    owner_id: number;
  }>();

  const apiCall = useApiCall();

  const emit = defineEmits(["update"]);

  const user_list = ref<UserLite[]>();

  const modal_user_id_to_delete = ref<number>();
  const showModal = ref<boolean>();

  async function removeUser() {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_REMOVE_USER,
      {},
      {
        params: {
          ws_id: props.workspace_id,
          user_id: modal_user_id_to_delete.value,
        },
      }
    )) as UserLite[];
    if (response.length > 0) {
      user_list.value = response;
      emit("update");
      setTimeout(() => {
        showModal.value = false;
      }, 1000);
    }
  }

  function openModal(id: number) {
    modal_user_id_to_delete.value = id;
    showModal.value = true;
  }

  function closeModal() {
    showModal.value = false;
  }

  function checkIsMod(id: number) {
    return props.mod_list.filter((u: UserLite) => u.user_id == id).length > 0;
  }

  watch(
    () => props.user_list,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        user_list.value = props.user_list;
      }
    }
  );

  onBeforeMount(() => {
    user_list.value = props.user_list;
  });
</script>
