<template>
  <div class="bg-base-300 max-h-screen h-4/6 p-4 overflow-y-scroll">
    <div class="max-w-full mx-auto bg-base-200 rounded-lg">
      <!-- Header -->
      <div class="bg-base-100 p-4 text-base-content text-center">
        <h2 class="text-lg font-semibold">Agenda del Día</h2>
        <p class="text-sm text-base-content" v-if="selectedDay">
          {{
            selectedDay?.day +
            " / " +
            selectedDay?.month +
            " / " +
            selectedDay?.year
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
    <div
      v-if="showAddEventModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
      @click="closeModal"
    >
      <div class="bg-neutral rounded-lg p-6 w-full max-w-md" @click.stop>
        <h3 class="text-lg font-semibold text-base-content mb-4">
          Nuevo Evento
        </h3>

        <form @submit.prevent="addEvent">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-300 mb-2"
              >Título</label
            >
            <input
              v-model="newEvent.title"
              type="text"
              required
              class="w-full bg-gray-700 text-base-content rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Título del evento"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-300 mb-2"
              >Descripción</label
            >
            <input
              v-model="newEvent.description"
              type="text"
              class="w-full bg-gray-700 text-base-content rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Descripción del evento"
            />
          </div>

          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-300 mb-2"
              >Ubicación</label
            >
            <input
              v-model="newEvent.location"
              type="text"
              class="w-full bg-gray-700 text-base-content rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Ubicación del evento"
            />
          </div>

          <div class="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label class="block text-sm font-medium text-gray-300 mb-2"
                >Hora Inicio</label
              >
              <input
                v-model="newEvent.hourRange.start"
                type="time"
                step="900"
                required
                class="w-full bg-gray-700 text-base-content rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-300 mb-2"
                >Hora Fin</label
              >
              <input
                v-model="newEvent.hourRange.end"
                type="time"
                step="900"
                required
                class="w-full bg-gray-700 text-base-content rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div class="mb-6">
            <label class="block text-sm font-medium text-gray-300 mb-2"
              >Color</label
            >
            <div class="grid grid-cols-6 gap-2">
              <button
                v-for="color in eventColors"
                :key="color.name"
                type="button"
                :class="[
                  color.bg,
                  'w-8 h-8 rounded-full border-2',
                  newEvent.color === color.name
                    ? 'border-white'
                    : 'border-transparent',
                ]"
                @click="newEvent.color = color.name"
              ></button>
            </div>
          </div>

          <div class="flex gap-3">
            <button
              type="button"
              @click="closeModal"
              class="flex-1 bg-gray-600 hover:bg-gray-700 text-base-content py-2 px-4 rounded-lg transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              class="flex-1 bg-blue-600 hover:bg-blue-700 text-base-content py-2 px-4 rounded-lg transition-colors"
            >
              Crear Evento
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, onMounted, watch } from "vue";
  import {
    CalendarDay,
    CalendarItem,
    DateHelper,
    UserCalendar,
    UserLocal,
  } from "../../utils/types";

  interface EventColor {
    name: string;
    bg: string;
  }

  // Props
  interface Props {
    userCalendar?: UserCalendar;
    selectedDay?: DateHelper;
    hourRangeDisplay?: { start: number; end: number };
  }

  const props = withDefaults(defineProps<Props>(), {
    hourRangeDisplay: () => ({ start: 8, end: 20 }),
  });

  // Reactive data
  const selectedDate = ref(props.selectedDay);
  const showAddEventModal = ref(false);

  // Mock user for development
  const mockUser: UserLocal = {
    user_id: 1,
    username: "Usuario Local",
    local: true,
    avatar: "",
  };

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

  const newEvent = ref<
    Omit<CalendarItem, "id" | "owner" | "created_at" | "updated_at" | "date">
  >({
    title: "",
    description: "",
    location: "",
    color: "blue",
    hourRange: {
      start: "",
      end: "",
    },
  });

  // Color options
  const eventColors: EventColor[] = [
    { name: "blue", bg: "bg-blue-600" },
    { name: "purple", bg: "bg-purple-600" },
    { name: "green", bg: "bg-green-600" },
    { name: "orange", bg: "bg-orange-600" },
    { name: "red", bg: "bg-red-600" },
    { name: "gray", bg: "bg-gray-600" },
  ];

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
    return obj
      ? new Date(`${obj.day}/${obj.month}/${obj.year}`)
      : new Date().toLocaleDateString();
  }

  function addEvent(): void {
    if (
      newEvent.value.title &&
      newEvent.value.hourRange.start &&
      newEvent.value.hourRange.end &&
      isValidTimeRange(
        newEvent.value.hourRange.start,
        newEvent.value.hourRange.end
      )
    ) {
      const now = new Date();
      const calendarItem: CalendarItem = {
        ...newEvent.value,
        id: `event-${Date.now()}`,
        owner: mockUser,
        created_at: now,
        updated_at: now,
        date: helperDateToDate(selectedDate.value as DateHelper),
      };

      // Add to local calendar day
      localCalendarDay.value.set(calendarItem.hourRange, calendarItem);

      // If we have a userCalendar prop, also update it
      if (props.userCalendar) {
        const dateKey = helperDateToDate(selectedDate.value as DateHelper)
          .toISOString()
          .split("T")[0];
        let dayMap = props.userCalendar.items.get(dateKey);
        if (!dayMap) {
          dayMap = new Map();
          props.userCalendar.items.set(dateKey, dayMap);
        }
        dayMap.set(calendarItem.hourRange, calendarItem);
      }

      emit("eventAdded", calendarItem);
      closeModal();
    } else if (
      !isValidTimeRange(
        newEvent.value.hourRange.start,
        newEvent.value.hourRange.end
      )
    ) {
      alert("La hora de fin debe ser posterior a la hora de inicio");
    }
  }

  function closeModal(): void {
    showAddEventModal.value = false;
    newEvent.value = {
      title: "",
      description: "",
      location: "",
      color: "blue",
      hourRange: {
        start: "",
        end: "",
      },
    };
  }

  // Validation helper
  function isValidTimeRange(start: string, end: string): boolean {
    return timeToMinutes(end) > timeToMinutes(start);
  }

  // Watch for changes in selectedDate to load events for that day
  watch(
    selectedDate,
    (newDate) => {
      if (props.userCalendar) {
        const dateKey = helperDateToDate(newDate as DateHelper)
          .toISOString()
          .split("T")[0];
        const dayFromProps = props.userCalendar.items.get(dateKey);
        if (dayFromProps) {
          localCalendarDay.value = new Map(dayFromProps);
        } else {
          localCalendarDay.value = new Map();
        }
      }
    },
    { immediate: true }
  );

  onMounted(() => {
    // Initialize with some sample events for demonstration
    if (!props.userCalendar) {
      const sampleEvents: CalendarItem[] = [
        {
          id: "1",
          owner: mockUser,
          title: "Moto Track Day",
          description: "All Motorcycles",
          location: "Autódromo",
          hourRange: { start: "09:00", end: "10:30" },
          color: "purple",
          created_at: new Date(),
          updated_at: new Date(),
          date: helperDateToDate(selectedDate.value as DateHelper),
        },
        {
          id: "2",
          owner: mockUser,
          title: "Drift Series Second Round",
          description: "JDM",
          location: "Pista Principal",
          hourRange: { start: "10:45", end: "12:30" },
          color: "blue",
          created_at: new Date(),
          updated_at: new Date(),
          date: helperDateToDate(selectedDate.value as DateHelper),
        },
      ];

      sampleEvents.forEach((event) => {
        localCalendarDay.value.set(event.hourRange, event);
      });
    }
  });
</script>
