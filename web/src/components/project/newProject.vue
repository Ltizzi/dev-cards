<template lang="">
  <div
    :class="[
      freshUser && !isOffline
        ? 'mx-7 lg:mx-auto lg:w-3/6 flex flex-col justify-center my-auto'
        : 'lg:w-1/6 w-3/6 lg:mx-auto    flex  flex-col mx-7 justify-center align-middle',
      'min-h-screen',
    ]"
  >
    <h1 class="my-5 text-xl font-bold">Hi {{ user.username }}</h1>
    <h2 class="text-lg mb-5">
      {{
        freshUser ? "Now you can create a new Project" : "Create  new project"
      }}
    </h2>
    <div class="flex flex-col justify-center gap-5">
      <label for="">Project name:</label>
      <input
        type="text"
        class="input input-bordered input-primary w-full max-w-xs"
        v-model="name"
        placeholder="Enter the project name"
      />
      <div
        :class="[
          'flex flex-row gap-4',
          !freshUser ? 'w-full gap-10 relative' : '',
        ]"
      >
        <div class="flex flex-col gap-5 w-5/6">
          <label for="">Project avatar link:</label>
          <input
            type="text"
            class="input input-bordered input-primary w-full max-w-xs"
            v-model="avatar"
            placeholder="Write profile picture url"
          />
        </div>

        <div
          :class="[
            freshUser
              ? 'avatar my-auto w-1/6'
              : 'absolute avatar my-auto -right-36',
          ]"
          v-if="avatar"
        >
          <div
            :class="[
              freshUser
                ? 'ring-primary ring-offset-base-100 w-12 h-12 rounded-full ring ring-offset-2 mt-10'
                : 'size-40',
            ]"
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

      <div
        :class="[
          freshUser
            ? 'flex flex-row justify-around'
            : 'flex flex-row gap-5 -ml-12 lg:ml-5 2xl:-ml-12 mt-5 justify-center',
        ]"
      >
        <button :class="['btn btn-outline btn-accent']" @click="newProject">
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
        <button
          class="btn btn-outline btn-secondary"
          @click="notNow"
          v-if="!isInSignUpProcess"
        >
          Cancel
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
  import {
    User,
    UserLite,
    UserLocal,
    Workspace,
    WorkspaceWithJwt,
  } from "../../utils/types";
  // import { useApiCall } from "../../composables/useAPICall";
  // import { EndpointType } from "../../utils/endpoints";
  import { saveToken } from "../../utils/auth.utils";
  import { utils } from "../../utils/utils";

  const userStore = useUserStore();
  const projectStore = useProjectStore();

  const router = useRouter();
  const route = useRoute();

  //const apiCall = useApiCall();

  const freshUser = ref<boolean>(false);

  const user = ref<User | UserLocal>();

  const name = ref<string>();
  const description = ref<string>();
  const avatar = ref<string>();

  const isOffline = ref(false);
  const isWaiting = ref(false);
  const hasError = ref(false);
  const isInSignUpProcess = ref(false);

  function checkUserIsLocal(obj: any): obj is UserLocal {
    console.log("local" in obj);
    return "local" in obj;
  }

  async function newProject() {
    isWaiting.value = true;
    console.log("USER DATA:");
    console.log(user.value);
    let userLite = {} as UserLite;
    if (user.value && !userStore.offlineMode && !checkUserIsLocal(user.value)) {
      userLite = {
        user_id: user.value.user_id as number,
        username: user.value.username as string,
        email: user.value.email as string,
        avatar: user.value.avatar as string,
      };
    } else if (
      user.value &&
      userStore.offlineMode &&
      checkUserIsLocal(user.value)
    ) {
      console.log("OFFLINE");
      userLite = {
        user_id: user.value.user_id,
        username: user.value.username,
        email: "user@localhost.com",
        avatar: user.value.avatar,
      };
    }

    const newProject = {
      project_name: name.value as string,
      project_avatar: avatar.value
        ? (avatar.value as string)
        : "/assets/workspace.png",
      description: description.value as string,
      owner: userLite,
      users: [],
      moderators: [],
      tasks: [],
      collaborators: [],
      created_at: new Date(Date.now()),
      updated_at: new Date(Date.now()),
    };

    const response = (await projectStore.createWorkspace(
      newProject as unknown as Workspace
    )) as WorkspaceWithJwt;
    // (await apiCall.post(
    //   EndpointType.WORKSPACE_NEW,
    //   newProject
    // )) as WorkspaceWithJwt;
    if (!userStore.offlineMode) {
      if (response && response.workspace.workspace_id) {
        projectStore.setCurrent(response.workspace);
        projectStore.setJustCreated();
        projectStore.addProjectToOwner(response.workspace);

        saveToken(response.token);
        isWaiting.value = false;
        projectStore.setMemberOf([response.workspace]);
        router.push("/");
      } else {
        isWaiting.value = false;
        console.error(response);
        hasError.value = true;
        setTimeout(() => {
          hasError.value = false;
        }, 5000);
      }
    } else {
      projectStore.setJustCreated();
      projectStore.addProjectToOwner(response.workspace);
      isWaiting.value = false;
      router.push("/");
    }
  }

  function notNow() {
    router.push("/");
  }

  onBeforeMount(() => {
    freshUser.value = userStore.checkIfUserIsNew();
    user.value = userStore.getSelf();
    isOffline.value = userStore.checkOfflineMode();
    if (route.path == "/signup/project") isInSignUpProcess.value = true;
  });
</script>
