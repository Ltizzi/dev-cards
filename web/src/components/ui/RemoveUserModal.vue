<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col gap-5 justify-center px-2 py-2 bg-base bg-opacity-95 w-full"
    >
      <ul>
        <li v-for="user in users">
          <div class="form-control">
            <label class="cursor-pointer label">
              <div class="flex flex-row gap-5">
                <div class="avatar">
                  <div class="w-5 rounded-full">
                    <img :src="user.avatar" />
                  </div>
                </div>
                <span class="label-text">{{ user.username }}</span>
              </div>
              <!-- :checked="getChecked(user.user_id)
                 :disabled="getChecked(user.user_id)"
                class="checkbox checkbox-success""
                -->
              <input
                type="checkbox"
                class="checkbox checkbox-error ml-3"
                @change="checkUser(user)"
              />
            </label>
          </div>
        </li>
      </ul>

      <div role="alert" class="alert alert-success" v-if="success">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6 shrink-0 stroke-current"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span>Success!</span>
      </div>

      <div role="alert" class="alert alert-error" v-if="failed">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6 shrink-0 stroke-current"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span>Error! Can't remove user.</span>
      </div>
      <button
        class="btn btn-outline btn-primary flex flex-row"
        @click="removeUsers()"
      >
        Remove Users
        <span
          class="loading loading-dots loading-sm"
          v-if="awaitingResponse"
        ></span>
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import BaseModal from "../common/BaseModal.vue";
  //import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  import { Task, UserLite } from "../../utils/types";
  //import { EndpointType } from "../../utils/endpoints";
  import { useUserStore } from "../../store/user.store";

  //const apiCall = useApiCall();

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);
  const taskStore = useTaskStore();
  const userStore = useUserStore();

  const users = ref<Array<UserLite>>();
  const usersToDelete = ref<Array<UserLite>>([]);

  const awaitingResponse = ref<boolean>();
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  function checkUser(user: UserLite) {
    usersToDelete.value.push(user);
  }

  function closeModal() {
    emit("close", false);
  }

  function checkUserIsSelf(id: number) {
    return id == userStore.self.user_id;
  }

  async function removeUsers() {
    awaitingResponse.value = true;
    let addedSelf = false;
    usersToDelete.value.forEach(async (user: UserLite) => {
      if (!addedSelf) {
        addedSelf = checkUserIsSelf(user.user_id);
      }
      const response = (await taskStore.unassignUserFromTask(
        taskStore.currentTask.task_id as number,
        user.user_id
      )) as Task;
      //  (await apiCall.post(
      //   EndpointType.TASK_UNASSIGN_USER,
      //   null,
      //   {
      //     params: {
      //       task_id: taskStore.currentTask.task_id,
      //       user_id: user.user_id,
      //     },
      //   }
      // )) as Task;
      awaitingResponse.value = false;
      if (response.task_id) {
        if (addedSelf) userStore.refreshSelf();
        taskStore.setCurrentTask(response);
        success.value = true;
        setTimeout(() => {
          success.value = false;
          closeModal();
        }, 2000);
      } else {
        failed.value = true;
        errorMessage.value = JSON.stringify(response);
        setTimeout(() => {
          failed.value = false;
          errorMessage.value = "";
        }, 2000);
      }
    });
  }

  watch(
    () => taskStore.currentTask.designated_to,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        users.value = taskStore.currentTask.designated_to;
      }
    }
  );

  onBeforeMount(() => {
    users.value = taskStore.currentTask.designated_to;
  });
</script>
