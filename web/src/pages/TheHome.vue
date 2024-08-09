<template lang="">
  <div class="w-screen relative bg-base-100">
    <LateralMenu
      :class="
        firstLoaded
          ? 'translate-x-0'
          : '-translate-x-14 hover:translate-x-0 duration-150 transition-all ease-in-out h-screen fixed '
      "
    />
    <div class="w-full">
      <router-view></router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
  import LateralMenu from "../layouts/LateralMenu.vue";
  import { useRouter } from "vue-router";
  import { ref, onMounted } from "vue";

  const router = useRouter();

  const firstLoaded = ref(true);

  onMounted(() => {
    setTimeout(() => {
      firstLoaded.value = false;
    }, 1500);
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    }
  });
</script>
