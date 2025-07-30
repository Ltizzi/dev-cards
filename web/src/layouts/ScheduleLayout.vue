<template>
  <div
    class="flex flex-col justify-center items-center max-h-screen w-7/12 bg-base-100 rounded-lg gap-5 py-14"
  >
    <div class="flex justify-start w-full ml-28">
      <h1 class="card-title text-start w-full text-3xl font-bold">
        Calendario
      </h1>
    </div>
    <div class="flex flex-row justify-center gap-10 max-h-fit w-full px-5">
      <div
        class="flex flex-col justify-between align-top gap-20 py-10 h-full w-3/12"
      >
        <ModularCalendar class="" />
        <ModularCalendar class="" />
      </div>

      <DaySchedule class="w-9/12" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import ModularCalendar from "../components/calendar/ModularCalendar.vue";
  import DaySchedule from "../components/calendar/DaySchedule.vue";
</script>
