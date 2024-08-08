<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col gap-5 justify-center px-2 py-2 w-full"
    >
      <ul>
        <li v-for="user in users">
          <div class="form-control">
            <label class="cursor-pointer label">
              <div class="flex flex-row">
                <div class="avatar">
                  <div class="w-5 rounded-full">
                    <img :src="user.avatar" />
                  </div>
                </div>
                <span class="label-text text-neutral-content">{{
                  user.username
                }}</span>
              </div>
              <input
                type="checkbox"
                :checked="getChecked(user.user_id)"
                :disabled="getChecked(user.user_id)"
                class="checkbox checkbox-success"
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
        <span>Error! Can't add user.</span>
      </div>
      <button
        class="btn btn-outline btn-primary flex flex-row"
        @click="addUsers()"
      >
        Assign Users
        <span
          class="loading loading-dots loading-sm"
          v-if="awaitingResponse"
        ></span>
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  import { Task, UserLite } from "../../utils/types";
  import BaseModal from "../common/BaseModal.vue";
  import { useProjectStore } from "../../store/project.store";
  import { EndpointType } from "../../utils/endpoints";

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);
  const apiCall = useApiCall();
  const taskStore = useTaskStore();
  const projectStore = useProjectStore();

  const users = ref<Array<UserLite>>();
  const checkedUsers = ref<Array<UserLite>>([]);

  const awaitingResponse = ref<boolean>();
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  function closeModal() {
    emit("close", false);
  }

  function getChecked(id: number) {
    let arr = [];
    const list = taskStore.currentTask.designated_to;
    if (list) {
      list.forEach((user: UserLite) => {
        if (user.user_id == id) arr.push(user);
      });

      return arr.length > 0;
    } else {
      return false;
    }
  }

  function checkUser(user: UserLite) {
    if (checkedUsers.value.includes(user)) {
      const index = checkedUsers.value.indexOf(user);
      checkedUsers.value.splice(index, 1);
    } else {
      checkedUsers.value.push(user);
    }
  }

  async function addUsers() {
    awaitingResponse.value = true;
    checkedUsers.value.forEach(async (user: UserLite) => {
      const response = (await apiCall.post(
        EndpointType.TASK_ASSIGN_USER,
        null,
        {
          params: {
            task_id: taskStore.currentTask.task_id,
            user_id: user.user_id,
          },
        }
      )) as Task;
      awaitingResponse.value = false;
      if (response.task_id) {
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

  onBeforeMount(() => {
    users.value = projectStore.current.users;
  });
</script>
<style lang=""></style>
