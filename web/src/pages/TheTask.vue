<template lang="">
  <div class="flex justify-center mx-auto align-middle mt-20">
    <div class="w-10/12 rounded-xl" v-if="card">
      <div class="flex flex-col text-center gap-2">
        <h1
          :class="['text-3xl py-2 font-bold rounded-t-lg', `${title_color}`]"
          ref="card_title"
        >
          {{ card.title }}
        </h1>
        <div class="flex flex-row justify-between">
          <h2 class="ml-5 text-xl">{{ card.project.project_name }}</h2>
          <div class="flex flex-row justify-evenly gap-5">
            <h3>{{ card.priority }}</h3>
            <h3>{{ card.progress }}</h3>
            <h3>{{ card.status }}</h3>
            <h3>{{ card.effort }}</h3>
            <h3>{{ card.task_type }}</h3>
          </div>
        </div>
        <h2 class="text-2xl">{{ card.subtitle }}</h2>
        <div class="flex flex-row justify-center gap-2">
          <p v-for="tag in card.task_tags" :class="['px-2 py-0.5 rounded-lg']">
            {{ tag }}
          </p>
        </div>
      </div>
      <div class="flex flex-col gap-1">
        <p>Owner: {{ card.owner.username }}</p>
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
</template>
<script setup lang="ts">
  import { ref, onBeforeMount, onMounted } from "vue";
  import { Task } from "../utils/types";
  import { useTaskStore } from "../store/task.store";
  import { useApiCall } from "../composables/useAPICall";
  import { EndpointType } from "../utils/endpoints";

  const card = ref<Task>();
  const taskStore = useTaskStore();

  const title_color = ref<string>();

  const apiCall = useApiCall();

  onBeforeMount(async () => {
    const data = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
      params: { id: 4 },
    })) as Task;
    console.log(data);
    console.log(data.color);
    if (data) {
      card.value = data;
      taskStore.setTask(data);
      title_color.value = data.color.toLowerCase();
    }
  });

  onMounted(() => {});
</script>

<style></style>
