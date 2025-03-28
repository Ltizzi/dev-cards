<template>
  <BaseModal
    @closeModal="cancelOperation"
    :is-active="props.showModal"
    v-if="props.showModal"
  >
    <h1 class="bg-emerald-500 py-2 w-full text-center text-white font-semibold">
      Add new Issue
    </h1>
    <dir
      class="flex flex-col gap-5 justify-center px-5 my-0 bg-base w-full h-full py-5"
    >
      <input
        type="text"
        placeholder="Type here"
        class="input input-bordered input-secondary w-96 max-w-lg"
        v-model="sentence"
      />
      <div class="flex flex-row justify-center gap-3">
        <button class="btn btn-success font-bold" @click="submit">
          Submit
        </button>
        <button
          class="btn btn-active btn-accent txt-white font-bold"
          @click="cancelOperation"
        >
          Cancel
        </button>
      </div>
    </dir>
  </BaseModal>
</template>
<script setup lang="ts">
  import { useTaskStore } from "../../store/task.store";

  import { ProgressItem, Task } from "../../utils/types";
  import BaseModal from "../common/BaseModal.vue";
  import { defineProps, ref, watch } from "vue";
  import { utils } from "../../utils/utils";

  const props = defineProps<{ showModal: boolean; task_id: number }>();

  const sentence = ref<string>();

  const taskStore = useTaskStore();

  const emit = defineEmits(["update", "cancel"]);

  function cancelOperation() {
    emit("cancel");
  }

  const success = ref<boolean>(false);
  const failed = ref<boolean>(false);
  const isWaiting = ref<boolean>(false);

  watch(
    () => props.showModal,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        sentence.value = "";
      }
    }
  );

  async function submit() {
    isWaiting.value = true;
    const newIssue: ProgressItem = {
      sentence: sentence.value as string,
      isCompleted: false,
    };
    const localMode = taskStore.checkOfflineMode();
    if (localMode) {
      newIssue.issue_id = utils.generateRandomId();
    }

    const response = (await taskStore.addIssue(
      props.task_id,
      newIssue
    )) as unknown as Task;

    if (response.task_id == props.task_id) {
      success.value = true;
      isWaiting.value = false;
      if (!localMode) emit("update", response);
      setTimeout(() => {
        success.value = false;
        cancelOperation();
      }, 500);
    } else {
      isWaiting.value = false;
      failed.value = true;
      setTimeout(() => {
        failed.value = false;
      }, 3000);
    }
  }
</script>
