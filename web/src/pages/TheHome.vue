<template lang="">
  <div class="flex flex-row justify-between">
    <LateralMenu class="w-1/12" />
    <div class="w-11/12">Logged in!</div>
  </div>
</template>
<script setup lang="ts">
  import LateralMenu from "../layouts/LateralMenu.vue";
  import { useRouter } from "vue-router";
  import { onMounted } from "vue";

  const router = useRouter();

  onMounted(() => {
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    }
  });
</script>
