<template lang="">
  <ul
    class="menu bg-base-200 rounded-box w-56 mt-10 before:ring-offset-purple-400"
    v-if="isLoaded"
  >
    <li class="active"><a @click="goTo('info')">Info</a></li>
    <li>
      <details open>
        <summary>
          <span>Tasks</span>
        </summary>

        <ul class="before:ring-offset-purple-400">
          <li v-for="task in project.tasks">
            <a>{{ task.name }}</a>
          </li>
          <!-- <li><a>Submenu 2</a></li>
          <li>
            <details open>
              <summary>Parent</summary>
              <ul>
                <li><a>Submenu 1</a></li>
                <li><a>Submenu 2</a></li>
              </ul>
            </details>
          </li> -->
        </ul>
      </details>
    </li>
    <li>
      <details open>
        <summary>Users</summary>
        <ul class="before:ring-offset-purple-400">
          <li
            v-for="user in project.users"
            class="text-white flex flex-row justify-start align-middle w-full"
          >
            <div class="avatar">
              <div class="w-6 rounded-full">
                <img :src="user.avatar" />
              </div>
            </div>
            <p class="text-base text-start -ml-6">{{ user.username }}</p>
          </li>
        </ul>
      </details>
    </li>
  </ul>
</template>
<script setup lang="ts">
  import { useRoute, useRouter } from "vue-router";
  import { onBeforeMount, ref } from "vue";
  import { Workspace } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";

  const route = useRoute();
  const router = useRouter();

  const id = ref<number>();

  const projectStore = useProjectStore();

  const project = ref<Workspace>();

  const isLoaded = ref(false);

  function goTo(route: string) {
    switch (route) {
      case "info":
        router.push(`/project/info?id=${id.value}`);
    }
  }

  onBeforeMount(() => {
    if (route.query.id) id.value = +route.query.id;
    project.value = projectStore.current;
    if (project.value.workspace_id) {
      isLoaded.value = true;
    }
  });
</script>
