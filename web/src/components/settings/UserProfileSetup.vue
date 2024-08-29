<template lang="">
  <div class="h-full py-10 px-5 flex flex-col justify-start gap-10">
    <h1 class="text-start font-semibold text-2xl">Edit User Profile</h1>
    <div class="w-full flex flex-row justify-center gap-5">
      <div class="w-1/2 flex flex-col gap-7">
        <label
          class="input input-bordered input-primary input-sm flex items-center gap-2"
        >
          Username:
          <input
            type="text"
            class="grow"
            placeholder="Edit your username"
            v-model="username"
          />
        </label>
        <label
          class="input input-bordered input-primary input-sm flex items-center gap-2"
        >
          Email:
          <input
            type="text"
            class="grow"
            placeholder="Edit your e-mail"
            v-model="email"
          />
        </label>
      </div>

      <div class="w-1/2 flex justify-center">
        <div class="avatar">
          <div class="w-24 rounded-full">
            <img :src="newImg ? newImg : user.avatar" />
          </div>
        </div>
      </div>
    </div>

    <label
      class="input input-bordered input-primary input-sm flex items-center gap-2"
    >
      Avatar:
      <input
        type="text"
        class="grow"
        placeholder="Link a new avatar url"
        v-model="newImg"
      />
    </label>
    <label
      class="input input-bordered input-primary input-sm flex items-center gap-2"
    >
      Github Profile:
      <input
        type="text"
        class="grow"
        placeholder="Write your github profile url"
        v-model="github"
      />
    </label>
    <div class="flex flex-col">
      <label>About you:</label>
      <textarea
        class="textarea textarea-primary textarea-sm"
        rows="4"
        placeholder="Write a few lines about you"
        v-model="about"
      ></textarea>
    </div>

    <button
      :class="[
        'btn ',
        !success
          ? failed
            ? 'btn-error'
            : 'btn-outline btn-primary'
          : 'btn-success',
      ]"
      @click="updateProfile"
    >
      <span class="loading loading-dots loading-lg" v-if="isLoading"></span>

      <p v-else>Update Profile</p>
    </button>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { useUserStore } from "../../store/user.store";
  import { User } from "../../utils/types";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";

  const user = ref<User>();

  const username = ref<string>();
  const email = ref<string>();
  const newImg = ref<string>();
  const about = ref<string>();
  const github = ref<string>();

  const userStore = useUserStore();
  const apiCall = useApiCall();

  const success = ref<boolean>();
  const isLoading = ref<boolean>();
  const failed = ref<boolean>();

  async function updateProfile() {
    isLoading.value = true;
    let newUser = user.value as User;
    if (username.value && username.value.length > 0)
      newUser.username = username.value;
    if (email.value && email.value.length > 0) newUser.email = email.value;
    if (newImg.value && newImg.value.length > 0) newUser.avatar = newImg.value;
    if (about.value && about.value.length > 0) newUser.about = about.value;
    if (github.value && github.value.length > 0)
      newUser.githubProfile = github.value;

    const response = (await apiCall.patch(EndpointType.USER_UPDATE, newUser, {
      params: {
        user_id: newUser.user_id,
      },
    })) as User;
    if (response.user_id == user.value?.user_id) {
      isLoading.value = false;
      success.value = true;
      user.value = response;
      userStore.setSelf(response);
      setTimeout(() => {
        success.value = false;
      }, 2000);
    } else {
      failed.value = true;
      setTimeout(() => {
        failed.value = false;
      }, 1000);
    }
    cleanFields();
  }

  function setPropsInFields() {
    username.value = user.value?.username;
    email.value = user.value?.email;
    about.value = user.value?.about;
    github.value = user.value?.githubProfile;
  }

  function cleanFields() {
    username.value = "";
    email.value = "";
    newImg.value = "";
    about.value = "";
    github.value = "";
  }

  onBeforeMount(() => {
    user.value = userStore.self;
    setPropsInFields();
  });
</script>
