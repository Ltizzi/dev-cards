<template lang="">
  <div class="flex flex-col justify-start" v-if="isLoaded">
    <div
      :class="[
        'text-center w-fit flex flex-row flex-wrap justify-start gap-2 mx-9 motion-duration-300 motion-ease-in-out ',
        props.show ? 'motion-opacity-in-0 motion-preset-slide-down-lg' : '',
        hideTags ? 'motion-opacity-out-0 -motion-translate-y-out-150' : '',
      ]"
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
  const hideTags = ref<boolean>();
  const isLoaded = ref<boolean>();

  function goToTag(tag: UITag) {
    if (!props.info) emit("update", tag);
    else router.push(`/project/tasks?tag=${tag.name}`);
  }

  function clearSearch() {
    emit("update", null);
  }

  function handleTags() {
    hideTags.value = !hideTags.value ? true : hideTags.value;
    setTimeout(() => {
      showTags.value = !showTags.value;
      hideTags.value = hideTags.value ? false : hideTags.value;
    }, 300);
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
        handleTags();
      } else handleTags();
    }
  );

  onBeforeMount(async () => {
    // tags.value = taskUtils.getTags(props.ws_id);
    // if (!tags.value) taskUtils.createTagPool(props.ws_id);
    //FIXME: volver a tags.value = await configStore.getTags(), esto es un parchepara evitar duplicados
    const unfiltered_tags = await configStore.getTags();
    let checked_tags = [] as string[];
    tags.value = unfiltered_tags
      .map((ut: UITag) => {
        if (!checked_tags.includes(ut.name.toLowerCase())) {
          checked_tags.push(ut.name);
          return ut;
        }
      })
      .filter((t: any) => t != undefined) as UITag[];

    isLoaded.value = tags.value.length > 0;
  });

  onMounted(() => {
    if (route.path == "/project/info") showTags.value = true;
  });
</script>
