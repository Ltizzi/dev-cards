<template lang="">
  <div class="w-screen relative bg-base-100">
    <LateralMenu
      :class="[
        'duration-150 transition-all  ease-in-out opacity-90 z-0 h-screen fixed  bg-gradient-to-br from-0% via-30% from-primary via-secondary to-100% to-transparent -mx-16   hover:translate-x-16 hover:z-20',
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

  const router = useRouter();

  const float = ref(0);

  const firstLoaded = ref(true);

  const randomNum = ref(0);

  const blur = ref(0);
  const blur_max = ref(false);
  const blur_min = ref(true);
  const blur_class = ref("none");

  function chooseColor() {
    console.log("Choosing color");
    randomNum.value = Math.floor(Math.random() * 100);
    console.log(randomNum.value);
  }

  // setInterval(() => {
  //   if (blur_min.value) blur.value += 1;
  //   if (blur_max.value) blur.value -= 1;
  //   if (blur.value >= 12) {
  //     blur_max.value = true;
  //     blur_min.value = false;
  //   }
  //   if (blur.value <= 0) {
  //     blur_max.value = false;
  //     blur_min.value = true;
  //   }
  //   if (blur.value == 0) {
  //     blur_class.value = "none";
  //   }
  //   if (blur.value == 6) {
  //     blur_class.value = "sm";
  //   }
  //   if (blur.value == 12) {
  //     blur_class.value = "md";
  //   }
  // }, 150);

  setInterval(() => {
    chooseColor();
  }, 500);

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
