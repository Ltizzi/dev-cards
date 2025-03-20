<template>
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div class="py-5 px-5 w-full">
      <h1 class="text-center text-2xl pb-4 font-medium">Create new Glosary</h1>
      <p class="pb-5 font-light">
        Glosaries are used to customize tasks states's labels.
      </p>
      <ul class="flex flex-row justify-around gap-3">
        <li v-for="type in types">
          <button
            :class="[
              'btn  btn-md hover:cursor-pointer btn-primary hover:btn-secondary',
              state.selectedState == type ? '' : ' btn-outline',
            ]"
            @click="selectType(type)"
          >
            {{ type }}
          </button>
        </li>
      </ul>
      <div class="py-5">
        <div v-for="(input, index) in state.labels" class="pb-2">
          <label class="input">
            <span class="label">{{ state.labels[index] }}</span>
            <input
              type="text"
              :placeholder="state.inputs[index].value"
              v-model="state.inputs[index].value"
            />
          </label>
        </div>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, reactive, watch } from "vue";
  import { CustomConfiguration, GlosaryItem } from "../../../utils/types";
  import BaseModal from "../../common/BaseModal.vue";

  const props = defineProps<{
    showModal: boolean;
    config_id: number;
    ws_id: number;
  }>();
  const emit = defineEmits(["updateList", "close"]);

  const types = ["PRIORITY", "EFFORT", "STATUS", "PROGRESS", "TASK_TYPE"];

  //  const glosary_type = ref<string>();

  const state = reactive({
    totalInputs: 0,
    selectedState: "PRIORITY",
    labels: [] as string[],
    inputs: [] as GlosaryItem[],
  });

  watch(
    () => state.selectedState,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        calcNumberOfTextInputs(newValue);
      }
    }
  );

  function closeModal() {
    emit("close");
  }

  function selectType(type: string) {
    state.selectedState = type;
  }

  function addLabels(labels: string[]) {
    state.labels = [];

    for (let i = 0; i < labels.length; i++) {
      state.labels.push(labels[i]);
      state.inputs.push({
        key: labels[i],
        value: labels[i],
      });
    }
  }

  function getLabelsByType(type: string) {
    switch (type) {
      case "PRIORITY":
        state.totalInputs = 5;
        return ["Very low", "Low", "Medium", "High", "Very High"];
      case "EFFORT":
        state.totalInputs = 3;
        return ["Low", "Medium", "High"];

      case "STATUS":
        state.totalInputs = 5;
        return ["Pending", "Progress", "Testing", "Completed", "Blocked"];

      case "PROGRESS":
        state.totalInputs = 5;
        return ["Null", "Not functional", "Basic", "Intermediate", "Advance"];

      case "TASK_TYPE":
        state.totalInputs = 8;
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
    }
  }

  function calcNumberOfTextInputs(newValue: string) {
    let labels = [] as string[] | undefined;
    labels = getLabelsByType(newValue);
    console.log(labels);
    addLabels(labels as string[]);
  }

  function addNewGlosary() {
    const newGlosary = {
      type: state.selectedState,
    };
  }

  onBeforeMount(() => {
    calcNumberOfTextInputs("PRIORITY");
  });
</script>
