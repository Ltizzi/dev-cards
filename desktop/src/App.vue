<template>
  <router-view></router-view>
  <LoadingModal />
</template>
<script setup lang="ts">
  import TheHome from "./pages/TheHome.vue";
  import LoadingModal from "./components/ui/LoadingModal.vue";
  import { watch } from "vue";
  import { useProjectStore } from "./store/project.store";
  import { useRoute } from "vue-router";

  const projectStore = useProjectStore();
  const route = useRoute();

  watch(
    () => projectStore.current.project_name,
    (newValue: string, oldValue: string) => {
      if (
        newValue &&
        newValue != oldValue &&
        !route.path.includes("/project/")
      ) {
        (window as any).appControl.setProjectTitle(newValue);
      }
    }
  );
</script>
