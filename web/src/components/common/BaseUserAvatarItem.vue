<template lang="">
  <div
    class="flex flex-row gap-4 hover:cursor-pointer hover:bg-accent hover:bg-opacity-10 py-1.5 px-2 rounded-lg"
    @click="goToProfile"
  >
    <div class="avatar">
      <div class="w-6 rounded-full">
        <img :src="props.avatar" />
      </div>
    </div>
    <p class="text-sm my-auto">
      {{ props.username
      }}{{
        props.createdAt
          ? ",  at " + dateUtils.generateDateTemplate(props.createdAt)
          : ""
      }}
    </p>
  </div>
</template>
<script setup lang="ts">
  import { useRouter } from "vue-router";
  import { dateUtils } from "../../utils/date.utils";

  const props = defineProps<{
    avatar: string;
    username: string;
    id: number;
    createdAt?: Date;
    disableLink?: boolean;
  }>();

  const router = useRouter();

  function goToProfile() {
    if (!props.disableLink) router.push(`/project/user?id=${props.id}`);
  }
</script>
