<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 text-start flex flex-col gap-5 justify-center px-2 py-2 w-full"
    >
      <h1 class="py-5 text-2xl">
        You are about to delete the task with id {{ task_id }}, are you sure?
      </h1>

      <h2 class="py-2 text-lg text-center">
        Write the task title ({{ task_title }})
      </h2>
      <input
        v-model="input_name"
        type="text"
        placeholder="Write task title"
        class="input input-bordered input-error w-full max-w-xs mx-auto"
      />
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
        <span>Error! {{ errorMessage }}</span>
      </div>
      <button class="btn btn-outline btn-error" @click="deleteTask()">
        DELETE
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { useRoute, useRouter } from "vue-router";
  //import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  //import { EndpointType } from "../../utils/endpoints";
  import BaseModal from "../common/BaseModal.vue";
  import { ref, onBeforeMount, watch } from "vue";
  import { useProjectStore } from "../../store/project.store";

  const taskStore = useTaskStore();
  //const apiCall = useApiCall();
  const router = useRouter();
  const route = useRoute();

  const task_id = ref<number | null>();
  const task_title = ref<string>();
  const task_input = ref<string>();

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);

  const awaitingResponse = ref<boolean>(false);
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  function closeModal() {
    emit("close", false);
  }

  function setFailed(msg: string) {
    failed.value = true;
    errorMessage.value = msg;
    setTimeout(() => {
      failed.value = false;
      errorMessage.value = "";
    }, 2000);
  }

  async function deleteTask() {
    // if (task_title.value?.toLowerCase() == task_input.value?.toLowerCase()) {
    awaitingResponse.value = true;
    const response = (await taskStore.deleteTask(
      task_id.value as number
    )) as any;
    //  (await apiCall.del(EndpointType.TASK_DELETE, {
    //   params: { id: task_id.value },
    // })) as any;
    awaitingResponse.value = false;
    if (response.message && response.message.toLowerCase() == "task deleted") {
      success.value = true;

      setTimeout(async () => {
        taskStore.removeCurrent();
        reset();
        success.value = false;
        const projectStore = useProjectStore();
        const project_id = projectStore.current.workspace_id;
        await projectStore.updateCurrent();
        router.push(`/project/info?id=${project_id}`);
      }, 2000);
    } else {
      setFailed("Something went worng!");
    }
    // } else setFailed("Write the task title");
  }

  watch(
    () => props.showModal,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        reset();
        getData();
        // if (route.path == "project/task" && route.query.id) {
        //   task_id.value = +route.query.id;
        //   task_title.value = taskStore.currentTask.title;
        // }
      }
    }
  );

  function getData() {
    task_id.value = taskStore.currentTask.task_id;
    task_title.value = taskStore.currentTask.title;
  }

  function reset() {
    task_id.value = null;
    task_input.value = "";
    task_title.value = "";
  }

  onBeforeMount(() => {
    getData();
  });
</script>
