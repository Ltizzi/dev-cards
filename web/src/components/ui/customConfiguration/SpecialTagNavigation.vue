<template>
  <div v-if="isLoaded" class="flex flex-col justify-start">
    <div
      :class="[
        'text-center w-fit flex flex-row flex-wrap justify-start gap-2 mx-9 motion-duration-300 motion-ease-in-out ',
        showTags ? 'motion-opacity-in-0 motion-preset-slide-down-lg' : '',
        !showTags ? 'motion-opacity-out-0 -motion-translate-y-out-150' : '',
      ]"
    >
      <SpecialTagElement
        v-for="tag in tags"
        :tag="tag.value"
        :fromControl="false"
        @click="goToTag(tag.value)"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import SpecialTagElement from "./SpecialTagElement.vue";
  import { useConfigStore } from "../../../store/config.store";
  import { SpecialTag } from "../../../utils/types";
  import { useRouter } from "vue-router";

  const props = defineProps<{
    showTags: boolean;
  }>();

  const configStore = useConfigStore();
  const router = useRouter();

  const tags = ref<SpecialTag[]>([]);
  const isLoaded = ref<boolean>(false);

  function goToTag(tag: string) {
    router.push(`/project/tasks?tag=${tag}`);
  }

  onBeforeMount(async () => {
    tags.value = await configStore.getSpecialTags();
    isLoaded.value = true;
  });
</script>
