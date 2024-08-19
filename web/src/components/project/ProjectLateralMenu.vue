<template lang="">
  <ul
    class="menu bg-base-200 rounded-box w-52 mt-10 before:ring-offset-purple-400 overflow-x-hidden"
    v-if="isLoaded"
  >
    <li
      :class="[
        'active hover:bg-accent',
        state.selected === -10
          ? 'border-l-2  border-primary -ml-0.5 opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
          : '',
      ]"
      @click="goHome()"
    >
      <a
        class="py-3 rounded-xl hover:cursor-pointer transition-all ease-in-out w-full"
        >Info</a
      >
    </li>
    <li @click="goScrum()">
      <div
        :class="[
          'disabled hover:bg-accent ',
          state.selected === -1
            ? 'border-l-2  border-primary -ml-0.5 opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
            : '',
        ]"
      >
        <summary
          class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out w-full"
        >
          <span>Scrum</span>
        </summary>
      </div>
    </li>
    <li @click="goAllTasks()">
      <div
        :class="[
          'disabled hover:bg-accent ',
          state.selected === -3
            ? 'border-l-2  border-primary -ml-0.5 opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
            : '',
        ]"
      >
        <summary
          class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out"
        >
          <span>All Tasks</span>
        </summary>
      </div>
      <!-- <details close> -->

      <!-- <ul class="before:ring-offset-purple-400">
          <li
            v-for="task in project.tasks"
            :class="[
              'rounded-xl w-full flex flex-row justify-start hover:bg-accent',
              state.selected == task.task_id
                ? 'bg-secondary text-secondary-content'
                : '',
            ]"
            @click="goTask(task.task_id)"
          >
            <p class="text-base-content w-full">
              <span
                :class="['size-2 rounded-full', taskUtils.getColor(task.color)]"
              ></span>
              {{ shortName(task.title) }}
            </p>
          </li>
        </ul> -->
      <!-- </details> -->
    </li>

    <li class="flex">
      <details close>
        <summary
          class="py-3 rounded-xl hover:cursor-pointer transition-all ease-in-out"
          @click="goDesignated()"
          :class="[
            'disabled hover:bg-accent ',
            state.selected === -2
              ? 'border-l-2  border-primary -ml-0.5 opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
              : '',
          ]"
        >
          <span>Designated</span>
        </summary>

        <ul class="before:ring-offset-purple-400">
          <li
            v-for="task in user_designated_tasks"
            :class="[
              'disabled rounded-xl w-full flex flex-row justify-start hover:bg-accent',
              state.selected === task.task_id
                ? 'opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
                : '',
            ]"
            @click="goTask(task.task_id)"
          >
            <p
              :class="[
                'w-full ',
                state.selected === task.task_id
                  ? 'opacity-70 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent text-secondary-content font-semibold'
                  : 'text-base-content',
              ]"
            >
              <span
                :class="['size-2 rounded-full', taskUtils.getColor(task.color)]"
              ></span>
              {{ shortName(task.title) }}
            </p>
          </li>
        </ul>
      </details>
      <!-- <details open>
        <summary>
          <span>Tasks</span>
        </summary>
        <ul class="before:ring-offset-purple-400"></ul>
      </details> -->
    </li>
    <li>
      <details close>
        <summary>Users</summary>
        <ul class="before:ring-offset-purple-400">
          <li
            v-for="user in project.users"
            class="text-white flex flex-row justify-start align-middle w-full"
          >
            <div class="avatar">
              <div class="w-6 rounded-full">
                <img :src="user.avatar" />
              </div>
            </div>
            <p class="text-base-content text-start -ml-6">
              {{ user.username }}
            </p>
          </li>
        </ul>
      </details>
    </li>
  </ul>
</template>
<script setup lang="ts">
  import { useRoute, useRouter } from "vue-router";
  import { onBeforeMount, ref, reactive, watch } from "vue";
  import { Status, TaskLite, Workspace } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { taskUtils } from "../../utils/task.utils";
  import { useUserStore } from "../../store/user.store";

  const route = useRoute();
  const router = useRouter();

  const id = ref<number>();

  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const apiCall = useApiCall();

  const project = ref<Workspace>();

  const user_designated_tasks = ref<TaskLite[]>();

  const isLoaded = ref(false);

  const state = reactive({
    selected: -10,
  });

  watch(
    () => route.query.id,
    (newValue, oldValue) => {
      if (route.path != "/project/info")
        state.selected = newValue as unknown as number;
    }
  );

  watch(
    () => projectStore.current.workspace_id,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        project.value = projectStore.current;
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  );

  watch(
    () => projectStore.current.tasks,
    async (newValue, oldValue) => {
      if (
        project.value?.workspace_id != projectStore.current.workspace_id &&
        newValue != oldValue
      ) {
        project.value = await projectStore.updateCurrent();
      }
    }
  );

  watch(
    () => projectStore.justUpdated,
    async (newValue, oldValue) => {
      if (
        newValue &&
        project.value?.workspace_id != projectStore.current.workspace_id
      ) {
        project.value = await projectStore.updateCurrent();
      }
    }
  );

  function goHome() {
    // state.selected = -10;
    router.push(`/project/info?id=${id.value}`);
    setTimeout(() => (state.selected = -10), 100);
  }

  function goTask(id: number) {
    // state.selected = id;
    router.push(`/project/task?id=${id}`);
  }

  function goScrum() {
    // state.selected = -1;
    router.push("/project/scrum");
    setTimeout(() => (state.selected = -1), 100);
  }

  function goDesignated() {
    //state.selected = -2;
    router.push("/project/designated");
    setTimeout(() => (state.selected = -2), 100);
  }

  function goAllTasks() {
    // state.selected = -3;
    router.push("/project/tasks");
    setTimeout(() => (state.selected = -3), 100);
  }

  function shortName(name: string) {
    if (name.length > 15) {
      return name.slice(0, 15) + "(...)";
    } else return name;
  }
  //TODO: AGREGAR A TASKS UTILS
  function getProjectDesignatedTasks() {
    let userTasks = [] as TaskLite[];
    const allUserTasks = userStore.getDesignatedTask();
    allUserTasks.forEach((task: TaskLite) => {
      if (
        task.workspace.workspace_id == project.value?.workspace_id &&
        task.status != Status.COMPLETED
      ) {
        userTasks.push(task);
      }
    });
    return userTasks;
  }

  onBeforeMount(async () => {
    if (route.query.id) id.value = +route.query.id;
    project.value = projectStore.current;
    if (project.value.workspace_id) {
      user_designated_tasks.value = getProjectDesignatedTasks();
      isLoaded.value = true;
    } else {
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: id.value },
      })) as Workspace;
      if (response.workspace_id) {
        projectStore.setCurrent(response);
        project.value = response;
        isLoaded.value = true;
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  });
</script>
