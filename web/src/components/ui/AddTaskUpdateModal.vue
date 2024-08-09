<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col gap-5 justify-center px-5 py-3 w-full divide-y-2 divide-secondary min-w-max text-neutral"
    >
      <h1 class="text-center text-xl py-5">Post a task update</h1>
      <textarea
        class="textarea textarea-secondary textarea-lg"
        cols="70"
        rows="5"
        placeholder="Write update info"
        v-model="description"
      ></textarea>
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
        class="btn btn-outline btn-primary w-20 mx-auto mt-5"
        @click="addUpdate()"
      >
        Add
        <span
          class="loading loading-dots loading-sm"
          v-if="awaitingResponse"
        ></span>
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { useUserStore } from "../../store/user.store";
  import BaseModal from "../common/BaseModal.vue";
  import { TaskUpdate, User } from "../../utils/types";
  import { useRoute } from "vue-router";
  import { onBeforeMount, ref, watch } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";

  const userStore = useUserStore();
  const route = useRoute();
  const apiCall = useApiCall();

  const user = ref<User>();
  const task_id = ref<number>();
  const description = ref<string>();

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);

  const awaitingResponse = ref<boolean>(false);
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  function closeModal() {
    emit("close", false);
  }

  async function addUpdate() {
    awaitingResponse.value = true;
    if (
      description.value?.length &&
      description.value?.length > 0 &&
      user.value
    ) {
      const taskUpdate: TaskUpdate = {
        creator_user_id: user.value.user_id,
        creator_username: user.value.username,
        description: description.value,
      };
      const response = (await apiCall.post(
        EndpointType.TASK_ADD_UPDATE,
        taskUpdate,
        {
          params: {
            task_id: task_id.value,
          },
        }
      )) as Array<TaskUpdate>;
      awaitingResponse.value = false;
      if (response.length > 0) {
        success.value = true;
        setTimeout(() => {
          success.value = false;
          description.value = "";
        }, 2000);
      } else {
        setFailed("Something went wrong");
      }
    } else {
      setFailed("Description can't be empty");
    }
  }

  function setFailed(msg: string) {
    failed.value = true;
    errorMessage.value = msg;
    setTimeout(() => {
      failed.value = false;
      errorMessage.value = "";
    }, 2000);
  }

  watch(
    () => route.query.id,
    (newValue, oldValue) => {
      if (newValue != oldValue && route.query.id) {
        task_id.value = +route.query.id;
      }
    }
  );

  onBeforeMount(() => {
    if (route.query.id) {
      task_id.value = +route.query.id;
    } else {
      setFailed("Can't get task id");
    }
    user.value = userStore.self;
  });
</script>
