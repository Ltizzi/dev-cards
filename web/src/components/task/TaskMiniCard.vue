<template lang="">
  <div class="card bg-base-100 w-72 shadow-xl">
    <div class="bg-white text-slate-700 rounded-xl">
      <div :class="['h-7 w-full -mb-5 rounded-t-xl z-10', color]"></div>
      <div class="px-7 py-7">
        <div class="flex flex-row justify-between">
          <h2 class="card-title">{{ props.task.title }}</h2>
          <h3 :class="['font-bold', priority_color]">
            {{ props.task.priority }}
          </h3>
        </div>
        <div class="flex flex-col justify-start gap-2 pt-2">
          <h3>{{ props.task.status }}</h3>
          <progress
            class="progress progress-success w-56 py-2"
            :value="progress"
            max="100"
            v-if="progress > 0"
          ></progress>

          <div class="flex flex-row justify-evenly">
            <h4 v-for="tag in task_tags">{{ tag }}</h4>
          </div>
        </div>

        <p>{{ props.task.description }}</p>

        <div class="card-actions justify-end">
          <button class="btn btn-primary text-white">Enter</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { defineProps, onBeforeMount, ref } from "vue";
  import { Task, Progress, Status, Priority } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";

  const props = defineProps<{ task: Task }>();

  const color = ref<string>();

  const priority_color = ref<string>();

  const progress = ref<number>(0);

  // function calcProgress(progress: Progress) {
  //   switch (progress) {
  //     case Progress.NULL:
  //       return 0;

  //     case Progress.NOT_FUNCTIONAL:
  //       return 30;

  //     case Progress.BASIC:
  //       return 45;

  //     case Progress.INTERMEDIATE:
  //       return 60;

  //     case Progress.ADVANCE:
  //       return 80;
  //     default:
  //       return 0;
  //   }
  // }

  // function calcPriorityColor(priority: Priority) {
  //   switch (priority) {
  //     case Priority.VERY_LOW:
  //       return "font_emerald";
  //     case Priority.LOW:
  //       return "font_green";
  //     case Priority.MEDIUM:
  //       return "font_lime";
  //     case Priority.HIGH:
  //       return "font_amber";
  //     case Priority.VERY_HIGH:
  //       return "font_red";
  //   }
  // }

  onBeforeMount(() => {
    color.value = taskUtils.getColor(props.task.color);
    if (props.task.status != Status.COMPLETED) {
      progress.value = taskUtils.calcProgress(props.task.progress);
      // progress.value = Math.floor(Math.random() * 100);
    } else {
      progress.value = 100;
    }
    priority_color.value = taskUtils.calcPriorityColor(props.task.priority);
  });
</script>
