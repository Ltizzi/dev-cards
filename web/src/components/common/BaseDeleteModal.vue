<template lang="">
  <BaseModal
    @closeModal="cancelOperation"
    :is-active="props.showModal"
    v-if="props.showModal"
  >
    <div class="flex flex-col gap-3 justify-center bg-neutral">
      <h1 class="bg-error py-2 text-center text-white font-semibold">
        DELETE {{ toUpper(props.type) }}
      </h1>
      <h2 class="text-center py-5 px-5 text-lg">Are you sure?</h2>
      <div class="flex flex-row gap-5 pb-5 px-10">
        <button class="btn btn-error text-white" @click="delElement">
          DELETE
        </button>
        <button
          class="btn btn-active btn-accent text-white"
          @click="cancelOperation"
        >
          Cancel
        </button>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import BaseModal from "./BaseModal.vue";

  const props = defineProps<{ id: number; type: string; showModal: boolean }>();
  const emit = defineEmits(["delete", "cancel"]);

  function delElement() {
    emit("delete", props.id);
  }

  function toUpper(value: string) {
    return value.toUpperCase();
  }

  function cancelOperation() {
    emit("cancel");
  }
</script>
<style lang=""></style>
