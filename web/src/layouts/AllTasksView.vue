<template lang="">
  <div class="text-center">
    <h1 class="text-4xl mt-5 text-base-content">All Tasks View</h1>
    <div class="flex flex-row justify-between py-5">
      <TaskFilterInput @search="defineSearch" class="ml-0" />
      <ChangeCardSizeBtn @changeIconSize="changeIconSize" class="mr-16" />
    </div>
    <TaskList :isMicro="isMicro" :tasks="getTasks()" class="-ml-1" />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { TaskLite } from "../utils/types";
  import TaskList from "../components/task/TaskList.vue";
  import TaskFilterInput from "../components/ui/TaskFilterInput.vue";
  import ChangeCardSizeBtn from "../components/ui/ChangeCardSizeBtn.vue";
  import { useProjectStore } from "../store/project.store";
  import { taskUtils } from "../utils/task.utils";

  const projectStore = useProjectStore();

  const tasks = ref<TaskLite[]>();

  const filteredTasks = ref<TaskLite[]>();

  const isMicro = ref<boolean>(false);

  const search = ref<string>();

  watch(
    () => search.value,
    (newValue, oldValue) => {
      filteredTasks.value = taskUtils.searchTasks(
        newValue as string,
        tasks.value as TaskLite[]
      );
    }
  );

  function defineSearch(value: string) {
    search.value = value;
  }

  function changeIconSize() {
    isMicro.value = !isMicro.value;
  }

  function getTasks() {
    return filteredTasks.value && filteredTasks.value?.length > 0
      ? filteredTasks.value
      : tasks.value;
  }

  onBeforeMount(() => {
    tasks.value = projectStore.current.tasks;
  });
</script>
<style lang=""></style>
