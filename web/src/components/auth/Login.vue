<template lang="">
  <div class="mx-auto flex flex-col justify-center my-56">
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
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { useUserStore } from "../../store/user.store";
  import { AuthResponse } from "../../utils/types";
  import { useRouter } from "vue-router";
  import { saveToken } from "../../utils/auth.utils";

  const username = ref<String>();
  const password = ref<String>();

  const isWaiting = ref(false);

  const apiCall = useApiCall();

  const store = useUserStore();

  const router = useRouter();

  async function logIn() {
    isWaiting.value = true;
    const user = {
      username: username.value,
      password: password.value,
    };
    const response = (await apiCall.post(
      EndpointType.USER_LOGIN,
      user
    )) as AuthResponse;
    if (response && response.token) {
      store.setSelf(response.user);
      localStorage.setItem("user", JSON.stringify(response.user));
      // localStorage.setItem("token", response.token);
      saveToken(response.token);
      isWaiting.value = false;
      router.push({ path: "/" });
    } else console.error(response);
  }
</script>
