<template lang="">
  <div class="min-h-screen">
    <h1 class="text-4xl mt-5 text-base-content text-center pb-0">
      Blocked Tasks View
    </h1>
    <TaskListFilters
      :tasks="tasks"
      @changeSize="changeIconSize"
      @filterTasks="setFilteredTasks"
      @noResults="noResults"
    />

    <TaskList :tasks="tasks" :isDark="isDark" :isMicro="isMicro" />
    <div
      role="alert"
      class="alert alert-info mt-20 flex flex-row justify-center"
      v-if="state.noResults"
    >
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
      <span class="text-center text-2xl font-semibold text-info-content">
        No results.</span
      >
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, reactive, watch } from "vue";
  import { useProjectStore } from "../store/project.store";
  import { TaskLite } from "../utils/types";
  import { taskUtils } from "../utils/task.utils";
  import { checkIsDark } from "../utils/client.utils";
  import TaskList from "../components/task/TaskList.vue";
  import TaskListFilters from "../components/ui/TaskListFilters.vue";

  const tasks = ref<TaskLite[]>();
  const filteredTasks = ref<TaskLite[]>();

  const state = reactive({
    noResults: false,
  });

  const projectStore = useProjectStore();
  const isDark = ref<boolean>();
  const isMicro = ref<boolean>();

  watch(
    () => state.noResults,
    (newValue, oldValue) => {
      if (newValue) {
        setTimeout(() => {
          state.noResults = false;
        }, 1500);
      }
    }
  );

  function changeIconSize() {
    isMicro.value = !isMicro.value;
  }

  function setFilteredTasks(tasks: TaskLite[]) {
    if (tasks.length == 0) {
      state.noResults = true;
      filteredTasks.value = tasks;
    } else {
      state.noResults = false;
      filteredTasks.value = tasks;
    }
  }

  function noResults() {
    state.noResults = true;
  }

  onBeforeMount(() => {
    tasks.value = taskUtils.searchBlockedTasks(projectStore.current.tasks);
    if (tasks.value.length == 0) {
      state.noResults = true;
    }

    isDark.value = checkIsDark();
  });
</script>
