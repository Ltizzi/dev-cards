<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col gap-5 justify-center px-2 py-2 w-full divide-y-2 divide-secondary"
    >
      <div class="flex flex-col">
        <h1 class="text-xl text-center">Select dependency:</h1>
      </div>

      <input
        type="text"
        placeholder="Search task"
        class="input input-bordered input-primary w-full max-w-xs mx-auto mb-2"
        v-model="search_value"
      />
      <ul
        class="flex flex-row pt-5 flex-wrap lg:gap-5 lg:h-full h-96 overflow-y-auto mx-auto"
      >
        <li
          v-for="task in filtered_tasks"
          class="flex flex-row gap-2 min-w-60 max-w-60 hover:cursor-pointer transition-all hover:bg-slate-600 py-1 px-2 rounded-xl ease-in-out duration-300 my-auto"
          @click="selectTask(task)"
        >
          <span
            :class="[
              'rounded-full p-2 h-3 w-3 my-auto',
              taskUtils.getColor(task.color),
            ]"
          >
          </span>
          {{ taskUtils.stringShortener(task.title) }}
        </li>
      </ul>
      <div class="w-full flex flex-col py-2">
        <p class="flex flex-row gap-2 pt-2 mx-auto" v-if="selectedTask">
          Selected:
          <span
            :class="[
              'rounded-full p-2 h-3 w-3 my-auto',
              taskUtils.getColor(selectedTask.color),
            ]"
          ></span>
          {{ selectedTask.title }}
        </p>
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
          @click="addDependency()"
        >
          Add
          <span
            class="loading loading-dots loading-sm"
            v-if="awaitingResponse"
          ></span>
        </button>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import BaseModal from "../common/BaseModal.vue";
  import { taskUtils } from "../../utils/task.utils";
  import { onBeforeMount, ref, watch } from "vue";
  import { Task, TaskLite, Workspace } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { useTaskStore } from "../../store/task.store";

  const tasks = ref<Array<TaskLite>>();

  const filtered_tasks = ref<TaskLite[]>();

  const projectStore = useProjectStore();
  const taskStore = useTaskStore();
  const apiCall = useApiCall();

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);

  const selectedTask = ref<TaskLite | null>();

  const awaitingResponse = ref<boolean>();
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  const search_value = ref<string>();

  function closeModal() {
    //selectedTask.value = {} as TaskLite;
    emit("close", false);
  }

  function selectTask(task: TaskLite) {
    selectedTask.value = task;
  }

  async function addDependency() {
    awaitingResponse.value = true;
    if (selectedTask.value?.task_id) {
      const id = taskStore.currentTask.task_id as number;
      const parent_id = selectedTask.value.task_id;
      const response = (await taskStore.addDependency(id, parent_id)) as Task;
      // const response = (await apiCall.post(
      //   EndpointType.TASK_ADD_DEPENDENCY,
      //   null,
      //   {
      //     params: {
      //       task_id: taskStore.currentTask.task_id,
      //       parent_id: selectedTask.value.task_id,
      //     },
      //   }
      // )) as Task;
      awaitingResponse.value = false;
      if (response.task_id) {
        success.value = true;
        taskStore.setCurrentTask(response);
        setTimeout(() => {
          success.value = false;
          closeModal();
        }, 2000);
      }
    } else {
      failed.value = true;
      errorMessage.value = "Pick a dependency";
      setTimeout(() => {
        failed.value = false;
        errorMessage.value = "";
      }, 3000);
    }
  }

  function filterTask(tasks: Array<TaskLite>) {
    return tasks
      ? tasks.filter(
          (task: TaskLite) =>
            task.task_id != taskStore.currentTask.task_id &&
            !checkDependency(task)
        )
      : [];
  }

  function checkDependency(task: TaskLite) {
    if (taskStore.currentTask.dependencies)
      return (
        taskStore.currentTask.dependencies?.filter(
          (d: TaskLite) => d.task_id == task.task_id
        ).length > 0
      );
  }

  watch(
    () => props.showModal,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        selectedTask.value = null;
        const ws = projectStore.getCurrent() as Workspace;
        tasks.value = filterTask(ws.tasks);
      }
    }
  );

  watch(
    () => search_value.value,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        filtered_tasks.value = [];
        filtered_tasks.value = taskUtils.searchTasks(
          newValue,
          tasks.value as TaskLite[]
        );
      }
    }
  );

  onBeforeMount(() => {
    tasks.value = filterTask(projectStore.current.tasks);
    filtered_tasks.value = tasks.value;
    //console.log(tasks.value);
  });
</script>
<style lang=""></style>
