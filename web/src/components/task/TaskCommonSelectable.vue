<template lang="">
  <div
    :class="[
      'hover:cursor-pointer ',
      props.type == 'TaskType' ? 'min-w-28' : 'border-r-2  border-secondary',
    ]"
  >
    <h3
      :class="[
        '  px-2 hover:animate-pulse transition-all ease-in-out duration-200',
      ]"
      @click="changeElement"
      v-if="state.default"
    >
      {{ props.selected }}
    </h3>
    <select
      :class="[
        'select select-secondary w-fit select-xs max-w-28 font-semibold',
        props.isDark
          ? 'text-base-300 bg-base-content'
          : 'bg-base-100 text-base-content',
      ]"
      v-model="selectedOption"
      v-if="!state.default"
    >
      <option
        disabled
        selected
        :class="[
          props.isDark
            ? 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content',
        ]"
      >
        Pick task priority
      </option>
      <option
        :class="
          props.isDark
            ? 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content'
        "
        v-for="option in options"
      >
        {{ option }}
      </option>
    </select>
  </div>
</template>
<script setup lang="ts">
  import { Effort, Status, TaskType } from "../../utils/types";
  import { watch, ref, reactive } from "vue";
  import {
    EffortEnumArray,
    StatusEnumArray,
    TaskTypeEnumArray,
  } from "../../utils/types";

  const props = defineProps<{
    type: string;
    selected: Status | Effort | TaskType;
    isDark: boolean;
  }>();

  const emit = defineEmits(["updateStatus", "updateEffort", "updateTaskType"]);

  const state = reactive({
    default: true,
  });

  const options = ref<Array<Effort> | Array<Status> | Array<TaskType>>();
  const selectedOption = ref<Status | Effort | TaskType>();

  function changeElement() {
    if (state.default) {
      state.default = false;
    }
  }

  function prepareTemplate() {
    switch (props.type) {
      case "Effort":
        options.value = EffortEnumArray;
        break;
      case "Status":
        options.value = StatusEnumArray;
        break;
      case "TaskType":
        options.value = TaskTypeEnumArray;
        break;
    }
  }

  watch(
    () => state.default,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        prepareTemplate();
      }
    }
  );

  watch(
    () => selectedOption.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        state.default = true;
        const type: any = "update" + props.type;
        emit(type, props.type, selectedOption.value);
      }
    }
  );
</script>
<style lang=""></style>
