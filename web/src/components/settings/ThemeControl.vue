<template lang="">
  <div
    class="h-full py-10 px-5 flex flex-col justify-start gap-10 overflow-y-auto"
  >
    <h1 class="text-start font-semibold text-2xl">Theme setup</h1>
    <div>
      <h2 class="text-start text-lg font-semibold pb-5">Change Theme:</h2>
      <ThemeSelector :dropdown="false" />
    </div>
    <div>
      <h2 class="text-start text-lg font-semibold pb-5">
        Change Task background color:
      </h2>
      <div class="w-60">
        <BaseToggle
          @update="handleDarker"
          :isActive="state.darkerCards"
          :title="'Darker Cards'"
          :type="'card'"
        />
      </div>
    </div>
    <div>
      <h2 class="text-start text-lg font-semibold pb-5">
        Change Thumbnails background color:
      </h2>
      <div class="w-60">
        <BaseToggle
          @update="handleDarker"
          :isActive="state.darkerMiniCards"
          :title="'Darker Thumbnails'"
          :type="'minicard'"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useUIStore } from "../../store/ui.store";
  import BaseToggle from "../common/BaseToggle.vue";
  import ThemeSelector from "./ThemeSelector.vue";

  import { reactive, onBeforeMount } from "vue";

  const UIStore = useUIStore();

  const state = reactive({
    darkerCards: false,
    darkerMiniCards: false,
  });

  function handleDarker(value: boolean, type?: string) {
    if (type && type == "card") {
      UIStore.setDarkerCards(value);
    } else if (type && type == "minicard") {
      UIStore.setDarkerMiniCards(value);
    }
  }

  onBeforeMount(() => {
    state.darkerCards = UIStore.darkerCard;
    state.darkerMiniCards = UIStore.darkerMiniCard;
  });
</script>
<style lang=""></style>
