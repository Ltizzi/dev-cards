<template lang="">
  <div class="">
    <font-awesome-icon
      :icon="['fas', 'trash']"
      class="size-6 text-error mt-1 hover:cursor-pointer hover:scale-110 transition-all ease-in-out duration-100"
      @click="openModal"
    />
    <BaseDeleteModal
      @cancel="closeModal"
      @delete="delElement"
      :id="props.id"
      :type="props.type"
      :showModal="show"
      v-if="show"
    />
  </div>
</template>
<script setup lang="ts">
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import { ref } from "vue";

  const props = defineProps<{ id: number; type: string }>();
  const emit = defineEmits(["delete", "close"]);

  const show = ref<boolean>(false);

  function openModal() {
    show.value = true;
  }

  function closeModal() {
    show.value = false;
  }

  function switchModal() {
    show.value = !show.value;
  }

  function delElement() {
    emit("delete", props.id);
    switchModal();
  }
</script>
<style lang=""></style>
