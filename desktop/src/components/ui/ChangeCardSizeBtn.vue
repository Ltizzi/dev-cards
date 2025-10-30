<template lang="">
  <button
    class="btn btn-outline btn-secondary xl:text-sm text-xs btn-sm lg:btn-xs xl:btn-sm lg:py-1 lg:pb-5"
    @click="changeIconSize"
  >
    Change card size
  </button>
</template>
<script setup lang="ts">
  const emit = defineEmits(["changeIconSize"]);

  function changeIconSize() {
    emit("changeIconSize");
  }
</script>
