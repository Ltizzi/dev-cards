<template lang="">
  <div
    class="pt-5 min-h-screen h-screen flex flex-col pb-10 w-auto"
    v-if="isLoaded"
  >
    <div class="h-1/6">
      <h1 class="text-center text-4xl lg:text-2xl xl:text-4xl">Scrum board</h1>
      <div
        class="flex flex-row py-5 lg:py-1 xl:py-5 xl:justify-between justify-between lg:justify-around items-center"
      >
        <TaskFilterInput @search="defineSearch" />
        <div
          class="flex flex-row justify-between 2xl:mx-0 xl:mx-10 xl:gap-5 gap-1"
        >
          <NewTaskBtn @update="updateProject" />
          <!-- <button class="btn btn-outline btn-secondary" @click="changeIconSize">
            Change card size
          </button> -->
          <ChangeCardSizeBtn @changeIconSize="changeIconSize" />
        </div>
      </div>
    </div>

    <div
      class="h-5/6 overflow-y-hidden overflow-x-hidden w-full lg:flex-1 xl:ml-0 lg:-ml-6"
    >
      <div
        class="w-full grid border-2 border-t-0 border-l-0 border-r-0 border-opacity-40 border-spacing-10 border-dashed border-secondary grid-cols-5 text-xl font-semibold text-center text-base-content rounded-t-xl flex-1"
      >
        <div
          :class="[
            '2xl:w-80 w-auto flex justify-center items-center py-3 border-r-2 border-opacity-20  border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r from-0% via-transparent via-50%   to-100% rounded-tl-xl',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
        >
          <h1>Backlog</h1>
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto flex justify-center items-center py-3 border-r-2 border-opacity-20  border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%   to-100%',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
        >
          <h1>Top Priority</h1>
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto flex justify-center items-center py-3 border-spacing-10 border-opacity-20  border-dashed border-r-2 border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%   to-100%',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
        >
          <h1>In Progress</h1>
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto flex justify-center items-center py-3 border-spacing-10 border-opacity-20  border-dashed border-r-2 border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%   to-100% ',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
        >
          <h1>Testing</h1>
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto flex justify-center items-center py-3 bg-gradient-to-r rounded-tr-xl  from-0% via-transparent via-50%  to-100%',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
        >
          <h1>Complete</h1>
        </div>
      </div>
      <div
        class="w-full overflow-x-auto grid grid-cols-5 border-opacity-30 border-spacing-10 border-dashed border-x-0 border-b-0 border-secondary rounded-b-xl shadow-lg shadow-slate-900 h-full relative"
        ref="cols"
      >
        <div
          :class="[
            '2xl:w-80 w-auto border-r-2 border-opacity-20 border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%  to-100% rounded-bl-xl    overflow-x-hidden max-h-full h-full',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
          ref="col_pool"
          id="col_pool"
        >
          <TaskList :tasks="pool" :isMicro="isMicro" />
          <!--           :isDraggable="true"
              :col_name="'pool'"
              @dropped="dropped" -->
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto border-r-2 border-opacity-20  border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%  to-100% overflow-x-hidden overflow-y-auto max-h-full',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
          ref="col_priority"
          id="col_priority"
        >
          <TaskList :tasks="top_priority" :isMicro="isMicro" />
          <!--      :isDraggable="true"
              :col_name="'priority'"
              @dropped="dropped" -->
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto border-r-2 border-opacity-20  border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r from-0% via-transparent via-50% to-100% overflow-x-hidden overflow-y-auto max-h-full',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
          ref="col_progress"
          id="col_progress"
        >
          <TaskList :tasks="in_progress" :isMicro="isMicro" />
          <!--       :isDraggable="true"
              :col_name="'progress'"
              @dropped="dropped" -->
        </div>
        <div
          :class="[
            '2xl:w-80 w-autoborder-r-2 border-opacity-20  border-spacing-10 border-dashed border-r-secondary bg-gradient-to-r  from-0% via-transparent via-50%  to-100%  overflow-x-hidden overflow-y-auto max-h-full',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
          ref="col_testing"
          id="col_testing"
        >
          <TaskList :tasks="testing" :isMicro="isMicro" />
          <!--     :isDraggable="true"
              :col_name="'testing'"
              @dropped="dropped" -->
        </div>
        <div
          :class="[
            '2xl:w-80 w-auto bg-gradient-to-r border-opacity-50  border-spacing-10 border-dashed from-0% via-transparent via-50%  to-100% rounded-br-xl  overflow-x-hidden overflow-y-auto max-h-full',
            isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300',
          ]"
          ref="col_completed"
          id="col_completed"
        >
          <TaskList :tasks="completed" :isMicro="isMicro" />
          <!--         :isDraggable="true"
              :col_name="'completed'"
              @dropped="dropped" -->
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, onMounted, ref, watch } from "vue";
  import {
    Priority,
    Progress,
    Status,
    Task,
    TaskLite,
    TaskType,
    Workspace,
  } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import { useProjectStore } from "../store/project.store";
  import NewTaskBtn from "../components/task/NewTaskBtn.vue";
  import { useApiCall } from "../composables/useAPICall";
  import TaskFilterInput from "../components/ui/TaskFilterInput.vue";
  import ChangeCardSizeBtn from "../components/ui/ChangeCardSizeBtn.vue";
  import { taskUtils } from "../utils/task.utils";
  import { useUIStore } from "../store/ui.store";
  import { useTaskStore } from "../store/task.store";

  const projectStore = useProjectStore();
  const UIStore = useUIStore();

  const tasks = ref<Array<TaskLite>>([]);

  const pool = ref<Array<TaskLite>>([]);
  const top_priority = ref<Array<TaskLite>>([]);
  const in_progress = ref<Array<TaskLite>>([]);
  const testing = ref<Array<TaskLite>>([]);
  const completed = ref<Array<TaskLite>>([]);
  const search = ref<string>("");

  const isMicro = ref<boolean>(false);

  const apiCall = useApiCall();

  const isDark = ref<boolean>();

  const isLoaded = ref<boolean>();

  watch(
    () => projectStore.current.tasks,
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
        let searched_tasks = taskUtils.searchTasks(newValue, tasks.value);
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
          (task.status == Status.PENDING || task.status == Status.PROGRESS) &&
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

  function defineSearch(value: string) {
    search.value = value;
  }

  function clearColumns() {
    pool.value = [];
    top_priority.value = [];
    in_progress.value = [];
    testing.value = [];
    completed.value = [];
  }

  function hasAssignedUser(task: TaskLite) {
    if (!projectStore.offlineMode) return task.hasUsers;
    else {
      const taskStore = useTaskStore();
      const saved_task = taskStore.getLocalTask(task.task_id) as Task;
      return saved_task.designated_to
        ? saved_task.designated_to.length > 0
        : false;
    }
  }

  function getTasks() {
    const ws = projectStore.getCurrent() as Workspace;
    return ws.tasks;
  }

  function changeIconSize() {
    isMicro.value = !isMicro.value;
    localStorage.setItem("ui-card-btn-micro", JSON.stringify(isMicro.value));
  }

  async function updateProject() {
    await projectStore.updateCurrent();
    prepareTemplate(getTasks());
  }

  onBeforeMount(() => {
    isDark.value = UIStore.checkIsDarkTheme();

    isMicro.value = JSON.parse(
      localStorage.getItem("ui-card-btn-micro") as string
    );
  });

  onMounted(() => {
    tasks.value = getTasks();
    prepareTemplate(getTasks());
    isLoaded.value = true;
  });
</script>
<style lang=""></style>
