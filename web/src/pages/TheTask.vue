<template lang="">
  <div class="flex flex-col justify-center mt-1 w-full ml-7 rounded-2xl">
    <div
      class="w-full rounded-xl border-b-4 border-x-4 border-secondary bg-white text-black pb-2"
      v-if="card"
    >
      <!-- #MARK: TITLE      
       -->
      <div class="flex flex-col text-center gap-0">
        <div :class="['w-full h-5 rounded-t-lg', title_color]"></div>

        <div
          class="flex flex-row justify-stretch mb-0 border-b-4 border-secondary gap-0"
        >
          <h2
            class="text-2xl ml-2 border-r-2 border-secondary px-2 py-4 my-auto min-w-44"
          >
            {{ card.workspace.project_name }}
          </h2>

          <div class="rounded-t-lg my-auto w-6/12 text-center">
            <TaskTitle
              :title="card.title"
              :task_id="card.task_id"
              @update="updateTask"
            ></TaskTitle>
          </div>

          <div class="my-auto w-1/12 border-l-2 border-secondary py-5 px-0.5">
            <p>by {{ card.owner.username }}</p>
          </div>
          <div
            class="flex flex-col justify-center gap-3 w-4/12 border-l-2 border-secondary"
          >
            <!-- MARK: TASK STATE
           -->
            <div
              class="flex flex-row justify-start gap-1 h-7 border-b-2 border-secondary"
            >
              <TaskPrioritySelectable
                :priority="card.priority"
                @update-priority="updatePriority"
              ></TaskPrioritySelectable>

              <TaskCommonSelectable
                :type="'Status'"
                :selected="card.status"
                @update-status="updateTaskOptions"
              ></TaskCommonSelectable>

              <TaskCommonSelectable
                :type="'Effort'"
                :selected="card.effort"
                @update-effort="updateTaskOptions"
              ></TaskCommonSelectable>

              <TaskCommonSelectable
                :type="'TaskType'"
                :selected="card.task_type"
                @update-task-type="updateTaskOptions"
              ></TaskCommonSelectable>
            </div>

            <TaskProgress
              :progress_value="progress_value"
              @update="updateProgress"
            ></TaskProgress>
          </div>
        </div>
        <!-- 
        #MARK: TASK BODY
        -->
        <div class="px-3 py-5 flex flex-col gap-2 justify-start">
          <!-- <h2 class="text-2xl text-start">{{ card.subtitle }}</h2> -->
          <TaskSubtitle
            :subtitle="card.subtitle"
            :task_id="card.task_id"
            @update="updateTask"
          />
          <div class="flex flex-row justify-start gap-2">
            <div v-for="tag in card.task_tags" class="flex flex-row gap-1">
              <p
                class="rounded-lg bg-secondary text-white capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110"
              >
                {{ tag }}
              </p>
            </div>
          </div>

          <div class="flex flex-row gap-2" v-if="card.dependencies">
            <p v-for="task in card.dependencies">{{ task.title }}</p>
          </div>

          <div class="text-start">
            <p class="text-xl py-5 underline">Designated to:</p>
            <div class="flex flex-row justify-start gap-5 ml-5">
              <div class="w-auto" v-for="user of card.designated_to">
                <div
                  class="flex flex-row gap-2 align-middle justify-start text-lg font-semibold"
                >
                  <div class="avatar">
                    <div class="w-8 rounded-full">
                      <img :src="user.avatar" />
                    </div>
                  </div>
                  <span> {{ user.username }}, </span>
                </div>
              </div>
            </div>
          </div>
          <div class="flex flex-col divide-y-2 divide-secondary">
            <div class="flex my-5 mx-2 flex-col text-start">
              <p class="text-xl underline font-semibold pb-2">Description:</p>
              <p class="indent-5 text-lg">{{ card.description }}</p>
            </div>

            <div class="flex my-5 mx-2 flex-col text-start">
              <p class="text-xl underline font-semibold pb-2">Issues:</p>
              <div
                class="flex flex-col gap-2 indent-4"
                v-for="issue in card.progressItems"
              >
                <div class="form-control w-3/5">
                  <label
                    class="cursor-pointer label flex flex-row justify-between gap-5"
                  >
                    <span class="label-text text-black text-lg"
                      >{{ issue.sentence }}
                    </span>
                    <input
                      type="checkbox"
                      :checked="issue.isCompleted"
                      class="checkbox checkbox-secondary"
                    />
                  </label>
                </div>
              </div>
            </div>
          </div>

          <!-- 
          #MARK: task updates
          -->
          <div class="flex flex-col border-t-2 border-secondary pt-5">
            <div v-for="update in card.updates">
              <div class="flex flex-col justify-center">
                <p>{{ update.description }}</p>
                <div class="flex flex-row justify-end gap-5">
                  <p>{{ update.created_at }}</p>
                  <p>by {{ update.creator_username }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <TaskControlSideMenu @update="updateTaskById"></TaskControlSideMenu>
  </div>
</template>
<!-- 
#MARK: SCRIPT SETUP
-->
<script setup lang="ts">
  import { ref, onBeforeMount, watch } from "vue";
  import {
    Effort,
    Priority,
    Progress,
    Status,
    Task,
    TaskType,
  } from "../utils/types";
  import { useTaskStore } from "../store/task.store";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import { useRoute } from "vue-router";
  import { taskUtils } from "../utils/task.utils";
  import TaskControlSideMenu from "../components/task/TaskControlSideMenu.vue";
  import TaskProgress from "../components/task/TaskProgress.vue";
  import TaskPrioritySelectable from "../components/task/TaskPrioritySelectable.vue";
  import TaskCommonSelectable from "../components/task/TaskCommonSelectable.vue";
  import TaskTitle from "../components/task/TaskTitle.vue";
  import TaskSubtitle from "../components/task/TaskSubtitle.vue";

  // #region: variables
  const card = ref<Task>();
  const taskStore = useTaskStore();

  const title_color = ref<string>();
  const progress_value = ref<number>();
  const priority_color = ref<string>();

  const route = useRoute();

  const apiCall = useApiCall();

  // #MARK:asdas

  async function fetchTask(task_id: number) {
    const data = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
      params: { id: task_id },
    })) as Task;
    return data;
  }

  async function updateTaskById() {
    if (route.query.id) {
      const id = +route.query.id;
      card.value = await fetchTask(id);
      taskStore.setCurrentTask(card.value);
    }
  }

  async function updateTask(task: Task) {
    taskStore.setCurrentTask(task);
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

  function prepareTaskData(data: Task) {
    title_color.value = taskUtils.getColor(data.color);
    progress_value.value = taskUtils.calcProgress(data.progress);
    priority_color.value = taskUtils.calcPriorityColor(data.priority);
  }

  watch(
    () => route.query.id,
    async (newValue, oldValue) => {
      const id = newValue;
      if (id) {
        const task = await fetchTask(+id);
        if (task) {
          card.value = task;
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
        prepareTaskData(newValue);
      }
    }
  );

  onBeforeMount(async () => {
    const task_id = route.query.id;
    if (task_id) {
      const data = await fetchTask(+task_id);

      if (data) {
        card.value = data;
        taskStore.setCurrentTask(data);
        prepareTaskData(data);
      }
    }
  });
</script>
