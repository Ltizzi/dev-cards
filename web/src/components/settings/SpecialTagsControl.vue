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
              <th class="text-start" v-if="!isMobile && checkWindowWidth()">
                Value
              </th>
              <th class="text-start" v-if="!isMobile && checkWindowWidth()">
                Description
              </th>
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
                <p class="w-auto">{{ getDescription(tag.description) }}</p>
              </td>

              <td class="w-auto flex justify-center">
                <div class="tooltip tooltip-info flex flex-row gap-3">
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="editTag(tag)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      class="size-4 mt-3 text-success"
                    />
                  </div>
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="removeTag(tag)"
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
    <SpecialTagModalEditor
      :showModal="state.showEditorModal"
      :config_id="state.config_id"
      :ws_id="state.ws_id"
      :isEditor="state.isEditor"
      :tagToEdit="state.tagToEdit"
      @close="closeModal('editor')"
      @updateList="updateList"
    />
    <BaseDeleteModal
      :id="target_id"
      :type="'special Tag'"
      :showModal="state.showDelModal"
      @cancel="closeModal('del')"
      @delete="removeTag"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, reactive } from "vue";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import SpecialTagModalEditor from "../ui/customConfiguration/SpecialTagModalEditor.vue";

  import { useConfigStore } from "../../store/config.store";
  import { SpecialTag } from "../../utils/types";
  import { useUIStore } from "../../store/ui.store";
  import { utils } from "../../utils/utils";

  const props = defineProps<{ ws_id: number }>();
  const emit = defineEmits(["update"]);

  const isLoaded = ref<boolean>(false);
  const isMobile = ref<boolean>(false);

  const state = reactive({
    showDelModal: false,
    showEditorModal: false,
    target_id: 0,
    config_id: 0,
    ws_id: 0,
    isEditor: false,
    tagToEdit: {} as SpecialTag | undefined,
  });

  const configStore = useConfigStore();
  const UIStore = useUIStore();

  const tags_list = ref<SpecialTag[]>();

  function editTag(tag: SpecialTag) {
    state.isEditor = true;
    state.showEditorModal = true;
    state.tagToEdit = tag;
  }

  function removeTag(tag: SpecialTag) {
    state.target_id = tag.id as number;
    state.showDelModal = true;
  }

  function closeModal(type: string) {
    switch (type) {
      case "del":
        state.showDelModal = false;
        state.target_id = 0;
        break;
      case "editor":
        state.showEditorModal = false;
        state.isEditor = false;
        state.tagToEdit = undefined;
        break;
    }
    updateList();
  }

  function newTag() {
    state.isEditor = false;
    state.showEditorModal = true;
    state.tagToEdit = undefined;
  }

  function updateList() {
    tags_list.value = configStore.current.tagPool.specialTags;
    emit("update");
  }

  function checkWindowWidth() {
    return window.innerWidth > 1280;
  }

  function getDescription(desc: string) {
    const LENGTH = 20;
    return desc.length < LENGTH ? desc : utils.textReduce(desc, LENGTH);
  }

  onBeforeMount(async () => {
    isMobile.value = UIStore.isMobile;
    const config = await configStore.getCurrent();
    tags_list.value = config.tagPool.specialTags;
    state.config_id = config.config_id;
    state.ws_id = config.workspace.workspace_id;
    updateList();
    isLoaded.value = true;
  });
</script>
