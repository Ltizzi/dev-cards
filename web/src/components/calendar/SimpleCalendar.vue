<template>
  <div
    :class="[
      'flex flex-col justify-center items-center w-auto max-w-56 bg-neutral rounded-lg transition-all duration-200',
      selected ? 'shadow-lg  shadow-secondary' : 'hover:opacity-100 opacity-60',
      actualMonth == month && !selected
        ? 'shadow-md shadow-success opacity-80'
        : '',
    ]"
  >
    <h1
      :class="[
        'text-center font-extrabold border-2 border-secondary w-full border-b-0 hover:cursor-pointer',
        selected ? 'bg-accent text-accent-content' : '',
      ]"
      @click="setMonth(month)"
    >
      {{ getStringMonth(month) }}
    </h1>
    <div
      class="grid grid-cols-7 gap-x-1 text-center text-base border-2 border-secondary bg-neutral text-base-content px-5 py-3 w-full min-h-56"
    >
      <div v-for="day in daysOfWeek" :key="day" class="font-bold">
        {{ day }}
      </div>
      <div
        v-for="n in days"
        :key="n"
        :class="[
          'aspect-square transition-all duration-200',
          n.length > 0
            ? 'border rounded hover:bg-primary cursor-pointer font-semibold border-secondary text-sm size-6'
            : '',
          selectedDay.length > 0 && selectedDay == n
            ? 'bg-primary text-primary-content'
            : '',
          actualDay === n && selectedDay != n
            ? 'shadow-md shadow-success bg-opacity-50 bg-success text-success-content'
            : '',
        ]"
        @click="setDay(n)"
      >
        {{ n }}
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, ref } from "vue";
  import { dateUtils } from "../../utils/date.utils";

  const daysOfWeek = dateUtils.daysOfWeek;
  const daysOfWeekEN = dateUtils.daysOfWeekEN;
  const months = dateUtils.months;

  const props = defineProps<{
    year: number;
    month: number;
    selected: boolean;
  }>();

  const emit = defineEmits(["setDay", "setMonth"]);

  const days = ref<Array<string>>();

  const selectedDay = ref<string>("");

  const actualDay = ref<string>();
  const actualMonth = ref<number>();

  function getStringMonth(month: number) {
    return months[month].toUpperCase();
  }

  function setDay(day: string) {
    selectedDay.value = day;
    emit("setDay", { day: +day, month: props.month, year: props.year });
  }

  function setMonth(month: number) {
    emit("setMonth", month);
  }

  onBeforeMount(() => {
    const newDate = new Date(props.year, props.month, 1);
    const dayOfWeek = newDate.getDay(); //
    const emptyDays = dayOfWeek > 0 ? dayOfWeek - 1 : 6;
    const monthTotalDays = new Date(props.year, props.month + 1, 0).getDate();
    const totalDays = [];
    for (let i = 0; i < emptyDays; i++) {
      totalDays.push("");
    }
    for (let i = 1; i <= monthTotalDays; i++) {
      totalDays.push(i.toString());
    }
    days.value = totalDays;

    actualMonth.value = new Date().getMonth();
    if (actualMonth.value == props.month)
      actualDay.value = new Date().getDate().toString();
  });
</script>
