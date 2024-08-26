<template lang="">
  <div class="flex flex-row justify-between py-5">
    <TaskFilterInput @search="defineSearch" class="ml-0" />
    <div class="flex flex-row gap-1 my-auto">
      <TaskPropFilters @selected="filterByOptions" />
      <ChangeCardSizeBtn @changeIconSize="changeIconSize" class="mr-16 mt-1" />
    </div>
  </div>
</template>
<script setup lang="ts">
  import TaskFilterInput from "./TaskFilterInput.vue";
  import TaskPropFilters from "./TaskPropFilters.vue";
  import ChangeCardSizeBtn from "./ChangeCardSizeBtn.vue";
  import { TaskLite } from "../../utils/types";
  import { reactive, ref, watch } from "vue";
  import { taskUtils } from "../../utils/task.utils";

  const props = defineProps<{
    tasks: TaskLite[];
  }>();

  const filteredTasks = ref<TaskLite[]>();

  const search = ref<string>();

  const state = reactive({
    selectedColors: [] as any[],
    selectedStatus: [] as any[],
    selectedProgress: [] as any[],
    selectedPriority: [] as any[],
    selectedTypes: [] as any[],
    selectedEffort: [] as any[],
    selectedValues: [] as any[],
    lastSelected: "" as string,
    lastSearchResultsCount: 0,
    noResults: false,
  });

  const emit = defineEmits(["changeSize", "filterTasks", "noResults"]);

  watch(
    () => search.value,
    (newValue, oldValue) => {
      let tasksToFilter =
        filteredTasks.value && filteredTasks.value?.length > 0 && newValue != ""
          ? filteredTasks.value
          : props.tasks;
      filteredTasks.value = taskUtils.searchTasks(
        newValue as string,
        tasksToFilter as TaskLite[]
      );
      state.noResults = checkNoResults();
      filterByOptions([], "");
    }
  );

  watch(
    () => state.noResults,
    (newValue, oldValue) => {
      if (newValue) {
        emit("noResults");
        setTimeout(() => {
          state.noResults = false;
        }, 1500);
      }
    }
  );

  function defineSearch(value: string) {
    search.value = value;
  }

  function filterByOptions(opts: any[], type: string) {
    state.selectedValues = opts;
    switch (type.toLowerCase()) {
      case "color":
        state.selectedColors = opts;

        break;
      case "status":
        state.selectedStatus = opts;
        break;
      case "progress":
        state.selectedProgress = opts;
        break;
      case "priority":
        state.selectedPriority = opts;
        break;
      case "type":
        state.selectedTypes = opts;
        break;
      case "effort":
        state.selectedEffort = opts;
        break;
    }
    if (opts.length == 0) {
      prepareCheckedValues();
    }
    if (
      filteredTasks.value &&
      filteredTasks.value.length > 0 &&
      type == state.lastSelected &&
      !search.value
    ) {
      filteredTasks.value = taskUtils.searchTasksByFilters(
        state.selectedValues,
        props.tasks as TaskLite[]
      );
    } else if (filteredTasks.value && filteredTasks.value.length > 0) {
      filteredTasks.value = taskUtils.searchTasksByFilters(
        state.selectedValues,
        filteredTasks.value as TaskLite[]
      );
    } else {
      filteredTasks.value = taskUtils.searchTasksByFilters(
        state.selectedValues,
        props.tasks as TaskLite[]
      );
    }
    if (state.selectedValues.length > 0) state.noResults = checkNoResults();
    state.lastSelected = type;
    state.lastSearchResultsCount = filteredTasks.value.length;
    state.selectedValues = [];
    if (state.lastSearchResultsCount == 0 && search.value) {
      filteredTasks.value = taskUtils.searchTasks(
        search.value,
        props.tasks as TaskLite[]
      );
    }
    if (filteredTasks.value) emit("filterTasks", filteredTasks.value);
    // else emit("noResults");
  }

  function changeIconSize() {
    emit("changeSize");
  }

  function prepareCheckedValues() {
    state.selectedValues = state.selectedValues.concat(
      state.selectedColors,
      state.selectedEffort,
      state.selectedPriority,
      state.selectedProgress,
      state.selectedStatus,
      state.selectedTypes
    );
  }

  function checkNoResults() {
    return (
      filteredTasks.value?.length == 0 ||
      filteredTasks.value?.length == state.lastSearchResultsCount
    );
  }
</script>
<style lang=""></style>
