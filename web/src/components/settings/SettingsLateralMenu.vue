<template lang="">
  <ul
    class="menu rounded-box w-fit mr-10 bg-gradient-to-br from-base-200 to-transparent"
  >
    <li>
      <h2 class="menu-title text-xl">Project Settings</h2>
      <ul>
        <template v-for="(option, index) in options">
          <li
            :class="[
              state.selected == index
                ? 'bg-primary bg-opacity-70 text-white rounded-lg '
                : '',
              'py-2 px-2 w-fit hover:cursor-pointer',
            ]"
            @click="changeMenu(index)"
            v-if="checkVisible(option)"
          >
            {{ option.title }}
          </li>
        </template>
      </ul>
    </li>
  </ul>
</template>
<script setup lang="ts">
  import { reactive } from "vue";
  import { MenuOptionUI } from "../../utils/types";

  const props = defineProps<{ isOwner: boolean }>();
  const emit = defineEmits(["menuOption"]);

  const options: MenuOptionUI[] = reactive([
    {
      title: "Basic Settings",
      path: "basic",
      needOwner: false,
    },
    {
      title: "Moderators",
      path: "mods",
      needOwner: true,
    },
    {
      title: "Remove Users",
      path: "remove_users",
      needOwner: false,
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

  function checkVisible(option: MenuOptionUI) {
    return option.needOwner ? props.isOwner : true;
  }
</script>
