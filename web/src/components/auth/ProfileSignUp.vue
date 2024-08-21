<template lang="">
  <div class="mx-auto flex flex-col justify-center my-10">
    <h1 class="my-5 text-xl font-bold">Hi {{ user.username }}</h1>
    <h2 class="text-lg mb-5">Please complete your profile information:</h2>
    <div class="flex flex-col justify-center gap-5">
      <div class="flex flex-row gap-4">
        <div class="flex flex-col gap-5 w-5/6">
          <label for="">Avatar link:</label>
          <input
            type="text"
            class="input input-bordered input-primary w-full max-w-xs"
            v-model="avatar"
            placeholder="Write profile picture url"
          />
        </div>

        <div class="avatar my-auto w-1/6">
          <div
            class="ring-primary ring-offset-base-100 w-12 h-12 rounded-full ring ring-offset-2 mt-10"
          >
            <img :src="avatar ? avatar : default_avatar" />
          </div>
        </div>
      </div>

      <label for="">Github profile:</label>
      <input
        type="text"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="githubProfile"
        placeholder="Enter your github profile"
      />
      <label for="">About you:</label>
      <textarea
        class="textarea textarea-secondary textarea-lg w-full max-w-xs"
        placeholder="Bio"
        v-model="about"
      ></textarea>

      <button class="btn btn-outline btn-accent" @click="updateProfile">
        Continue
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
  import { onBeforeMount, ref } from "vue";
  import { User } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { useRouter } from "vue-router";
  import { AxiosRequestConfig } from "axios";

  const store = useUserStore();
  const apiCall = useApiCall();
  const router = useRouter();

  const user = ref<User>();
  const default_avatar = ref<string>(
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Default_avatar_profile.jpg/600px-Default_avatar_profile.jpg?20231202140256"
  ); //new URL(`@/assets/avatar.png`, import.meta.url).href;
  const avatar = ref<string>();
  const githubProfile = ref<string>();
  const about = ref<string>();

  const isWaiting = ref(false);
  const hasError = ref(false);

  async function updateProfile() {
    isWaiting.value = true;
    if (user.value) {
      const updatedProfile = {
        user_id: user.value.user_id,
        username: user.value.username,
        email: user.value.email,
        avatar: avatar.value ? avatar.value : default_avatar.value,
        about: about.value,
        githubProfile: githubProfile.value,
        roles: user.value.roles,
        created_at: user.value.created_at,
        updated_at: user.value.updated_at,
      };

      const config = {
        params: {
          user_id: updatedProfile.user_id as number,
        },
      } as AxiosRequestConfig;

      const response = (await apiCall.patch(
        EndpointType.USER_UPDATE,
        updatedProfile,
        config
      )) as User;
      if (response && response.user_id == user.value.user_id) {
        store.setSelf(response);
        localStorage.setItem("user", JSON.stringify(response));
        isWaiting.value = false;
        store.flagAsNewUser();
        router.push("/signup/project");
      } else {
        isWaiting.value = false;
        console.error(response);
        hasError.value = true;
        setTimeout(() => {
          hasError.value = false;
        }, 5000);
      }
    }
  }

  onBeforeMount(() => {
    user.value = store.self;
  });
</script>
