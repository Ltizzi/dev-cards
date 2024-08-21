<template lang="">
  <div class="w-52 bg-base-200 flex flex-col justify-between" v-if="isLoaded">
    <ul
      class="menu menu-vertical bg-base-200 rounded-box before:ring-offset-purple-400 overflow-x-clip overflow-y-auto mt-3 h-auto"
    >
      <li class="font-extrabold text-base text-center pb-3 sticky">
        {{ project.project_name }}
      </li>
      <li @click="goHome()">
        <div
          :class="[
            'hover:bg-accent py-3 rounded-xl hover:cursor-pointer transition-all ease-in-out w-full',
            state.selected === -10
              ? 'border-l-2 text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
              : '',
          ]"
        >
          Info
        </div>
      </li>
      <li @click="goScrum()">
        <div
          :class="[
            'disabled hover:bg-accent ',
            state.selected === -1
              ? 'border-l-2  text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent font-semibold'
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
              ? 'border-l-2 text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent font-semibold'
              : '',
          ]"
        >
          <summary
            class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out"
          >
            <span>All Tasks</span>
          </summary>
        </div>
      </li>

      <li class="flex">
        <details close>
          <summary
            class="py-3 rounded-xl hover:cursor-pointer transition-all ease-in-out"
            @click="goDesignated()"
            :class="[
              'disabled hover:bg-accent ',
              state.selected === -2
                ? 'border-l-2 text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
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
                  ? 'opacity-70 text-white bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                  : '',
              ]"
              @click="goTask(task.task_id)"
            >
              <p
                :class="[
                  'w-full ',
                  state.selected === task.task_id
                    ? 'opacity-70 text-white bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                    : 'text-base-content',
                ]"
              >
                <!-- text-secondary-content -->
                <span
                  :class="[
                    'size-2 rounded-full',
                    taskUtils.getColor(task.color),
                  ]"
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
            <li
              class
              clas="text-base-content flex flex-row w-full justify-center"
              @click="showFindUserByMailModal"
              v-if="isModOrOwner"
            >
              <p class="w-full">
                <font-awesome-icon
                  :icon="['fas', 'circle-plus']"
                  class="size-5 text-success"
                />Invitar Usuario
              </p>
            </li>
          </ul>
        </details>
      </li>
    </ul>
    <div
      class="flex flex-col justify-cemter gap-0.5 min-h-28 max-h-28 fixed bottom-0 z-30"
    >
      <NewTaskBtn
        :class="['mx-auto', isModOrOwner ? '' : 'mb-5']"
        :icon="true"
      />
      <div class="divider divider-secondary my-2" v-if="isModOrOwner"></div>

      <div
        class="flex flex-row flex-wrap justify-evenly gap-2 mb-3"
        v-if="isModOrOwner"
      >
        <font-awesome-icon
          class="size-6 text-success py-2 px-3 rounded-xl hover:cursor-pointer hover:bg-neutral"
          :icon="['fas', 'user-plus']"
          @click="showFindUserByMailModal"
        />

        <font-awesome-icon
          class="size-6 py-2 px-3 text-primary rounded-xl hover:cursor-pointer hover:bg-neutral"
          :icon="['fas', 'sliders']"
          @click="goProjectSettings()"
        />
      </div>
    </div>

    <AddUserByEmailModal
      :showModal="state.addUserModal"
      :ws_id="project.workspace_id"
      @cancel="closeModal"
      @update="updateProject"
    />
  </div>
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
  import { checkIsModOrOwner } from "../../utils/auth.utils";
  import AddUserByEmailModal from "../ui/AddUserByEmailModal.vue";
  import NewTaskBtn from "../task/NewTaskBtn.vue";

  const route = useRoute();
  const router = useRouter();

  const emit = defineEmits(["update"]);

  const id = ref<number>();

  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const apiCall = useApiCall();

  const project = ref<Workspace>();

  const user_designated_tasks = ref<TaskLite[]>();

  const isLoaded = ref(false);

  const isModOrOwner = ref<boolean>();

  const state = reactive({
    selected: -10,
    addUserModal: false,
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
        isModOrOwner.value = checkIsModOrOwner(newValue);
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
      } else if (newValue != oldValue) {
        project.value = projectStore.current;
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  );

  //NAVIGATION

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

  function goProjectSettings() {
    router.push(`/project/settings?id=${project.value?.workspace_id}`);
    setTimeout(() => (state.selected = -4), 100);
  }

  function showFindUserByMailModal() {
    state.addUserModal = true;
  }

  function closeModal() {
    state.addUserModal = false;
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

  function initComponent(id: number) {
    isLoaded.value = true;
    isModOrOwner.value = checkIsModOrOwner(id);
  }

  async function updateProject() {
    project.value = await projectStore.updateCurrent();
    emit("update");
  }

  onBeforeMount(async () => {
    if (route.query.id) id.value = +route.query.id;
    project.value = projectStore.current;
    if (project.value.workspace_id) {
      user_designated_tasks.value = getProjectDesignatedTasks();
      initComponent(project.value.workspace_id);
    } else {
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: id.value },
      })) as Workspace;
      if (response.workspace_id) {
        projectStore.setCurrent(response);
        project.value = response;
        initComponent(response.workspace_id);
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  });
</script>
