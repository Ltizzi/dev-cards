<template lang="">
  <div class="flex flex-row flex-wrap justify-start gap-5 my-5">
    <TaskMiniCard v-for="task in props.tasks" :task="task" />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, defineProps, ref } from "vue";
  import TaskMiniCard from "./TaskMiniCard.vue";
  import { Task } from "../../utils/types";
  import { useTaskStore } from "../../store/task.store";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";

  const taskStore = useTaskStore();

  const apiCall = useApiCall();

  const props = defineProps<{ tasks: Task[] }>();

  const tasks = ref<Array<Task>>();

  onBeforeMount(async () => {
    const list = taskStore.currentProjectTasks;
    if (list.length > 0) {
      tasks.value = list;
    }
  });
</script>
