<template lang="">
  <div
    :class="[
      'flex flex-col justify-start lg:my-5 w-screen lg:w-full  ml-16 rounded-2xl min-h-screen lg:-ml-8 xl:ml-0 ',
    ]"
    v-if="isLoaded"
  >
    <div
      :class="[
        'w-full rounded-xl border-b-4 border-x-4 border-secondary  pb-2  motion-duration-700 motion-preset-rebound-up motion-scale-in-50 motion-ease-in-out max-w-fit lg:max-w-full ',
        !isDark
          ? 'bg-base-100 text-base-content'
          : darkerCard
          ? 'bg-neutral text-neutral-content'
          : 'text-base-300 bg-base-content',
      ]"
      v-if="card"
    >
      <!-- #MARK: TITLE      
  
       -->
      <div class="flex flex-col text-center gap-0">
        <div :class="['lg:w-full  h-6  rounded-t-lg', title_color]"></div>

        <div
          class="flex flex-row 2xl:flex-nowrap flex-wrap 2xl:h-24 h-auto justify-stretch mb-0 border-b-4 w-fit lg:w-full border-secondary gap-0"
        >
          <div class="w-fit lg:w-full flex flex-row justify-center">
            <h2
              class="2xl:text-2xl text-xs text-start lg:text-center lg:ml-2 border-r-2 border-secondary lg:px-2 py-7 my-auto lg:min-w-44 lg:w-1/6 w-12 max-w-xl"
              v-if="!isMobile"
            >
              {{ card.workspace.project_name + " " + card.task_id }}
            </h2>

            <div
              class="rounded-t-lg my-auto 2xl:w-10/12 w-9/12 max-w-3xl min-w-40 text-center"
            >
              <TaskTitle
                :title="card.title"
                :task_id="card.task_id"
                :isDark="isDark"
                :darkerCard="darkerCard"
                :canModify="canModify"
                @update="updateTask"
              ></TaskTitle>
            </div>

            <div
              class="my-auto 2xl:w-1/6 w-2/12 max-w-32 border-l-2 border-secondary py-8 xl:px-0.5 px-5 text-xs"
            >
              <p>by {{ card.owner.username }}</p>
            </div>
          </div>

          <div
            class="flex 2xl:w-4/12 xl:max-w-full lg:max-w-5xl w-full md:max-w-4xl sm:max-w-3xl max-w-xl flex-col justify-center gap-3 border-l-2 border-secondary"
          >
            <!-- MARK: TASK STATE
           -->
            <div
              class="flex flex-row flex-wrap lg:flex-nowrap justify-start w-full gap-0 lg:sgap-1 xl:h-12 items-center 2xl:border-b-2 border-secondary text-xs"
            >
              <TaskPrioritySelectable
                class="ml-0.5 lg:ml-0"
                :priority="card.priority"
                :isDark="isDark"
                :darkerCard="darkerCard"
                :canModify="canModify"
                @update-priority="updatePriority"
              ></TaskPrioritySelectable>

              <TaskCommonSelectable
                :type="'Status'"
                :selected="card.status"
                :canModify="canModify"
                :isDark="isDark"
                :darkerCard="darkerCard"
                @update-status="updateTaskOptions"
              ></TaskCommonSelectable>

              <TaskCommonSelectable
                :type="'Effort'"
                :selected="card.effort"
                :canModify="canModify"
                :isDark="isDark"
                :darkerCard="darkerCard"
                @update-effort="updateTaskOptions"
              ></TaskCommonSelectable>

              <TaskCommonSelectable
                :type="'TaskType'"
                :selected="card.task_type"
                :canModify="canModify"
                :isDark="isDark"
                :darkerCard="darkerCard"
                @update-task-type="updateTaskOptions"
              ></TaskCommonSelectable>
            </div>

            <TaskProgress
              :progress_value="progress_value"
              :canModify="canModify"
              :isDark="isDark"
              :darkerCard="darkerCard"
              @update="updateProgress"
            ></TaskProgress>
          </div>
        </div>
        <!-- 
        #MARK: TASK BODY
        -->
        <div class="px-7 pt-10 flex flex-col gap-5 justify-start">
          <TagsList
            :isSpecial="false"
            :tags="normal_tags"
            :canModify="canModify"
            @removeTag="removeTag"
            @removeSpecialTag="removeSpecialTag"
            @navigate="goToTag"
          />
          <!-- <h2 class="text-2xl text-start">{{ card.subtitle }}</h2> -->
          <!-- <div>
            <div
              v-if="normal_tags && normal_tags.length > 0"
              :class="[
                'flex flex-row justify-between gap-2 lg:w-2/4 py-5 h-20   my-auto',
                removeTagActive ? 'hover:cursor-not-allowed' : '',
              ]"
              @mouseover="normal_tags.length > 0 ? (mouseOverTag = true) : ''"
              @mouseleave="mouseOverTag = false"
              @keydown.esc="removeTagActive ? (removeTagActive = false) : ''"
            >
              <div
                class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto"
              >
                <div v-for="tag in normal_tags">
                  <p
                    :class="[
                      'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 ',
                      removeTagActive
                        ? 'hover:bg-error hover:cursor-not-allowed'
                        : 'hover:cursor-pointer',
                      `${tag.color}`,
                    ]"
                    @click="
                      removeTagActive ? removeTag(tag.name) : goToTag(tag.name)
                    "
                  >
                    <!- getColor(tag) 
                    {{ tag.name }}
                  </p>
                </div>
              </div>
              <div class="tooltip" data-tip="Click to remove tags!">
                <button
                  :class="[
                    'btn btn-error',
                    removeTagActive
                      ? 'hover:cursor-not-allowed'
                      : 'hover:cursor:pointer ',
                  ]"
                  v-if="mouseOverTag && canModify"
                  @click="activeRemoveTag()"
                >
                  <font-awesome-icon
                    :icon="['fas', 'trash']"
                    class="text-white size-5 my-auto"
                  />
                </button>
              </div>
            </div>
          </div> -->

          <div
            class="flex flex-row gap-2 py-1 align-middle"
            v-if="card.dependencies && card.dependencies.length > 0"
          >
            <font-awesome-icon
              :icon="['fas', 'sitemap']"
              class="text-primary text-2xl"
            />
            <div v-for="task in card.dependencies" class="flex flex-row">
              <router-link :to="`/project/task?id=${task.task_id}`">
                <p
                  class="text-secondary font-bold lg:text-lg text-base italic underline"
                >
                  {{ task.title }}
                </p>
              </router-link>
              <p class="text-info font-bold text-lg ml-1">,</p>
            </div>
          </div>

          <TaskSubtitle
            :subtitle="card.subtitle"
            :task_id="card.task_id"
            :canModify="canModify"
            :isDark="isDark"
            :darkerCard="darkerCard"
            @update="updateTask"
            class="ml-0"
          />

          <div
            class="text-start"
            v-if="card.designated_to && card.designated_to.length > 0"
          >
            <p class="lg:text-lg text-sm font-semibold py-5 underline">
              Designated to:
            </p>
            <div class="flex flex-row justify-start gap-5 ml-5">
              <div class="w-auto" v-for="user of card.designated_to">
                <!-- <div
                  class="flex flex-row gap-4 items-center align-middle justify-start text-lg font-semibold"
                >
                  <div class="avatar">
                    <div class="w-6 rounded-full">
                      <img :src="user.avatar" />
                    </div>
                  </div>
                  <span class="text-base"> {{ user.username }}, </span>
                </div> -->
                <BaseUserAvatarItem
                  :avatar="user.avatar"
                  :username="user.username"
                  :id="user.user_id"
                />
              </div>
            </div>
          </div>
          <div
            v-if="
              card.designated_to &&
              card.designated_to.length == 0 &&
              (checkIsCollaborator(card.workspace.workspace_id) ||
                checkIsModOrOwner)
            "
            class="flex justify-start"
          >
            <button class="btn btn-outline btn-primary" @click="autoAssignTask">
              Auto assign
            </button>
          </div>
          <div class="flex flex-col divide-y-2 divide-secondary">
            <!-- <TaskDescription
              :description="card.description"
              :task_id="card.task_id"
              :isDark="isDark"
              @update="updateTask"
            /> -->
            <BaseEditDescription
              :description="card.description"
              :id="card.task_id"
              :isDark="isDark"
              :canModify="canModify"
              :type="'task'"
              @update="updateTask"
            />
            <div
              class="flex my-5 mx-2 flex-col text-start"
              @mouseover="
                canModify ? (showAddIssueBtn = true) : (showAddIssueBtn = false)
              "
              @mouseleave="showAddIssueBtn = false"
            >
              <div
                class="flex flex-row justify-start gap-5 mt-7 min-h-9 h-auto"
              >
                <p
                  class="text-lg underline font-semibold pb-5"
                  v-if="card.progressItems.length > 0"
                >
                  Issues:
                </p>
                <p v-else>Add Issue</p>
                <AddIssueBtn
                  @update="updateTask"
                  :task_id="card.task_id"
                  :showBtn="showAddIssueBtn"
                />
              </div>

              <div
                class="flex flex-col gap-1 mb-5 indent-4"
                v-for="issue in card.progressItems"
              >
                <TaskIssue
                  :task_id="card.task_id"
                  :issue="issue"
                  :canModify="canModify"
                  :darkerCard="darkerCard"
                  @update="updateTask"
                />
              </div>
            </div>
          </div>

          <!-- 
          #MARK: task updates
          -->
          <div
            class="flex flex-col justify-start border-t-2 border-secondary pt-4"
            v-show="card.updates && card.updates.length > 0"
          >
            <div v-for="update in card.updates" class="flex flex-col gap-2">
              <div class="flex flex-col text-start gap-1">
                <p>{{ update.description }}</p>
                <div class="flex flex-row justify-end gap-5">
                  <p>{{ dateUtils.generateDateTemplate(update.created_at) }}</p>
                  <p>by {{ update.creator_username }}</p>
                </div>
              </div>
              <div class="divider divider-secondary"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <TaskControlSideMenu
      @update="updateTaskById"
      :projectId="ws_id"
      :taskId="taskId"
      :isMobile="isMobile"
    ></TaskControlSideMenu>
  </div>
</template>
<!-- 
#MARK: SCRIPT SETUP
-->
<script setup lang="ts">
  import { ref, onBeforeMount, onMounted, watch } from "vue";
  import {
    Effort,
    Priority,
    Progress,
    SpecialTag,
    Status,
    TagPool,
    Task,
    TaskType,
    UITag,
    Workspace,
  } from "../utils/types";
  import { useTaskStore } from "../store/task.store";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import { useRoute, useRouter } from "vue-router";
  import { taskUtils } from "../utils/task.utils";
  import TaskControlSideMenu from "../components/task/TaskControlSideMenu.vue";
  import TaskProgress from "../components/task/TaskProgress.vue";
  import TaskPrioritySelectable from "../components/task/TaskPrioritySelectable.vue";
  import TaskCommonSelectable from "../components/task/TaskCommonSelectable.vue";
  import TaskTitle from "../components/task/TaskTitle.vue";
  import TaskSubtitle from "../components/task/TaskSubtitle.vue";
  import BaseEditDescription from "../components/common/BaseEditDescription.vue";
  //import TaskDescription from "../components/task/TaskDescription.vue";
  import TaskIssue from "../components/task/TaskIssue.vue";
  import AddIssueBtn from "../components/ui/AddIssueBtn.vue";
  import BaseUserAvatarItem from "../components/common/BaseUserAvatarItem.vue";
  import TagsList from "../components/ui/TagsList.vue";
  import { useProjectStore } from "../store/project.store";
  import {
    checkIfUserisTaskOwner,
    checkIsDesignated,
    checkIsModOrOwner,
    checkIsCollaborator,
  } from "../utils/auth.utils";
  import { dateUtils } from "../utils/date.utils";
  import { useUserStore } from "../store/user.store";
  import { useUIStore } from "../store/ui.store";
  import { useConfigStore } from "../store/config.store";

  // #region: variables
  const card = ref<Task>();
  const taskId = ref<number>();
  const ws_id = ref<number>();
  const taskStore = useTaskStore();
  const projectStore = useProjectStore();
  const userStore = useUserStore();
  const UIStore = useUIStore();
  const configStore = useConfigStore();

  const title_color = ref<string>();
  const progress_value = ref<number>();
  const priority_color = ref<string>();
  const mouseOverTag = ref<boolean>(false);
  const removeTagActive = ref<boolean>(false);

  const normal_tags = ref<UITag[]>();
  const special_tags = ref<SpecialTag[]>();

  const route = useRoute();
  const router = useRouter();

  const apiCall = useApiCall();

  const showAddIssueBtn = ref<boolean>();

  const isDark = ref<boolean>();

  const darkerCard = ref<boolean>();

  const canModify = ref<boolean>();

  const isLoaded = ref<boolean>();

  const isMobile = ref<boolean>(false);

  // #MARK:asdas

  async function fetchTask(task_id: number) {
    const data = await taskStore.fetchTaskById(task_id); //(await apiCall.get(EndpointType.TASK_GET_BY_ID, { params: { id: task_id },  })) as Task;
    return data;
  }

  async function updateTaskById() {
    console.log("SOY RE YO");
    if (route.query.id) {
      const id = +route.query.id;
      card.value = await fetchTask(id);
      //taskStore.setCurrentTask(card.value);
    }
  }

  async function updateTask(task: Task) {
    taskStore.setCurrentTask(task);
    // await projectStore.updateCurrent();
    await prepareTaskData(task);
  }

  async function updateProgress(progress: Progress) {
    const response = await taskStore.updateTaskProgress(progress); //(await apiCall.patch(EndpointType.TASK_UPDATE_PROGRESS, {}, { params: {  task_id: card.value?.task_id,progress: progress,},})) as Task;

    if (response.task_id == card.value?.task_id) {
      // taskStore.setCurrentTask(response);
      // prepareTaskData(taskStore.currentTask);
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK PROGRESS");
  }

  async function updatePriority(priority: Priority) {
    const response = await taskStore.updatePriority(priority); //(await apiCall.patch(EndpointType.TASK_UPDATE_PRIORITY,{},{params: {task_id: card.value?.task_id,priority: priority,},})) as Task;
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK PRIORITY");
  }

  async function updateTaskOptions(
    type: string,
    option: Status | Effort | TaskType
  ) {
    console.log("FROM TASK: ", type, " ", option);
    switch (type) {
      case "Status":
        await updateTaskStatus(option as Status);
        break;
      case "Effort":
        await updateTaskEffort(option as Effort);
        break;
      case "TaskType":
        await updateTaskType(option as TaskType);
        break;
    }
  }

  async function updateTaskStatus(option: Status) {
    const response = await taskStore.updateTaskStatus(option);
    // (await apiCall.patch(EndpointType.TASK_UPDATE_STATUS,{},{params: {task_id: card.value?.task_id,status: option,},})) as Task;
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK STATUS");
  }

  async function updateTaskEffort(option: Effort) {
    const response = await taskStore.updateTaskEffort(option);
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK EFFORT");
  }

  async function updateTaskType(option: TaskType) {
    const response = await taskStore.updateTaskType(option);
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK TYPE");
  }

  function activeRemoveTag() {
    removeTagActive.value = !removeTagActive.value;
  }

  async function removeTag(tag: string) {
    // if (card.value) {
    //   card.value.task_tags = card.value.task_tags?.filter(
    //     (t: string) => t.toLowerCase() != tag.toLowerCase()
    //   );
    // }
    const response = (await taskStore.removeTag(tag)) as unknown as Task;

    if (response.task_id == card.value?.task_id) {
      card.value = response;
      //TODO: Testear si se actualiza correctamente o si hace falta llamar a "prepareData acá"
    }
  }

  async function removeSpecialTag(id: number) {
    //TODO: lógica de store para remover special tags
    //TODO: O quizás no haga falta porque eso quizás sólo debería hacerse desde la configuración del proyecto
    //TODO: o quizás sólo se mueve de la tarea por lo que sería válido usarlo.
  }

  function goToTag(tag: string) {
    router.push(`/project/tasks?tag=${tag}`);
  }

  async function prepareTaskData(data: Task) {
    console.log(data);
    title_color.value = taskUtils.getColor(data.color);
    progress_value.value = taskUtils.calcProgress(data.progress);
    priority_color.value = taskUtils.calcPriorityColor(data.priority);

    const tags = await taskUtils.parseTags(data.task_tags as string[]);
    normal_tags.value = tags.tags;
    special_tags.value = tags.specialTags;
  }

  function checkUserCanModifyTask() {
    const ws = projectStore.getCurrent() as Workspace;
    if (UIStore.offlineMode) return true;
    return (
      checkIsModOrOwner(ws.workspace_id) ||
      checkIsDesignated(ws.workspace_id, card.value?.task_id as number) ||
      checkIfUserisTaskOwner(card.value?.task_id as number, userStore.self)
    );
  }

  async function autoAssignTask() {
    const taskId = card.value?.task_id as number;
    const wsId = card.value?.workspace.workspace_id as number;
    const response = (await taskStore.autoAssignTask(
      taskId,
      wsId
    )) as unknown as Task;
    if (response.task_id == card.value?.task_id) {
      card.value = response;
      await userStore.refreshSelf();
    }
  }

  const just_fetched = ref<boolean>();

  watch(
    () => route.query.id,
    async (newValue, oldValue) => {
      const id = newValue;
      if (newValue != oldValue && id && !just_fetched.value) {
        isLoaded.value = false;
        const task = await fetchTask(+id);
        just_fetched.value = true;
        setTimeout(() => {
          just_fetched.value = false;
        }, 50);
        if (task) {
          isLoaded.value = true;
          card.value = task;
          taskId.value = task.task_id;
          canModify.value = checkUserCanModifyTask();
          taskStore.setCurrentTask(task);
          await prepareTaskData(task);
        }
      }
    }
  );

  watch(
    () => taskStore.currentTask,
    async (newValue, oldValue) => {
      if (newValue != oldValue) {
        card.value = newValue;
        taskId.value = newValue.task_id;
        await prepareTaskData(newValue);
      }
    }
  );

  onBeforeMount(() => {
    isDark.value = UIStore.checkIsDarkTheme(); //JSON.parse(localStorage.getItem("darkTheme") as string);
    darkerCard.value = UIStore.darkerCard; //JSON.parse(localStorage.getItem("darkerCards") as string);
    isMobile.value = UIStore.isMobile;
  });

  onMounted(async () => {
    UIStore.setLoading(true);
    const task_id = route.query.id;
    const ws = projectStore.getCurrent() as Workspace;
    ws_id.value = ws.workspace_id;
    if (taskStore.currentTask.task_id == +taskId) {
      card.value = taskStore.getCurrent();
    }
    // else if (localStorage.getItem("currentTask")) {
    //   const task = JSON.parse(
    //     localStorage.getItem("currentTask") as string
    //   ) as Task;
    //   if (task_id && task.task_id == +task_id) {
    //     card.value = task;
    //   }
    // }
    else {
      if (task_id) {
        const data = (await fetchTask(+task_id)) as Task;

        if (data.task_id == +task_id) {
          card.value = data;
        }
      }
    }
    if (card.value) {
      taskId.value = card.value.task_id;
      canModify.value = checkUserCanModifyTask();
      taskStore.setCurrentTask(card.value);
      await prepareTaskData(card.value);
      isLoaded.value = true;
      UIStore.setLoading(false);
    }
  });
</script>
