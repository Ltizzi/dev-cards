<template lang="">
  <div class="min-h-screen flex items-center justify-center">
    <div
      class="flex justify-center items-center text-lg mx-auto w-11/12 h-5/6 px-20 my-auto text-center bg-gradient-to-r from-transparent via-secondary py-28 rounded-xl shadow-lg shadow-accent to-primary"
      v-if="!failed"
    >
      <div
        class="flex flex-row flex-grow justify-center gap-5 bg-neutral bg-opacity-50 w-10/12 py-7 h-full rounded-2xl"
      >
        <div
          class="flex flex-col gap-5 justify-center w-2/5 min-h-96 items-end"
        >
          <div class="avatar">
            <div class="w-44 rounded-full">
              <img :src="user.avatar ? user.avatar : default_avatar" />
            </div>
          </div>
          <div class="text-start flex items-end flex-col gap-2">
            <h1
              :class="[
                'font-bold text-2xl px-2 py-0.5 rounded-xl w-fit mt-0 ',
                !isDark ? 'text-white' : '',
              ]"
            >
              {{ user.username }}
            </h1>
            <h2 class="italic text-info">
              <p
                :class="[
                  'bg-neutral px-2 py-0.5 rounded-xl w-fit mx-auto',
                  !isDark ? 'text-white  bg-opacity-30' : 'bg-opacity-60',
                ]"
              >
                {{ user.email }}
              </p>
            </h2>
            <h2 v-if="user.githubProfile" class="text-info">
              <a
                :href="user.githubProfile"
                :class="[
                  'hover:underline bg-neutral px-2 py-0.5 rounded-xl w-fit mx-auto',
                  !isDark ? 'text-white  bg-opacity-30' : 'bg-opacity-60',
                ]"
                target="_blank"
                >{{ user.githubProfile }}</a
              >
            </h2>
          </div>
        </div>
        <div
          class="flex flex-col item-middle align-middle my-auto justify-center text-center w-3/5"
        >
          <p
            :class="[
              ' px-5 grow py-5 rounded-xl w-fit mx-auto ',
              !isDark ? 'text-white' : '',
            ]"
          >
            {{ user.about }}
          </p>
        </div>
      </div>
    </div>
    <div role="alert" class="alert alert-error my-auto" v-else>
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
      <span>Error! Can't show user's profile.</span>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useRoute } from "vue-router";
  import { ref, onBeforeMount, watch } from "vue";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import { useProjectStore } from "../store/project.store";
  import { User } from "../utils/types";
  import { useUIStore } from "../store/ui.store";

  const route = useRoute();
  const id = ref<number>();

  const apiCall = useApiCall();
  const projectStore = useProjectStore();
  const UIStore = useUIStore();

  const user = ref<User>();
  const failed = ref<boolean>();
  const isDark = ref<boolean>();

  const default_avatar = ref<string>(
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Default_avatar_profile.jpg/600px-Default_avatar_profile.jpg?20231202140256"
  );

  watch(
    () => route.query.id,
    async (newValue, oldValue) => {
      if (route.query.id && newValue != oldValue) {
        id.value = +route.query.id;
        await getUserById();
      }
    }
  );

  async function getUserById() {
    const response = (await apiCall.get(EndpointType.USER_GET_BY_ID, {
      params: {
        user_id: id.value,
        ws_id: projectStore.current.workspace_id,
      },
    })) as User;
    if (response.user_id) {
      user.value = response;
    } else {
      failed.value = true;
    }
  }
  onBeforeMount(async () => {
    if (route.query.id) {
      id.value = +route.query.id;
      await getUserById();
    } else failed.value = true;
    isDark.value = UIStore.darkTheme;
  });
</script>
<style lang=""></style>
