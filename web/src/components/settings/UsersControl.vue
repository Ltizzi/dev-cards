import { UserLite } from '../../utils/types';
<template lang="">
  <div class="flex flex-col justify-center gap-10 py-10 px-10">
    <h1 class="text-start text-2xl">Workspace's users</h1>
    <div class="flex flex-col gap-5">
      <div class="overflow-x-auto">
        <table class="table">
          <!-- head -->
          <thead>
            <tr>
              <th>Avatar</th>
              <th class="text-center">Id</th>
              <th>Userame</th>
              <th>Email</th>
              <th class="text-center">Role</th>
              <th class="text-center">Collaborator Control</th>
              <th class="text-center">Remove</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in user_list">
              <th>
                <div class="avatar">
                  <div class="w-8 rounded-full border-primary border-2">
                    <img :src="user.avatar" />
                  </div>
                </div>
              </th>
              <td>
                <p class="text-center w-auto">{{ user.user_id }}</p>
              </td>
              <td>
                <p class="w-auto">{{ user.username }}</p>
              </td>
              <td>
                <p class="w-auto">{{ user.email }}</p>
              </td>
              <td>
                <div
                  :class="[
                    'w-auto flex justify-center',
                    isDark ? 'text-white' : 'text-primary',
                  ]"
                >
                  <font-awesome-icon
                    :icon="['fas', 'hammer']"
                    class="text-lg"
                    v-if="checkIsMod(user.user_id)"
                  />
                  <font-awesome-icon
                    :icon="['fas', 'crown']"
                    class="text-lg"
                    v-if="user.user_id == props.owner_id"
                  />
                  <font-awesome-icon
                    :icon="['fas', 'people-group']"
                    class="text-lg"
                    v-if="checkIsCollaborator(user.user_id)"
                  />
                </div>
              </td>
              <td class="w-auto flex justify-center">
                <div
                  class="tooltip tooltip-info"
                  :data-tip="
                    checkIsCollaborator(user.user_id)
                      ? 'Remove Collaborator'
                      : 'Add Collaborator'
                  "
                >
                  <div
                    v-if="
                      !checkIsCollaborator(user.user_id) &&
                      !checkIsMod(user.user_id) &&
                      user.user_id != props.owner_id
                    "
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="addCollab(user.user_id)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      class="size-4 mt-3 text-success"
                    />
                  </div>
                  <div
                    v-if="checkIsCollaborator(user.user_id)"
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="removeCollab(user.user_id)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-minus']"
                      class="size-4 mt-3 text-error"
                    />
                  </div>
                </div>
              </td>
              <td>
                <div class="flex justify-center">
                  <font-awesome-icon
                    :icon="['fas', 'trash']"
                    class="size-6 text-error mt-1 hover:cursor-pointer hover:scale-110 transition-all ease-in-out duration-100"
                    @click="openModal(user.user_id)"
                    v-if="
                      !checkIsMod(user.user_id) &&
                      user.user_id != props.owner_id
                    "
                  />
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
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
  import { checkIsDark } from "../../utils/client.utils";

  const props = defineProps<{
    workspace_id: number;
    user_list: UserLite[];
    mod_list: UserLite[];
    coll_list: UserLite[];
    owner_id: number;
  }>();

  const isDark = ref<boolean>();

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

  function checkIsCollaborator(id: number) {
    return (
      // props.mod_list.filter((u: UserLite) => u.user_id != id).length > 0 &&
      // props.owner_id != id &&
      props.coll_list.filter((u: UserLite) => u.user_id == id).length > 0
    );
  }

  async function addCollab(id: number) {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_ADD_COLLABORATOR,
      {},
      {
        params: {
          ws_id: props.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite[];

    emit("update");
  }

  async function removeCollab(id: number) {
    const response = (await apiCall.patch(
      EndpointType.WORKSPACE_REMOVE_COLLABORATOR,
      {},
      {
        params: {
          ws_id: props.workspace_id,
          user_id: id,
        },
      }
    )) as UserLite[];
    emit("update");
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
    isDark.value = checkIsDark();
  });
</script>
