<template lang="">
  <div
    class="flex flex-col justify-center gap-10 py-10 px-10 motion-duration-300 motion-preset-fade-lg"
  >
    <h1 class="text-start text-2xl">Workspace's Special Tags</h1>

    <button class="btn btn-primary text-white w-36" @click="newTag">
      Create Tag
    </button>
    <div class="flex flex-col gap-5">
      <div class="overflow-x-auto">
        <table class="table">
          <!-- head -->
          <thead>
            <tr>
              <th class="text-center" v-if="!isMobile && checkWindowWidth()">
                Id
              </th>
              <th>Name</th>
              <th class="text-center">Value</th>
              <th class="text-center">Description</th>
              <th class="text-center">Control</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="tag in tags_list">
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="text-center w-auto">{{ tag.id }}</p>
              </td>
              <td>
                <p class="w-auto">{{ tag.name }}</p>
              </td>
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="w-auto">{{ tag.value }}</p>
              </td>
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="w-auto">{{ tag.description }}</p>
              </td>

              <td class="w-auto flex justify-center">
                <div class="tooltip tooltip-info">
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="editGlosary(tag.id)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      class="size-4 mt-3 text-success"
                    />
                  </div>
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="removeGlosary(tag.id)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-minus']"
                      class="size-4 mt-3 text-error"
                    />
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <BaseDeleteModal
      :id="target_id"
      :type="'special Tag'"
      :showModal="showDelModal"
      @cancel="closeDelModal"
      @delete="removeTag"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";

  import { useConfigStore } from "../../store/config.store";
  import { SpecialTag } from "../../utils/types";

  const props = defineProps<{ ws_id: number }>();

  const isLoaded = ref<boolean>(false);
  const isMobile = ref<boolean>(false);
  const showDelModal = ref<boolean>(false);
  const target_id = ref<number>();

  const configStore = useConfigStore();

  const tags_list = ref<SpecialTag[]>();

  function editTag(id: number) {}

  function removeTag(id: number) {
    target_id.value = id;
    showDelModal.value = true;
  }

  function closeDelModal() {
    target_id.value = 0;
    showDelModal.value = false;
  }

  function newTag() {}

  function checkWindowWidth() {}

  onBeforeMount(async () => {
    tags_list.value = await configStore.getSpecialTags();
  });
</script>
