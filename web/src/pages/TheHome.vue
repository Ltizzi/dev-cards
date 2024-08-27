<template lang="">
  <div
    :class="[
      'w-full relative  bg-gradient-to-br ',
      isDark ? 'from-base-100 to-neutral' : 'from-base-100 to-base-300',
    ]"
  >
    <LateralMenu
      :class="[
        'duration-150 transition-all  ease-in-out opacity-90 z-0 h-screen fixed  bg-gradient-to-br from-0%  from-secondary  to-100% to-transparent -mx-16   hover:translate-x-16 hover:z-20',
        firstLoaded
          ? `translate-x-16 z-50  
              `
          : ` `,
      ]"
    />
    <!--      `blur-${blur_class}`bg-opacity-${float} -->

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
  import {
    changeTheme,
    checkThemeIsDark,
    getActualTheme,
  } from "../utils/client.utils";

  const router = useRouter();

  const firstLoaded = ref(true);

  const isDark = ref<boolean>(false);

  onMounted(() => {
    const theme = localStorage.getItem("theme") as string;
    if (theme) {
      changeTheme(theme);
      isDark.value = checkThemeIsDark();
    }
    setTimeout(() => {
      firstLoaded.value = false;
    }, 1500);
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    }
  });
</script>

<style></style>
