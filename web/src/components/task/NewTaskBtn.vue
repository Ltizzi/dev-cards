<template lang="">
  <div>
    <button class="btn btn-outline btn-secondary" @click="modalSwitch">
      Create Task
    </button>
    <NewTaskModal
      :showModal="showModal"
      @close="modalSwitch"
      @update="update"
    />
  </div>
</template>
<script setup lang="ts">
  import NewTaskModal from "./NewTaskModal.vue";
  import { ref } from "vue";

  const showModal = ref(false);
  const emit = defineEmits(["update"]);

  function modalSwitch() {
    showModal.value = !showModal.value;
  }

  function update() {
    emit("update");
  }
</script>
