<template lang="">
  <div
    class="card bg-base-100 image-full w-72 min-h-56 max-h-56 shadow-xl z-0 duration-300 transition-all ease-in-out hover:scale-110"
  >
    <figure>
      <img
        :src="props.ws.project_avatar"
        :alt="`Avatar of ${props.ws.project_name}`"
        class="motion-preset-pop motion-duration-700 motion-delay-300"
      />
    </figure>
    <div class="card-body">
      <h2
        class="card-title motion-duration-700 motion-delay-200 motion-opacity-in-0 motion-blur-in-xl"
      >
        {{ props.ws.project_name }}
      </h2>
      <p
        class="text-sm motion-duration-700 motion-delay-200 motion-opacity-in-0 motion-blur-in-xl"
      >
        {{ shortString(props.ws.description) }}
      </p>
      <div class="card-actions justify-between">
        <div
          class="flex flex-col justify-start gap-0.5 items-center motion-duration-700 motion-delay-1000 motion-opacity-in-0 motion-blur-in-xl"
        >
          <p v-if="ws.users && ws.users.length > 0" class="italic text-sm">
            <span class="text-base font-semibold mr-1">{{
              ws.users.length
            }}</span>
            {{ ` user${ws.users.length > 1 ? "s" : ""}` }}
          </p>
          <p v-if="ws.tasks.length > 0" class="italic text-sm">
            <span class="text-base font-semibold mr-1">{{
              ws.tasks.length
            }}</span>
            {{ ` task${ws.tasks.length > 1 ? "s" : ""}` }}
          </p>
        </div>
        <button
          class="btn btn-primary motion-preset-bounce motion-duration-700 motion-delay-500 motion-opacity-in-0 motion-blur-in-sm"
          @click="goToProject(ws)"
        >
          Enter
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, onBeforeUnmount } from "vue";
  import { Workspace } from "../../utils/types";
  import { useRouter } from "vue-router";
  import { useProjectStore } from "../../store/project.store";

  const props = defineProps<{ ws: Workspace }>();

  const router = useRouter();

  const projectStore = useProjectStore();

  const isLoaded = ref<boolean>(false);
  const avatarURL = ref<string>();

  function goToProject(ws: Workspace) {
    projectStore.setCurrent(ws);
    router.push(`/project/info?id=${ws.workspace_id}`);
  }

  function shortString(description: string) {
    if (description.length > 80) return description.slice(0, 80) + "...";
    else return description;
  }
  onBeforeUnmount(() => {
    console.log(props.ws);
    //if (props.ws) isLoaded.value = true;
    isLoaded.value = props.ws ? true : false;
    avatarURL.value = props.ws.avatar ? props.ws.avatar : "";
  });
</script>
