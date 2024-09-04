<template lang="">
  <button
    class="btn btn-outline btn-secondary xl:text-sm text-xs"
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
