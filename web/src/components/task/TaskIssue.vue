<template lang="">
  <div class="w-full">
    <div
      class="form-control w-3/5 flex flex-row gap-5"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
      v-if="!state.showEditable"
    >
      <label class="cursor-pointer label flex flex-row justify-between gap-5">
        <span class="label-text text-black text-lg"
          >{{ props.issue.sentence }}
        </span>
        <input
          type="checkbox"
          :checked="props.issue.isCompleted"
          class="checkbox checkbox-secondary"
        />
      </label>
      <font-awesome-icon
        :class="[
          'my-auto size-7 hover:cursor-pointer',
          hovered ? 'animate-pulse duration-150 text-secondary' : '',
        ]"
        :icon="['fas', 'pen-to-square']"
        v-show="hovered"
        @click="showEditable"
        ref="editIcon"
      />
    </div>
    <div v-else class="flex flex-row gap-3 w-full" ref="issueElement">
      <input
        type="text"
        :placeholder="props.issue.sentence"
        class="input input-bordered input-secondary w-full max-w-xs bg-gray-100 text-black"
        v-model="sentence"
      />
      <label class="cursor-pointer label flex flex-row justify-between gap-5">
        <input
          type="checkbox"
          :checked="checked"
          class="checkbox checkbox-secondary"
          v-model="checked"
        />
      </label>
      <button class="btn btn-success" @click="updateIssue">
        <font-awesome-icon :icon="['fas', 'check']" />
      </button>
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

  watch(
    () => props.issue,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        sentence.value = props.issue.sentence;
        checked.value = props.issue.isCompleted;
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
