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
      class="w-full grid grid-cols-5 border-x-2 border-b-2 border-primary rounded-b-xl shadow-lg shadow-slate-900 relative"
      ref="cols"
    >
      <div
        class="w-80 border-r-2 border-r-primary min-h-96"
        ref="col_pool"
        id="col_pool"
      >
        <TaskList :tasks="pool" :isMicro="isMicro" />
        <!--           :isDraggable="true"
          :col_name="'pool'"
          @dropped="dropped" -->
      </div>
      <div
        class="w-80 border-r-2 border-r-primary"
        ref="col_priority"
        id="col_priority"
      >
        <TaskList :tasks="top_priority" :isMicro="isMicro" />
        <!--      :isDraggable="true"
          :col_name="'priority'"
          @dropped="dropped" -->
      </div>
      <div
        class="w-80 border-r-2 border-r-primary"
        ref="col_progress"
        id="col_progress"
      >
        <TaskList :tasks="in_progress" :isMicro="isMicro" />
        <!--       :isDraggable="true"
          :col_name="'progress'"
          @dropped="dropped" -->
      </div>
      <div
        class="w-80 border-r-2 border-r-primary"
        ref="col_testing"
        id="col_testing"
      >
        <TaskList :tasks="testing" :isMicro="isMicro" />
        <!--     :isDraggable="true"
          :col_name="'testing'"
          @dropped="dropped" -->
      </div>
      <div class="w-80" ref="col_completed" id="col_completed">
        <TaskList :tasks="completed" :isMicro="isMicro" />
        <!--         :isDraggable="true"
          :col_name="'completed'"
          @dropped="dropped" -->
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
  import { useApiCall } from "../composables/useAPICall";
  //import { EndpointType } from "../utils/endpoints";

  const projectStore = useProjectStore();

  const tasks = ref<Array<TaskLite>>([]);

  const pool = ref<Array<TaskLite>>([]);
  const top_priority = ref<Array<TaskLite>>([]);
  const in_progress = ref<Array<TaskLite>>([]);
  const testing = ref<Array<TaskLite>>([]);
  const completed = ref<Array<TaskLite>>([]);
  const search = ref<string>("");

  const isMicro = ref<boolean>(false);

  const apiCall = useApiCall();

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
    localStorage.setItem("ui-card-btn-micro", JSON.stringify(isMicro.value));
  }

  async function updateProject() {
    await projectStore.updateCurrent();
    prepareTemplate(getTasks());
  }

  // function dropped(col_name: string, pos: any, task: TaskLite) {
  //   console.log("col name:");
  //   console.log(col_name);
  //   console.log(col_name, " ", pos, " ", task.title);
  //   console.log("DROPPEADO GATO");
  //   console.log(document.elementsFromPoint(pos.x, pos.y));
  //   const element_id = document
  //     .elementsFromPoint(pos.x, pos.y)
  //     .filter((el: any) => el.id && el.id != "app")[0].id;
  //   handleDropped(col_name, element_id, task);
  // }

  // async function handleDropped(from: string, to: string, task: TaskLite) {
  //   if (from == "pool") {
  //     pool.value = pool.value.filter(
  //       (t: TaskLite) => t.task_id != task.task_id
  //     );
  //     if (to == "priority") {
  //       top_priority.value.push(task);
  //       let taskEntity = await fetchTaskById(task.task_id);
  //       if (taskEntity) {
  //         taskEntity.priority = Priority.VERY_HIGH;
  //       }
  //     }
  //     if (to == "progress") {
  //       in_progress.value.push(task);
  //     }
  //     if (to == "testing") {
  //       testing.value.push(task);
  //     }
  //     if (to == "completed") {
  //       completed.value.push(task);
  //     }
  //   }
  //   if (from == "priority") {
  //     top_priority.value = top_priority.value.filter(
  //       (t: TaskLite) => t.task_id != task.task_id
  //     );
  //     if (to == "pool") {
  //       pool.value.push(task);
  //     }
  //     if (to == "progress") {
  //       in_progress.value.push(task);
  //     }
  //     if (to == "testing") {
  //       testing.value.push(task);
  //     }
  //     if (to == "completed") {
  //       completed.value.push(task);
  //     }
  //   }
  //   if (from == "progress") {
  //     in_progress.value = in_progress.value.filter(
  //       (t: TaskLite) => t.task_id != task.task_id
  //     );
  //     if (to == "pool") {
  //       pool.value.push(task);
  //     }
  //     if (to == "priority") {
  //       top_priority.value.push(task);
  //     }
  //     if (to == "testing") {
  //       testing.value.push(task);
  //     }
  //     if (to == "completed") {
  //       completed.value.push(task);
  //     }
  //   }
  //   if (from == "testing") {
  //     testing.value = testing.value.filter(
  //       (t: TaskLite) => t.task_id != task.task_id
  //     );
  //     if (to == "pool") {
  //       pool.value.push(task);
  //     }
  //     if (to == "priority") {
  //       top_priority.value.push(task);
  //     }
  //     if (to == "progress") {
  //       in_progress.value.push(task);
  //     }
  //     if (to == "completed") {
  //       completed.value.push(task);
  //     }
  //   }
  //   if (from == "completed") {
  //     completed.value = completed.value.filter(
  //       (t: TaskLite) => t.task_id != task.task_id
  //     );
  //     if (to == "pool") {
  //       pool.value.push(task);
  //     }
  //     if (to == "priority") {
  //       top_priority.value.push(task);
  //     }
  //     if (to == "progress") {
  //       in_progress.value.push(task);
  //     }
  //     if (to == "testing") {
  //       testing.value.push(task);
  //     }
  //   }
  // }

  // async function fetchTaskById(id: number) {
  //   const response = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
  //     params: { id: id },
  //   })) as Task;
  //   if (response.task_id == id) {
  //     return response;
  //   }
  // }

  onBeforeMount(() => {
    tasks.value = getTasks();
    prepareTemplate(getTasks());
    isMicro.value = JSON.parse(
      localStorage.getItem("ui-card-btn-micro") as string
    );
  });
</script>
<style lang=""></style>
