<template lang="">
  <div>
    <font-awesome-icon
      :icon="['fas', 'square-plus']"
      class="size-7 text-emerald-600 hover:cursor-pointer hover:scale-110 transition-all ease-in-out duration-150"
      v-if="props.showBtn"
      @click="openModal"
    />
    <AddNewTaskIssueModal
      @cancel="closeModal"
      @update="update"
      :showModal="show"
      :task_id="props.task_id"
    />
  </div>
</template>
<script setup lang="ts">
  // import { useApiCall } from "../../composables/useAPICall";
  // import { EndpointType } from "../../utils/endpoints";
  import { ProgressItem, Task } from "../../utils/types";
  import AddNewTaskIssueModal from "./AddNewTaskIssueModal.vue";
  import { defineProps, ref } from "vue";

  const props = defineProps<{ showBtn: boolean; task_id: number }>();
  const emit = defineEmits(["update"]);

  const show = ref<boolean>(false);

  //const apiCall = useApiCall();

  function openModal() {
    show.value = true;
  }

  function closeModal() {
    show.value = false;
  }

  function switchModal() {
    show.value = !show.value;
  }

  function update(task: Task) {
    emit("update", task);
  }
</script>
