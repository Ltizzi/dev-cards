<template lang="">
  <div class="flex flex-col justify-between w-full" v-if="isLoaded">
    <div class="flex flex-row justify-between max-w-full ml-24">
      <div class="w-full">
        <h1>asdasd</h1>
      </div>

      <button
        class="btn btn-outline btn-success"
        @click="login"
        v-if="!isLoggedIn"
      >
        Log in
      </button>
      <button
        class="btn btn-outline btn-error"
        @click="signoff"
        v-if="isLoggedIn"
      >
        Logout
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useRouter } from "vue-router";
  import { onBeforeMount, ref } from "vue";
  import { useUserStore } from "../store/user.store";

  const router = useRouter();

  const userStore = useUserStore();

  const isLoggedIn = ref(false);
  const isLoaded = ref(false);

  function login() {
    router.push("/login");
  }

  function signoff() {
    userStore.logoutUser();
    isLoggedIn.value = false;
  }

  onBeforeMount(() => {
    const user = JSON.parse(localStorage.getItem("user") as string);
    if (!user) {
      router.push("/login");
    } else {
      isLoggedIn.value = true;
      isLoaded.value = true;
    }
  });
</script>