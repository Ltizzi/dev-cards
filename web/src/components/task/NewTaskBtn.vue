<template lang="">
  <div>
    <div class="tooltip tooltip-primary" data-tip="Create new task">
      <button
        :class="[
          'btn  btn-secondary',
          props.icon
            ? 'text-neutral-content  hover:scale-105 transition-all duration-150 ease-in-out w-48'
            : 'btn-outline',
        ]"
        @click="openModal"
      >
        <font-awesome-icon
          :icon="['fas', 'square-plus']"
          class="size-7"
          v-if="props.icon"
        />
        <div v-else>Create Task</div>
      </button>
    </div>

    <NewTaskModal :showModal="showModal" @close="closeModal" @update="update" />
  </div>
</template>
<script setup lang="ts">
  import NewTaskModal from "./NewTaskModal.vue";
  import { ref, defineProps } from "vue";

  const props = defineProps<{ icon: boolean }>();

  const showModal = ref(false);
  const emit = defineEmits(["update"]);

  function openModal() {
    showModal.value = true;
  }

  function closeModal() {
    showModal.value = false;
  }

  function modalSwitch() {
    showModal.value = !showModal.value;
  }

  function update() {
    emit("update");
  }
</script>
