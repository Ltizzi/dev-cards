<template lang="">
  <div class="flex flex-col justify-start mx-10 mt-10">
    <h1 class="text-2xl font-semibold">Projects:</h1>
    <div
      class="flex flex-row lg:justify-start justify-center flex-wrap gap-2.5 pt-10 pb-5 mx-5"
      v-if="isLoaded"
    >
      <ProjectCard v-for="ws in workspaces" :ws="ws" />
    </div>
    <div v-else class="text-lg">
      <h1>No current projects avaible</h1>
    </div>
  </div>
</template>
<script setup lang="ts">
  //import { useApiCall } from "../../composables/useAPICall";
  import { useProjectStore } from "../../store/project.store";
  import { useUserStore } from "../../store/user.store";
  //import { EndpointType } from "../../utils/endpoints";
  import { Workspace } from "../../utils/types";
  import ProjectCard from "./ProjectCard.vue";
  import { onMounted, ref } from "vue";

  const projectStore = useProjectStore();
  const userStore = useUserStore();

  const workspaces = ref<Workspace[]>();
  const isLoaded = ref<boolean>(false);

  async function fetchProjects(userId: number) {
    return (await userStore.fetchAllWorkspacesMember(userId)) as Workspace[];
  }

  onMounted(async () => {
    if (projectStore.memberOf.length > 0) {
      workspaces.value = projectStore.memberOf;
    } else {
      const id = userStore.getSelf().user_id; //JSON.parse(localStorage.getItem("user") as string).user_id;
      const wss = await fetchProjects(id);
      projectStore.setMemberOf(wss);
      workspaces.value = projectStore.memberOf;
    }
    if (workspaces.value.length > 0) isLoaded.value = true;
  });
</script>
