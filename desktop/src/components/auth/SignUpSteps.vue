<template lang="">
  <ul class="steps steps-vertical">
    <li class="step step-primary">Register</li>
    <li :class="['step', state.second ? 'step-primary' : '']">
      Complete Your Profile
    </li>
    <li :class="['step', state.third ? 'step-primary' : '']">
      Start a new project
    </li>
  </ul>
</template>
<script setup lang="ts">
  import { useRoute } from "vue-router";
  import { reactive, watch } from "vue";

  const route = useRoute();

  const state = reactive({
    second: false,
    third: false,
  });

  watch(
    () => route.fullPath,
    (newPath, oldPath) => {
      console.log(newPath, " ", oldPath);
      if (newPath == "/signup/register") {
        console.log("ASD");
        state.second = false;
        state.second = false;
      }
      if (newPath == "/signup/complete") {
        console.log("ASD2");
        state.second = true;
        state.third = false;
      }
      if (newPath == "/signup/project") {
        console.log("ASD3");
        state.second = true;
        state.third = true;
      }
    }
  );
</script>
