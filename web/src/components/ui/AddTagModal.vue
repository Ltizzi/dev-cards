<template lang="">
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div
      class="sm:px-7 sm:py-5 flex flex-col justify-center px-2 py-2 bg-base-100 w-full"
    >
      <ul class="flex flex-row gap-2 py-5">
        <li v-for="tag in tags" class="">{{ tag }},</li>
        <!-- <button
          class="btn btn-circle btn-error rounded-full btn-xs"
          @click="clearTags()"
          v-if="checkView()"
        >
          <font-awesome-icon class="text-white" :icon="['fas', 'minus']" />
        </button> -->
      </ul>

      <div class="flex flex-row gap-5">
        <input
          type="text"
          placeholder="Type new tag"
          v-model="tag"
          class="input input-bordered input-secondary w-full max-w-xs"
          @keydown.enter="addTag(tag)"
        />
        <button
          class="btn btn-success btn-outline rounded-full btn-circle btn-sm my-auto"
          @click="addTag(tag)"
        >
          <font-awesome-icon
            class="text-white size-5"
            :icon="['fas', 'plus']"
          />
        </button>
      </div>

      <div class="h-20">
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
          <span>Success!</span>
        </div>

        <div role="alert" class="alert alert-error" v-if="failed">
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
          <span>Error! Can't remove user.</span>
        </div>
      </div>

      <button class="btn btn-outline btn-primary" @click="closeModal">
        Ok
        <span
          class="loading loading-dots loading-sm"
          v-if="awaitingResponse"
        ></span>
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { useTaskStore } from "../../store/task.store";
  import BaseModal from "../common/BaseModal.vue";
  import { EndpointType } from "../../utils/endpoints";
  import { Task, UITag } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";

  const props = defineProps<{ showModal: boolean }>();
  const emit = defineEmits(["close"]);
  const apiCall = useApiCall();
  const taskStore = useTaskStore();

  const tags = ref<Array<string>>([]);

  const tag = ref<string>();

  const awaitingResponse = ref<boolean>();
  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const errorMessage = ref<string>();

  function closeModal() {
    clearTags();
    emit("close", false);
  }

  async function addTag(newTag: string) {
    await saveTag(newTag);
  }

  function checkView() {
    if (tags.value) {
      return tags.value.length > 0;
    } else return false;
  }

  function clearTags() {
    tag.value = "";
    tags.value = [];
  }

  function checkTag(name: string) {
    return taskUtils.getTagColor(
      taskStore.currentTask.workspace.workspace_id,
      name
    );
  }

  async function saveTag(newTag: string) {
    if (!taskStore.offlineMode) tags.value?.push(newTag);
    awaitingResponse.value = true;

    //TODO: manejar el agregado de task a TagPool
    const response = (await taskStore.saveTag(newTag)) as Task;
    awaitingResponse.value = false;
    if (response.task_id) {
      success.value = true;
      tag.value = "";
      taskStore.setCurrentTask(response);
      setTimeout(() => {
        success.value = false;
      }, 1000);
    } else {
      failed.value = true;
      errorMessage.value = JSON.stringify(response);
      setTimeout(() => {
        failed.value = false;
        errorMessage.value = "";
      }, 2000);
    }
  }

  function reset() {
    // tags.value = [];
    tags.value = taskStore.currentTask.task_tags as Array<string>;
    tag.value = "";
  }

  watch(
    () => props.showModal,
    (newValue, oldValue) => {
      if (newValue) {
        reset();
      } else clearTags();
    }
  );

  onBeforeMount(() => {
    tags.value = taskStore.currentTask.task_tags as Array<string>;
  });
</script>
