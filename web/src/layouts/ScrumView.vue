<template lang="">
  <div class="pt-5">
    <h1 class="text-center text-2xl">Scrum board</h1>
    <div class="flex flex-row py-5 justify-between">
      <label
        class="input input-bordered flex items-center gap-2 input-secondary"
      >
        Search
        <input
          type="text"
          class="grow"
          placeholder="by Tag or Title/subtitle"
          v-model="search"
        />
      </label>
      <div class="flex flex-row justify-between gap-5">
        <NewTaskBtn @update="updateProject" />
        <button class="btn btn-outline btn-secondary" @click="changeIconSize">
          Change card size
        </button>
      </div>
    </div>

    <div
      class="w-full grid border-2 border-primary grid-cols-5 text-xl font-semibold text-center text-base-content rounded-t-xl"
    >
      <div class="w-80 py-3 border-r-2 border-r-primary"><h1>Pool</h1></div>
      <div class="w-80 py-3 border-r-2 border-r-primary">
        <h1>Top Priority</h1>
      </div>
      <div class="w-80 py-3 border-r-2 border-r-primary">
        <h1>In Progress</h1>
      </div>
      <div class="w-80 py-3 border-r-2 border-r-primary"><h1>Testing</h1></div>
      <div class="w-80 py-3"><h1>Complete</h1></div>
    </div>
    <div
      class="w-full grid grid-cols-5 border-x-2 border-b-2 border-primary rounded-b-xl shadow-lg shadow-slate-900"
    >
      <div
        class="w-80 border-r-2 border-r-primary min-h-96 relative"
        ref="col_pool"
      >
        <TaskList :tasks="pool" :isMicro="isMicro" :isDraggable="true" />
      </div>
      <div class="w-80 border-r-2 border-r-primary relative" ref="col_priority">
        <TaskList
          :tasks="top_priority"
          :isMicro="isMicro"
          :isDraggable="true"
        />
      </div>
      <div class="w-80 border-r-2 border-r-primary relative" ref="col_progress">
        <TaskList :tasks="in_progress" :isMicro="isMicro" :isDraggable="true" />
      </div>
      <div class="w-80 border-r-2 border-r-primary relative" ref="col_testing">
        <TaskList :tasks="testing" :isMicro="isMicro" :isDraggable="true" />
      </div>
      <div class="w-80 relative" ref="col_completed">
        <TaskList :tasks="completed" :isMicro="isMicro" :isDraggable="true" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import {
    Priority,
    Progress,
    Status,
    TaskLite,
    TaskType,
  } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import { useProjectStore } from "../store/project.store";
  import NewTaskBtn from "../components/task/NewTaskBtn.vue";

  const projectStore = useProjectStore();

  const tasks = ref<Array<TaskLite>>([]);

  const pool = ref<Array<TaskLite>>([]);
  const top_priority = ref<Array<TaskLite>>([]);
  const in_progress = ref<Array<TaskLite>>([]);
  const testing = ref<Array<TaskLite>>([]);
  const completed = ref<Array<TaskLite>>([]);
  const search = ref<string>("");

  const isMicro = ref<boolean>(false);

  const col_pool = ref<HTMLElement | null>();
  const col_priority = ref<HTMLElement | null>();
  const col_progress = ref<HTMLElement | null>();
  const col_testing = ref<HTMLElement | null>();
  const col_completed = ref<HTMLElement | null>();

  const cols = ref<Array<HTMLElement | null | undefined>>([]);

  watch(
    () => getTasks(),
    (newValue, oldValue) => {
      if (newValue.length != oldValue.length) {
        tasks.value = getTasks();
        prepareTemplate(getTasks());
      }
    }
  );

  watch(
    () => search.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        let searched_tasks = searchTasks(newValue);
        if (searched_tasks) {
          prepareTemplate(searched_tasks);
        }
      }
    }
  );

  function prepareTemplate(inputTasks: Array<TaskLite>) {
    clearColumns();
    inputTasks.forEach((task: TaskLite) => {
      if (task.task_type != TaskType.DOCUMENTATION) {
        if (
          !hasAssignedUser(task) &&
          task.status == Status.PENDING &&
          task.priority != Priority.VERY_HIGH
        ) {
          pool.value.push(task);
        } else if (
          !hasAssignedUser(task) &&
          task.priority == Priority.VERY_HIGH
        ) {
          top_priority.value.push(task);
        } else if (
          hasAssignedUser(task) &&
          task.status == Status.PROGRESS &&
          task.progress != Progress.ADVANCE
        ) {
          in_progress.value.push(task);
        } else if (hasAssignedUser(task) && task.status == Status.TESTING) {
          testing.value.push(task);
        } else if (
          hasAssignedUser(task) &&
          task.progress == Progress.ADVANCE &&
          task.status == Status.COMPLETED
        ) {
          completed.value.push(task);
        }
      }
    });
  }

  function searchTasks(searchValue: string): TaskLite[] {
    let returned_tasks: TaskLite[] = [];
    searchValue = searchValue.toLowerCase();
    if (!searchValue) {
      return tasks.value;
    }
    tasks.value.forEach((task: TaskLite) => {
      if (task.title.toLowerCase().includes(searchValue)) {
        returned_tasks.push(task);
        return;
      }
      task.task_tags.forEach((tag: string) => {
        if (tag.toLowerCase().includes(searchValue)) {
          returned_tasks.push(task);
          return;
        }
      });
    });
    return returned_tasks;
  }

  function clearColumns() {
    pool.value = [];
    top_priority.value = [];
    in_progress.value = [];
    testing.value = [];
    completed.value = [];
  }

  function hasAssignedUser(task: TaskLite) {
    return task.hasUsers;
  }

  function getTasks() {
    return projectStore.current.tasks;
  }

  function changeIconSize() {
    isMicro.value = !isMicro.value;
  }

  async function updateProject() {
    await projectStore.updateCurrent();
    prepareTemplate(getTasks());
  }

  function dropped(event: MouseEvent) {
    console.log(event);
    console.log("DROPPEADO GATO");
  }

  onBeforeMount(() => {
    tasks.value = getTasks();
    prepareTemplate(getTasks());

    col_pool.value?.addEventListener("mouseup", dropped);
    col_priority.value?.addEventListener("mouseup", dropped);
    col_progress.value?.addEventListener("mouseup", dropped);
    col_testing.value?.addEventListener("mouseup", dropped);
    col_completed.value?.addEventListener("mouseup", dropped);
  });
</script>
<style lang=""></style>
