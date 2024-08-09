<template lang="">
  <div
    :class="[
      'flex  flex-wrap justify-start gap-5 my-5 flex-row ml-4',
      props.isMicro ? '' : '',
    ]"
  >
    <div v-for="task in props.tasks">
      <TaskMiniCard
        :task="task"
        :isMicro="props.isMicro"
        :isDarkTheme="props.isDark"
      />
      <!--         :isDraggable="props.isDraggable"
        :col_name="props.col_name"
        @dropped="justDrop" -->
    </div>
  </div>
</template>
<script setup lang="ts">
  import { defineProps, ref } from "vue";
  import TaskMiniCard from "./TaskMiniCard.vue";
  import { Task } from "../../utils/types";

  const props = defineProps<{
    tasks: Task[];
    isMicro: boolean;
    isDark: boolean | null;
    // isDraggable: boolean;
    // col_name: string;
  }>();

  // const emit = defineEmits(["dropped"]);

  // function justDrop(col_name: string, pos: any, task: TaskLite) {
  //   emit("dropped", col_name, pos, task);
  // }

  //const tasks = ref<Array<Task>>();
</script>
