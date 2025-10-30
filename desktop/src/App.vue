<template>
  <router-view></router-view>
  <LoadingModal />
</template>
<script setup lang="ts">
  import TheHome from "./pages/TheHome.vue";
  import LoadingModal from "./components/ui/LoadingModal.vue";
</script>
