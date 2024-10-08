<template lang="">
  <div class="flex flex-col justify-start" v-if="isLoaded">
    <div
      class="text-center w-fit flex flex-row flex-wrap justify-start gap-2 mx-9"
    >
      <div v-for="tag in tags" v-if="showTags">
        <p
          :class="[
            'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 hover:cursor-pointer',
            props.show ? 'hover:bg-error' : '',
            `${tag.color}`,
          ]"
          @click="goToTag(tag)"
        >
          {{ tag.name }}
        </p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, onBeforeMount, onMounted, watch } from "vue";
  import { useRoute, useRouter } from "vue-router";
  import { UITag } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";
  import { useConfigStore } from "../../store/config.store";

  const props = defineProps<{ ws_id: number; info?: boolean; show: boolean }>();

  const emit = defineEmits(["update"]);

  const configStore = useConfigStore();

  const router = useRouter();

  const route = useRoute();

  const tags = ref<UITag[]>();
  const showTags = ref<boolean>();
  const isLoaded = ref<boolean>();

  function goToTag(tag: UITag) {
    if (!props.info) emit("update", tag);
    else router.push(`/project/tasks?tag=${tag.name}`);
  }

  function clearSearch() {
    emit("update", null);
  }

  function handleTags() {
    showTags.value = !showTags.value;
  }

  watch(
    () => props.ws_id,
    async (newValue, oldValue) => {
      if (newValue != oldValue) {
        tags.value = await configStore.getTags(); //taskUtils.getTags(props.ws_id) as UITag[];

        isLoaded.value = tags.value.length > 0;
      }
    }
  );
  watch(
    () => props.show,
    (newValue, oldValue) => {
      if (newValue) {
        showTags.value = true;
      } else showTags.value = false;
    }
  );

  onBeforeMount(async () => {
    // tags.value = taskUtils.getTags(props.ws_id);
    // if (!tags.value) taskUtils.createTagPool(props.ws_id);
    tags.value = await configStore.getTags();

    isLoaded.value = tags.value.length > 0;
  });

  onMounted(() => {
    if (route.path == "/project/info") showTags.value = true;
  });
</script>
