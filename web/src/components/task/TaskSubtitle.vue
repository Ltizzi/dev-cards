<template lang="">
  <div>
    <div
      class="flex flex-row justify-start gap-5 w-full"
      v-if="!state.showEditable"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
    >
      <h2 class="text-2xl text-start">{{ props.subtitle }}</h2>
      <font-awesome-icon
        :class="[
          'my-auto size-7 hover:cursor-pointer',
          hovered ? 'animate-pulse duration-150 text-secondary' : '',
        ]"
        :icon="['fas', 'pen-to-square']"
        v-show="hovered"
        @click="showEditable"
        ref="editIcon"
      />
    </div>
    <label
      class="input input-bordered input-secondary flex items-center gap-2 w-full mx-auto bg-white"
      v-else
    >
      Subtitle
      <input
        type="text"
        class="grow"
        v-model="subtitle"
        :placeholder="subtitle"
        @keydown.esc="state.showEditable = false"
        @keydown.enter="updateSubtitle"
        ref="subtitleComponent"
      />
    </label>
  </div>
</template>
<script setup lang="ts">
  import { ref, reactive, onBeforeMount, onUnmounted } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { Task } from "../../utils/types";

  const props = defineProps<{ subtitle: string; task_id: number }>();
  const emit = defineEmits(["update"]);

  const subtitle = ref<string>();

  const apiCall = useApiCall();

  const hovered = ref<boolean>(false);

  const editIcon = ref<HTMLElement | null>();
  const subtitleComponent = ref<HTMLElement | null>();

  const state = reactive({
    showEditable: false,
    recentCHange: false,
  });

  addEventListener("keydown", (event) => {
    if (event.key == "Escape" && state.showEditable) {
      state.showEditable = false;
    }
  });

  const handeOutsideClicks = (event: MouseEvent) => {
    if (
      state.showEditable &&
      !state.recentCHange &&
      subtitleComponent.value &&
      !subtitleComponent.value.contains(event.target as Node) &&
      !editIcon.value?.contains(event.target as Node)
    ) {
      state.showEditable = false;
      hovered.value = false;
    }
  };

  function showEditable() {
    state.showEditable = true;
    state.recentCHange = true;
    setTimeout(() => {
      state.recentCHange = false;
    }, 50);
  }

  async function updateSubtitle() {
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_SUBTITLE,
      {},
      {
        params: {
          task_id: props.task_id,
          subtitle: subtitle.value,
        },
      }
    )) as Task;
    if (response.task_id == props.task_id) {
      emit("update", response);
    }
    state.showEditable = false;
  }

  onBeforeMount(() => {
    subtitle.value = props.subtitle;
    document.addEventListener("click", handeOutsideClicks);
  });

  onUnmounted(() => {
    document.removeEventListener("click", handeOutsideClicks);
  });
</script>