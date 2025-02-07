<template lang="">
  <div
    class="my-14 flex flex-col gap-5 justify-center motion-duration-300 motion-preset-fade-lg"
  >
    <h1 class="text-3xl text-start font-bold mb-14">Workspace details</h1>

    <label
      class="input input-bordered input-primary lg:input-md input-sm lg:text-base text-xs flex items-center gap-2 lg:w-4/6 my-auto max-w-3xl"
    >
      Project name:
      <input
        type="text"
        class="grow"
        v-model="newName"
        :placeholder="project.project_name"
      />
      <button
        class="bg-primary pt-1 hover:scale-105 hover:bg-secondary transition-all ease-in-out duration-150 px-1.5 rounded-xl shadow-md shadow-base-300"
        @click="changeName"
      >
        <font-awesome-icon
          class="lg:text-2xl text-base"
          :icon="['fas', 'square-check']"
        />
      </button>
    </label>
    <div class="flex lg:flex-row flex-col justify-between h-auto gap-40 w-full">
      <label
        class="input input-bordered input-primary lg:input-md input-sm lg:text-base text-xs flex items-center gap-2 lg:w-4/6 my-auto max-w-3xl"
      >
        Avatar URL:
        <input
          type="text"
          class="grow"
          v-model="avatarUrl"
          :placeholder="project.avatarUrl"
        />
        <button
          class="bg-primary pt-1 hover:scale-105 hover:bg-secondary transition-all ease-in-out duration-150 px-1.5 rounded-xl shadow-md shadow-base-300"
          @click="changeAvatar"
        >
          <font-awesome-icon
            class="lg:text-2xl text-base"
            :icon="['fas', 'square-check']"
          />
        </button>
      </label>
      <div class="avatar w-2/6 -mt-36" v-if="avatarUrl">
        <div
          class="ring-primary ring-offset-base-100 w-52 rounded-full ring ring-offset-2"
        >
          <img :src="avatarUrl" class="w-full" />
        </div>
      </div>
    </div>

    <div class="w-11/12">
      <BaseEditDescription
        :description="project.description"
        :id="project.workspace_id"
        :isDark="isDark"
        :type="workspace"
        :canModify="true"
        @update="updateProject"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { useProjectStore } from "../../store/project.store";
  import { Workspace } from "../../utils/types";
  import BaseEditDescription from "../common/BaseEditDescription.vue";
  import { onBeforeMount, ref } from "vue";

  const props = defineProps<{ project: Workspace }>();

  const emit = defineEmits(["update"]);

  const projectStore = useProjectStore();

  const project = ref<Workspace>();

  const newName = ref<string>();
  const avatarUrl = ref<string>();

  async function changeName() {
    const response = (await projectStore.updateWorkspaceName(
      project.value?.workspace_id as number,
      newName.value as string
    )) as Workspace;

    if (response.workspace_id == project.value?.workspace_id) {
      //projectStore.setCurrent(response);
      project.value = response;
      emit("update", response);
    }
  }

  async function changeAvatar() {
    const response = (await projectStore.updateWorkspaceAvatar(
      project.value?.workspace_id as number,
      avatarUrl.value as string
    )) as Workspace;

    console.log(response.workspace_id);
    console.log(response.workspace_id == project.value?.workspace_id);
    if (response.workspace_id == project.value?.workspace_id) {
      //projectStore.setCurrent(response);
      project.value = response;
      console.log("updating");
      emit("update", response);
    }
  }

  function updateProject(ws: Workspace) {
    project.value = ws;
    emit("update", ws);
  }

  onBeforeMount(() => {
    project.value = props.project;
  });
</script>
