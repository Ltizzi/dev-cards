<template lang="">
  <div
    class="text-center min-h-screen h-full overflow-x-hidden 2xl:ml-0 xl:ml-6"
  >
    <h1 class="text-4xl mt-7 lg:mt-5 text-base-content">All Tasks View</h1>

    <TaskListFilters
      :ws_id="state.ws_id"
      :tasks="tasks"
      :tagSearch="state.searchedByTag"
      @changeSize="changeIconSize"
      @filterTasks="setFilteredTasks"
      @noResults="noResults"
      @handleTags="handleTags"
      @clearSearch="searchTasks(null)"
    />
    <!-- <TagNavigationPanel
      :ws_id="projectStore.current.workspace_id"
      :show="showTags"
      @update="searchTasks"
    /> -->
    <TaskList
      :isMicro="isMicro"
      :viewList="true"
      :tasks="getTasks()"
      class="-ml-1"
    />

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
  import { TaskLite, UITag, Workspace } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import { useProjectStore } from "../store/project.store";
  import TaskListFilters from "../components/ui/TaskListFilters.vue";
  import { useRoute, useRouter } from "vue-router";
  import { taskUtils } from "../utils/task.utils";
  import { useUIStore } from "../store/ui.store";

  const projectStore = useProjectStore();
  const UIStore = useUIStore();

  const tasks = ref<TaskLite[]>();

  const filteredTasks = ref<TaskLite[]>();

  const filteredByTags = ref<TaskLite[]>();

  const isMicro = ref<boolean>(true);
  const isDark = ref<boolean>();

  const showTags = ref<boolean>();

  const route = useRoute();
  const router = useRouter();

  const state = reactive({
    lastSearchResultsCount: 0,
    noResults: false,
    searchedByTag: false,
    ws_id: 0,
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

  watch(
    () => projectStore.getCurrent(),
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        state.ws_id = projectStore.getCurrent()?.workspace_id as number;
      }
    }
  );

  function handleTags() {
    showTags.value = !showTags.value;
  }

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
    let ws = projectStore.getCurrent() as Workspace;
    if (!ws) ws = projectStore.current;
    tasks.value = ws.tasks;
    state.ws_id = ws.workspace_id;
    isDark.value = UIStore.checkIsDarkTheme();
  });
</script>
<style lang=""></style>
