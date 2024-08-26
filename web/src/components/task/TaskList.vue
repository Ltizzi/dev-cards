<template lang="">
  <div
    :class="['flex  flex-wrap justify-start gap-5 my-5 flex-row ml-4 w-full']"
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
  import { defineProps, ref, onBeforeMount } from "vue";
  import TaskMiniCard from "./TaskMiniCard.vue";
  import { Task } from "../../utils/types";
  import { checkThemeIsDark, getActualTheme } from "../../utils/client.utils";

  const props = defineProps<{
    tasks: Task[];
    isMicro: boolean;
    isDark: boolean | null;
    // isDraggable: boolean;
    // col_name: string;
  }>();

  const isDark = ref<boolean>();

  // const emit = defineEmits(["dropped"]);

  // function justDrop(col_name: string, pos: any, task: TaskLite) {
  //   emit("dropped", col_name, pos, task);
  // }

  //const tasks = ref<Array<Task>>();

  onBeforeMount(() => {
    if (!props.isDark) {
      isDark.value = checkThemeIsDark();
    } else isDark.value = props.isDark as boolean;
  });
</script>
