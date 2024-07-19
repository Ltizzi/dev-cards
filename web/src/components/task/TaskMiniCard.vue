<template lang="">
  <div class="card bg-base-100 w-72 shadow-xl">
    <div
      class="bg-white text-slate-700 rounded-xl transition-all hover:scale-105 max-h-48 min-h-48"
    >
      <div :class="['h-7 w-full -mb-5 rounded-t-xl z-10', color]"></div>
      <div class="px-5 py-7">
        <div class="flex flex-row justify-between my-auto align-middle">
          <h2 class="card-title">{{ generateTitle(props.task.title) }}</h2>
          <h3 :class="['font-bold mt-1 text-end', priority_color]">
            {{ generatePriority(props.task.priority) }}
          </h3>
        </div>
        <div class="flex flex-col justify-start gap-2 pt-3">
          <h3>{{ props.task.status }}</h3>
          <!-- <progress
            class="progress progress-success w-56 py-2"
            :value="progress"
            max="100"
            v-if="progress > 0"
          ></progress> -->

          <div class="h-16">
            <div
              v-if="task_tags"
              class="flex flex-row flex-nowrap justify-between"
            >
              <h4 v-for="tag in task_tags">{{ tag }}</h4>
            </div>
          </div>
        </div>
        <div class="card-actions justify-end relative">
          <button
            class="btn btn-primary text-white absolute -top-10"
            @click="goToTask()"
          >
            Enter
          </button>
        </div>

        <!-- <p>{{ props.task.description }}</p> -->
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { defineProps, onBeforeMount, ref } from "vue";
  import { Task, Status } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";
  import { useRouter } from "vue-router";

  const router = useRouter();

  const props = defineProps<{ task: Task }>();

  const color = ref<string>();

  const priority_color = ref<string>();

  const progress = ref<number>(0);

  function goToTask() {
    router.push(`/project/task?id=${props.task.task_id}`);
  }

  function generatePriority(priority: string) {
    let divided = priority.split("_");

    if (divided.length > 1) {
      return divided[0] + " " + divided[1];
    } else return priority;
  }

  function generateTitle(title: string) {
    if (title.length > 15) {
      let splited_title = title.slice(0, 15);
      console.log(splited_title);
      return splited_title + "...";
    } else return title;
  }

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
