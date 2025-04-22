<template lang="">
  <div
    :class="[
      'w-56 lg:w-40 xl:w-56 bg-base-200 flex flex-col flex-nowrap h-screen  motion-duration-500 motion-opacity-in-0 motion-ease-in-out',
      state.isMobile
        ? 'fixed left-7 z-10'
        : 'fixed lg:left-5 lg:ml-0 2xl:left-28 2xl:-ml-20',
    ]"
    v-if="isLoaded && state.showMenu"
  >
    <ul
      class="menu bg-base-200 rounded-box before:ring-offset-purple-400 overflow-x-hidden overflow-y-auto pt-3 whitespace-nowrap flex-shrink-0 flex-nowrap h-5/6"
    >
      <li
        :class="[
          'font-extrabold  text-center py-3 lg:py-0.5 xl:py-3 sticky top-0 bg-gradient-to-br from-base-100 to-neutral my-3 whitespace-nowrap flex-shrink-0 w-52 lg:w-36 xl:w-52 rounded-xl',
          state.isDark
            ? 'from-base-100 to-neutral text-base'
            : 'from-base-100 via-30% via-base-300 to-primary text-neutral',
        ]"
      >
        {{ project.project_name }}
      </li>
      <li @click="goTo('home')">
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
      <li @click="goTo('all')">
        <div
          :class="[
            'disabled hover:bg-accent ',
            state.selected === -3
              ? 'border-l-2 text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent font-semibold'
              : '',
          ]"
        >
          <div
            class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out"
          >
            <span>All Tasks</span>
          </div>
        </div>
      </li>
      <li @click="goTo('scrum')">
        <div
          :class="[
            'disabled hover:bg-accent ',
            state.selected === -1
              ? 'border-l-2  text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent font-semibold'
              : '',
          ]"
        >
          <div
            class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out w-full"
          >
            <span>Scrum Board</span>
          </div>
        </div>
      </li>
      <li @click="goTo('blocked')" v-if="isModOrOwner">
        <div
          :class="[
            'disabled hover:bg-accent ',
            state.selected === -5
              ? 'border-l-2  text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent font-semibold'
              : '',
          ]"
        >
          <div
            class="py-2 rounded-xl hover:cursor-pointer transition-all ease-in-out w-full"
          >
            <span>Blocked</span>
          </div>
        </div>
      </li>

      <li class="whitespace-nowrap flex-shrink-0 flex">
        <details close>
          <summary
            class="py-3 rounded-xl hover:cursor-pointer transition-all ease-in-out"
            @click="goTo('designated')"
            :class="[
              'hover:bg-accent ',
              state.selected === -2
                ? 'border-l-2 text-white border-primary -ml-0.5 opacity-90 bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                : '',
            ]"
          >
            <span>Designated</span>
          </summary>

          <ul
            :class="[
              'before:ring-offset-purple-400',
              state.designatedOpen
                ? 'motion-duration-500 motion-ease-in-out-cubic motion-preset-slide-down-lg motion-opacity-in-0'
                : '',
            ]"
          >
            <li
              v-for="task in user_designated_tasks"
              :class="[
                'rounded-xl w-full flex flex-row justify-start hover:bg-accent',
                state.selectedTask === task.task_id
                  ? 'opacity-70 text-white bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                  : '',
              ]"
              @click="goTo('task', task.task_id)"
            >
              <p
                :class="[
                  'w-full ',
                  state.selected === task.task_id
                    ? 'opacity-70 text-white bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                    : 'text-base-content',
                ]"
              >
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
      <li class="whitespace-nowrap flex-shrink-0" v-if="!state.offlineMode">
        <details close>
          <summary
            :class="[
              'hover:cursor-pointer transition-all ease-in-out hover:bg-accent',
            ]"
            @click="handleDropdown('users')"
          >
            Users
          </summary>
          <ul
            :class="[
              'before:ring-offset-purple-400 motion-duration-500 motion-ease-in-quart',
              state.usersOpen
                ? 'motion-opacity-in-0 motion-preset-slide-down-lg'
                : 'motion-opacity-out-0 motion-preset-slide-up-lg',
            ]"
          >
            <li
              v-for="user in project.users"
              :class="[
                'text-white flex flex-row justify-start align-middle w-full hover:bg-accent rounded-md transition-all ease-in-out',
                state.selectedUser == user.user_id
                  ? 'opacity-70 text-white bg-gradient-to-r from-secondary from-0% via-secondary to-100% to-transparent  font-semibold'
                  : '',
              ]"
              @click="goTo('user', user.user_id)"
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
      class="flex flex-col justify-center gap-0.5 lg:gap-0 xl:gap-0.5 w-56 lg:w-40 xl:w-56 bg-gradient-to-br from-base-100 to-base-300 bottom-0 z-10 h-1/6"
    >
      <NewTaskBtn
        :class="['mx-auto', isModOrOwner ? '' : 'mb-5']"
        :icon="true"
      />
      <div
        class="divider divider-secondary my-2 lg:my-0 xl:my-2 opacity-40"
        v-if="isModOrOwner"
      ></div>

      <div
        class="flex flex-row flex-wrap justify-evenly gap-2 mb-3 lg:mt-1 xl:mt-0"
        v-if="isModOrOwner"
      >
        <font-awesome-icon
          class="size-6 xl:size-6 lg:size-5 text-success py-2 xl:py-3 lg:py-1 px-3 rounded-xl hover:cursor-pointer hover:bg-neutral"
          :icon="['fas', 'user-plus']"
          @click="showFindUserByMailModal"
        />

        <font-awesome-icon
          class="size-6 xl:size-6 lg:size-5 py-2 px-3 xl:py-3 lg:py-1 text-primary rounded-xl hover:cursor-pointer hover:bg-neutral"
          :icon="['fas', 'sliders']"
          @click="goTo('settings')"
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
  <button
    class="btn btn-square btn-outline absolute top-2 left-16 bg-base-300 z-50"
    v-show="state.showMenuBtn"
    @click="showMenu"
  >
    <font-awesome-icon :icon="['fas', 'bars']" />
  </button>
  <button
    class="btn btn-square btn-outline absolute top-5 left-1/2 ml-5 bg-base-300 z-50"
    v-show="state.showHideBtn"
    @click="hideMenu"
  >
    <font-awesome-icon :icon="['fas', 'eye-slash']" />
  </button>
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
  import { useUIStore } from "../../store/ui.store";

  const props = defineProps<{ isMobile?: boolean }>();

  const route = useRoute();
  const router = useRouter();

  const emit = defineEmits(["update"]);

  const id = ref<number>();

  const projectStore = useProjectStore();
  const userStore = useUserStore();
  const UIStore = useUIStore();

  const apiCall = useApiCall();

  const project = ref<Workspace>();

  const user_designated_tasks = ref<TaskLite[]>();

  const isLoaded = ref(false);

  const isModOrOwner = ref<boolean>();

  const state = reactive({
    selected: -10,
    selectedUser: 0 as number,
    selectedTask: 0 as number,
    addUserModal: false,
    isDark: false,
    isMobile: false,
    offlineMode: false,
    showMenu: true,
    showHideBtn: false,
    showMenuBtn: false,
    designatedOpen: false,
    usersOpen: false,
  });

  watch(
    () => UIStore.isMobile,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        //showMenu.value = !isMobile.value;
        state.isMobile = UIStore.isMobile;
        state.showMenu = !state.isMobile;
        state.showMenuBtn = state.isMobile;
      }
    }
  );

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
        project.value = projectStore.getCurrent();
        user_designated_tasks.value = getProjectDesignatedTasks();
        isModOrOwner.value = checkIsModOrOwner(newValue as number);
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
      const ws = projectStore.getCurrent() as Workspace;
      if (newValue && project.value?.workspace_id != ws.workspace_id) {
        project.value = await projectStore.updateCurrent();
      } else if (newValue != oldValue) {
        project.value = projectStore.getCurrent();
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  );

  //NAVIGATION

  function handleDropdown(name: string) {
    if (name == "designated") state.designatedOpen = !state.designatedOpen;
    if (name == "users") state.usersOpen = !state.usersOpen;
  }

  function goTo(path: string, obj_id?: number) {
    let stateSelected: number;
    if (
      (path !== "task" && path != "user" && state.selectedTask) ||
      state.selectedUser
    ) {
      state.selectedUser = 0;
      state.selectedTask = 0;
    }
    if (path == "talk" || (path == "user" && state.selected < 0)) {
      state.selected = 0;
    }
    switch (path) {
      case "home":
        router.push(`/project/info?id=${id.value}`);
        stateSelected = -10;
        break;
      case "scrum":
        router.push("/project/scrum");
        stateSelected = -1;
        break;
      case "designated":
        handleDropdown("designated");
        router.push("/project/designated");
        stateSelected = -2;
        break;
      case "all":
        router.push("/project/tasks");
        stateSelected = -3;
        break;
      case "settings":
        router.push(`/project/settings?id=${id.value}`);
        stateSelected = -4;
        break;
      case "blocked":
        router.push(`/project/blocked`);
        stateSelected = -5;
        break;
      case "task":
        router.push(`/project/task?id=${obj_id}`);
        stateSelected = obj_id as number;
        break;
      case "user":
        router.push(`/project/user?id=${obj_id}`);
        stateSelected = obj_id as number;
        break;
    }
    setTimeout(
      () =>
        path == "task" || path == "user"
          ? path == "task"
            ? (state.selectedTask = stateSelected)
            : (state.selectedUser = stateSelected)
          : (state.selected = stateSelected),
      100
    );
  }

  function showMenu() {
    state.showMenu = true;
    state.showHideBtn = true;
    state.showMenuBtn = false;
  }

  function hideMenu() {
    state.showMenu = false;
    state.showHideBtn = false;
    state.showMenuBtn = true;
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
    if (allUserTasks)
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
    state.isDark = UIStore.checkIsDarkTheme(); //JSON.parse(localStorage.getItem("darkTheme") as string);
  }

  async function updateProject() {
    project.value = await projectStore.updateCurrent();
    emit("update");
  }

  onBeforeMount(async () => {
    state.offlineMode = UIStore.checkOfflineMode();
    state.isMobile = UIStore.isMobile;
    state.showMenuBtn = state.isMobile;
    state.showMenu = !state.isMobile;
    state.showHideBtn = state.showMenu && state.isMobile;
    if (route.query.id) id.value = +route.query.id;
    project.value = projectStore.getCurrent() as Workspace;
    if (project.value && project.value.workspace_id) {
      user_designated_tasks.value = getProjectDesignatedTasks();
      initComponent(project.value.workspace_id);
    } else {
      const response = (await projectStore.fetchProjectById(
        id.value as number
      )) as Workspace;
      // (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
      //   params: { id: id.value },
      // })) as Workspace;
      if (response.workspace_id) {
        projectStore.setCurrent(response);
        project.value = response;
        initComponent(response.workspace_id);
        user_designated_tasks.value = getProjectDesignatedTasks();
      }
    }
  });
</script>
