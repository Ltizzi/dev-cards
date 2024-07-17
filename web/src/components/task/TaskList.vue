<template lang="">
  <div class="flex flex-row flex-wrap justify-center">
    <TaskMiniCard v-for="task in props" />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, defineProps } from "vue";
  import TaskMiniCard from "./TaskMiniCard.vue";
  import { Task } from "../../utils/types";

  const props = defineProps<{ tasks: Task[] }>();
</script>
