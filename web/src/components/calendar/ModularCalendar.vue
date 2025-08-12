<template>
  <div class="flex flex-col justify-center items-center w-auto">
    <div class="flex flex-col justify-center w-auto max-w-32">
      <div class="flex flex-row justify-center w-full">
        <button class="btn btn-square" @click="handleNav('back', true)">
          <font-awesome-icon icon="fa-solid fa-chevron-left" />
        </button>
        <button class="btn w-full">{{ selectedYear }}</button>
        <button class="btn btn-square" @click="handleNav('next', true)">
          <font-awesome-icon icon="fa-solid fa-chevron-right" />
        </button>
      </div>
      <div class="flex flex-row justify-center w-full">
        <button class="btn btn-square" @click="handleNav('back', false)">
          <font-awesome-icon icon="fa-solid fa-chevron-left" />
        </button>
        <button class="btn w-full">{{ months[selectedMonth] }}</button>
        <button class="btn btn-square" @click="handleNav('next', false)">
          <font-awesome-icon icon="fa-solid fa-chevron-right" />
        </button>
      </div>
    </div>
    <SimpleCalendar
      :selected="false"
      :fromModular="true"
      :year="selectedYear"
      :month="selectedMonth"
      @setDay="setDay"
      @setMonth="setMonth"
    />
  </div>
</template>
<script setup lang="ts">
  import { dateUtils } from "../../utils/date.utils";
  import { DateHelper } from "../../utils/types";
  import SimpleCalendar from "./SimpleCalendar.vue";
  import { onBeforeMount, ref } from "vue";

  const months = dateUtils.months;

  const emit = defineEmits(["setDay"]);

  const selectedYear = ref<number>(new Date().getFullYear());
  const selectedMonth = ref<number>(new Date().getMonth());

  function handleNav(type: string, isYear: boolean) {
    if (type === "next") {
      if (isYear) {
        selectedYear.value += 1;
      } else {
        if (selectedMonth.value < 11) {
          selectedMonth.value += 1;
        } else {
          selectedYear.value += 1;
          selectedMonth.value = 0;
        }
      }
    } else {
      if (isYear) {
        selectedYear.value -= 1;
      } else {
        if (selectedMonth.value > 0) {
          selectedMonth.value -= 1;
        } else {
          selectedYear.value -= 1;
          selectedMonth.value = 11;
        }
      }
    }
  }

  function setDay(obj: DateHelper) {
    emit("setDay", obj);
  }

  function setMonth(month: number) {}

  //   onBeforeMount(() => {
  //     selectedYear.value = new Date().getFullYear();
  //     selectedMonth.value = new Date().getMonth();
  //   });
</script>
