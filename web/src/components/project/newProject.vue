<template lang="">
  <div class="mx-auto flex flex-col justify-center my-10">
    <h1 class="my-5 text-xl font-bold">Hi {{ user.username }}</h1>
    <h2 class="text-lg mb-5">Now you can create a new Project</h2>
    <div class="flex flex-col justify-center gap-5">
      <label for="">Project name:</label>
      <input
        type="text"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="name"
        placeholder="Enter the project name"
      />
      <div class="flex flex-row gap-4">
        <div class="flex flex-col gap-5 w-5/6">
          <label for="">Project avatar link:</label>
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

      <label for="">Project description:</label>
      <textarea
        class="textarea textarea-secondary textarea-lg w-full max-w-xs"
        placeholder="Description"
        v-model="description"
      ></textarea>

      <div class="flex flex-row justify-around">
        <button class="btn btn-outline btn-accent" @click="newProject">
          Create Project
          <span class="loading loading-dots loading-sm" v-if="isWaiting"></span>
        </button>
        <button
          class="btn btn-outline btn-secondary"
          @click="notNow"
          v-if="isInSignUpProcess"
        >
          Not now
        </button>
      </div>

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
  import { useUserStore } from "../../store/user.store";
  import { useProjectStore } from "../../store/project.store";
  import { useRoute, useRouter } from "vue-router";
  import { User, UserLite, Workspace } from "../../utils/types";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";

  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const router = useRouter();
  const route = useRoute();

  const apiCall = useApiCall();

  const user = ref<User>();

  const name = ref<string>();
  const description = ref<string>();
  const avatar = ref<string>();

  const isWaiting = ref(false);
  const hasError = ref(false);
  const isInSignUpProcess = ref(false);

  async function newProject() {
    isWaiting.value = true;
    if (user) {
      const userLite: UserLite = {
        user_id: user.value?.user_id as number,
        username: user.value?.username as string,
        email: user.value?.email as string,
        avatar: user.value?.avatar as string,
      };

      const newProject = {
        project_name: name.value,
        project_avatar: avatar.value,
        description: description.value,
        owner: userLite,
      };

      const response = (await apiCall.post(
        EndpointType.WORKSPACE_NEW,
        newProject
      )) as Workspace;

      if (response && response.workspace_id) {
        projectStore.setCurrent(response);
        projectStore.addProjectToOwner(response);
        isWaiting.value = false;
        router.push("/");
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

  function notNow() {
    router.push("/");
  }

  onBeforeMount(() => {
    console.log(userStore.self);
    user.value = JSON.parse(localStorage.getItem("user") as string);
    if (route.path == "/signup/project") isInSignUpProcess.value = true;
  });
</script>
