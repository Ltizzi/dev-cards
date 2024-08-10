<template lang="">
  <div class="text-center">
    <h1 class="text-4xl mt-5 mb-7 text-base-content">All Tasks View</h1>
    <TaskList :isMicro="false" :tasks="tasks" />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { TaskLite } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";

  import { useProjectStore } from "../store/project.store";

  const projectStore = useProjectStore();

  const tasks = ref<TaskLite[]>();

  onBeforeMount(() => {
    tasks.value = projectStore.current.tasks;
  });
</script>
<style lang=""></style>
