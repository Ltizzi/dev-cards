<template lang="">
  <div class="flex flex-col justify-start mx-10 mt-10">
    <h1 class="text-2xl font-semibold">Projects:</h1>
    <div
      class="flex flex-row justify-start flex-wrap gap-5 pt-10 pb-5 mx-5"
      v-if="workspaces && workspaces.length > 0"
    >
      <ProjectCard v-for="ws in workspaces" :ws="ws" />
    </div>
    <div v-else class="text-lg">
      <h1>No current projects avaible</h1>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useApiCall } from "../../composables/useAPICall";
  import { useProjectStore } from "../../store/project.store";
  import { EndpointType } from "../../utils/endpoints";
  import { Workspace } from "../../utils/types";
  import ProjectCard from "./ProjectCard.vue";
  import { onBeforeMount, ref } from "vue";

  const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const workspaces = ref<Workspace[]>();
  const isLoaded = ref<boolean>(false);

  async function fetchProjects(userId: number) {
    return (await apiCall.get(EndpointType.USER_MEMBER, {
      params: { user_id: userId },
    })) as Array<Workspace>;
  }

  onBeforeMount(async () => {
    if (projectStore.memberOf.length > 0) {
      workspaces.value = projectStore.memberOf;
      isLoaded.value = true;
    } else {
      const id = JSON.parse(localStorage.getItem("user") as string).user_id;
      const wss = await fetchProjects(id);
      projectStore.setMemberOf(wss);
      workspaces.value = projectStore.memberOf;
      isLoaded.value = true;
    }
  });
</script>
