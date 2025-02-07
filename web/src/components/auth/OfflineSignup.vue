<template lang="">
  <h1 class="text-4xl text-center pt-5">Select local user</h1>
  <div class="flex flex-row justify-evenly">
    <div class="flex flex-col justify-center items-center w-1/2">
      <div
        class="flex flex-col justify-end items-end"
        v-if="isLoaded && !noUsers"
      >
        <div class="card bg-neutral w-64 shadow-xl" v-for="user in localUsers">
          <div class="card-body">
            <h2 class="card-title">{{ user.nickname }}</h2>
            <div class="flex flex-row justify-between py-5">
              <div class="avatar">
                <div
                  class="ring-primary ring-offset-base-100 w-12 rounded-full ring ring-offset-2"
                >
                  <img :src="user.avatar" />
                </div>
              </div>
              <div class="card-actions justify-end">
                <button class="btn btn-primary" @click="selectUser(user)">
                  Select
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="flex flex-col justify-center items-start my-auto min-h-screen w-1/2"
    >
      <div class="flex flex-col justify-start gap-5">
        <h1 class="text-start text-2xl">Create User</h1>
        <label for="">Username:</label>
        <input
          type="text"
          class="input input-bordered input-primary w-full max-w-xs"
          v-model="nickname"
          placeholder="Enter your username"
        />
        <label for="">Avatar URL:</label>
        <input
          type="text"
          class="input input-bordered input-primary w-full max-w-xs"
          v-model="avatarUrl"
          placeholder="Enter a Avatar"
        />
        <button
          class="btn btn-outline btn-accent mt-5"
          @click="createOfflineUser"
        >
          Create local user
          <span class="loading loading-dots loading-sm" v-if="isWaiting"></span>
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { TaskLite, UserLocal, WorkspaceLite } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useRouter } from "vue-router";
  import { useUIStore } from "../../store/ui.store";
  import { utils } from "../../utils/utils";

  const userStore = useUserStore();
  const UIStore = useUIStore();

  const router = useRouter();

  const nickname = ref<string>();
  const avatarUrl = ref<string>();
  const isWaiting = ref<boolean>();
  const localUsers = ref<UserLocal[]>();
  const noUsers = ref<boolean>(false);
  const isLoaded = ref<boolean>(false);

  function createOfflineUser() {
    console.log("Creating local user...");
    const newUser: UserLocal = {
      user_id: 1,
      nickname: nickname.value as string,
      avatar: avatarUrl.value as string,
      designated_tasks: [] as TaskLite[],
      workspaces: [] as WorkspaceLite[],
      created_tasks: [] as TaskLite[],
      local: true,
    };
    console.log("USER DATA: " + newUser);
    console.log("Setting profile as user...");
    userStore.setLocalUser(newUser);
    console.log("Saving user to local storage....");
    userStore.saveLocal();
    console.log("Flagging User as New...");
    userStore.flagAsNewUser();
    console.log("Turning on Offline Mode");
    UIStore.setOfflineMode(true);
    console.log("Re-routing to /");
    router.push("/");
  }

  function selectUser(user: UserLocal) {
    userStore.setLocalUser(user);
    userStore.saveLocal();
    UIStore.setOfflineMode(true);
    router.push("/");
  }

  function getLocalUsers() {
    return userStore.getLocalUsers();
  }

  onBeforeMount(() => {
    const users = getLocalUsers();
    if (users) localUsers.value = users;
    else noUsers.value = true;

    isLoaded.value = true;
  });
</script>
