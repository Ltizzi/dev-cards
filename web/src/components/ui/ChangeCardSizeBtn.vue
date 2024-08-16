<template lang="">
  <button class="btn btn-outline btn-secondary" @click="changeIconSize">
    Change card size
  </button>
</template>
<script setup lang="ts">
  const emit = defineEmits(["changeIconSize"]);

  function changeIconSize() {
    emit("changeIconSize");
  }
</script>
