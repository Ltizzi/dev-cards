<template lang="">
  <div
    :class="['mt-10', checkInsideProject() ? 'min-h-screen' : '']"
    v-if="isLoaded"
  >
    <div
      v-if="no_tasks"
      class="text-center mt-2/4 min-h-screen align-middle my-56"
    >
      <h1 class="text-3xl font-bold text-secondary">
        {{
          props.isLoggedIn
            ? "You dont have any current designated tasks"
            : checkInsideProject()
            ? "No Tasks avaible"
            : "No tasks in local enviroment. You need to log in to see remote tasks or load local workspace data."
        }}
      </h1>
    </div>
    <div v-else class="flex flex-col gap-5 justify-center">
      <div v-show="active_tasks.length > 0">
        <h1 class="text-2xl lg:ml-0 ml-5 font-semibold text-base-content">
          Active tasks:
        </h1>
        <TaskList
          :tasks="active_tasks"
          :isMicro="isMobile"
          :isDark="props.isDark"
          :darkerCards="props.darkerCards"
          :viewList="true"
        />
      </div>

      <div v-show="completed_tasks.length > 0">
        <h1 class="text-2xl lg:ml-0 ml-5 font-semibold text-base-content">
          Completed tasks:
        </h1>
        <TaskList
          :tasks="completed_tasks"
          :isMicro="isMobile"
          :isDark="props.isDark"
          :darkerCards="props.darkerCards"
          :viewList="true"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import TaskList from "../components/task/TaskList.vue";
  import { Status, TaskLite } from "../utils/types";
  import { useUserStore } from "../store/user.store";
  import { useRoute } from "vue-router";
  import { useProjectStore } from "../store/project.store";
  import { useUIStore } from "../store/ui.store";

  const props = defineProps<{
    isDark: boolean;
    darkerCards: boolean;
    isLoggedIn: boolean;
  }>();

  const active_tasks = ref<TaskLite[]>();
  const completed_tasks = ref<TaskLite[]>();

  const route = useRoute();

  const no_tasks = ref<boolean>(false);

  const userStore = useUserStore();
  const projectStore = useProjectStore();
  const UIStore = useUIStore();

  const isLoaded = ref<boolean>();
  const isMobile = ref<boolean>();

  interface FilteredTasks {
    active: TaskLite[];
    completed: TaskLite[];
  }

  watch(
    () => props.isLoggedIn,
    (newValue, oldValue) => {
      console.log("Deslogueoo");
      if (!newValue) {
        cleanTasks();
        no_tasks.value = true;
        //isLoaded.value = false;
      } else {
        no_tasks.value = false;
        prepareTasks();
      }
    }
  );

  watch(
    () => userStore.getDesignatedTask(),
    (newValue, oldValue) => {
      prepareTasks();
    }
  );
  // watch(
  //   () => props.isLoggedIn,
  //   (newValue, oldValue) => {
  //     console.log("DESLOGUEO");
  //     if (newValue != oldValue) {
  //       isLoaded.value = newValue;
  //     }
  //   }
  // );

  watch(
    () => UIStore.isMobile,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        console.log("HIJO DE MIL");
        isMobile.value = UIStore.isMobile;
      }
    }
  );

  function cleanTasks() {
    active_tasks.value = [];
    completed_tasks.value = [];
  }

  function checkInsideProject(): boolean {
    //console.log(route.path);
    return route.path.includes("project");
  }

  function getUserDesignatedTasks() {
    if (userStore.getDesignatedTask()) {
      return userStore.getDesignatedTask();
    } else {
      no_tasks.value = true;
      return [];
    }
  }

  function prepareProjectUserDesignatedTasks(): FilteredTasks {
    let active = [] as TaskLite[];
    let completed = [] as TaskLite[];
    const tasks = projectStore.current.tasks;
    const designated_tasks = getUserDesignatedTasks();
    tasks.forEach((task: TaskLite) => {
      designated_tasks.forEach((t: TaskLite) => {
        if (task.task_id == t.task_id) {
          if (task.status == Status.COMPLETED) {
            completed.push(task);
          } else {
            if (task.status != Status.BLOCKED) {
              active.push(task);
            }
          }
        }
      });
    });
    return { active: active, completed: completed };
  }

  function prepareDesignatedTasks(): FilteredTasks {
    let active = [] as TaskLite[];
    let completed = [] as TaskLite[];
    const tasks = getUserDesignatedTasks();
    if (tasks) {
      tasks.forEach((task: TaskLite) => {
        if (task.status == Status.COMPLETED) {
          completed.push(task);
        } else {
          if (task.status != Status.BLOCKED) {
            active.push(task);
          }
        }
      });
    }
    return { active: active, completed: completed };
  }

  function prepareTasks() {
    if (checkInsideProject()) {
      const tasks = prepareProjectUserDesignatedTasks();
      active_tasks.value = tasks.active;
      completed_tasks.value = tasks.completed;
    } else {
      const tasks = prepareDesignatedTasks();
      active_tasks.value = tasks.active;
      completed_tasks.value = tasks.completed;
    }
  }

  onBeforeMount(() => {
    if (userStore.self && userStore.self.designated_tasks) {
      prepareTasks();
      if (userStore.self.designated_tasks.length == 0) no_tasks.value = true;
      if (active_tasks.value.length == 0 && completed_tasks.value == 0)
        no_tasks.value = true;
    } else {
      no_tasks.value = true;
    }
    UIStore.setIsMobile(true);
    isMobile.value = UIStore.isMobile;
    isLoaded.value = true;
  });
</script>
