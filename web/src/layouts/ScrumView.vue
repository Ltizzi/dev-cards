<template lang="">
  <div class="pt-5">
    <h1 class="text-center text-2xl">Scrum view</h1>
    <div class="flex flex-row py-5">
      <label
        class="input input-bordered flex items-center gap-2 input-secondary"
      >
        Search
        <input
          type="text"
          class="grow"
          placeholder="by Tag or Title/subtitle"
          v-model="search"
        />
      </label>
    </div>

    <div
      class="w-full grid border-2 border-primary grid-cols-5 text-xl font-semibold text-center text-base-content rounded-t-xl"
    >
      <div class="w-80 py-3 border-r-2 border-r-primary"><h1>Pool</h1></div>
      <div class="w-80 py-3 border-r-2 border-r-primary">
        <h1>Top Priority</h1>
      </div>
      <div class="w-80 py-3 border-r-2 border-r-primary">
        <h1>In Progress</h1>
      </div>
      <div class="w-80 py-3 border-r-2 border-r-primary"><h1>Testing</h1></div>
      <div class="w-80 py-3"><h1>Complete</h1></div>
    </div>
    <div
      class="w-full grid grid-cols-5 border-x-2 border-b-2 border-primary rounded-b-xl shadow-lg shadow-slate-900"
    >
      <div class="w-80 border-r-2 border-r-primary min-h-96">
        <TaskList :tasks="pool" :isMicro="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="top_priority" :isMicro="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="in_progress" :isMicro="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="testing" :isMicro="false" />
      </div>
      <div class="w-80">
        <TaskList :tasks="completed" :isMicro="false" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import {
    Priority,
    Progress,
    Status,
    TaskLite,
    TaskType,
  } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import { useProjectStore } from "../store/project.store";

  const projectStore = useProjectStore();

  const tasks = ref<Array<TaskLite>>([]);

  const pool = ref<Array<TaskLite>>([]);
  const top_priority = ref<Array<TaskLite>>([]);
  const in_progress = ref<Array<TaskLite>>([]);
  const testing = ref<Array<TaskLite>>([]);
  const completed = ref<Array<TaskLite>>([]);
  const search = ref<string>("");

  watch(
    () => getTasks(),
    (newValue, oldValue) => {
      if (newValue.length != oldValue.length) {
        tasks.value = getTasks();
        prepareTemplate(getTasks());
      }
    }
  );

  watch(
    () => search.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        let searched_tasks = searchTasks(newValue);
        if (searched_tasks) {
          prepareTemplate(searched_tasks);
        }
      }
    }
  );

  function prepareTemplate(inputTasks: Array<TaskLite>) {
    clearColumns();
    inputTasks.forEach((task: TaskLite) => {
      if (task.task_type != TaskType.DOCUMENTATION) {
        if (
          !hasAssignedUser(task) &&
          task.status == Status.PENDING &&
          task.priority != Priority.VERY_HIGH
        ) {
          pool.value.push(task);
        } else if (
          !hasAssignedUser(task) &&
          task.priority == Priority.VERY_HIGH
        ) {
          top_priority.value.push(task);
        } else if (
          hasAssignedUser(task) &&
          task.status == Status.PROGRESS &&
          task.progress != Progress.ADVANCE
        ) {
          in_progress.value.push(task);
        } else if (hasAssignedUser(task) && task.status == Status.TESTING) {
          testing.value.push(task);
        } else if (
          hasAssignedUser(task) &&
          task.progress == Progress.ADVANCE &&
          task.status == Status.COMPLETED
        ) {
          completed.value.push(task);
        }
      }
    });
  }

  function searchTasks(searchValue: string): TaskLite[] {
    let returned_tasks: TaskLite[] = [];
    searchValue = searchValue.toLowerCase();
    if (!searchValue) {
      return tasks.value;
    }
    tasks.value.forEach((task: TaskLite) => {
      if (task.title.toLowerCase().includes(searchValue)) {
        returned_tasks.push(task);
        return;
      }
      task.task_tags.forEach((tag: string) => {
        if (tag.toLowerCase().includes(searchValue)) {
          returned_tasks.push(task);
          return;
        }
      });
    });
    return returned_tasks;
  }

  function clearColumns() {
    pool.value = [];
    top_priority.value = [];
    in_progress.value = [];
    testing.value = [];
    completed.value = [];
  }

  function hasAssignedUser(task: TaskLite) {
    return task.hasUsers;
  }

  function getTasks() {
    return projectStore.current.tasks;
  }

  onBeforeMount(() => {
    tasks.value = getTasks();
    prepareTemplate(getTasks());
  });
</script>
<style lang=""></style>
