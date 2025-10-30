<template lang="">
  <div class="form-control w-auto mt-2">
    <label class="label cursor-pointer">
      <span class="label-text mr-5">{{ props.title }}</span>
      <input
        type="checkbox"
        class="toggle toggle-primary"
        :checked="switchOnOf"
        v-model="switchOnOf"
        @change="switchToggle"
      />
    </label>
  </div>
  <div></div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";

  const props = defineProps<{
    isActive: boolean;
    title: string;
    type: string;
  }>();
  const emit = defineEmits(["update"]);

  const switchOnOf = ref<boolean>();

  function switchToggle() {
    emit("update", switchOnOf.value, props.type);
  }

  onBeforeMount(() => {
    switchOnOf.value = props.isActive;
  });
</script>
