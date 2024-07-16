<template lang="">
  <div class="flex flex-col justify-center w-full" v-if="isLoaded">
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
      <p class="mx-10 text-lg">{{ project.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { Workspace } from "../utils/types";
  import { useProjectStore } from "../store/project.store";
  import { useRoute } from "vue-router";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";

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
