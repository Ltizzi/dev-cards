<template lang="">
  <div>
    <!-- ref="task_card" :style="style" -->
    <div class="card bg-base-100 w-72 shadow-xl" v-if="!props.isMicro">
      <!--      :style="style" -->
      <div
        :class="[
          'bg-white text-slate-700 rounded-xl transition-all ease-in-out duration-300 hover:scale-105 max-h-48 min-h-48',
        ]"
      >
        <div :class="['h-7 w-full -mb-5 rounded-t-xl z-10', color]"></div>
        <div class="px-3 py-7">
          <div class="flex flex-row justify-between my-auto align-middle">
            <h2 class="card-title w-4/6">
              {{ generateTitle(props.task.title, 12) }}
            </h2>
            <h3
              :class="[
                'font-bold mt-1 text-base text-end  w-2/5',
                priority_color,
              ]"
            >
              {{ generatePriority(props.task.priority) }}
            </h3>
          </div>
          <div class="flex flex-col justify-start gap-2 pt-3">
            <h3>{{ props.task.status }}</h3>
          </div>
          <div class="card-actions justify-end relative mr-2.5">
            <button
              class="btn btn-primary text-white absolute top-5 -right-2.5"
              @click="goToTask()"
            >
              Enter
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="card bg-base-100 w-32 shadow-xl" v-if="props.isMicro">
      <!--       :style="style" -->
      <!-- @mousedown="dragMouseDown" -->
      <div
        :class="[
          'bg-white text-slate-700 rounded-xl transition-all ease-in-out duration-300 hover:scale-105 max-h-24 min-h-24 relaive',
        ]"
      >
        <!--    props.isDraggable ? 'hover:cursor-pointer' : '', -->
        <div :class="['h-5 w-full -mb-5 rounded-t-xl z-10', color]"></div>
        <div class="pt-4 mx-auto flex text-center">
          <h2 class="w-full text-base text-center leading-4 mt-2 mx-0.5">
            {{ generateTitle(props.task.title, 20) }}
          </h2>
          <button
            class="btn btn-xs btn-primary text-white absolute bottom-1 left-9"
            @click="goToTask()"
          >
            Enter
          </button>
        </div>
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

  const props = defineProps<{
    task: Task;
    isMicro: boolean;
    // isDraggable: boolean;
    // col_name: string;
  }>();

  const emit = defineEmits(["dropped"]);

  const color = ref<string>();

  const priority_color = ref<string>();

  const progress = ref<number>(0);

  const task_card = ref<HTMLElement | null>();

  // const { x, y, style } = useDraggable(task_card, {
  //   preventDefault: true,
  //   onStart: (pos: any, e: PointerEvent) => {
  //     pos.x = e.clientX;
  //     pos.y = e.clientY;
  //   },
  //   onEnd: (pos: any, e: PointerEvent) => {
  //     let mousePos = {
  //       x: e.clientX,
  //       y: e.clientY,
  //     };
  //     emit("dropped", props.col_name, mousePos, props.task);
  //   },
  //   disabled: !props.isDraggable,
  // });

  function goToTask() {
    router.push(`/project/task?id=${props.task.task_id}`);
  }

  function generatePriority(priority: string) {
    let divided = priority.split("_");

    if (divided.length > 1) {
      return divided[0] + " " + divided[1];
    } else return priority;
  }

  function generateTitle(title: string, num: number) {
    if (title.length > num) {
      let splited_title = title.slice(0, num);
      //console.log(splited_title);
      return splited_title + "...";
    } else return title;
  }

  onBeforeMount(() => {
    color.value = taskUtils.getColor(props.task.color);
    if (props.task.status != Status.COMPLETED) {
      progress.value = taskUtils.calcProgress(props.task.progress);
    } else {
      progress.value = 100;
    }
    priority_color.value = taskUtils.calcPriorityColor(props.task.priority);
  });
</script>
