<template lang="">
  <div class="flex justify-center mt-20 w-full ml-7 rounded-2xl">
    <div
      class="w-full rounded-xl border-b-4 border-x-4 border-secondary bg-white text-black pb-2"
      v-if="card"
    >
      <div class="flex flex-col text-center gap-0">
        <div :class="['w-full h-5 rounded-t-lg', title_color]"></div>

        <div
          class="flex flex-row justify-stretch mb-0 border-b-4 border-secondary gap-0"
        >
          <h2
            class="text-2xl ml-2 border-r-2 border-secondary px-2 py-4 my-auto min-w-44"
          >
            {{ card.workspace.project_name }}
          </h2>

          <h1
            class="text-3xl py-2 font-bold rounded-t-lg my-auto w-6/12"
            ref="card_title"
          >
            {{ card.title }}
          </h1>
          <div class="my-auto w-1/12 border-l-2 border-secondary py-5 px-0.5">
            <p>by {{ card.owner.username }}</p>
          </div>
          <div
            class="flex flex-col justify-start gap-3 w-4/12 border-l-2 border-secondary"
          >
            <div
              class="flex flex-row justify-start gap-1 h-7 border-b-2 border-secondary"
            >
              <h3
                :class="[
                  'font-bold border-r-2 min-w-32 border-secondary px-2',
                  priority_color,
                ]"
              >
                {{ card.priority }}
              </h3>
              <!-- <h3>{{ card.progress }}</h3> -->
              <h3 class="border-r-2 border-secondary pr-2">
                {{ card.status }}
              </h3>
              <h3 class="border-r-2 pr-2 border-secondary">
                {{ card.effort }}
              </h3>
              <h3 class="min-w-36">
                {{ card.task_type }}
              </h3>
            </div>

            <progress
              class="progress progress-success w-11/12 mx-auto bg-gray-300 border-secondary"
              :value="progress_value"
              max="100"
            ></progress>
          </div>
        </div>
        <div class="px-3 py-5 flex flex-col gap-5 justify-start">
          <h2 class="text-2xl text-start">{{ card.subtitle }}</h2>
          <div class="flex flex-row justify-start gap-2">
            <p
              v-for="tag in card.task_tags"
              :class="['px-2 py-0.5 rounded-lg']"
            >
              {{ tag }}
            </p>
          </div>

          <div class="text-start">
            <p>
              Designated to:
              <span class="gap-2" v-for="user of card.designated_to">
                {{ user.username }}</span
              >
            </p>
          </div>

          <div class="flex my-5 mx-5">
            <p>{{ card.description }}</p>
          </div>

          <div class="flex flex-row gap-2" v-if="card.dependencies">
            <p v-for="task in card.dependencies">{{ task.title }}</p>
          </div>

          <div class="flex flex-col">
            <div v-for="update in card.updates">
              <div class="flex flex-col justify-center">
                <p>{{ update.description }}</p>
                <div class="flex flex-row justify-end gap-5">
                  <p>{{ update.created_at }}</p>
                  <p>by {{ update.creator_username }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, onBeforeMount, watch } from "vue";
  import { Task } from "../utils/types";
  import { useTaskStore } from "../store/task.store";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";
  import { useRoute } from "vue-router";
  import { taskUtils } from "../utils/task.utils";

  const card = ref<Task>();
  const taskStore = useTaskStore();

  const title_color = ref<string>();
  const progress_value = ref<number>();
  const priority_color = ref<string>();

  const route = useRoute();

  const apiCall = useApiCall();

  async function fetchTask(task_id: number) {
    const data = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
      params: { id: task_id },
    })) as Task;
    return data;
  }

  function prepareTaskData(data: Task) {
    title_color.value = taskUtils.getColor(data.color);
    progress_value.value = taskUtils.calcProgress(data.progress);
    priority_color.value = taskUtils.calcPriorityColor(data.priority);
  }

  watch(
    () => route.query.id,
    async (newValue, oldValue) => {
      const id = newValue;
      if (id) {
        const task = await fetchTask(+id);
        if (task) {
          card.value = task;
          taskStore.setCurrentTask(task);
          prepareTaskData(task);
        }
      }
    }
  );

  onBeforeMount(async () => {
    const task_id = route.query.id;
    if (task_id) {
      const data = await fetchTask(+task_id);

      if (data) {
        card.value = data;
        taskStore.setCurrentTask(data);
        prepareTaskData(data);
      }
    }
  });
</script>
