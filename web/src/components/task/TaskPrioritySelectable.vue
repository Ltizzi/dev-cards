<template>
  <div
    :class="[
      ' border-r-2 2xl:border-t-0 border-t-2 2xl:border-b-0 border-b-2 py-3 2xl:min-w-32 xl:min-w-72 lg:min-w-40 min-w-24  border-secondary',
      props.canModify ? 'hover:cursor-pointer' : '',
    ]"
  >
    <h3
      :class="[
        'font-bold  px-2 hover:animate-pulse transition-all ease-in-out duration-200',
        taskUtils.calcPriorityColor(props.priority),
      ]"
      @click="changeElement"
      v-if="state.default"
    >
      {{ state.hasCustomGlosary ? state.priorityToShow : props.priority }}
    </h3>
    <select
      :class="[
        'select select-secondary w-fit select-xs max-w-28   font-bold',
        selectedPriority && selectedPriority?.length > 0
          ? taskUtils.calcPriorityColor(selectedPriority)
          : '',
        props.isDark
          ? props.darkerCard
            ? 'bg-neutral text-neutral-content'
            : 'text-base-300 bg-base-content'
          : 'bg-base-100 text-base-content',
      ]"
      v-model="selectedPriority"
      v-if="!state.default"
    >
      <option
        disabled
        selected
        :class="[
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral text-neutral-content'
              : 'bg-base-content text-base-300'
            : 'bg-base-100 text-base-content',
        ]"
      >
        Pick task priority
      </option>
      <option
        :class="[
          'text-info font-bold',
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral '
              : 'bg-base-content'
            : 'bg-base-100 ',
        ]"
      >
        {{ state.hasCustomGlosary ? props.glosary.items[0].value : "VERY_LOW" }}
      </option>
      <option
        :class="[
          'text-success font-bold',
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral'
              : 'bg-base-content'
            : 'bg-base-100 ',
        ]"
      >
        {{ state.hasCustomGlosary ? props.glosary.items[1].value : "LOW" }}
      </option>
      <option
        :class="[
          'text-accent font-bold',
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral'
              : 'bg-base-content'
            : 'bg-base-100 ',
        ]"
      >
        {{ state.hasCustomGlosary ? props.glosary.items[2].value : "MEDIUM" }}
      </option>
      <option
        :class="[
          'text-warning font-bold',
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral'
              : 'bg-base-content'
            : 'bg-base-100 ',
        ]"
      >
        {{ state.hasCustomGlosary ? props.glosary.items[3].value : "HIGH" }}
      </option>
      <option
        :class="[
          'text-error font-bold',
          props.isDark
            ? props.darkerCard
              ? 'bg-neutral'
              : 'bg-base-content'
            : 'bg-base-100 ',
        ]"
      >
        {{
          state.hasCustomGlosary ? props.glosary.items[4].value : "VERY_HIGH"
        }}
      </option>
    </select>
  </div>
</template>
<script setup lang="ts">
  import { taskUtils } from "../../utils/task.utils";
  import { Glosary, GlosaryItem, Priority } from "../../utils/types";
  import { onBeforeMount, reactive, ref, watch } from "vue";

  const props = defineProps<{
    priority: Priority;
    isDark: boolean;
    canModify: boolean;
    darkerCard: boolean;
    glosary: Glosary;
  }>();
  const emit = defineEmits(["updatePriority"]);

  const state = reactive({
    default: true,
    hasCustomGlosary: false,
    priorityToShow: "",
  });

  const selectedPriority = ref<Priority>();

  function changeElement() {
    if (props.canModify) {
      if (state.default) {
        state.default = false;
      }
    }
  }

  function getGlosaryItem() {
    let label = "";
    props.glosary.items.forEach((i: GlosaryItem) => {
      if (props.priority.toLowerCase() == i.key.toLowerCase()) {
        console.log(props.priority + " " + i.key + " " + i.value);
        label = i.value;
      }
    });
    return label;
  }

  watch(
    () => selectedPriority.value,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        state.default = true;
        if (!state.hasCustomGlosary)
          emit("updatePriority", selectedPriority.value);
        else {
          let selected: any;
          props.glosary.items.forEach((item: GlosaryItem) => {
            if (selectedPriority.value == item.value) {
              console.log("PASO");
              selected = item.key;
            }
          });
          emit("updatePriority", selected);
        }
      }
    }
  );

  watch(
    () => props.glosary,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        if (props.glosary.type) {
          state.hasCustomGlosary = true;
          state.priorityToShow = getGlosaryItem() as unknown as string;
        }
      }
    }
  );

  onBeforeMount(() => {
    // console.log("FROM SELECTABLE");
    // console.log(props.glosary);
    if (props.glosary.type) {
      state.hasCustomGlosary = true;
      state.priorityToShow = getGlosaryItem() as unknown as string;
    }
    //console.log(state.priorityToShow);
  });
</script>
<style lang=""></style>
