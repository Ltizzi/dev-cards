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
  import { onBeforeMount, ref, watch } from "vue";
  import SpecialTagElement from "./SpecialTagElement.vue";
  import { useConfigStore } from "../../../store/config.store";
  import { SpecialTag } from "../../../utils/types";
  import { useRouter } from "vue-router";
  import { useProjectStore } from "../../../store/project.store";

  const props = defineProps<{
    showTags: boolean;
    info: boolean;
  }>();

  const emit = defineEmits(["update"]);

  const configStore = useConfigStore();
  const projectStore = useProjectStore();
  const router = useRouter();

  const tags = ref<SpecialTag[]>([]);
  const isLoaded = ref<boolean>(false);

  function goToTag(tag: string) {
    if (!props.info) emit("update", tag);
    else router.push(`/project/tasks?tag=${tag}`);
  }

  watch(
    () => projectStore.current,
    async (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        tags.value = await configStore.getSpecialTags();
      }
    }
  );

  onBeforeMount(async () => {
    tags.value = await configStore.getSpecialTags();
    isLoaded.value = true;
  });
</script>
