<template lang="">
  <div
    :class="[
      'lg:sw-full   items-center ml-2  lg:ml-0 px-2 flex flex-col justify-center my-auto border-secondary  xl:max-h-5 py-1.5 xl:py-0',
      props.canModify ? 'hover:cursor-pointer' : '',
    ]"
    @click="changeElement()"
    @mouseup="updateProgress()"
  >
    <div class="w-full mx-auto">
      <div
        class="tooltip tooltip-bottom text-white tooltip-accent w-full"
        :data-tip="`${getProgressEnumValue(
          props.progress_value
        )}, Click to change!`"
        v-if="state.default"
      >
        <!-- :value="props.progress_value" -->
        <progress
          class="progress progress-success mx-auto h-5 -mt-1"
          :value="props.progress_value"
          max="4"
        ></progress>
      </div>
    </div>

    <div v-if="!state.default" class="-mt-1.5 mx-auto w-full">
      <div
        class="tooltip tooltip-bottom text-white tooltip-accent w-full"
        :data-tip="`${getProgressEnumValue(progress_selected)}`"
        v-if="!state.default"
      >
        <input
          type="range"
          min="0"
          max="4"
          v-model="progress_selected"
          class="range range-secondary"
          step="1"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { reactive, ref } from "vue";
  import { Progress, ProgressEnumArray } from "../../utils/types";

  const props = defineProps<{ progress_value: number; canModify: boolean }>();
  const emit = defineEmits(["update"]);

  const progress_selected = ref();

  const progress_array = ref(ProgressEnumArray);

  const state = reactive({
    default: true,
  });

  function getProgressEnumValue(num: number) {
    return progress_array.value[num];
  }

  function changeElement() {
    if (props.canModify) {
      if (state.default) state.default = false;
    }
  }

  function updateProgress() {
    if (!state.default) {
      console.log(getProgressEnumValue(progress_selected.value as number));
      const progress_value = progress_selected.value as number;
      state.default = true;
      emit("update", getProgressEnumValue(progress_value));
    }
  }

  //   watch(
  //     () => progress_selected.value,
  //     (newValue, oldValue) => {
  //       if (newValue != oldValue) {
  //         emit("update", getProgressEnumValue(newValue as number));
  //       }
  //     }
  //   );
</script>
