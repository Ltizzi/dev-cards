<template lang="">
  <div class="card bg-base-100 w-96 shadow-xl">
    <div class="card-body">
      <h2 class="card-title">{{ props.task.title }}</h2>
      <p>{{ props.task.description }}</p>
      <div class="card-actions justify-end">
        <button class="btn btn-primary">Buy Now</button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { defineProps } from "vue";
  import { Task } from "../../utils/types";

  const props = defineProps<{ task: Task }>();
</script>
