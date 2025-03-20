<template>
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div class="py-5 px-5">
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
      <div class="py-5 flex flex-row flex-wrap max-w-96 mx-auto gap-y-3">
        <div v-for="(input, index) in state.labels" class="w-48">
          <label class="form-control">
            <div class="label">
              <span class="label-text text-xs"> {{ state.labels[index] }}</span>
            </div>
            <input
              type="text"
              :placeholder="state.inputs[index].value"
              v-model="state.inputs[index].value"
              class="input input-bordered focus:input-primary input-secondary input-sm w-44"
            />
            <!-- w-full max-w-xs -->
          </label>
        </div>
      </div>
      <div class="flex flex-row gap-5 justify-center w-full py-3">
        <button class="btn btn-outline btn-primary" @click="addNewGlosary">
          Send
        </button>
        <button class="btn btn-outline btn-error" @click="closeModal">
          Cancel
        </button>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, reactive, watch } from "vue";
  import {
    CustomConfiguration,
    Glosary,
    GlosaryItem,
  } from "../../../utils/types";
  import BaseModal from "../../common/BaseModal.vue";
  import { useConfigStore } from "../../../store/config.store";

  const props = defineProps<{
    showModal: boolean;
    config_id: number;
    ws_id: number;
  }>();
  const emit = defineEmits(["updateList", "close"]);

  const configStore = useConfigStore();

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
    state.inputs = [];

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

  function labelsToUpper() {
    console.log("INPUTS:");
    console.log(state.inputs);
    return state.inputs.map((input: GlosaryItem) => ({
      ...input,
      key: input.key.toLocaleUpperCase(),
    }));
  }

  async function addNewGlosary() {
    const items = labelsToUpper();
    const newGlosary = {
      type: state.selectedState,
      items: items as unknown as GlosaryItem[],
    };
    console.log(newGlosary);
    const response = (await configStore.addGlosary(
      props.config_id,
      props.ws_id,
      newGlosary
    )) as Glosary[];
    if (response.length > 0) {
      emit("updateList", response);
      emit("close");
    } else console.error("ERROR SAVING GLOSARY");
  }

  onBeforeMount(() => {
    calcNumberOfTextInputs("PRIORITY");
  });
</script>
