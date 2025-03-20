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
                <p class="w-auto">{{ glosary.name }}</p>
              </td>
              <td v-if="!isMobile && checkWindowWidth()">
                <p class="w-auto">{{ glosary.items.length }}</p>
              </td>

              <td class="w-auto flex justify-center">
                <div class="tooltip tooltip-info">
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
                    @click="removeGlosary(glosary.id)"
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
    <AddNewGlosaryModal
      :showModal="state.showNewModal"
      @close="closeModal('new')"
      :config_id="state.config_id"
      :ws_id="state.ws_id"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref, reactive } from "vue";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import AddNewGlosaryModal from "../ui/customConfiguration/AddNewGlosaryModal.vue";
  import { Glosary } from "../../utils/types";
  import { useConfigStore } from "../../store/config.store";

  const props = defineProps<{ ws_id: number }>();

  const isLoaded = ref<boolean>(false);
  const isMobile = ref<boolean>(false);
  const showDelModal = ref<boolean>(false);
  const id_to_delete = ref<number>(0);

  const state = reactive({
    showDelModal: false,
    showNewModal: false,
    config_id: 0,
    ws_id: 0,
  });

  const configStore = useConfigStore();

  const glosary_list = ref<Glosary[]>();

  function editGlosary(id: number) {}

  function removeGlosary(id: number) {
    id_to_delete.value = id;
    showDelModal.value = true;
  }

  function closeModal(modal: string) {
    switch (modal) {
      case "del":
        state.showDelModal = false;
        break;
      case "new":
        state.showNewModal = false;
        break;
    }
  }

  function newGlosary() {
    state.showNewModal = true;
  }

  function checkWindowWidth() {}

  onBeforeMount(async () => {
    const config = await configStore.getCurrent();
    state.config_id = config.config_id;
    state.ws_id = config.workspace.workspace_id;
    glosary_list.value = await configStore.getGLosaries();
    console.log("FROM GLOSARY CONTROL");
    console.log(state.config_id);
    console.log(state.ws_id);
    console.log(glosary_list.value);
    isLoaded.value = true;
  });
</script>
