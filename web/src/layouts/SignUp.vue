<template lang="">
  <div class="flex flex-row justify-center mx-auto w-3/5">
    <SignUpSteps class="w-1/4" />
    <router-view></router-view>
  </div>
</template>
<script setup lang="ts">
  import SignUpSteps from "../components/auth/SignUpSteps.vue";
</script>
