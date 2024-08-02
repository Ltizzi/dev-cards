<template lang="">
  <div class="w-full flex flex-row">
    <div
      class="form-control w-4/5 flex flex-row gap-5"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
      v-if="!state.showEditable"
    >
      <h1 class="cursor-pointer label flex flex-row justify-between gap-5">
        <span class="label-text text-black text-lg"
          >{{ props.issue.sentence }}
        </span>
      </h1>
      <div class="flex flex-row gap-1 my-auto" v-show="hovered">
        <font-awesome-icon
          :class="[
            'my-auto size-7 hover:cursor-pointer',
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
        />
      </div>
    </div>
    <div v-else class="flex flex-row gap-3 w-4/5" ref="issueElement">
      <input
        type="text"
        :placeholder="props.issue.sentence"
        class="input input-bordered input-secondary w-full bg-gray-100 text-black"
        v-model="sentence"
        @keydown.enter="updateIssue"
      />
    </div>
    <div>
      <input
        type="checkbox"
        :checked="props.issue.isCompleted"
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
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import DeleteIssueBtn from "../ui/DeleteIssueBtn.vue";

  const props = defineProps<{ issue: ProgressItem; task_id: number }>();
  const emit = defineEmits(["update"]);

  const sentence = ref<string>();
  const checked = ref<boolean>();

  const apiCall = useApiCall();

  const hovered = ref<boolean>(false);

  const editIcon = ref<HTMLElement | null>();
  const issueElement = ref<HTMLElement | null>();

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
    state.showEditable = true;
    state.recentChange = true;
    setTimeout(() => {
      state.recentChange = false;
    }, 50);
  }

  async function updateIssue() {
    const updateIssue: ProgressItem = {
      issue_id: props.issue.issue_id,
      sentence: sentence.value as string,
      isCompleted: checked.value as boolean,
    };
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_ISSUE,
      updateIssue,
      {
        params: {
          task_id: props.task_id,
        },
      }
    )) as Task;
    if (response.task_id == props.task_id) {
      emit("update", response);
    }
    state.showEditable = false;
  }

  async function deleteIssue(id: number) {
    const response = (await apiCall.del(EndpointType.TASK_REMOVE_ISSUE, {
      params: {
        task_id: props.task_id,
        issue_id: props.issue.issue_id,
      },
    })) as Task;
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
  });

  onUnmounted(() => {
    document.removeEventListener("click", handleOutsideClicks);
  });
</script>
