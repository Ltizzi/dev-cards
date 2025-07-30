<template>
  <div class="bg-base-300 max-h-screen h-4/6 p-4 overflow-y-scroll">
    <div class="max-w-full mx-auto bg-base-200 rounded-lg">
      <!-- Header -->
      <div class="bg-base-100 p-4 text-base-content text-center">
        <h2 class="text-lg font-semibold">Agenda del Día</h2>
        <p class="text-sm text-base-content">{{ formatDate(selectedDate) }}</p>
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
          >
            <!-- 15-minute subdivisions -->
            <div
              class="absolute w-full h-4 border-b border-neutral opacity-50"
              style="top: 25%"
            ></div>
            <div
              class="absolute w-full h-4 border-b border-neutral opacity-50"
              style="top: 50%"
            ></div>
            <div
              class="absolute w-full h-4 border-b border-neutral opacity-50"
              style="top: 75%"
            ></div>
          </div>

          <!-- Events -->
          <div
            v-for="event in events"
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
            <div class="text-xs text-base-content opacity-60 mt-1">
              {{ formatTime(event.startTime) }} -
              {{ formatTime(event.endTime) }}
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

          <div class="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label class="block text-sm font-medium text-gray-300 mb-2"
                >Hora Inicio</label
              >
              <input
                v-model="newEvent.startTime"
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
                v-model="newEvent.endTime"
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
  import { ref, computed, onMounted } from "vue";

  interface Event {
    id: string;
    title: string;
    description: string;
    startTime: string;
    endTime: string;
    color: string;
  }

  interface EventColor {
    name: string;
    bg: string;
  }

  // Reactive data
  const selectedDate = ref(new Date());
  const showAddEventModal = ref(false);
  const events = ref<Event[]>([
    {
      id: "1",
      title: "Moto Track Day",
      description: "All Motorcycles",
      startTime: "09:00",
      endTime: "10:30",
      color: "purple",
    },
    {
      id: "2",
      title: "Drift Series Second Round",
      description: "JDM",
      startTime: "08:45",
      endTime: "09:30",
      color: "purple",
    },
    {
      id: "3",
      title: "Moto Track Day",
      description: "All Motorcycles",
      startTime: "10:00",
      endTime: "11:15",
      color: "blue",
    },
    {
      id: "4",
      title: "Moto Track Day",
      description: "All Motorcycles",
      startTime: "10:45",
      endTime: "11:30",
      color: "gray",
    },
    {
      id: "5",
      title: "Moto Track Day",
      description: "All Motorcycles",
      startTime: "01:00",
      endTime: "02:15",
      color: "green",
    },
    {
      id: "6",
      title: "Private Event",
      description: "All Motorcycles",
      startTime: "02:00",
      endTime: "03:15",
      color: "orange",
    },
    {
      id: "7",
      title: "Drift Series Second Round",
      description: "All Motorcycles",
      startTime: "10:45",
      endTime: "11:30",
      color: "gray",
    },
  ]);

  const newEvent = ref<Omit<Event, "id">>({
    title: "",
    description: "",
    startTime: "",
    endTime: "",
    color: "blue",
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

  // Hours array (24 hour format)
  const hours = computed(() => {
    return Array.from({ length: 12 }, (_, i) => i + 8);
  });

  // Helper functions
  const formatDate = (date: Date): string => {
    return date.toLocaleDateString("es-ES", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  const formatHour = (hour: number): string => {
    return `${hour.toString().padStart(2, "0")}:00`;
  };

  const formatTime = (time: string): string => {
    return time;
  };

  const timeToMinutes = (time: string): number => {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
  };

  const getEventStyle = (event: Event) => {
    const startMinutes = timeToMinutes(event.startTime);
    const endMinutes = timeToMinutes(event.endTime);
    const duration = endMinutes - startMinutes;

    // Convert to pixels (64px per hour = 1.067px per minute)
    const topPixels = (startMinutes / 60) * 64;
    const heightPixels = (duration / 60) * 64;

    return {
      top: `${topPixels}px`,
      height: `${heightPixels}px`,
      left: "8px",
      right: "8px",
    };
  };

  const getEventClass = (event: Event): string => {
    const colorMap: Record<string, string> = {
      blue: "bg-blue-600",
      purple: "bg-purple-600",
      green: "bg-green-600",
      orange: "bg-orange-600",
      red: "bg-red-600",
      gray: "bg-gray-600",
    };
    return colorMap[event.color] || "bg-blue-600";
  };

  const selectEvent = (event: Event): void => {
    console.log("Event selected:", event);
  };

  const addEvent = (): void => {
    if (
      newEvent.value.title &&
      newEvent.value.startTime &&
      newEvent.value.endTime
    ) {
      const event: Event = {
        ...newEvent.value,
        id: Date.now().toString(),
      };
      events.value.push(event);
      closeModal();
    }
  };

  const closeModal = (): void => {
    showAddEventModal.value = false;
    newEvent.value = {
      title: "",
      description: "",
      startTime: "",
      endTime: "",
      color: "blue",
    };
  };

  onMounted(() => {
    // Component mounted
  });
</script>
