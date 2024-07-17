<template lang="">
  <div>
    <button class="btn btn-outline btn-secondary" @click="modalSwitch">
      Create Task
    </button>
    <NewTaskModal :showModal="showModal" @close="modalSwitch" />
  </div>
</template>
<script setup lang="ts">
  import NewTaskModal from "./NewTaskModal.vue";
  import { ref } from "vue";

  const showModal = ref(false);

  function modalSwitch() {
    showModal.value = !showModal.value;
  }
</script>
