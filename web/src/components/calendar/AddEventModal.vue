<template>
  <div
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
    @click="closeModal"
  >
    <div
      class="bg-neutral rounded-lg p-6 w-full max-w-md"
      @click.stop
      @keydown.escape="closeModal"
      tabindex="0"
    >
      <h3 class="text-lg font-semibold text-base-content mb-4">Nuevo Evento</h3>

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
              :key="color"
              type="button"
              :class="[
                taskUtils.getColor(color),
                'w-8 h-8 rounded-full border-2',
                newEvent.color === color
                  ? 'border-white'
                  : 'border-transparent',
              ]"
              @click="newEvent.color = color"
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
</template>
<script setup lang="ts">
  import { ref } from "vue";
  import {
    CalendarDay,
    CalendarItem,
    ColorEnumArray,
    DateHelper,
  } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { taskUtils } from "../../utils/task.utils";

  const userStore = useUserStore();

  const props = defineProps<{
    showAddEventModal: boolean;
    selectedDate: DateHelper | undefined;
    dateString: string;
  }>();

  const emit = defineEmits(["closeModal", "update"]);

  // interface EventColor {
  //   name: string;
  //   bg: string;
  // }

  // const eventColors: EventColor[] = [
  //   { name: "blue", bg: "bg-blue-600" },
  //   { name: "purple", bg: "bg-purple-600" },
  //   { name: "green", bg: "bg-green-600" },
  //   { name: "orange", bg: "bg-orange-600" },
  //   { name: "red", bg: "bg-red-600" },
  //   { name: "gray", bg: "bg-gray-600" },
  // ];

  const eventColors = ColorEnumArray;

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

  const localCalendarDay = ref<CalendarDay>(new Map());

  function closeModal() {
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
    emit("closeModal");
  }

  // Validation helper
  function isValidTimeRange(start: string, end: string): boolean {
    return timeToMinutes(end) > timeToMinutes(start);
  }

  function timeToMinutes(time: string): number {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
  }

  function helperDateToDate(obj: DateHelper) {
    return obj ? new Date(`${obj.day}/${obj.month}/${obj.year}`) : new Date();
  }

  function addEvent() {
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
        owner: userStore.getSelfAsUserLite(),
        created_at: now,
        updated_at: now,
        date: helperDateToDate(props.selectedDate as DateHelper),
      };

      // Add to local calendar day
      const hourRange =
        calendarItem.hourRange.start + "|" + calendarItem.hourRange.end;
      localCalendarDay.value.set(hourRange, calendarItem);

      //TODO: BUSINESS LOGIC
      // If we have a userCalendar prop, also update it
      //   if (props.userCalendar) {
      //     const dateKey = helperDateToDate(props.selectedDate as DateHelper)
      //       .toISOString()
      //       .split("T")[0];
      //     let dayMap = props.userCalendar.items.get(dateKey);
      //     if (!dayMap) {
      //       dayMap = new Map();
      //       props.userCalendar.items.set(dateKey, dayMap);
      //     }
      //     dayMap.set(hourRange, calendarItem);
      //   }

      //   emit("eventAdded", calendarItem);
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
</script>
