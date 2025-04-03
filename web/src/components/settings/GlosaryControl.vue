import { Glosary } from '../../utils/types';
<template lang="">
  <div
    class="flex flex-col justify-center gap-10 py-10 px-10 motion-duration-300 motion-preset-fade-lg"
    v-if="isLoaded"
  >
    <h1 class="text-start text-2xl">Workspace's custom glosaries</h1>

    <button class="btn btn-primary text-white w-36" @click="newGlosary">
      Create Glosary
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
              <th>Type</th>
              <th class="text-center">Total items</th>
              <th class="text-center">Control</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="glosary in glosary_list">
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="text-center w-auto">{{ glosary.id }}</p>
              </td>
              <td>
                <p class="w-auto">{{ glosary.type }}</p>
              </td>
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="w-auto text-center">{{ glosary.items.length }}</p>
              </td>

              <td class="flex justify-center">
                <div class="tooltip tooltip-info flex flex-row gap-5">
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="editGlosary(glosary.id)"
                  >
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      class="size-4 mt-3 text-success"
                    />
                  </div>
                  <div
                    class="hover:cursor-pointer duration-100 hover:scale-110 ease-in-out transition-all"
                    @click="showDeleteModal(glosary.id)"
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
      :id="id_to_delete"
      :type="'custom glosary'"
      :showModal="state.showDelModal"
      @cancel="closeModal('del')"
      @delete="removeGlosary"
    />
    <GlosaryModalEditor
      :showModal="state.showEditorModal"
      @close="closeModal('editor')"
      @updateList="updateList(list)"
      :config_id="state.config_id"
      :ws_id="state.ws_id"
      :isEditor="state.isEditor"
      :glosaryToEdit="state.glosaryToEdit"
      :glosaries="state.glosaries"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, reactive } from "vue";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import GlosaryModalEditor from "../ui/customConfiguration/GlosaryModalEditor.vue";
  import { Glosary } from "../../utils/types";
  import { useConfigStore } from "../../store/config.store";
  import { useUIStore } from "../../store/ui.store";

  const props = defineProps<{ ws_id: number }>();
  const emit = defineEmits(["update"]);

  const isLoaded = ref<boolean>(false);
  const isMobile = ref<boolean>(false);
  const id_to_delete = ref<number>(0);

  const state = reactive({
    showDelModal: false,
    showEditorModal: false,
    config_id: 0,
    ws_id: 0,
    isEditor: false,
    glosaryToEdit: {} as Glosary | undefined,
    glosaries: [] as Glosary[],
  });

  const configStore = useConfigStore();
  const UIStore = useUIStore();

  const glosary_list = ref<Glosary[]>();

  function editGlosary(id: number) {
    state.isEditor = true;
    state.showEditorModal = true;
    state.glosaryToEdit = glosary_list.value
      ?.filter((g: Glosary) => g.id == id)
      .at(0) as Glosary;
  }

  async function closeModal(modal: string) {
    switch (modal) {
      case "del":
        state.showDelModal = false;
        break;
      case "editor":
        state.showEditorModal = false;
        state.isEditor = false;
        state.glosaryToEdit = undefined;
        break;
    }
    updateList();
  }

  function showDeleteModal(id: number) {
    id_to_delete.value = id;
    state.showDelModal = true;
  }

  async function removeGlosary(id: number) {
    const glosaries = glosary_list.value?.filter(
      (g: Glosary) => g.id == id
    ) as Glosary[];
    const glosary = glosaries[0];
    const response = (await configStore.removeGlosary(
      state.ws_id,
      state.config_id,
      glosary
    )) as Glosary[];
    updateList();
    if (response.length > 0) {
      state.showDelModal = false;
    } else {
      state.showDelModal = false;
      console.error("ERROR WHEN REMOVING CUSTOM GLOSARY");
    }
  }

  function newGlosary() {
    state.isEditor = false;
    state.showEditorModal = true;
    state.glosaryToEdit = undefined;
  }

  function updateList() {
    glosary_list.value = configStore.current.customGlosaries;
    state.glosaries = glosary_list.value;
    emit("update");
  }

  function checkWindowWidth() {
    return window.innerWidth > 1600;
  }

  onBeforeMount(async () => {
    isMobile.value = UIStore.isMobile;
    const config = await configStore.getCurrent();
    state.config_id = config.config_id;
    state.ws_id = config.workspace.workspace_id;
    updateList();
    isLoaded.value = true;
  });
</script>
