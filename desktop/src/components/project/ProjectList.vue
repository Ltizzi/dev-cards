<template lang="">
  <div class="flex flex-col justify-start mx-10 mt-0 min-h-screen">
    <h1 class="text-2xl font-semibold">Projects:</h1>
    <div
      class="flex flex-row lg:justify-start justify-center flex-wrap gap-2.5 pt-10 pb-5 mx-5"
      v-if="isLoaded"
    >
      <ProjectCard
        v-for="ws in workspaces"
        :ws="ws"
        class="motion-duration-1000 motion-preset-pop"
      />
    </div>
    <div v-if="!isLoaded && workspaces.length < 1" class="text-lg">
      <h1>No current projects avaible</h1>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useProjectStore } from "../../store/project.store";
  import { useUIStore } from "../../store/ui.store";
  import { useUserStore } from "../../store/user.store";
  import { Workspace } from "../../utils/types";
  import ProjectCard from "./ProjectCard.vue";
  import { ref, onBeforeMount, watch } from "vue";

  const projectStore = useProjectStore();
  const userStore = useUserStore();
  const UIStore = useUIStore();

  const workspaces = ref<Workspace[]>();
  const isLoaded = ref<boolean>(false);
  const fetching = ref<boolean>(false);

  async function fetchProjects(userId: number) {
    return (await userStore.fetchAllWorkspacesMember(userId)) as Workspace[];
  }

  watch(
    () => projectStore.memberOf,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        workspaces.value = projectStore.memberOf;
      }
    }
  );

  watch(
    () => projectStore.justCreated,
    async (newValue, oldValue) => {
      if (newValue != oldValue) {
        const user_id = userStore.getCurrent().user_id;
        workspaces.value = await projectStore.fetchProjectsByUser(user_id);
      }
    }
  );

  onBeforeMount(async () => {
    UIStore.setLoading(true);
    if (projectStore.memberOf) {
      workspaces.value = projectStore.memberOf;
      UIStore.setLoading(false);
    } else {
      const id = userStore.getSelf().user_id; //JSON.parse(localStorage.getItem("user") as string).user_id;
      const wss = await fetchProjects(id);
      if (wss) {
        projectStore.setMemberOf(wss);
        workspaces.value = projectStore.memberOf;
        UIStore.setLoading(false);
      }
    }
    isLoaded.value = true;
    fetching.value = true;
  });
</script>
