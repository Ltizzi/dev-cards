<template lang="">
  <teleport to="body">
    <div
      class="lg:items-center 2xl:items-center fixed left-0 top-0 z-50 flex h-screen w-screen items-start justify-center"
      :class="{ 'is-active': isActive }"
    >
      <div
        class="fixed left-0 top-0 z-20 h-full w-full backdrop-blur-lg"
        @click="$emit('closeModal')"
      ></div>
      <div
        class="container fixed z-50 w-auto rounded-md border-2 border-secondary px-5 shadow-xl"
      >
        <slot></slot>
      </div>
    </div>
  </teleport>
</template>
<script setup lang="ts">
  import { ref, defineProps } from "vue";

  const props = defineProps<{ isActive: boolean }>();

  const isActive = ref(props.isActive);

  function close() {
    isActive.value = false;
  }
</script>
<style lang=""></style>
