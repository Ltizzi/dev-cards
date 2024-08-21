<template lang="">
  <ul
    class="menu rounded-box w-fit mr-10 bg-gradient-to-br from-secondary-content to-transparent"
  >
    <li>
      <h2 class="menu-title text-xl">Project Settings</h2>
      <ul>
        <li
          :class="[
            state.selected == index
              ? 'bg-primary bg-opacity-70 text-white rounded-lg '
              : '',
            'py-2 px-2 w-fit',
          ]"
          v-for="(option, index) in options"
          @click="changeMenu(index)"
        >
          {{ option.title }}
        </li>
      </ul>
    </li>
  </ul>
</template>
<script setup lang="ts">
  import { reactive } from "vue";
  import { MenuOptionUI } from "../../utils/types";

  const emit = defineEmits(["menuOption"]);

  const options: MenuOptionUI[] = reactive([
    {
      title: "Basic Settings",
      path: "basic",
    },
    {
      title: "Moderators",
      path: "mods",
    },
  ]);

  const state = reactive({
    selected: 0,
  });

  function changeMenu(num: number) {
    console.log("INDEX: ", num);
    state.selected = num;
    emit("menuOption", options[num]);
  }
</script>
