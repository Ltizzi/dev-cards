<template lang="">
  <input
    type="file"
    class="file-input file-input-bordered file-input-primary w-full max-w-xs file-input-xs lg:file-input-lg"
    accept=".json"
    @change="handleFileUpload"
  />
</template>
<script setup lang="ts">
  import { ref } from "vue";
  import { useProjectStore } from "../../store/project.store";
  import { JSONWorkspace } from "../../utils/types";

  const fileContent = ref<Record<string, any> | null>(null);

  const projectStore = useProjectStore();

  function handleFileUpload(event: Event) {
    console.log("LOADING...");
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    console.log(file);

    if (!file) return;

    const reader = new FileReader();

    reader.onload = (event) => {
      const content = event.target?.result as string;
      try {
        console.log(content);
        const parsedData = JSON.parse(content);
        projectStore.saveJSONWStoLocalStorage(parsedData as JSONWorkspace);
      } catch (err: any) {
        console.error("Invalid JSON file: ", err);
      }
    };

    reader.onerror = () => {
      console.error("An Error ocurred while reading file: ", reader.error);
    };
    reader.readAsText(file);
  }
</script>
