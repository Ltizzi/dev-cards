<template lang="">
  <div>
    <div
      class="flex flex-row gap-5 my-5"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
    >
      <p class="text-xl underline font-semibold pb-2">Description:</p>
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
    <div
      class="flex my-5 mx-2 flex-col text-start"
      v-if="!state.showEditable"
      @mouseover="hovered = true"
      @mouseleave="hovered = false"
    >
      <p class="indent-5 text-lg whitespace-pre-line">
        {{ props.description }}
      </p>
    </div>
    <div v-else class="flex flex-col mx-10" ref="descriptionComponent">
      <textarea
        :class="[
          'textarea textarea-secondary textarea-lg  my-5',
          props.isDark
            ? 'text-base-300 bg-base-content'
            : 'bg-base-100 text-base-content',
        ]"
        :placeholder="description"
        v-model="description"
        @keydown.esc="state.showEditable = false"
        rows="5"
        cols="170"
      ></textarea>
      <button class="btn btn-secondary text-white" @click="updateDescription">
        Update
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, reactive, onBeforeMount, onUnmounted, watch } from "vue";
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { Task, Workspace } from "../../utils/types";

  const props = defineProps<{
    description: string;
    id: number;
    isDark: boolean;
    type: string;
  }>();

  const emit = defineEmits(["update"]);

  const description = ref<string>();

  const apiCall = useApiCall();

  const hovered = ref<boolean>(false);

  const editIcon = ref<HTMLElement | null>();
  const descriptionComponent = ref<HTMLElement | null>();

  const state = reactive({
    showEditable: false,
    recentChange: false,
  });

  addEventListener("keydown", (event) => {
    if (event.key == "Escape" && state.showEditable) {
      state.showEditable = false;
    }
  });

  const handleOutsideClicks = (event: MouseEvent) => {
    if (
      state.showEditable &&
      !state.recentChange &&
      descriptionComponent.value &&
      !descriptionComponent.value.contains(event.target as Node) &&
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

  async function updateDescription() {
    const newDes = { description: description.value };
    let endpoint: EndpointType;
    let params: any;
    if (props.type == "task") {
      endpoint = EndpointType.TASK_UPDATE_DESCRIPTION;
      params = {
        params: {
          task_id: props.id,
        },
      };
    } else {
      endpoint = EndpointType.WORKSPACE_UPDATE_DESCRIPTION;
      params = {
        params: {
          ws_id: props.id,
        },
      };
    }
    const response = (await apiCall.patch(endpoint, newDes, params)) as any;
    if (response.task_id == props.id || response.workspace_id == props.id) {
      emit("update", response);
    }
    state.showEditable = false;
  }

  async function updateTaskDescription() {
    const newDes = {
      description: description.value,
    };
    const response = (await apiCall.patch(
      EndpointType.TASK_UPDATE_DESCRIPTION,
      newDes,
      {
        params: {
          task_id: props.id,
        },
      }
    )) as Task;
    if (response.task_id == props.id) {
      emit("update", response);
    }
    state.showEditable = false;
  }

  watch(
    () => props.description,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        description.value = props.description;
      }
    }
  );

  onBeforeMount(() => {
    description.value = props.description;
    document.addEventListener("click", handleOutsideClicks);
  });

  onUnmounted(() => {
    document.removeEventListener("click", handleOutsideClicks);
  });
</script>
