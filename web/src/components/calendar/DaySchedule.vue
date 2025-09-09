<template>
  <div class="bg-base-300 max-h-screen h-4/6 p-4 overflow-y-scroll">
    <div class="max-w-full mx-auto bg-base-200 rounded-lg">
      <!-- Header -->
      <div class="bg-base-100 p-4 text-base-content text-center">
        <h2 class="text-lg font-semibold">Agenda del Día</h2>
        <p class="text-sm text-base-content">
          {{
            selectedDate?.day +
            " / " +
            selectedDate?.month +
            " / " +
            selectedDate?.year
          }}
        </p>
      </div>

      <!-- Time Grid -->
      <div class="relative">
        <!-- Time Labels -->
        <div class="absolute left-0 top-0 w-16 h-full">
          <div
            v-for="hour in hours"
            :key="hour"
            class="h-16 border-b border-neutral flex items-start justify-end pr-2 pt-1"
          >
            <span class="text-xs text-base-content">{{
              formatHour(hour)
            }}</span>
          </div>
        </div>

        <!-- Events Container -->
        <div class="ml-16 relative">
          <!-- Grid Lines -->
          <div
            v-for="hour in hours"
            :key="`grid-${hour}`"
            class="h-16 border-b border-neutral"
          ></div>

          <!-- Events -->
          <div
            v-for="event in sortedEvents"
            :key="event.id"
            :style="getEventStyle(event)"
            :class="getEventClass(event)"
            class="absolute rounded-lg p-3 cursor-pointer transition-all hover:opacity-90"
            @click="selectEvent(event)"
          >
            <div class="text-sm font-medium text-base-content">
              {{ event.title }}
            </div>
            <div class="text-xs text-base-content opacity-80">
              {{ event.description }}
            </div>
            <div
              v-if="event.location"
              class="text-xs text-base-content opacity-60"
            >
              📍 {{ event.location }}
            </div>
            <div class="text-xs text-base-content opacity-60 mt-1">
              {{ event.hourRange.start }} - {{ event.hourRange.end }}
            </div>
          </div>
        </div>
      </div>

      <!-- Add Event Button -->
      <div class="p-4">
        <button
          @click="showAddEventModal = true"
          class="w-full btn btn-primary btn-outline text-primary-content hover:bg-info-content py-2 px-4 rounded-lg transition-colors"
        >
          Agregar Evento
        </button>
      </div>
    </div>

    <!-- Add Event Modal -->
    <AddEventModal
      :showAddEventModal="showAddEventModal"
      :selected-date="selectedDate"
      :date-string="generateDateString(selectedDate as DateHelper)"
      @close-modal="closeModal"
      @update="updateCalendar"
      v-if="showAddEventModal"
    />
  </div>
</template>

<script setup lang="ts">
  import AddEventModal from "./AddEventModal.vue";
  import { ref, computed, onMounted, watch } from "vue";
  import {
    CalendarDay,
    CalendarItem,
    DateHelper,
    UserCalendar,
    UserLocal,
  } from "../../utils/types";

  interface Props {
    userCalendar?: UserCalendar;
    selectedDay?: DateHelper;
    calendarDay?: CalendarDay;
    hourRangeDisplay?: { start: number; end: number };
  }

  const props = withDefaults(defineProps<Props>(), {
    hourRangeDisplay: () => ({ start: 8, end: 20 }),
  });

  const selectedDate = ref(props.selectedDay);
  const showAddEventModal = ref(false);

  // Local calendar day state
  const localCalendarDay = ref<CalendarDay>(new Map());

  // Get calendar day for selected date
  const currentCalendarDay = computed((): CalendarDay => {
    if (props.userCalendar) {
      const dateKey = ""; //TODO: selectedDate.value.toISOString().split("T")[0];
      const dayFromProps = props.userCalendar.items.get(dateKey);
      if (dayFromProps) {
        return dayFromProps;
      }
    }
    return localCalendarDay.value;
  });

  // Convert CalendarDay Map to array and sort by start time
  const sortedEvents = computed((): CalendarItem[] => {
    const eventsArray = Array.from(currentCalendarDay.value.values());

    return eventsArray.sort((a, b) => {
      const timeA = timeToMinutes(a.hourRange.start);
      const timeB = timeToMinutes(b.hourRange.start);
      return timeA - timeB;
    });
  });

  // Hours array based on hourRangeDisplay
  const hours = computed(() => {
    const start = props.hourRangeDisplay.start;
    const end = props.hourRangeDisplay.end;
    return Array.from({ length: end - start + 1 }, (_, i) => i + start);
  });

  // Emit events
  const emit = defineEmits<{
    eventAdded: [event: CalendarItem];
    eventSelected: [event: CalendarItem];
  }>();

  // Helper functions
  function formatDate(date: Date): string {
    return date.toLocaleDateString("es-ES", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  }

  function formatHour(hour: number): string {
    return `${hour.toString().padStart(2, "0")}:00`;
  }

  function timeToMinutes(time: string): number {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
  }

  function getEventStyle(event: CalendarItem) {
    const startMinutes = timeToMinutes(event.hourRange.start);
    const endMinutes = timeToMinutes(event.hourRange.end);
    const duration = endMinutes - startMinutes;

    // Calculate position relative to display start hour
    const displayStartMinutes = props.hourRangeDisplay.start * 60;
    const adjustedStartMinutes = startMinutes - displayStartMinutes;

    // Convert to pixels (64px per hour = 1.067px per minute)
    const topPixels = (adjustedStartMinutes / 60) * 64;
    const heightPixels = (duration / 60) * 64;

    return {
      top: `${Math.max(0, topPixels)}px`,
      height: `${heightPixels}px`,
      left: "8px",
      right: "8px",
    };
  }

  function getEventClass(event: CalendarItem): string {
    const colorMap: Record<string, string> = {
      blue: "bg-blue-600",
      purple: "bg-purple-600",
      green: "bg-green-600",
      orange: "bg-orange-600",
      red: "bg-red-600",
      gray: "bg-gray-600",
    };
    return colorMap[event.color] || "bg-blue-600";
  }

  function selectEvent(event: CalendarItem): void {
    emit("eventSelected", event);
  }

  function helperDateToDate(obj: DateHelper) {
    return obj ? new Date(`${obj.day}/${obj.month}/${obj.year}`) : new Date();
  }

  function generateDateString(obj: DateHelper) {
    if (obj) return obj.year + "/" + obj.month + "/" + obj.day;
    else {
      const date = new Date();
      return date.getFullYear() + "/" + date.getMonth() + "/" + date.getDay();
    }
  }

  function closeModal(): void {
    showAddEventModal.value = false;
  }

  function updateCalendar(calendar: UserCalendar) {
    const dateKey = helperDateToDate(props.selectedDay as DateHelper)
      .toISOString()
      .split("T")[0];
    const dayFromProps = calendar.items.get(dateKey);
    if (dayFromProps) {
      localCalendarDay.value = new Map(dayFromProps);
    } else {
      localCalendarDay.value = new Map();
    }
  }

  // Watch for changes in selectedDate to load events for that day
  watch(
    selectedDate,
    (newDate) => {
      if (props.userCalendar) {
        updateCalendar(props.userCalendar);
        // const dateKey = helperDateToDate(newDate as DateHelper)
        //   .toISOString()
        //   .split("T")[0];
        // const dayFromProps = props.userCalendar.items.get(dateKey);
        // if (dayFromProps) {
        //   localCalendarDay.value = new Map(dayFromProps);
        // } else {
        //   localCalendarDay.value = new Map();
        // }
      }
    },
    { immediate: true }
  );

  watch(
    () => props.selectedDay,
    (newValue, oldValue) => {
      if (newValue != oldValue) {
        selectedDate.value = newValue;
      }
    }
  );

  onMounted(() => {
    if (!props.userCalendar) {
      if (!props.selectedDay) {
        const date = new Date();
        const actualDate: DateHelper = {
          day: date.getDate(),
          month: date.getMonth(),
          year: date.getFullYear(),
        };
        selectedDate.value = actualDate;
      }
    }
    if (props.calendarDay) localCalendarDay.value = props.calendarDay;
  });
</script>
