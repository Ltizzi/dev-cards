<template lang="">
  <div>
    <!-- ref="task_card" :style="style" -->
    <div
      :class="[
        'card w-72 shadow-lg hover:z-50 -ml-1',
        isDark
          ? !props.darkerCards
            ? 'shadow-md  shadow-secondary'
            : 'shadow-accent-content bg-white'
          : 'shadow-accent-content',
      ]"
      v-if="!props.isMicro"
    >
      <!--      :style="style" -->
      <div
        :class="[
          ' rounded-xl transition-all ease-in-out duration-300 hover:scale-105 max-h-48 min-h-48',
          !isDark
            ? 'bg-base-100 text-base-content'
            : darkerCards
            ? 'bg-neutral bg-opacity-95 text-neutral-content'
            : 'text-base-300 bg-base-content',
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
              :class="[
                'btn btn-primary  absolute top-5 -right-2.5',
                isDark ? 'text-white' : 'text-base-100',
              ]"
              @click="goToTask()"
            >
              Enter
            </button>
          </div>
        </div>
      </div>
    </div>
    <div
      :class="[
        'card w-32 shadow-xl  hover:z-50',
        isDark
          ? !props.darkerCards
            ? 'shadow-sm  shadow-secondary'
            : 'shadow-accent-content bg-white'
          : 'bg-base-100',
        props.viewList ? 'mb-2' : '-mb-12',
      ]"
      v-if="props.isMicro"
    >
      <!--       :style="style" -->
      <!-- @mousedown="dragMouseDown" -->
      <div
        :class="[
          'rounded-xl transition-all ease-in-out duration-300 hover:scale-110 max-h-24 min-h-24 relative hover:shadow-2xl hover:shadow-neutral',
          !isDark
            ? 'bg-base-100 text-base-content'
            : darkerCards
            ? 'bg-neutral bg-opacity-95 text-neutral-content'
            : 'text-base-300 bg-base-content',
        ]"
      >
        <!--    props.isDraggable ? 'hover:cursor-pointer' : '', -->
        <div :class="['h-5 w-full -mb-5 rounded-t-xl z-10', color]"></div>
        <div class="pt-4 mx-auto flex text-center">
          <h2 class="w-full text-sm text-center leading-4 mt-2 mx-0.5">
            {{ generateTitle(props.task.title, 20) }}
          </h2>
          <button
            :class="[
              'btn btn-xs btn-primary absolute bottom-1 left-9',
              isDark ? 'text-white' : 'text-base-100',
            ]"
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
  import { defineProps, onBeforeMount, ref, watch } from "vue";
  import { Task, Status } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";
  import { useRoute, useRouter } from "vue-router";
  import { useProjectStore } from "../../store/project.store";
  import { isDarkerCardsActive } from "../../utils/client.utils";

  const router = useRouter();
  const route = useRoute();
  const projectStore = useProjectStore();

  const props = defineProps<{
    task: Task;
    isMicro: boolean;
    isDarkTheme: boolean;
    darkerCards: boolean | null;
    viewList: boolean | null;
    // isDraggable: boolean;
    // col_name: string;
  }>();

  const emit = defineEmits(["dropped"]);

  const color = ref<string>();

  const priority_color = ref<string>();

  const progress = ref<number>(0);

  const task_card = ref<HTMLElement | null>();

  const isDark = ref<boolean>();

  const darkerCards = ref<boolean>();

  watch(
    () => props.task,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        priority_color.value = taskUtils.calcPriorityColor(newValue.priority);
        color.value = taskUtils.getColor(newValue.color);
        progress.value = taskUtils.calcProgress(newValue.progress);
      }
    }
  );

  watch(
    () => props.isDarkTheme,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        isDark.value = newValue as boolean;
      }
    }
  );

  watch(
    () => props.darkerCards,
    (newValue, oldValue) => {
      if (newValue) {
        darkerCards.value = newValue as boolean;
      } else darkerCards.value = isDarkerCardsActive();
    }
  );

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
    if (
      !projectStore.current ||
      projectStore.current.workspace_id != props.task.workspace.workspace_id
    ) {
      const project_id = props.task.workspace.workspace_id;
      projectStore.fetchProjectById(project_id);
    }
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

  function getIsDarkTheme() {
    return JSON.parse(localStorage.getItem("darkTheme") as string);
  }

  onBeforeMount(() => {
    color.value = taskUtils.getColor(props.task.color);
    if (props.task.status != Status.COMPLETED) {
      progress.value = taskUtils.calcProgress(props.task.progress);
    } else {
      progress.value = 100;
    }
    priority_color.value = taskUtils.calcPriorityColor(props.task.priority);
    if (localStorage.getItem("darkTheme")) {
      isDark.value = JSON.parse(localStorage.getItem("darkTheme") as string);
      if (props.darkerCards) darkerCards.value = props.darkerCards;
      else darkerCards.value = isDarkerCardsActive();
    }
  });
</script>
