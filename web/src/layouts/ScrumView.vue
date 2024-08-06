<template lang="">
  <div class="pt-10">
    <div
      class="w-full grid border-2 border-primary grid-cols-5 text-xl font-semibold text-center rounded-t-xl"
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
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="pool" :isRow="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="top_priority" :isRow="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="in_progress" :isRow="false" />
      </div>
      <div class="w-80 border-r-2 border-r-primary">
        <TaskList :tasks="testing" :isRow="false" />
      </div>
      <div class="w-80">
        <TaskList :tasks="complete" :isRow="false" />
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
    Task,
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
  const complete = ref<Array<TaskLite>>([]);

  watch(
    () => getTasks(),
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        tasks.value = getTasks();
        prepareTemplate();
      }
    }
  );

  function prepareTemplate() {
    tasks.value.forEach((task: TaskLite) => {
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
          complete.value.push(task);
        }
      }
    });
    console.log(pool.value);
    console.log(top_priority.value);
    console.log(in_progress.value);
    console.log(testing.value);
    console.log(complete.value);
  }

  function hasAssignedUser(task: TaskLite) {
    return task.hasUsers;
  }

  function getTasks() {
    return projectStore.current.tasks;
  }

  onBeforeMount(() => {
    tasks.value = getTasks();
    prepareTemplate();
  });
</script>
<style lang=""></style>
