<template lang="">
  <div class="fixed right-0 top-1/3">
    <ul
      class="menu bg-accent hover:bg-base-300 gap-3 rounded-box translate-x-16 hover:translate-x-0 transition-all"
    >
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Assign User"
        @click="modalSwitch('addUser')"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'user-plus']" />
        <AddUserModal
          :showModal="modalState.addUser"
          @close="modalSwitch('addUser')"
        ></AddUserModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Remove User"
        @click="modalSwitch('removeUser')"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'user-minus']" />
        <RemoveUserModal
          :showModal="modalState.removeUser"
          @close="modalSwitch('removeUser')"
        ></RemoveUserModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add tag"
        @click="modalSwitch('addTag')"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'circle-plus']" />
        <AddTagModal
          :showModal="modalState.addTag"
          @close="modalSwitch('addTag')"
        ></AddTagModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add dependency"
        @click="modalSwitch('addDependency')"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'folder-tree']" />
        <AddDependencyModal
          :showModal="modalState.addDependency"
          @close="modalSwitch('addDependency')"
        ></AddDependencyModal>
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add update"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'pen-to-square']" />
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
  import { reactive } from "vue";
  import AddUserModal from "../ui/AddUserModal.vue";
  import RemoveUserModal from "../ui/RemoveUserModal.vue";
  import AddTagModal from "../ui/AddTagModal.vue";
  import AddDependencyModal from "../ui/AddDependencyModal.vue";

  const modalState = reactive({
    addUser: false,
    removeUser: false,
    addTag: false,
    addDependency: false,
    addUpdate: false,
  });

  function modalSwitch(modal: string) {
    switch (modal) {
      case "addUser":
        modalState.addUser = !modalState.addUser;
        modalState.removeUser = false;
        modalState.addTag = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        break;
      case "removeUser":
        modalState.removeUser = !modalState.removeUser;
        modalState.addUser = false;
        modalState.addTag = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        break;
      case "addTag":
        modalState.addTag = !modalState.addTag;
        modalState.removeUser = false;
        modalState.addUser = false;
        modalState.addDependency = false;
        modalState.addUpdate = false;
        break;
      case "addDependency":
        modalState.addDependency = !modalState.addDependency;
        modalState.addTag = false;
        modalState.removeUser = false;
        modalState.addUser = false;
        modalState.addUpdate = false;
        break;
    }
  }
</script>
