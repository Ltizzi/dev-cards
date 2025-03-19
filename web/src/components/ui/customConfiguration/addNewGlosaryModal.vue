<template>
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
  </BaseModal>
</template>
<script setup lang="ts">
  import { reactive, ref, watch } from "vue";
  import { CustomConfiguration } from "../../../utils/types";
  import BaseModal from "../../common/BaseModal.vue";

  const props = defineProps<{
    showModal: boolean;
    config_id: number;
    ws_id: number;
  }>();
  const emit = defineEmits(["updateList", "close"]);

  const types = ["PRIORITY", "EFFORT", "STATUS", "PROGRESS", "TASK_TYPE"];

  const glosary_type = ref<string>();

  const state = reactive({
    inputs: 0,
    labels: [] as string[],
  });

  watch(
    () => glosary_type.value,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        calcNumberOfTextInputs(newValue);
      }
    }
  );

  function closeModal() {
    emit("close");
  }

  function addLabels(labels: string[]) {
    state.labels = [];

    for (let i = 0; i < labels.length; i++) {
      state.labels.push(labels[i]);
    }
  }

  function getLabelsByType(type: string) {
    switch (type) {
      case "PRIORTIY":
        state.inputs = 5;
        return ["Very low", "Low", "Medium", "High", "Very High"];
        break;
      case "EFFORT":
        state.inputs = 3;
        return ["Low", "Medium", "High"];
        break;
      case "STATUS":
        state.inputs = 5;
        return ["Pending", "Progress", "Testing", "Completed", "Blocked"];
        break;
      case "PROGRESS":
        state.inputs = 5;
        return ["Null", "Not functional", "Basic", "Intermediate", "Advance"];
        break;
      case "TASK_TYPE":
        state.inputs = 8;
        return [
          "Code",
          "Art",
          "Documentation",
          "Bug",
          "Testing",
          "Marketing",
          "Management",
          "Request",
        ];
        break;
    }
  }

  function calcNumberOfTextInputs(newValue: string) {
    let labels = [] as string[] | undefined;
    labels = getLabelsByType(newValue);
    addLabels(labels as string[]);
  }

  function addNewGlosary() {
    const newGlosary = {
      type: glosary_type.value,
    };
  }
</script>
