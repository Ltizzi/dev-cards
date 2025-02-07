import { Glosary } from '../../utils/types';
<template lang="">
  <div
    class="flex flex-col justify-center gap-10 py-10 px-10 motion-duration-300 motion-preset-fade-lg"
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
      :showModal="showDelModal"
      @cancel="closeDelModal"
      @delete="removeGlosary"
    />
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import BaseDeleteModal from "../common/BaseDeleteModal.vue";
  import { Glosary } from "../../utils/types";
  import { useConfigStore } from "../../store/config.store";

  const props = defineProps<{ ws_id: number }>();

  const isLoaded = ref<boolean>(false);
  const isMobile = ref<boolean>(false);
  const showDelModal = ref<boolean>(false);
  const id_to_delete = ref<number>();

  const configStore = useConfigStore();

  const glosary_list = ref<Glosary[]>();

  function editGlosary(id: number) {}

  function removeGlosary(id: number) {
    id_to_delete.value = id;
    showDelModal.value = true;
  }

  function closeDelModal() {
    showDelModal.value = false;
  }

  function newGlosary() {}

  function checkWindowWidth() {}

  onBeforeMount(async () => {
    glosary_list.value = await configStore.getGLosaries();
  });
</script>
