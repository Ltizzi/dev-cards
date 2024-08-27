<template lang="">
  <div class="text-center w-full min-h-screen">
    <h1 class="text-4xl mt-5 text-base-content">All Tasks View</h1>
    <!-- <div class="flex flex-row justify-between py-5">
      <TaskFilterInput @search="defineSearch" class="ml-0" />
      <div class="flex flex-row gap-1 my-auto">
        <TaskPropFilters @selected="filterByOptions" />
        <ChangeCardSizeBtn
          @changeIconSize="changeIconSize"
          class="mr-16 mt-1"
        />
      </div>
    </div> -->
    <TaskListFilters
      :tasks="tasks"
      @changeSize="changeIconSize"
      @filterTasks="setFilteredTasks"
      @noResults="noResults"
    />
    <TaskList :isMicro="isMicro" :tasks="getTasks()" class="-ml-1" />

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
  import { onBeforeMount, ref, watch, reactive } from "vue";
  import { TaskLite, DropdownCheckboxOption } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  //import TaskFilterInput from "../components/ui/TaskFilterInput.vue";
  //import ChangeCardSizeBtn from "../components/ui/ChangeCardSizeBtn.vue";
  import { useProjectStore } from "../store/project.store";
  //import { taskUtils } from "../utils/task.utils";
  import TaskListFilters from "../components/ui/TaskListFilters.vue";

  const projectStore = useProjectStore();

  const tasks = ref<TaskLite[]>();

  const filteredTasks = ref<TaskLite[]>();

  const isMicro = ref<boolean>(true);
  const isDark = ref<boolean>();

  // const search = ref<string>();

  const state = reactive({
    // selectedColors: [] as any[],
    // selectedStatus: [] as any[],
    // selectedProgress: [] as any[],
    // selectedPriority: [] as any[],
    // selectedTypes: [] as any[],
    // selectedEffort: [] as any[],
    // selectedValues: [] as any[],
    // lastSelected: "" as string,
    lastSearchResultsCount: 0,
    noResults: false,
  });

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

  function noResults() {
    state.noResults = true;
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

  function changeIconSize() {
    isMicro.value = !isMicro.value;
  }

  function getTasks() {
    return filteredTasks.value && filteredTasks.value?.length > 0
      ? filteredTasks.value
      : tasks.value;
  }

  onBeforeMount(() => {
    tasks.value = projectStore.current.tasks;
    const darkTHeme = JSON.parse(localStorage.getItem("darkTheme") as string);
    isDark.value = darkTHeme;
  });

  // watch(
  //   () => search.value,
  //   (newValue, oldValue) => {
  //     let tasksToFilter =
  //       filteredTasks.value && filteredTasks.value?.length > 0 && newValue != ""
  //         ? filteredTasks.value
  //         : tasks.value;
  //     filteredTasks.value = taskUtils.searchTasks(
  //       newValue as string,
  //       tasksToFilter as TaskLite[]
  //     );
  //     state.noResults = checkNoResults();
  //     filterByOptions([], "");
  //   }
  // );

  // function filterByOptions(opts: any[], type: string) {
  //   state.selectedValues = opts;
  //   switch (type.toLowerCase()) {
  //     case "color":
  //       state.selectedColors = opts;

  //       break;
  //     case "status":
  //       state.selectedStatus = opts;
  //       break;
  //     case "progress":
  //       state.selectedProgress = opts;
  //       break;
  //     case "priority":
  //       state.selectedPriority = opts;
  //       break;
  //     case "type":
  //       state.selectedTypes = opts;
  //       break;
  //     case "effort":
  //       state.selectedEffort = opts;
  //       break;
  //   }
  //   if (opts.length == 0) {
  //     prepareCheckedValues();
  //   }
  //   if (
  //     filteredTasks.value &&
  //     filteredTasks.value.length > 0 &&
  //     type == state.lastSelected &&
  //     !search.value
  //   ) {
  //     filteredTasks.value = taskUtils.searchTasksByFilters(
  //       state.selectedValues,
  //       tasks.value as TaskLite[]
  //     );
  //   } else if (filteredTasks.value && filteredTasks.value.length > 0) {
  //     filteredTasks.value = taskUtils.searchTasksByFilters(
  //       state.selectedValues,
  //       filteredTasks.value as TaskLite[]
  //     );
  //   } else {
  //     console.log("paso el switch3");
  //     filteredTasks.value = taskUtils.searchTasksByFilters(
  //       state.selectedValues,
  //       tasks.value as TaskLite[]
  //     );
  //   }
  //   if (state.selectedValues.length > 0) state.noResults = checkNoResults();
  //   state.lastSelected = type;
  //   state.lastSearchResultsCount = filteredTasks.value.length;
  //   state.selectedValues = [];
  //   if (state.lastSearchResultsCount == 0 && search.value) {
  //     filteredTasks.value = taskUtils.searchTasks(
  //       search.value,
  //       tasks.value as TaskLite[]
  //     );
  //   }
  // }

  // function prepareCheckedValues() {
  //   state.selectedValues = state.selectedValues.concat(
  //     state.selectedColors,
  //     state.selectedEffort,
  //     state.selectedPriority,
  //     state.selectedProgress,
  //     state.selectedStatus,
  //     state.selectedTypes
  //   );
  // }

  // function defineSearch(value: string) {
  //   search.value = value;
  // }

  // function checkNoResults() {
  //   return (
  //     filteredTasks.value?.length == 0 ||
  //     filteredTasks.value?.length == state.lastSearchResultsCount
  //   );
  // }

  // function prepareOptionArr(arr: Array<any>) {
  //   return arr.map((opt: any) => {
  //     const newOpt = {} as DropdownCheckboxOption;
  //     newOpt.text = opt;
  //     newOpt.check = false;
  //     return newOpt;
  //   });
  // }
</script>
<style lang=""></style>
