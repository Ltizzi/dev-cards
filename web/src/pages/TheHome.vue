<template lang="">
  <div
    :class="[
      'w-full relative  bg-gradient-to-br font-raleway h-auto ',
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
  import { User, UserLocal } from "../utils/types";

  const router = useRouter();

  const firstLoaded = ref(true);
  //const hasUser = ref(false);

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

  // watch(
  //   () => userStore.getSelf(),
  //   (newValue, oldValue) => {
  //     if (newValue != oldValue && !newValue) {
  //       router.push("/login");
  //     }
  //   }
  // );

  function handleResize() {
    const isMobile = window.innerWidth < 1024;
    //console.log("RESIZEEE");
    UIStore.setIsMobile(isMobile);
  }

  onBeforeMount(() => {
    UIStore.getOfflineMode();
    userStore.checkOfflineMode();
    UIStore.setLoading(true);
    // hasUser.value = userStore.getSelf() ? true : false;
    // console.log("HAS USER: " + hasUser.value);
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
    // if (!userStore.getCurrent()) {
    //   router.push("/login");
    // }

    // const user = userStore.getCurrent();
    // console.log("USER FROM HOME: " + user.username);
    // if (!user.user_id && router.currentRoute.value.path !== "/login") {
    //   router.push("/login");
    //   //  UIStore.setLoading(false);
    // } //else UIStore.setLoading(false);
    // else return;

    UIStore.setLoading(false);
  });
</script>

<style></style>
