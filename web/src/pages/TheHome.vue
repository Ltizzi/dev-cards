<template lang="">
  <div
    :class="[
      'w-full relative  bg-gradient-to-br font-raleway ',
      isDark ? 'from-base-100 to-neutral' : 'from-base-100 to-base-300',
    ]"
  >
    <LateralMenu
      :class="[
        'duration-150 transition-all  ease-in-out opacity-90 z-0 h-screen fixed  bg-gradient-to-br from-0%  from-secondary  to-100% to-transparent -mx-16   hover:translate-x-16 hover:z-20',
        firstLoaded ? `translate-x-16 z-50` : ` `,
      ]"
    />
    <!--      `blur-${blur_class}`bg-opacity-${float} -->

    <!-- </div> -->

    <div class="w-auto">
      <router-view></router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
  import LateralMenu from "../layouts/LateralMenu.vue";
  import { useRouter } from "vue-router";
  import { ref, onMounted, watch } from "vue";
  import {
    changeTheme,
    checkThemeIsDark,
    getActualTheme,
  } from "../utils/client.utils";
  import { useUIStore } from "../store/ui.store";

  const router = useRouter();

  const firstLoaded = ref(true);

  const isDark = ref<boolean>(false);

  const UIStore = useUIStore();

  watch(
    () => UIStore.justUpdated,
    (newValue, oldValue) => {
      if (newValue) {
        isDark.value = UIStore.darkTheme;
      }
    }
  );

  function handleResize() {
    const isMobile = window.innerWidth < 1024;
    console.log("RESIZEEE");
    UIStore.setIsMobile(isMobile);
  }

  onMounted(() => {
    window.addEventListener("resize", handleResize);
    const theme = localStorage.getItem("theme") as string;
    if (theme) {
      //changeTheme(theme);
      UIStore.setTheme(theme);
      isDark.value = UIStore.darkTheme;
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
