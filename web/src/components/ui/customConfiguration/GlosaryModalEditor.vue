<template>
  <BaseModal
    :is-active="props.showModal"
    v-if="props.showModal"
    @closeModal="closeModal"
  >
    <div class="py-5 px-5" v-if="!state.noAvailable">
      <h1 class="text-center text-2xl pb-4 font-medium">
        {{ props.isEditor ? "Edit Glosary" : "Create new Glosary" }}
      </h1>
      <p class="pb-5 font-light">
        Glosaries are used to customize tasks states's labels.
      </p>
      <ul class="flex flex-row justify-around gap-3">
        <li v-for="(type, index) in types">
          <button
            :class="[
              'btn  btn-md hover:cursor-pointer btn-primary hover:btn-secondary',

              state.selectedState == type
                ? ''
                : props.isEditor
                ? 'btn-disabled'
                : 'btn-outline',

              !props.isEditor && checkIsAvailable(type) //!state.enabledBtns[index]
                ? 'btn-disabled'
                : '',
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
    <div v-else class="w-full p-5">
      <h1 class="text-xl text-center py-5 mx-20 w-96">
        There aren't avaible glosaries slots to add a new one. You can edit an
        old one.
      </h1>
      <div class="flex flex-row gap-5 justify-center w-full py-3">
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
    isEditor: boolean;
    glosaryToEdit?: Glosary;
    glosaries: Glosary[];
  }>();
  const emit = defineEmits(["updateList", "close"]);

  const configStore = useConfigStore();

  const types = ["PRIORITY", "EFFORT", "STATUS", "PROGRESS", "TASK_TYPE"];

  //  const glosary_type = ref<string>();

  const state = reactive({
    selectedState: "PRIORITY",
    labels: [] as string[],
    inputs: [] as GlosaryItem[],
    disabledBtns: [],
    noAvailable: false,
  });

  watch(
    () => state.selectedState,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        //getAvaibleGlosaries();
        calcNumberOfTextInputs(newValue);
      }
    }
  );

  watch(
    () => props.isEditor,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        if (!newValue) {
          state.selectedState = getAvailableGlosaries() as string;
          if (state.selectedState) calcNumberOfTextInputs(state.selectedState);
          else state.noAvailable = true;
        } else {
          state.selectedState = props.glosaryToEdit?.type as string;
          calcNumberOfTextInputs(state.selectedState);
        }
      }
    }
  );

  watch(
    () => props.glosaries,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        if (props.glosaries.length < 5) state.noAvailable = false;
        else state.noAvailable = true;
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
        value: !props.isEditor
          ? labels[i]
          : (props.glosaryToEdit?.items[i].value as string),
      });
    }
  }

  function getLabelsByType(type: string) {
    switch (type) {
      case "PRIORITY":
        return ["Very low", "Low", "Medium", "High", "Very High"];
      case "EFFORT":
        return ["Low", "Medium", "High"];

      case "STATUS":
        return ["Pending", "Progress", "Testing", "Completed", "Blocked"];

      case "PROGRESS":
        return ["Null", "Not functional", "Basic", "Intermediate", "Advance"];

      case "TASK_TYPE":
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
    return state.inputs.map((input: GlosaryItem) => ({
      ...input,
      key: input.key.toLocaleUpperCase(),
    }));
  }

  async function addNewGlosary() {
    const items = labelsToUpper();

    if (!props.isEditor) {
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
        emit("updateList");
        emit("close");
      } else console.error("ERROR SAVING GLOSARY");
    } else {
      //TODO: check logic
      const updatedGlosary = {
        id: props.glosaryToEdit?.id,
        type: state.selectedState,
        items: items as unknown as GlosaryItem[],
      } as Glosary;
      const response = await configStore.updateGlosary(
        props.ws_id,
        props.config_id,
        updatedGlosary,
        updatedGlosary.id as number
      );
      if (response.length > 0) {
        emit("updateList");
        emit("close");
      } else console.error("ERROR UPDATING GLOSARY");
    }
  }

  function getAvailableGlosaries() {
    const arr = props.glosaries.map((g: Glosary) => g.type);
    state.disabledBtns = arr as any;
    for (let i = 0; i < types.length; i++) {
      if (!arr.includes(types[i])) return types[i];
    }
  }

  function checkIsAvailable(type: string) {
    return state.disabledBtns.filter((s: string) => s === type).length > 0;
  }

  onBeforeMount(() => {
    state.selectedState = getAvailableGlosaries() as string;
    if (state.selectedState) calcNumberOfTextInputs(state.selectedState);
    else state.noAvailable = true;
  });
</script>
