<template>
  <div class="hover:cursor-pointer border-r-2 min-w-32 border-secondary">
    <h3
      :class="[
        'font-bold  px-2 hover:animate-pulse transition-all ease-in-out duration-200',
        taskUtils.calcPriorityColor(props.priority),
      ]"
      @click="changeElement"
      v-if="state.default"
    >
      {{ props.priority }}
    </h3>
    <select
      :class="[
        'select select-secondary w-fit select-xs max-w-28  bg-white font-bold',
        selectedPriority && selectedPriority?.length > 0
          ? taskUtils.calcPriorityColor(selectedPriority)
          : 'text-slate-800',
      ]"
      v-model="selectedPriority"
      v-if="!state.default"
    >
      <option disabled selected class="text-black">Pick task priority</option>
      <option class="font_emerald font-bold">VERY_LOW</option>
      <option class="font_green font-bold">LOW</option>
      <option class="font_lime font-bold">MEDIUM</option>
      <option class="font_amber font-bold">HIGH</option>
      <option class="font_red font-bold">VERY_HIGH</option>
    </select>
  </div>
</template>
<script setup lang="ts">
  import { taskUtils } from "../../utils/task.utils";
  import { Priority } from "../../utils/types";
  import { reactive, ref, watch } from "vue";

  const props = defineProps<{ priority: Priority }>();
  const emit = defineEmits(["updatePriority"]);

  const state = reactive({
    default: true,
  });

  const selectedPriority = ref<Priority>();

  function changeElement() {
    if (state.default) {
      state.default = false;
    }
  }

  watch(
    () => selectedPriority.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        state.default = true;
        emit("updatePriority", selectedPriority.value);
      }
    }
  );
</script>
<style lang=""></style>
