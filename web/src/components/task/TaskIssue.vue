<template lang="">
  <div class="w-full lg:px-5 mx-auto flex flex-row">
    <div
      :class="[
        'form-control w-full flex flex-row gap-7 items-center',
        !isDark
          ? 'bg-base-100 text-base-content'
          : props.darkerCard
          ? 'bg-neutral bg-opacity-95 text-neutral-content'
          : 'text-base-300 bg-base-content',
      ]"
      @mouseover="props.canModify ? (hovered = true) : (hovered = false)"
      @mouseleave="hovered = false"
      v-if="!state.showEditable"
    >
      <h1
        class="cursor-pointer label flex flex-row justify-between gap-5 lg:w-11/12 w-4/5"
      >
        <span
          :class="[
            'label-text -indent-1 text-base',
            !isDark
              ? 'bg-base-100 text-base-content'
              : props.darkerCard
              ? 'bg-neutral bg-opacity-95 text-neutral-content'
              : 'text-base-300 bg-base-content',
          ]"
          >{{ props.issue.sentence }}
        </span>
      </h1>
      <div
        class="flex lg:flex-row flex-col items-center gap-0 my-auto justify-center mx-auto"
        v-show="hovered"
      >
        <font-awesome-icon
          :class="[
            'my-auto size-6 hover:cursor-pointer',
            hovered ? 'animate-pulse duration-150 text-secondary' : '',
          ]"
          :icon="['fas', 'pen-to-square']"
          @click="showEditable"
          ref="editIcon"
        />
        <DeleteIssueBtn
          :id="props.issue.issue_id"
          :type="'issue'"
          @delete="deleteIssue"
          class="-ml-4 lg:ml-0"
        />
      </div>
    </div>
    <div v-else class="flex flex-row gap-10 w-full" ref="issueElement">
      <input
        type="text"
        :placeholder="props.issue.sentence"
        :class="[
          'input input-bordered input-secondary w-4/5',
          isDark
            ? 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content',
        ]"
        v-model="sentence"
        @keydown.enter="updateIssue"
      />
    </div>
    <div class="flex flex-col justify-center items-center">
      <input
        type="checkbox"
        :checked="props.issue.isCompleted"
        :disabled="!canModify"
        class="checkbox checkbox-secondary"
        v-model="checked"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import {
    ref,
    onBeforeMount,
    onUnmounted,
    reactive,
    defineProps,
    watch,
  } from "vue";
  import { ProgressItem, Task } from "../../utils/types";
  // import { useApiCall } from "../../composables/useAPICall";
  // import { EndpointType } from "../../utils/endpoints";
  import DeleteIssueBtn from "../ui/DeleteIssueBtn.vue";
  import { useTaskStore } from "../../store/task.store";
  import { useUIStore } from "../../store/ui.store";

  const props = defineProps<{
    issue: ProgressItem;
    task_id: number;
    canModify: boolean;
    darkerCard: boolean;
  }>();
  const emit = defineEmits(["update"]);

  const taskStore = useTaskStore();
  const UIStore = useUIStore();

  const sentence = ref<string>();
  const checked = ref<boolean>();

  //  const apiCall = useApiCall();

  const hovered = ref<boolean>(false);

  const editIcon = ref<HTMLElement | null>();
  const issueElement = ref<HTMLElement | null>();

  const isDark = ref<boolean>();

  const state = reactive({
    showEditable: false,
    recentChange: false,
  });

  addEventListener("keydown", (event) => {
    if (event.key == "Escape" && state.showEditable) {
      state.showEditable = false;
    }
  });

  const handleOutsideClicks = (event: MouseEvent) => {
    if (
      state.showEditable &&
      !state.recentChange &&
      issueElement.value &&
      !issueElement.value.contains(event.target as Node) &&
      !editIcon.value?.contains(event.target as Node)
    ) {
      state.showEditable = false;
      hovered.value = false;
    }
  };

  function showEditable() {
    if (props.canModify) {
      state.showEditable = true;
      state.recentChange = true;
      setTimeout(() => {
        state.recentChange = false;
      }, 50);
    }
  }

  async function updateIssue() {
    const updateIssue: ProgressItem = {
      issue_id: props.issue.issue_id,
      sentence: sentence.value as string,
      isCompleted: checked.value as boolean,
    };
    const response = (await taskStore.updateIssue(
      updateIssue,
      props.task_id
    )) as Task;
    // (await apiCall.patch(
    //   EndpointType.TASK_UPDATE_ISSUE,
    //   updateIssue,
    //   {
    //     params: {
    //       task_id: props.task_id,
    //     },
    //   }
    // )) as Task;
    if (response.task_id == props.task_id) {
      emit("update", response);
    }
    state.showEditable = false;
  }

  async function deleteIssue(id: number) {
    const response = (await taskStore.deleteIssue(
      props.issue.issue_id as number,
      props.task_id
    )) as Task;
    // (await apiCall.del(EndpointType.TASK_REMOVE_ISSUE, {
    //   params: {
    //     task_id: props.task_id,
    //     issue_id: props.issue.issue_id,
    //   },
    // })) as Task;
    if (response.task_id == props.task_id) {
      emit("update", response);
    }
  }

  watch(
    () => props.issue,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        sentence.value = props.issue.sentence;
        checked.value = props.issue.isCompleted;
      }
    }
  );
  watch(
    () => checked.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        updateIssue();
      }
    }
  );

  onBeforeMount(() => {
    sentence.value = props.issue.sentence;
    checked.value = props.issue.isCompleted;
    document.addEventListener("click", handleOutsideClicks);

    // if (localStorage.getItem("darkTheme")) {
    isDark.value = UIStore.checkIsDarkTheme(); //JSON.parse(localStorage.getItem("darkTheme") as string);
    // }
  });

  onUnmounted(() => {
    document.removeEventListener("click", handleOutsideClicks);
  });
</script>
