<template>
  <div
    :class="['px-3 py-2 rounded-md text-xl font-semibold shadow-md', getColor(tag?.color as Color)]"
    v-if="tag"
  >
    {{ tag.name }}
  </div>
</template>

<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { taskUtils } from "../../../utils/task.utils";
  import { Color } from "../../../utils/types";
  const props = defineProps<{
    tag: string;
  }>();

  interface STagUtil {
    id: string;
    name: string;
    color: string;
  }

  const tag = ref<STagUtil>();

  const getColor = (color: Color) => taskUtils.getColor(color);

  onBeforeMount(() => {
    tag.value = taskUtils.getSpecialTagAsObject(props.tag);
  });
</script>
