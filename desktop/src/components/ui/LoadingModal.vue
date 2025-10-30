<template lang="">
  <BaseModal v-if="show" class="" :isLoading="true">
    <div class="relative w-44 h-36 bg-opacity-0">
      <img
        :src="cardIcon"
        class="object-fill w-full h-full z-40 absolute top-0"
      />
      <span
        class="loading loading-spinner loading-lg text-secondary z-50 relative top-14 left-16"
      ></span></div
  ></BaseModal>
</template>
<script setup lang="ts">
  import BaseModal from "../common/BaseModal.vue";
  import cardIcon from "../../../public/card.png";
  import { watch, ref } from "vue";
  import { useUIStore } from "../../store/ui.store";

  const cardImg = ref(cardIcon);

  const show = ref<boolean>(false);

  const UIstore = useUIStore();

  watch(
    () => UIstore.loading,
    (newValue, oldValue) => {
      if (newValue != oldValue) show.value = newValue;
    }
  );
</script>
