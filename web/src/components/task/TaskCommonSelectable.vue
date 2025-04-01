<template lang="html">
  <div
    :class="[
      'py-3 2xl:border-t-0 border-t-2 2xl:border-b-0 border-b-2 xl:min-w-52 min-w-20 lg:min-w-28 sm:max-w-72 w-auto  lg:w-full 2xl:min-w-32 2xl:max-w-32',
      canModify ? 'hover:cursor-pointer ' : '',
      type == 'TaskType'
        ? 'min-w-28 border-secondary'
        : 'border-r-2  border-secondary',
      selected == TaskType.DOCUMENTATION ? 'text-xs' : '',
    ]"
  >
    <h3
      :class="[
        'px-2 hover:animate-pulse transition-all ease-in-out duration-200',
      ]"
      @click="changeElement"
      v-if="state.default"
    >
      {{ state.hasGlosary ? state.optionToShow : selected }}
    </h3>
    <select
      :class="[
        'select select-secondary w-fit select-xs max-w-28 font-semibold',
        props.isDark
          ? props.darkerCard
            ? 'bg-neutral text-neutral-content'
            : 'text-base-300 bg-base-content'
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
            ? props.darkerCard
              ? 'bg-neutral text-neutral-content'
              : 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content',
        ]"
      >
        Pick task priority
      </option>
      <option
        :class="
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral text-neutral-content'
              : 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content'
        "
        v-for="(option, index) in options"
        :key="index"
      >
        {{ state.hasGlosary ? glosary.items[index].value : option }}
      </option>
    </select>
  </div>
</template>
<script setup lang="ts">
  import { Effort, Status, TaskType } from "../../utils/types";
  import type { Glosary, GlosaryItem } from "../../utils/types";
  import { watch, ref, reactive, onBeforeMount } from "vue";
  import {
    EffortEnumArray,
    StatusEnumArray,
    TaskTypeEnumArray,
  } from "../../utils/types";

  const props = defineProps<{
    type: string;
    selected: Status | Effort | TaskType;
    isDark: boolean;
    darkerCard: boolean;
    canModify: boolean;
    glosary: Glosary;
  }>();

  defineExpose({ TaskType });

  const emit = defineEmits(["updateStatus", "updateEffort", "updateTaskType"]);

  const state = reactive({
    default: true,
    hasGlosary: false,
    optionToShow: "",
  });

  const options = ref<Array<Effort> | Array<Status> | Array<TaskType>>([]);
  const selectedOption = ref<Status | Effort | TaskType>();

  function changeElement() {
    if (props.canModify) {
      if (state.default) {
        state.default = false;
      }
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

  function getGlosaryItem() {
    let label = "";
    props.glosary.items.forEach((i: GlosaryItem) => {
      if (props.selected.toLowerCase() == i.key.toLowerCase()) {
        label = i.value;
      }
    });
    return label;
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
        if (!state.hasGlosary) {
          emit(type, props.type, selectedOption.value);
        } else {
          let selected: any;
          props.glosary.items.forEach((item: GlosaryItem) => {
            if (selectedOption.value == item.value) {
              selected = item.key;
            }
          });
          emit(type, props.type, selected);
        }
      }
    }
  );

  watch(
    () => props.glosary,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        if (props.glosary.type) {
          state.hasGlosary = true;
          state.optionToShow = getGlosaryItem() as unknown as string;
        }
      }
    }
  );

  onBeforeMount(() => {
    if (props.glosary && props.glosary.type) {
      state.hasGlosary = true;
      state.optionToShow = getGlosaryItem() as unknown as string;
    }
  });
</script>
<style lang=""></style>
