<template lang="">
  <div class="text-center w-full min-h-screen">
    <h1 class="text-4xl mt-5 text-base-content">All Tasks View</h1>

    <TaskListFilters
      :tasks="tasks"
      :tagSearch="state.searchedByTag"
      @changeSize="changeIconSize"
      @filterTasks="setFilteredTasks"
      @noResults="noResults"
      @handleTags="handleTags"
      @clearSearch="searchTasks(null)"
    />
    <TagNavigationPanel
      :ws_id="projectStore.current.workspace_id"
      :show="showTags"
      @update="searchTasks"
    />
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
  import { TaskLite, UITag } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import { useProjectStore } from "../store/project.store";
  import TaskListFilters from "../components/ui/TaskListFilters.vue";
  import TagNavigationPanel from "../components/ui/TagNavigationPanel.vue";
  import { useRoute, useRouter } from "vue-router";
  import { taskUtils } from "../utils/task.utils";

  const projectStore = useProjectStore();

  const tasks = ref<TaskLite[]>();

  const filteredTasks = ref<TaskLite[]>();

  const isMicro = ref<boolean>(true);
  const isDark = ref<boolean>();

  const showTags = ref<boolean>();

  const route = useRoute();
  const router = useRouter();

  const state = reactive({
    lastSearchResultsCount: 0,
    noResults: false,
    searchedByTag: false,
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
    () => route.query.tag,
    (newValue, oldValue) => {
      if (newValue) {
        console.log("tag:", newValue);
        state.searchedByTag = true;
        filteredTasks.value = taskUtils.searchTasksByTag(
          newValue as string,
          tasks.value as TaskLite[]
        );
        console.log(filteredTasks.value);
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

  function searchTasks(tag: UITag) {
    if (tag) {
      const search_value = tag.name;
      router.push(`/project/tasks?tag=${search_value}`);
    } else {
      router.push("/project/tasks");
      state.searchedByTag = false;
      filteredTasks.value = [];
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
    if (route.query.tag) {
      state.searchedByTag = true;
      filteredTasks.value = taskUtils.searchTasksByTag(
        route.query.tag as string,
        tasks.value as TaskLite[]
      );
    }
  });
</script>
<style lang=""></style>
