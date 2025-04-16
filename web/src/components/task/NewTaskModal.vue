<template lang="">
  <BaseDialog
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col gap-5 lg:gap-2 xl:gap-5 justify-center px-2 py-2 text-base-content w-full"
    >
      <h1 class="2xl:text-3xl text-center text-xl font-bold">New Task</h1>
      <div
        class="flex flex-row justify-evenly gap-3 gap-y-5 lg:gap-y-0 py-3 flex-wrap lg:flex-nowrap"
      >
        <label
          class="input input-bordered input-primary lg:w-1/2 w-4/5 flex items-center gap-2 lg:input-sm xl:input-md"
        >
          Title
          <input type="text" class="grow" placeholder="Title" v-model="title" />
        </label>
        <label
          class="input input-bordered input-primary lg:w-1/2 w-4/5 lg:input-sm xl:input-md flex items-center gap-2"
        >
          Subtitle
          <input
            type="text"
            class="grow"
            placeholder="Subtitle"
            v-model="subtitle"
          />
        </label>
      </div>
      <div
        class="flex flex-row flex-wrap justify-evenly gap-5 py-3 lg:py-0.5 xl:py-3"
      >
        <select
          class="select select-primary select-sm lg:w-80 w-4/5 lg:max-w-xs"
          v-model="color"
        >
          <option disabled selected>Pick a color</option>
          <option v-for="color in ColorEnumArray" class="flex items-center">
            {{ color }}
          </option>
        </select>

        <select
          class="select select-primary select-sm lg:w-80 w-4/5 lg:max-w-xs"
          v-model="priority"
        >
          <option disabled selected>Choose Priority</option>
          <option v-for="priority in PriorityEnumArray">{{ priority }}</option>
        </select>
        <select
          class="select select-primary select-sm lg:w-80 w-4/5 lg:max-w-xs"
          v-model="effort"
        >
          <option disabled selected>Choose Effort</option>
          <option v-for="effort in EffortEnumArray">{{ effort }}</option>
        </select>

        <select
          class="select select-primary select-sm lg:w-80 w-4/5 lg:max-w-xs"
          v-model="task_type"
        >
          <option disabled selected>Choose Task type</option>
          <option v-for="taskType in TaskTypeEnumArray">{{ taskType }}</option>
        </select>
      </div>
      <!--       v-if="color != 'Pick a color'" -->
      <div
        :class="[
          'lg:w-full w-4/5 mx-auto h-2 rounded-xl pt-2',
          color != 'Pick a color' ? getColor(color) : '',
        ]"
      ></div>
      <div class="flex flex-col justify-evenly gap-5 pb-3 lg:pb-0.5 xl:pb-3">
        <ol class="flex flex-col justify-center text-white">
          <li
            v-for="item in progressItems"
            class="text-base lg:text-sm xl:text-base text-center text-base-content"
          >
            {{ item.sentence }}
          </li>
        </ol>

        <div class="flex flex-row justify-evenly gap-5">
          <label
            class="input input-bordered select-sm input-primary flex w-11/12 items-center gap-2"
          >
            Add Issue
            <input
              type="text"
              class="grow"
              placeholder="Write more code"
              v-model="issue"
            />
          </label>
          <button
            class="btn btn-outline btn-sm btn-info w-1/12"
            @click="addIssue"
          >
            Add
          </button>
        </div>
      </div>
      <textarea
        class="textarea textarea-primary xl:textarea-lg textarea-xs"
        placeholder="Description"
        rows="4"
        v-model="description"
      ></textarea>

      <div role="alert" class="alert alert-success" v-if="success">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6 shrink-0 stroke-current"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span>Your task has been created!</span>
      </div>
      <div role="alert" class="alert alert-error" v-if="failed && requestSent">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6 shrink-0 stroke-current"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span>Error! {{ error_message }}</span>
      </div>

      <button
        class="btn btn-accent btn-outline w-20 mx-auto"
        @click="createTask"
      >
        Create
        <span class="loading loading-dots loading-sm" v-if="requestSent"></span>
      </button>
    </div>
  </BaseDialog>
</template>
<script setup lang="ts">
  import BaseDialog from "../common/BaseModal.vue";
  import { defineProps, defineEmits, ref } from "vue";
  import {
    ProgressItem,
    ColorEnumArray,
    PriorityEnumArray,
    EffortEnumArray,
    TaskTypeEnumArray,
  } from "../../utils/types";
  //import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  import {
    Color,
    Priority,
    Effort,
    TaskType,
    UserLite,
    WorkspaceLite,
    Status,
    Progress,
    Task,
  } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useProjectStore } from "../../store/project.store";
  import { taskUtils } from "../../utils/task.utils";
  import { Workspace } from "../../utils/types";

  const taskStore = useTaskStore();
  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const title = ref<string>();
  const subtitle = ref<string>();
  const color = ref<Color | string>("Pick a color");
  const getColor = (color: Color) => taskUtils.getColor(color);
  const priority = ref<Priority | string>("Choose Priority");
  const effort = ref<Effort | string>("Choose Effort");
  const task_type = ref<TaskType | string>("Choose Task type");
  const description = ref<string>();

  const progressItems = ref<Array<ProgressItem>>([]);
  const issue = ref<string>();

  function addIssue() {
    console.log(issue.value);
    if (issue.value && progressItems.value) {
      const newIssue: ProgressItem = {
        sentence: issue.value,
        isCompleted: false,
      };
      progressItems.value.push(newIssue);
      console.log(progressItems.value);
      issue.value = "";
    }
  }

  const requestSent = ref(false);
  const success = ref(false);
  const failed = ref(false);
  const error_message = ref("");

  async function createTask() {
    requestSent.value = true;
    let owner: UserLite = {} as UserLite;
    // if (userStore.self.user_id || userStore.offlineSelf.user_id) {
    //   owner = userStore.getSelfAsUserLite() as UserLite;
    // } else {
    //   userStore.getCurrent();
    //   owner = userStore.getSelfAsUserLite() as UserLite;
    // }
    if (!userStore.self.user_id || !userStore.offlineSelf.user_id) {
      userStore.getCurrent();
    }

    owner = userStore.getSelfAsUserLite() as UserLite;

    const workspace = projectStore.getCurrent() as Workspace;
    const project: WorkspaceLite = {
      workspace_id: workspace.workspace_id as number,
      project_name: workspace.project_name,
      owner: {
        user_id: workspace.owner.user_id,
        username: workspace.owner.username,
        email: workspace.owner.email,
        avatar: workspace.owner.avatar,
      },
    };
    if (
      title.value &&
      subtitle.value &&
      color.value &&
      priority.value &&
      effort.value &&
      task_type.value &&
      progressItems.value &&
      description.value
    ) {
      const newTask: Task = {
        title: title.value,
        subtitle: subtitle.value,
        color: color.value as Color,
        priority: priority.value as Priority,
        effort: effort.value as Effort,
        status: Status.PENDING,
        progress: Progress.NULL,
        task_type: task_type.value as TaskType,
        description: description.value,
        progressItems: progressItems.value,
        owner: owner,
        workspace: project,
      };
      const response = (await taskStore.createTask(newTask)) as Task;
      // (await apiCall.post(
      //   EndpointType.TASK_NEW,
      //   newTask
      // )) as Task;
      if (response.task_id) {
        taskStore.addTaskToCurrentProject(response);
        await projectStore.updateCurrent();
        requestSent.value = false;
        success.value = true;
        setTimeout(() => {
          success.value = false;
          clearFields();
          emit("update");
          closeModal();
        }, 3000);
      } else {
        failed.value = true;
        error_message.value = response as unknown as string;
        setTimeout(() => {
          requestSent.value = false;
          //closeModal();
        }, 3000);
      }
    }
  }

  function clearFields() {
    progressItems.value = [];
    title.value = "";
    subtitle.value = "";
    color.value = "Pick a color";
    priority.value = "Choose Priority";
    effort.value = "Choose Effort";
    task_type.value = "Choose Task type";
    description.value = "";
    progressItems.value = [];
  }

  // MODAL

  const emit = defineEmits(["close", "update"]);

  function closeModal() {
    emit("close", false);
  }

  const props = defineProps<{ showModal: boolean }>();
</script>
