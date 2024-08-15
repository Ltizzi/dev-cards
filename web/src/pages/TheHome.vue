<template lang="">
  <div class="w-screen relative bg-base-100">
    <LateralMenu
      :class="[
        `transition-all bg-gradient-to-br from-${
          randomNum < 50 ? 'primary' : 'secondary'
        } via-${randomNum < 50 ? 'primary' : 'secondary'} to-${
          randomNum < 50 ? 'secondary' : 'primary'
        }  opacity-${float}`,
        firstLoaded
          ? `translate-x-0 h-screen fixed  
              }`
          : '-translate-x-14  h-screen fixed ',
      ]"
    />

    <!-- </div> -->

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

  const float = ref(0);

  const firstLoaded = ref(true);

  const randomNum = ref(0);

  function chooseColor() {
    randomNum.value = Math.floor(Math.random() * 100);
  }

  onMounted(() => {
    chooseColor();
    setTimeout(() => {
      firstLoaded.value = false;
    }, 2500);
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    }
  });
</script>

<style></style>
