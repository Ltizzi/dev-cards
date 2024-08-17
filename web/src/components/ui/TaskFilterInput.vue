<template lang="">
  <div>
    <label
      :class="['input input-bordered flex items-center gap-2 input-secondary ']"
    >
      <!-- bg-gradient-to-r from-0% via-transparent via-50%  to-100%',
        isDark ? 'from-neutral to-neutral' : 'from-base-300 to-base-300' -->
      Search
      <input
        type="text"
        class="grow"
        placeholder="by Tag or Title/subtitle"
        v-model="search"
      />
    </label>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { checkThemeIsDark } from "../../utils/client.utils";

  const isDark = ref<boolean>();

  const search = ref<string>();

  const emit = defineEmits(["search"]);

  watch(
    () => search.value,
    (newValue, oldValue) => {
      emit("search", newValue);
    }
  );

  onBeforeMount(() => {
    isDark.value = checkThemeIsDark();
  });
</script>
