<template lang="">
  <div class="flex flex-row justify-center w-screen" v-if="isLoaded">
    <ProjectLateralMenu class="h-screen left-20 fixed" />

    <div class="ml-56 w-10/12">
      <router-view></router-view>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useProjectStore } from "../store/project.store";
  import { Workspace } from "../utils/types";
  import { ref, onBeforeMount } from "vue";
  import { useRoute } from "vue-router";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import ProjectLateralMenu from "../components/project/ProjectLateralMenu.vue";

  const project = ref<Workspace>();

  const apiCall = useApiCall();

  const isLoaded = ref<boolean>(false);

  const route = useRoute();

  const projectStore = useProjectStore();

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
