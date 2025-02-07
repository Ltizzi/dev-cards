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
  import { ref, onMounted, watch, onBeforeMount, nextTick } from "vue";
  import {
    changeTheme,
    checkThemeIsDark,
    getActualTheme,
  } from "../utils/client.utils";
  import { useUIStore } from "../store/ui.store";
  import { useUserStore } from "../store/user.store";

  const router = useRouter();

  const firstLoaded = ref(true);

  const isDark = ref<boolean>(false);

  const UIStore = useUIStore();
  const userStore = useUserStore();

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

  onBeforeMount(() => {
    UIStore.checkOfflineMode();
    userStore.checkOfflineMode();
    UIStore.setLoading(true);
  });

  onMounted(() => {
    window.addEventListener("resize", handleResize);
    const theme = UIStore.getTHeme();
    if (theme) {
      isDark.value = UIStore.checkIsDarkTheme();
    }
    setTimeout(() => {
      firstLoaded.value = false;
    }, 1500);
  });
  nextTick(() => {
    const user = userStore.getSelf();
    if (!user.user_id) {
      router.push("/login");
      UIStore.setLoading(false);
    } else {
      UIStore.setLoading(false);
    }
  });
</script>

<style></style>
