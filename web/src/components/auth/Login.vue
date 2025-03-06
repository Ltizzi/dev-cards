<template lang="">
  <div
    class="mx-auto flex flex-col justify-center my-auto min-h-screen motion-scale-in-[0] motion-translate-x-in-[-150%] motion-translate-y-in-[300%] motion-opacity-in-[0%] motion-rotate-in-[-500deg] motion-blur-in-[100px] motion-duration-[1.08s]/scale motion-duration-[1.25s]/translate motion-duration-[0.48s]/opacity motion-duration-[1.46s]/rotate motion-duration-[0.77s]/blur motion-ease-spring-snappy"
  >
    <div class="flex flex-col justify-center mx-auto gap-5">
      <label for="">Username:</label>
      <input
        type="text"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="username"
        placeholder="Enter your username"
      />
      <label for="">Password:</label>
      <input
        type="password"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="password"
        placeholder="Enter your password"
      />
      <button class="btn btn-outline btn-accent mt-5" @click="logIn">
        Log in
        <span class="loading loading-dots loading-sm" v-if="isWaiting"></span>
      </button>
      <div class="text-center">
        <p>Don't have an account?</p>
        <router-link to="/signup/register" class="link link-info"
          >Just make one</router-link
        >
      </div>
      <div class="text-center">
        <p>Don't want to/can't be/API isn't online?</p>
        <p class="link link-info" @click="activeOfflineMode">
          Turn on local mode
        </p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref } from "vue";
  // import { useApiCall } from "../../composables/useAPICall";
  // import { EndpointType } from "../../utils/endpoints";
  import { useUserStore } from "../../store/user.store";
  import { AuthRequest, AuthResponse } from "../../utils/types";
  import { useRouter } from "vue-router";
  import { saveToken } from "../../utils/auth.utils";
  import { useUIStore } from "../../store/ui.store";

  const username = ref<String>();
  const password = ref<String>();
  const UIStore = useUIStore();

  const isWaiting = ref(false);

  //const apiCall = useApiCall();

  const store = useUserStore();

  const router = useRouter();

  function activeOfflineMode() {
    UIStore.setOfflineMode(true);
    router.push({ path: "/offline" });
  }

  async function logIn() {
    isWaiting.value = true;
    const user: AuthRequest = {
      username: username.value as string,
      password: password.value as string,
    };
    const response = (await store.login(user)) as AuthResponse;
    if (response && response.token) {
      UIStore.setOfflineMode(false);
      isWaiting.value = false;
      router.push({ path: "/" });
    } else console.error(response);
  }
</script>
