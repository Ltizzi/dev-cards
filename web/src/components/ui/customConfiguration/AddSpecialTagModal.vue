<template>
  <BaseModal :is-active="showModal" v-if="showModal" @closeModal="closeModal">
    <div
      class="py-5 px-10 rounded-md min-w-96 flex flex-col text-center justify-center gap-5"
    >
      <h1 class="text-3xl font-semibold">Add Special Tag</h1>
      <div class="flex flex-col gap-5" v-if="avaibleSpecialTags">
        <p class="font-extralight">
          Click and pick one or more Special Tag to add/remove to the current
          Task
        </p>
        <ul class="mx-5 flex flex-row justify-center">
          <li v-for="tag in tags">
            <SpecialTagElement
              :added="checkAlreadyAdded(tag.value)"
              :tag="tag.value"
              :class="[
                'hover:cursor-pointer rounded-md border-2',
                checkAlreadyAdded(tag.value)
                  ? ' border-success '
                  : ' border-transparent',
              ]"
              @click="addOrRemove(tag.value)"
            />
          </li>
        </ul>
      </div>
      <div v-else>
        <p class="font-extralight">Workspace has not Special Tags</p>
      </div>

      <div class="flex flex-row gap-2 justify-end pt-2 pb-2">
        <button
          class="btn btn-error btn-sm font-bold text-neutral-content"
          @click="closeModal"
        >
          Close
        </button>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, watch } from "vue";
  import { useConfigStore } from "../../../store/config.store";
  import BaseModal from "../../common/BaseModal.vue";
  import SpecialTagElement from "./SpecialTagElement.vue";
  import { SpecialTag, Task } from "../../../utils/types";
  import { useTaskStore } from "../../../store/task.store";
  import { taskUtils } from "../../../utils/task.utils";

  const tags = ref<SpecialTag[]>([]);

  const added_tags = ref<string[]>([]);

  const avaibleSpecialTags = ref<boolean>(false);

  const props = defineProps<{
    showModal: boolean;
  }>();

  const emit = defineEmits(["update", "close"]);

  const configstore = useConfigStore();
  const taskStore = useTaskStore();

  function checkAlreadyAdded(tag: string) {
    if (added_tags.value && added_tags.value.length > 0)
      return (
        added_tags.value.filter(
          (t: string) => t.toLowerCase() == tag.toLowerCase()
        ).length > 0
      );
    else false;
  }

  async function addOrRemove(tag: string) {
    if (checkAlreadyAdded(tag)) {
      added_tags.value = added_tags.value.filter(
        (t: string) => t.toLowerCase() != tag.toLowerCase()
      );
      const response = await taskStore.removeTag(tag);
      await prepareData();
    } else {
      added_tags.value.push(tag);
      const response = (await taskStore.saveTag(tag)) as Task;

      const tags = response.task_tags?.filter(
        (t: string) => t.toLowerCase() == tag.toLowerCase()
      );

      if (tags && tags.length > 0) {
        await prepareData();
      }
    }
    emit("update");
  }

  function closeModal() {
    console.log("AHRE");
    emit("close");
  }

  watch(
    () => configstore.current.tagPool.specialTags,
    async (newValue, oldValue) => {
      if (newValue != oldValue) {
        await prepareData();
      }
    }
  );
  watch(
    () => taskStore.currentTask.task_tags,
    async (newValue, oldValue) => {
      if (newValue != oldValue) await prepareData();
    }
  );

  async function prepareData() {
    tags.value = configstore.current.tagPool.specialTags;
    if (tags.value.length > 0) avaibleSpecialTags.value = true;
    const task_tags = (await taskStore.getCurrent().task_tags) as string[];
    if (task_tags && task_tags.length > 0)
      added_tags.value = await taskUtils.parseAndGetSpecialTags(task_tags);
  }

  onBeforeMount(async () => {
    await prepareData();
  });
</script>
