<template lang="">
  <div>Logged in!</div>
</template>
<script setup lang="ts">
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
