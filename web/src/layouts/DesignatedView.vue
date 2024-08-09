<template lang="">
  <div class="mt-10">
    <div v-if="no_tasks" class="text-center mt-2/4">
      <h1 class="text-3xl font-bold text-secondary">
        You dont have any current designated tasks
      </h1>
    </div>
    <div v-else class="flex flex-col gap-5 justify-center">
      <h1 class="text-2xl font-semibold text-base-content">Active tasks:</h1>
      <TaskList :tasks="active_tasks" :isMicro="false" />
      <h1 class="text-2xl font-semibold text-base-content">Completed tasks:</h1>
      <TaskList :tasks="completed_tasks" :isMicro="false" />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import TaskList from "../components/task/TaskList.vue";
  import { Status, TaskLite } from "../utils/types";
  import { useUserStore } from "../store/user.store";
  import { useRoute } from "vue-router";
  import { useProjectStore } from "../store/project.store";

  const active_tasks = ref<TaskLite[]>();
  const completed_tasks = ref<TaskLite[]>();

  const route = useRoute();

  const no_tasks = ref<boolean>(false);

  const userStore = useUserStore();
  const projectStore = useProjectStore();

  interface FilteredTasks {
    active: TaskLite[];
    completed: TaskLite[];
  }

  function checkInsideProject(): boolean {
    return route.path.includes("/project");
  }

  function getUserDesignatedTasks() {
    return userStore.self.designated_tasks;
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
    const tasks = projectStore.current.tasks;
    tasks.forEach((task: TaskLite) => {
      if (task.status == Status.COMPLETED) {
        completed.push(task);
      } else {
        if (task.status != Status.BLOCKED) {
          active.push(task);
        }
      }
    });
    return { active: active, completed: completed };
  }

  onBeforeMount(() => {
    if (userStore.self && userStore.self.designated_tasks) {
      if (checkInsideProject()) {
        const tasks = prepareProjectUserDesignatedTasks();
        active_tasks.value = tasks.active;
        completed_tasks.value = tasks.completed;
      } else {
        const tasks = prepareDesignatedTasks();
        active_tasks.value = tasks.active;
        completed_tasks.value = tasks.completed;
      }
    } else {
      no_tasks.value = true;
    }
  });
</script>
