<template lang="">
  <div class="mx-auto flex flex-col justify-center my-10">
    <h1 class="my-5 text-xl font-bold">Create a new user</h1>
    <h2 class="text-lg mb-7">
      Please, complete this form to register you as a new user:
    </h2>
    <div class="flex flex-col justify-center gap-5">
      <label for="">Username:</label>
      <input
        type="text"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="username"
        placeholder="Enter your username"
      />
      <label for="">E-mail:</label>
      <input
        type="email"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="email"
        placeholder="Enter your e-mail"
      />
      <label for="">Password:</label>
      <input
        type="password"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="password"
        placeholder="Enter your password"
      />
      <button class="btn btn-outline btn-accent" @click="signUp">
        Sign up
        <span class="loading loading-dots loading-sm" v-if="isWaiting"></span>
      </button>

      <div role="alert" class="alert alert-error" v-if="hasError">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-6 w-6 shrink-0 stroke-current"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span>Error! Task failed successfully.</span>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { useUserStore } from "../../store/user.store";
  import { useRouter } from "vue-router";
  import { AuthResponse } from "../../utils/types";

  const username = ref<String>();
  const password = ref<String>();
  const email = ref<String>();

  const isWaiting = ref(false);
  const hasError = ref(false);

  const apiCall = useApiCall();
  const store = useUserStore();
  const router = useRouter();

  async function signUp() {
    isWaiting.value = true;
    const newUser = {
      username: username.value,
      email: email.value,
      password: password.value,
    };
    const response = (await apiCall.post(
      EndpointType.USER_REGISTER,
      newUser
    )) as AuthResponse;
    if (response && response.token) {
      store.setSelf(response.user);
      localStorage.setItem("user", JSON.stringify(response.user));
      localStorage.setItem("token", response.token);
      isWaiting.value = false;
      router.push("/signup/complete");
    } else {
      isWaiting.value = false;
      console.error(response);
      hasError.value = true;
      setTimeout(() => {
        hasError.value = false;
      }, 5000);
    }
  }
</script>
