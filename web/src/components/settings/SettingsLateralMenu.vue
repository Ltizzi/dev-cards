<template lang="">
  <ul
    class="menu rounded-box w-fit mr-10 bg-gradient-to-br from-base-200 to-transparent"
  >
    <h2 class="menu-title mt-10">{{ props.title }}</h2>
    <li>
      <ul class="">
        <template v-for="(option, index) in props.options">
          <li
            :class="[
              state.selected == index
                ? 'bg-primary bg-opacity-70 text-white rounded-lg '
                : '',
              'py-2 px-2 w-fit hover:cursor-pointer text-xs',
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

  const props = defineProps<{
    isOwner: boolean;
    options: MenuOptionUI[];
    title: string;
  }>();
  const emit = defineEmits(["menuOption"]);

  const state = reactive({
    selected: 0,
  });

  function changeMenu(num: number) {
    console.log("INDEX: ", num);
    state.selected = num;
    emit("menuOption", props.options[num]);
  }

  function checkVisible(option: MenuOptionUI) {
    return option.needOwner ? props.isOwner : true;
  }
</script>
