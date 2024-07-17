<template lang="">
  <BaseDialog
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div class="sm:px-7 sm:py-5 flex flex-col gap-5 justify-center px-2 py-2">
      <h1 class="2xltext-3xl text-center text-xl font-bold">New Task</h1>
      <div class="flex flex-row justify-evenly gap-5 py-3">
        <label
          class="input input-bordered input-primary w-1/2 flex items-center gap-2"
        >
          Title
          <input type="text" class="grow" placeholder="Title" v-model="title" />
        </label>
        <label
          class="input input-bordered input-primary w-1/2 flex items-center gap-2"
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
      <div class="flex flex-row flex-wrap justify-evenly gap-5 py-3">
        <select class="select select-primary w-80 max-w-xs" v-model="color">
          <option disabled selected>Pick a color</option>
          <option v-for="color in ColorEnumArray">{{ color }}</option>
        </select>
        <select class="select select-primary w-80 max-w-xs" v-model="priority">
          <option disabled selected>Choose Priority</option>
          <option v-for="priority in PriorityEnumArray">{{ priority }}</option>
        </select>
        <select class="select select-primary w-80 max-w-xs" v-model="effort">
          <option disabled selected>Choose Effort</option>
          <option v-for="effort in EffortEnumArray">{{ effort }}</option>
        </select>

        <select class="select select-primary w-80 max-w-xs" v-model="task_type">
          <option disabled selected>Choose Task type</option>
          <option v-for="taskType in TaskTypeEnumArray">{{ taskType }}</option>
        </select>
      </div>
      <div class="flex flex-col justify-evenly gap-5 py-3">
        <ol class="flex flex-col justify-center text-white">
          <li
            v-for="item in progressItems"
            class="text-base text-center text-white"
          >
            {{ item.sentence }}
          </li>
        </ol>

        <div class="flex flex-row justify-evenly gap-5">
          <label
            class="input input-bordered input-primary flex w-11/12 items-center gap-2"
          >
            Add Issue
            <input
              type="text"
              class="grow"
              placeholder="Write more code"
              v-model="issue"
            />
          </label>
          <button class="btn btn-outline btn-info w-1/12" @click="addIssue">
            Add
          </button>
        </div>
      </div>
      <textarea
        class="textarea textarea-primary"
        placeholder="Description"
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
        <span>Error! Task failed successfully.</span>
      </div>

      <button class="btn btn-outline btn-accent" @click="createTask">
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
  import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  import {
    Color,
    Priority,
    Effort,
    TaskType,
    UserLite,
    WorkspaceLite,
  } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useProjectStore } from "../../store/project.store";
  import { EndpointType } from "../../utils/endpoints";
  import { Status, Progress, Task } from "../../utils/types";

  const apiCall = useApiCall();
  const taskStore = useTaskStore();
  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const title = ref<string>();
  const subtitle = ref<string>();
  const color = ref<Color>();
  const priority = ref<Priority>();
  const effort = ref<Effort>();
  const task_type = ref<TaskType>();
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

  async function createTask() {
    requestSent.value = true;
    const owner: UserLite = {
      user_id: userStore.self.user_id,
      username: userStore.self.username,
      email: userStore.self.email,
      avatar: userStore.self.avatar,
    };

    const project: WorkspaceLite = {
      workspace_id: projectStore.current.workspace_id,
      project_name: projectStore.current.project_name,
      owner: {
        user_id: projectStore.current.owner.user_id,
        username: projectStore.current.owner.username,
        email: projectStore.current.owner.email,
        avatar: projectStore.current.owner.avatar,
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
        color: color.value,
        priority: priority.value,
        effort: effort.value,
        status: Status.PENDING,
        progress: Progress.NULL,
        task_type: task_type.value,
        description: description.value,
        progressItems: progressItems.value,
        owner: owner,
        project: project,
      };
      const response = (await apiCall.post(
        EndpointType.TASK_NEW,
        newTask
      )) as Task;
      if (response.task_id) {
        taskStore.addTaskToCurrentProject(response);
        requestSent.value = false;
        success.value = true;
        setTimeout(() => {
          success.value = false;
        }, 3000);
      } else {
        failed.value = true;
        setTimeout(() => {
          requestSent.value = false;
          closeModal();
        }, 3000);
      }
    }
  }

  // MODAL

  const emit = defineEmits(["close"]);

  function closeModal() {
    progressItems.value = [];

    emit("close", false);
  }

  const props = defineProps<{ showModal: boolean }>();

  console.log(
    ColorEnumArray,
    PriorityEnumArray,
    EffortEnumArray,
    TaskTypeEnumArray
  );
</script>
