<template lang="">
  <ul
    class="menu bg-base-200 rounded-box w-52 mt-10 before:ring-offset-purple-400"
    v-if="isLoaded"
  >
    <li class="active"><a @click="goTo('info')">Info</a></li>
    <li>
      <details open>
        <summary>
          <span>Tasks</span>
        </summary>
        <ul class="before:ring-offset-purple-400">
          <details open>
            <summary>
              <span>All</span>
            </summary>
            <ul class="before:ring-offset-purple-400">
              <li
                v-for="task in project.tasks"
                class="rounded-xl w-full"
              >
            

                <p class="text-white">    <div>
                  <div
                    :class="[
                      'h-2 w-3 rounded-full',
                      taskUtils.getColor(task.color),
                    ]"
                  ></div>
                </div>{{ task.title }}</p>
              </li>
            </ul>
          </details>
          <details open>
            <summary>
              <span>Designated</span>
            </summary>
            <ul class="before:ring-offset-purple-400"></ul>
          </details>
          <details open>
            <summary>
              <span>Moderator</span>
            </summary>
            <ul class="before:ring-offset-purple-400"></ul>
          </details>
        </ul>

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
  import { useApiCall } from "../../composables/useAPICall";
  import { EndpointType } from "../../utils/endpoints";
  import { taskUtils } from "../../utils/task.utils";

  const route = useRoute();
  const router = useRouter();

  const id = ref<number>();

  const projectStore = useProjectStore();

  const apiCall = useApiCall();

  const project = ref<Workspace>();

  const isLoaded = ref(false);

  function goTo(route: string) {
    switch (route) {
      case "info":
        router.push(`/project/info?id=${id.value}`);
    }
  }

  onBeforeMount(async () => {
    if (route.query.id) id.value = +route.query.id;
    project.value = projectStore.current;
    if (project.value.workspace_id) {
      isLoaded.value = true;
    } else {
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: id.value },
      })) as Workspace;
      if (response.workspace_id) {
        projectStore.setCurrent(response);
        project.value = response;
        isLoaded.value = true;
      }
    }
  });
</script>
