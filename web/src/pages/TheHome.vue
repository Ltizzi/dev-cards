<template lang="">
  <div class="flex flex-row justify-between w-full">
    asdasd
    <!-- <LateralMenu class="w-24" />
    <div class="w-full">
      <router-view></router-view>
    </div> -->
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
