<template>
  <div
    :class="[
      ' border-r-2 min-w-32 border-secondary',
      props.canModify ? 'hover:cursor-pointer' : '',
    ]"
  >
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
        'select select-secondary w-fit select-xs max-w-28   font-bold',
        selectedPriority && selectedPriority?.length > 0
          ? taskUtils.calcPriorityColor(selectedPriority)
          : '',
        props.isDark
          ? 'text-base-300 bg-base-content'
          : 'bg-base-100 text-base-content',
      ]"
      v-model="selectedPriority"
      v-if="!state.default"
    >
      <option
        disabled
        selected
        :class="[
          props.isDark
            ? 'bg-base-content text-base-300'
            : 'bg-base-100 text-base-content',
        ]"
      >
        Pick task priority
      </option>
      <option
        :class="[
          'text-info font-bold',
          props.isDark ? 'bg-base-content' : 'bg-base-100 ',
        ]"
      >
        VERY_LOW
      </option>
      <option
        :class="[
          'text-success font-bold',
          props.isDark ? 'bg-base-content' : 'bg-base-100 ',
        ]"
      >
        LOW
      </option>
      <option
        :class="[
          'text-accent font-bold',
          props.isDark ? 'bg-base-content' : 'bg-base-100 ',
        ]"
      >
        MEDIUM
      </option>
      <option
        :class="[
          'text-warning font-bold',
          props.isDark ? 'bg-base-content' : 'bg-base-100 ',
        ]"
      >
        HIGH
      </option>
      <option
        :class="[
          'text-error font-bold',
          props.isDark ? 'bg-base-content' : 'bg-base-100 ',
        ]"
      >
        VERY_HIGH
      </option>
    </select>
  </div>
</template>
<script setup lang="ts">
  import { taskUtils } from "../../utils/task.utils";
  import { Priority } from "../../utils/types";
  import { reactive, ref, watch } from "vue";

  const props = defineProps<{
    priority: Priority;
    isDark: boolean;
    canModify: boolean;
  }>();
  const emit = defineEmits(["updatePriority"]);

  const state = reactive({
    default: true,
  });

  const selectedPriority = ref<Priority>();

  function changeElement() {
    if (props.canModify) {
      if (state.default) {
        state.default = false;
      }
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
