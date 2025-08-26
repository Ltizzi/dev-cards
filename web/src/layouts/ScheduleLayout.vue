<template>
  <div
    class="flex flex-col justify-center items-center lg:max-h-screen w-full bg-base-100 rounded-lg gap-5 pt-20 pb-14"
  >
    <div class="flex justify-start w-full ml-20">
      <h1 class="card-title text-start w-full text-2xl font-semibold">
        Calendario
      </h1>
    </div>
    <div
      class="flex flex-col lg:flex-row justify-center items-center gap-10 max-h-fit w-full px-5"
    >
      <div
        class="flex flex-col justify-between align-top gap-20 py-10 h-full w-full lg:w-3/12"
      >
        <ModularCalendar class="" @setDay="setDay" :calendar="calendar" />
        <ModularCalendar class="" :calendar="calendar" />
      </div>

      <DaySchedule class="w-full lg:w-6/12" :selectedDay="selectedDay" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import ModularCalendar from "../components/calendar/ModularCalendar.vue";
  import DaySchedule from "../components/calendar/DaySchedule.vue";
  import { ref, onBeforeMount } from "vue";
  import { DateHelper, UserCalendar, WorkspaceCalendar } from "../utils/types";
  import { useCalendarStore } from "../store/calendar.store";

  const calendarStore = useCalendarStore();

  const calendar = ref<UserCalendar | WorkspaceCalendar>();

  const selectedDay = ref<DateHelper>();

  function setDay(obj: DateHelper) {
    if (obj) selectedDay.value = obj;
  }

  onBeforeMount(async () => {
    calendar.value = await calendarStore.getUserCalendar();
  });
</script>
