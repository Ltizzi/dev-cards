<template>
  <div
    :class="[
      'px-3 py-2 rounded-lg text-xl font-semibold shadow-md border-double border-4 border-neutral-content transition-all duration-75 hover:border-neutral hover:cursor-pointer',
      `from-${color}-400 via-${color}-600 to-${color}-950 via  bg-gradient-to-br`,
      fromControl
        ? added
          ? 'hover:bg-error hover:text-error-content'
          : 'hover:bg-success hover:text-success-content'
        : '',
    ]"
    v-if="tag"
  >
    {{ tag.name.toUpperCase() }}
    <!-- getColor(tag?.color as Color) -->
  </div>
</template>

<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { taskUtils } from "../../../utils/task.utils";
  import { Color } from "../../../utils/types";
  const props = defineProps<{
    tag: string;
    added?: boolean;
    fromControl: boolean;
  }>();

  interface STagUtil {
    id: string;
    name: string;
    color: string;
  }

  const tag = ref<STagUtil>();

  const color = ref<string>("");

  const getColor = (color: Color) => taskUtils.getColor(color);

  onBeforeMount(() => {
    tag.value = taskUtils.getSpecialTagAsObject(props.tag);
    if (tag.value) color.value = getColor(tag.value.color as Color);
  });
</script>
