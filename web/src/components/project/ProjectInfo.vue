<template lang="">
  <div class="flex flex-col justify-center w-full mx-5" v-if="isLoaded">
    <div class="flex flex-row justify-center gap-5 align-middle">
      <h1 class="text-4xl text-center my-5 font-bold">
        {{ project.project_name }}
      </h1>
      <div class="avatar">
        <div class="w-14 h-14 rounded-full mx-auto my-auto">
          <img :src="project.project_avatar" />
        </div>
      </div>
    </div>
    <div class="my-5">
      <h1 class="mb-2 text-xl font-bold">Description:</h1>
      <p class="mx-10 text-lg">{{ project.description }}</p>
    </div>
    <div class="my-5">
      <h1 class="mb-2 text-xl font-bold">Created by:</h1>
      <div class="flex flex-row gap-0 ml-20 mt-5">
        <div class="avatar">
          <div class="w-8 rounded-full">
            <img :src="project.owner.avatar" />
          </div>
        </div>
        <p class="mx-10 text-lg">
          {{ project.owner.username }}, at {{ project.created_at }}
        </p>
      </div>
    </div>
    <div class="my-5">
      <h1 class="mb-2 text-xl font-bold">Moderators:</h1>
      <p class="mx-10 text-lg flex flex-row flex-wrap gap-5" v-for="mod in project.moderators">
        <div class="flex flex-row gap-0 ml-20 mt-5">
            <div class="avatar">
              <div class="w-8 rounded-full">
                <img :src="mod.avatar" />
              </div>
            </div>
            <p class="mx-10 text-lg">
              {{ mod.username }}
            </p>
          </div>
      </p>
    </div>
    <div class="my-5">
        <h1 class="mb-2 text-xl font-bold">Users:</h1>
        <p class="mx-10 text-lg flex flex-row flex-wrap gap-5" v-for="user in project.users">
          <div class="flex flex-row gap-0 ml-20 mt-5">
              <div class="avatar">
                <div class="w-8 rounded-full">
                  <img :src="user.avatar" />
                </div>
              </div>
              <p class="mx-10 text-lg">
                {{ user.username }}
              </p>
            </div>
        </p>
      </div>
      <h1 class="text-3xl my-5">Tasks:</h1>
      <TaskList/>
  </div>
</template>

<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { Workspace } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";
  import { useRoute } from "vue-router";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import TaskList from "../task/TaskList.vue";

  const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const route = useRoute();

  const project = ref<Workspace>();

  const isLoaded = ref(false);

  onBeforeMount(async () => {
    if (projectStore.current.workspace_id) {
      project.value = projectStore.current;
      isLoaded.value = true;
    } else {
      const id = route.query.id;
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: id },
      })) as Workspace;
      if (response.workspace_id) {
        project.value = response;
        isLoaded.value = true;
      } else {
        console.error(response);
      }
    }
  });
</script>