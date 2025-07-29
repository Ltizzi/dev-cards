<template>
  <div
    class="flex flex-row flex-wrap gap-x-8 gap-y-6 justify-evenly w-7/12"
    @keydown.esc="handleEsc"
    tabindex="0"
  >
    <div v-for="(month, index) in months">
      <SimpleCalendar
        :year="year"
        :month="index"
        :selected="selectedMonth == index"
        @setDay="setDay"
        @setMonth="setMonth"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref } from "vue";
  import { dateUtils } from "../../utils/date.utils";
  import SimpleCalendar from "./SimpleCalendar.vue";

  const props = defineProps<{
    year: number;
  }>();

  const months = dateUtils.months;

  const dateObj = ref<Object | null>(null);

  const selectedMonth = ref<number>();

  function setDay(obj: Object) {
    dateObj.value = obj;
  }

  function setMonth(month: number) {
    selectedMonth.value = month;
  }

  function handleEsc() {
    if (selectedMonth.value) {
      selectedMonth.value = undefined;
    }
  }
</script>
