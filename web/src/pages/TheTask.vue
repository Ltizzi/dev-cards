<template lang="">
  <div
    :class="[
      'flex flex-col justify-start my-5 w-full ml-0 rounded-2xl min-h-screen',
    ]"
    v-if="isLoaded"
  >
    <div
      :class="[
        'w-full rounded-xl border-b-4 border-x-4 border-secondary  pb-2 ',
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
        <div :class="['w-full h-6  rounded-t-lg', title_color]"></div>

        <div
          class="flex flex-row h-24 justify-stretch mb-0 border-b-4 border-secondary gap-0"
        >
          <h2
            class="text-2xl ml-2 border-r-2 border-secondary px-2 py-7 my-auto min-w-44"
          >
            {{ card.workspace.project_name }}
          </h2>

          <div class="rounded-t-lg my-auto w-6/12 text-center">
            <TaskTitle
              :title="card.title"
              :task_id="card.task_id"
              :isDark="isDark"
              :darkerCard="darkerCard"
              :canModify="canModify"
              @update="updateTask"
            ></TaskTitle>
          </div>

          <div class="my-auto w-1/12 border-l-2 border-secondary py-8 px-0.5">
            <p>by {{ card.owner.username }}</p>
          </div>
          <div
            class="flex flex-col justify-center gap-3 w-4/12 border-l-2 border-secondary"
          >
            <!-- MARK: TASK STATE
           -->
            <div
              class="flex flex-row justify-start gap-1 h-12 items-center border-b-2 border-secondary"
            >
              <TaskPrioritySelectable
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
          <!-- <h2 class="text-2xl text-start">{{ card.subtitle }}</h2> -->

          <div
            v-if="card.task_tags.length > 0"
            :class="[
              'flex flex-row justify-between gap-2 w-2/4 py-5 h-20   my-auto',
              removeTagActive ? 'hover:cursor-not-allowed' : '',
            ]"
            @mouseover="card.task_tags.length > 0 ? (mouseOverTag = true) : ''"
            @mouseleave="mouseOverTag = false"
            @keydown.esc="removeTagActive ? (removeTagActive = false) : ''"
          >
            <div class="flex flex-row justify-start w-full gap-1 my-auto">
              <div v-for="tag in card.task_tags">
                <p
                  :class="[
                    'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 hover:cursor-pointer',
                    removeTagActive ? 'hover:bg-error' : '',
                    `${getColor(tag)}`,
                  ]"
                  @click="removeTagActive ? removeTag(tag) : goToTag(tag)"
                >
                  {{ tag }}
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
          <div
            class="flex flex-row gap-2 py-1 align-middle"
            v-if="card.dependencies.length > 0"
          >
            <font-awesome-icon
              :icon="['fas', 'sitemap']"
              class="text-primary text-2xl"
            />
            <div v-for="task in card.dependencies" class="flex flex-row">
              <router-link :to="`/project/task?id=${task.task_id}`">
                <p class="text-secondary font-bold text-lg italic underline">
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

          <div class="text-start" v-if="card.designated_to.length > 0">
            <p class="text-lg font-semibold py-5 underline">Designated to:</p>
            <div class="flex flex-row justify-start gap-5 ml-5">
              <div class="w-auto" v-for="user of card.designated_to">
                <div
                  class="flex flex-row gap-4 items-center align-middle justify-start text-lg font-semibold"
                >
                  <div class="avatar">
                    <div class="w-6 rounded-full">
                      <img :src="user.avatar" />
                    </div>
                  </div>
                  <span class="text-base"> {{ user.username }}, </span>
                </div>
              </div>
            </div>
          </div>
          <div
            v-if="
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
              <div class="flex flex-row justify-start gap-5 mt-7">
                <p class="text-lg underline font-semibold pb-5">Issues:</p>
                <AddIssueBtn
                  @update="updateTask"
                  :task_id="card.task_id"
                  :showBtn="showAddIssueBtn"
                />
              </div>

              <div
                class="flex flex-col gap-2 indent-4"
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
      :projectId="projectStore.current.workspace_id"
      :taskId="taskId"
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
    Status,
    TagPool,
    Task,
    TaskType,
    UITag,
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
  import { useProjectStore } from "../store/project.store";
  import {
    checkIfUserisTaskOwner,
    checkIsDesignated,
    checkIsModOrOwner,
    checkIsCollaborator,
  } from "../utils/auth.utils";
  import { dateUtils } from "../utils/date.utils";
  import { useUserStore } from "../store/user.store";

  // #region: variables
  const card = ref<Task>();
  const taskId = ref<number>();
  const taskStore = useTaskStore();
  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const title_color = ref<string>();
  const progress_value = ref<number>();
  const priority_color = ref<string>();
  const mouseOverTag = ref<boolean>(false);
  const removeTagActive = ref<boolean>();

  const route = useRoute();
  //const router = useRouter();

  const apiCall = useApiCall();

  const showAddIssueBtn = ref<boolean>();

  const isDark = ref<boolean>();

  const darkerCard = ref<boolean>();

  const canModify = ref<boolean>();

  const isLoaded = ref<boolean>();

  // #MARK:asdas

  async function fetchTask(task_id: number) {
    console.log("soy yo");
    const data = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
      params: { id: task_id },
    })) as Task;
    return data;
  }

  async function updateTaskById() {
    console.log("SOY RE YO");
    if (route.query.id) {
      const id = +route.query.id;
      card.value = await fetchTask(id);
      taskStore.setCurrentTask(card.value);
    }
  }

  async function updateTask(task: Task) {
    taskStore.setCurrentTask(task);
    await projectStore.updateCurrent();
    prepareTaskData(task);
  }

  async function updateProgress(progress: Progress) {
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_PROGRESS,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          progress: progress,
        },
      }
    )) as Task;
    if (response.task_id == card.value?.task_id) {
      // taskStore.setCurrentTask(response);
      // prepareTaskData(taskStore.currentTask);
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK PROGRESS");
  }

  async function updatePriority(priority: Priority) {
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_PRIORITY,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          priority: priority,
        },
      }
    )) as Task;
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
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_STATUS,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          status: option,
        },
      }
    )) as Task;
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK STATUS");
  }

  async function updateTaskEffort(option: Effort) {
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_EFFORT,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          effort: option,
        },
      }
    )) as Task;
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK EFFORT");
  }

  async function updateTaskType(option: TaskType) {
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_TYPE,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          type: option,
        },
      }
    )) as Task;
    if (response.task_id == card.value?.task_id) {
      updateTask(response);
    } else console.error("CAN'T UPDATE TASK TYPE");
  }

  function activeRemoveTag() {
    removeTagActive.value = !removeTagActive.value;
  }

  async function removeTag(tag: string) {
    if (card.value) {
      card.value.task_tags = card.value.task_tags?.filter(
        (t: string) => t.toLowerCase() != tag.toLowerCase()
      );
    }
    const response = (await apiCall.patch(
      EndpointType.TASK_REMOVE_TAG,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          tag: tag,
        },
      }
    )) as Task;
    if (response.task_id == card.value?.task_id) {
      card.value = response;
      let tags = JSON.parse(localStorage.getItem("tags") as string); //removing tag from tag pool in local
      tags = tags.map((tp: TagPool) => {
        if (tp.workspace_id === card.value?.workspace.workspace_id) {
          tp.tags = tp.tags.filter(
            (t: UITag) => t.name.toLowerCase() !== tag.toLowerCase()
          );
        }
        return tp;
      });
      localStorage.setItem("tags", JSON.stringify(tags));
    }
  }

  function getColor(name: string) {
    const ws_id = projectStore.current.workspace_id;
    const tags: UITag[] = taskUtils.getTagColor(ws_id, name);
    if (tags.length > 0) {
      return tags[0].color;
    } else {
      taskUtils.addTagToTagsPool(name, ws_id);
      getColor(name);
    }
  }

  function prepareTaskData(data: Task) {
    title_color.value = taskUtils.getColor(data.color);
    progress_value.value = taskUtils.calcProgress(data.progress);
    priority_color.value = taskUtils.calcPriorityColor(data.priority);
  }

  function checkUserCanModifyTask() {
    return (
      checkIsModOrOwner(projectStore.current.workspace_id) ||
      checkIsDesignated(
        projectStore.current.workspace_id,
        card.value?.task_id as number
      ) ||
      checkIfUserisTaskOwner(card.value?.task_id as number, userStore.self)
    );
  }

  async function autoAssignTask() {
    const response = (await apiCall.post(
      EndpointType.TASK_AUTOASSIGN,
      {},
      {
        params: {
          task_id: card.value?.task_id,
          ws_id: card.value?.workspace.workspace_id,
        },
      }
    )) as Task;
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
          prepareTaskData(task);
        }
      }
    }
  );

  watch(
    () => taskStore.currentTask,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        card.value = newValue;
        taskId.value = newValue.task_id;
        prepareTaskData(newValue);
      }
    }
  );

  onBeforeMount(() => {
    if (localStorage.getItem("darkTheme")) {
      isDark.value = JSON.parse(localStorage.getItem("darkTheme") as string);
      darkerCard.value = JSON.parse(
        localStorage.getItem("darkerCards") as string
      );
    }
  });

  onMounted(async () => {
    const task_id = route.query.id;
    if (taskStore.currentTask.task_id == +taskId) {
      card.value = taskStore.currentTask;
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
        const data = await fetchTask(+task_id);

        if (data.task_id == +task_id) {
          card.value = data;
        }
      }
    }
    if (card.value) {
      taskId.value = card.value.task_id;
      canModify.value = checkUserCanModifyTask();
      taskStore.setCurrentTask(card.value);
      prepareTaskData(card.value);
      isLoaded.value = true;
    }
  });
</script>
