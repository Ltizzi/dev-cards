<template lang="">
  <div class="flex flex-row xl:flex-nowrap flex-wrap gap-1 my-auto">
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="colorOptions"
      :name="'Color'"
      @selected="filterByOptions"
    />
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="statusOptions"
      :name="'Status'"
      @selected="filterByOptions"
    />
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="effortOptions"
      :name="'Effort'"
      @selected="filterByOptions"
    />
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="progressOptions"
      :name="'Progress'"
      @selected="filterByOptions"
    />
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="priorityOptions"
      :name="'Priority'"
      @selected="filterByOptions"
    />
    <BaseDropdownSelectable
      :isDark="isDark"
      :options="taskTypeOptions"
      :name="'Type'"
      @selected="filterByOptions"
    />
  </div>
</template>
<script setup lang="ts">
  import BaseDropdownSelectable from "../common/BaseDropdownSelectable.vue";
  import { onBeforeMount, ref } from "vue";
  import {
    ColorEnumArray,
    EffortEnumArray,
    StatusEnumArray,
    PriorityEnumArray,
    ProgressEnumArray,
    TaskTypeEnumArray,
    DropdownCheckboxOption,
  } from "../../utils/types";

  const isDark = ref<boolean>();
  const emit = defineEmits(["selected"]);

  const colorOptions = ref<DropdownCheckboxOption[]>();
  const statusOptions = ref<DropdownCheckboxOption[]>();
  const progressOptions = ref<DropdownCheckboxOption[]>();
  const priorityOptions = ref<DropdownCheckboxOption[]>();
  const taskTypeOptions = ref<DropdownCheckboxOption[]>();
  const effortOptions = ref<DropdownCheckboxOption[]>();

  function filterByOptions(opts: any[], type: string) {
    emit("selected", opts, type);
  }

  function prepareOptionArr(arr: Array<any>) {
    return arr.map((opt: any) => {
      const newOpt = {} as DropdownCheckboxOption;
      newOpt.text = opt;
      newOpt.check = false;
      return newOpt;
    });
  }

  function prepareOptions() {
    colorOptions.value = prepareOptionArr(ColorEnumArray);
    statusOptions.value = prepareOptionArr(StatusEnumArray);
    progressOptions.value = prepareOptionArr(ProgressEnumArray);
    priorityOptions.value = prepareOptionArr(PriorityEnumArray);
    taskTypeOptions.value = prepareOptionArr(TaskTypeEnumArray);
    effortOptions.value = prepareOptionArr(EffortEnumArray);
  }

  onBeforeMount(() => {
    const darkTheme = JSON.parse(localStorage.getItem("darkTheme") as string);
    isDark.value = darkTheme;
    prepareOptions();
  });
</script>
