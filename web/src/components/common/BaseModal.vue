<template lang="">
  <teleport to="body">
    <!-- motion-duration-500 motion-ease-spring-snappy motion-preset-slide-up-right-md motion-opacity-in-0 motion-scale-in-0 -->
    <div
      class="lg:items-center 2xl:items-center fixed left-0 top-0 z-50 flex h-screen w-screen items-start justify-center"
      :class="{ 'is-active': isActive }"
    >
      <div
        class="fixed left-0 top-0 z-20 h-full w-full backdrop-blur-lg"
        @click="close()"
      ></div>
      <div
        :class="[
          'container fixed z-50 w-auto  text-base-content bg-opacity-200 shadow-xl ',
          props.isLoading
            ? 'bg-opacity-0 border-0 motion-duration-100 motion-ease-in-out motion-preset-pop'
            : 'rounded-md border-2 border-secondary bg-base-100 motion-scale-in-[0] motion-translate-x-in-[-150%] motion-translate-y-in-[300%] motion-opacity-in-[0%] motion-rotate-in-[-500deg] motion-blur-in-[100px] motion-duration-[0.36s]/scale motion-duration-[0.41s]/translate motion-duration-[0.16s]/opacity motion-duration-[0.48s]/rotate motion-duration-[0.26s]/blur motion-ease-spring-snappy',
        ]"
      >
        <slot></slot>
      </div>
    </div>
  </teleport>
</template>
<script setup lang="ts">
  import { ref, defineProps } from "vue";

  const props = defineProps<{ isActive: boolean; isLoading: boolean }>();

  const isActive = ref(props.isActive);

  const emit = defineEmits(["closeModal"]);

  function close() {
    isActive.value = false;
    emit("closeModal");
  }
</script>
<style lang=""></style>
