<template lang="">
  <div class="fixed right-0 top-1/3">
    <ul
      class="menu bg-accent hover:bg-base-300 gap-3 rounded-box translate-x-16 hover:translate-x-0 transition-all"
    >
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Assign User"
        @click="modalSwitch('addUser', true)"
        v-if="isModOrOwner"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'user-plus']" />
        <AddUserModal
          :showModal="modalState.addUser"
          @close="modalSwitch('addUser', false)"
        ></AddUserModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Remove User"
        @click="modalSwitch('removeUser', true)"
        v-if="isModOrOwner"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'user-minus']" />
        <RemoveUserModal
          :showModal="modalState.removeUser"
          @close="modalSwitch('removeUser', false)"
        ></RemoveUserModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add tag"
        @click="modalSwitch('addTag', true)"
        v-if="isModOrOwner || isDesignatedUser"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'circle-plus']" />
        <AddTagModal
          :showModal="modalState.addTag"
          @close="modalSwitch('addTag', false)"
        ></AddTagModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add dependency"
        @click="modalSwitch('addDependency', true)"
        v-if="isModOrOwner || isDesignatedUser"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'folder-tree']" />
        <AddDependencyModal
          :showModal="modalState.addDependency"
          @close="modalSwitch('addDependency', false)"
        ></AddDependencyModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add update"
        @click="modalSwitch('addUpdate', true)"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'pen-to-square']" />
        <AddTaskUpdateModal
          :showModal="modalState.addUpdate"
          @close="modalSwitch('addUpdate', false)"
        ></AddTaskUpdateModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-error hover:rounded-lg transition-all"
        data-tip="Delete Task"
        @click="modalSwitch('deleteTask', true)"
        v-if="isModOrOwner"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'trash']" />
        <DeleteTaskModal
          :showModal="modalState.deleteTask"
          @close="modalSwitch('deleteTask', false)"
        ></DeleteTaskModal>
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, reactive, ref } from "vue";
  import AddUserModal from "../ui/AddUserModal.vue";
  import RemoveUserModal from "../ui/RemoveUserModal.vue";
  import AddTagModal from "../ui/AddTagModal.vue";
  import AddDependencyModal from "../ui/AddDependencyModal.vue";
  import AddTaskUpdateModal from "../ui/AddTaskUpdateModal.vue";
  import DeleteTaskModal from "../ui/DeleteTaskModal.vue";
  import { checkIsDesignated, checkIsModOrOwner } from "../../utils/auth.utils";
  import { useProjectStore } from "../../store/project.store";
  import { useTaskStore } from "../../store/task.store";

  const props = defineProps<{ projectId: number; taskId: number }>();

  const modalState = reactive({
    addUser: false,
    removeUser: false,
    addTag: false,
    addDependency: false,
    addUpdate: false,
    deleteTask: false,
  });

  const isModOrOwner = ref<boolean>();
  const isDesignatedUser = ref<boolean>();

  const projectStore = useProjectStore();
  const taskStore = useTaskStore();

  const emit = defineEmits(["update"]);

  function modalSwitch(modal: string, condition: boolean) {
    switch (modal) {
      case "addUser":
        modalState.addUser = condition;
        modalState.removeUser = false;
        modalState.addTag = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        modalState.deleteTask = false;
        break;
      case "removeUser":
        modalState.removeUser = condition;
        modalState.addUser = false;
        modalState.addTag = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        modalState.deleteTask = false;
        break;
      case "addTag":
        modalState.addTag = condition;
        modalState.removeUser = false;
        modalState.addUser = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        modalState.deleteTask = false;
        break;
      case "addDependency":
        modalState.addDependency = condition;
        modalState.addTag = false;
        modalState.removeUser = false;
        modalState.addUser = false;
        modalState.addUpdate = false;
        modalState.deleteTask = false;
        break;
      case "addUpdate":
        modalState.addUpdate = condition;
        modalState.addDependency = false;
        modalState.addTag = false;
        modalState.removeUser = false;
        modalState.addUser = false;
        modalState.deleteTask = false;
        emit("update");
        break;
      case "deleteTask":
        modalState.deleteTask = condition;
        modalState.addUpdate = false;
        modalState.addDependency = false;
        modalState.addTag = false;
        modalState.removeUser = false;
        modalState.addUser = false;
        break;
    }
  }

  onBeforeMount(() => {
    const projectId = props.projectId
      ? props.projectId
      : projectStore.current.workspace_id;
    const taskId = props.taskId ? props.taskId : taskStore.currentTask.task_id;
    isModOrOwner.value = checkIsModOrOwner(projectId);
    isDesignatedUser.value = checkIsDesignated(projectId, taskId as number);
  });
</script>
