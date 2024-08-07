<template lang="">
  <div>
    <div
      class="flex flex-row relative w-full justify-center"
      v-if="!state.showEditable"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
    >
      <h1 class="text-3xl py-2 font-bold" ref="card_title">
        {{ props.title }}
      </h1>
      <font-awesome-icon
        :class="[
          'absolute right-10 top-3 size-7 hover:cursor-pointer',
          hovered ? 'animate-pulse duration-150 text-secondary' : '',
        ]"
        :icon="['fas', 'pen-to-square']"
        v-show="hovered"
        @click="showEditable"
        ref="editIcon"
      />
    </div>

    <label
      class="input input-bordered input-secondary flex items-center gap-2 w-5/6 mx-auto bg-white"
      v-else
    >
      Title
      <input
        type="text"
        :class="[
          'grow',
          props.isDark
            ? 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content',
        ]"
        v-model="title"
        :placeholder="title"
        @keydown.esc="state.showEditable = false"
        @keydown.enter="updateTitle"
        ref="titleComponent"
      />
    </label>
  </div>
</template>
<script setup lang="ts">
  import { ref, reactive, defineProps, onBeforeMount, onUnmounted } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { Task } from "../../utils/types";

  const props = defineProps<{
    title: string;
    task_id: number;
    isDark: boolean;
  }>();
  const emit = defineEmits(["update"]);

  const title = ref<string>();

  const apiCall = useApiCall();

  const hovered = ref<boolean>(false);

  const state = reactive({
    showEditable: false,
    recentChange: false,
  });

  const titleComponent = ref<HTMLElement | null>();
  const editIcon = ref<HTMLElement | null>();

  addEventListener("keydown", (event) => {
    if (event.key == "Escape" && state.showEditable) {
      state.showEditable = false;
    }
  });

  const handleOutsideClicks = (event: MouseEvent) => {
    if (
      state.showEditable &&
      !state.recentChange &&
      titleComponent.value &&
      !titleComponent.value.contains(event.target as Node) &&
      !editIcon.value?.contains(event.target as Node)
    ) {
      state.showEditable = false;
      hovered.value = false;
    }
  };

  function showEditable() {
    state.showEditable = true;
    state.recentChange = true;
    setTimeout(() => {
      state.recentChange = false;
    }, 50);
  }

  async function updateTitle() {
    if (title.value && title.value?.length > 0) {
      const response = (await apiCall.patch(
        EndpointType.TASK_UPDATE_TITLE,
        {},
        {
          params: {
            task_id: props.task_id,
            title: title.value,
          },
        }
      )) as Task;
      if (response.task_id == props.task_id) {
        emit("update", response);
      }
      state.showEditable = false;
    }
  }

  onBeforeMount(() => {
    title.value = props.title;
    document.addEventListener("click", handleOutsideClicks);
  });

  onUnmounted(() => {
    document.removeEventListener("click", handleOutsideClicks);
  });
</script>
