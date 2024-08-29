<template lang="">
  <div class="text-center w-screen">
    <div class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto">
      <div v-for="tag in tags">
        <p
          :class="[
            'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 hover:cursor-pointer',
            removeTagActive ? 'hover:bg-error' : '',
            `${tag.color}`,
          ]"
          @click="goToTag(tag.name)"
        >
          {{ tag.name }}
        </p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, onBeforeMount } from "vue";
  import { useRoute } from "vue-router";
  import { UITag } from "../utils/types";
  import { useProjectStore } from "../store/project.store";
  import { taskUtils } from "../utils/task.utils";

  const props = defineProps<{ ws_id: number }>();

  const emit = defineEmits(["update"]);

  const route = useRoute();

  const projectStore = useProjectStore();

  const tags = ref<UITag[]>();

  function goToTag(tag: UITag) {
    emit("update", tag);
  }

  onBeforeMount(() => {
    tags.value = taskUtils.getTags(props.ws_id);
  });
</script>
