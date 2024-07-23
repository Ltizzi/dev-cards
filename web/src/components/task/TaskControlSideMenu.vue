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
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'user-minus']" />
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add tag"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'circle-plus']" />
      </li>
      <li
        class="tooltip tooltip-left hover:bg-primary hover:rounded-lg transition-all"
        data-tip="Add dependency"
      >
        <font-awesome-icon class="size-8" :icon="['fas', 'folder-tree']" />
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

  const modalState = reactive({
    addUser: false,
    removeUser: false,
    addTag: false,
    addDependecy: false,
    addUpdate: false,
  });

  function modalSwitch(modal: string) {
    switch (modal) {
      case "addUser":
        modalState.addUser = !modalState.addUser;
    }
  }
</script>
