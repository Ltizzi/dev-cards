<template lang="">
  <div
    :class="[
      'flex  flex-wrap  justify-start gap-3 my-5 flex-row lg:ml-5 ml-6   w-full pb-24 ',
    ]"
  >
    <div v-for="task in props.tasks">
      <TaskMiniCard
        :task="task"
        :isMicro="props.isMicro"
        :isDarkTheme="props.isDark"
        :viewList="props.viewList"
      />
      <!-- :darkerCards="props.darkerCards" -->

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
  import { useUIStore } from "../../store/ui.store";

  const props = defineProps<{
    tasks: Task[];
    isMicro: boolean;
    isDark?: boolean | null;
    //darkerCards: boolean | null;
    viewList?: boolean;
    // isDraggable: boolean;
    // col_name: string;
  }>();

  const UIStore = useUIStore();

  const isDark = ref<boolean>();

  // const emit = defineEmits(["dropped"]);

  // function justDrop(col_name: string, pos: any, task: TaskLite) {
  //   emit("dropped", col_name, pos, task);
  // }

  //const tasks = ref<Array<Task>>();

  onBeforeMount(() => {
    if (!props.isDark) {
      isDark.value = UIStore.checkIsDarkTheme();
    } else isDark.value = props.isDark as boolean;
  });
</script>
