<template lang="">
  <ul
    class="menu bg-base-200 rounded-box w-52 mt-10 before:ring-offset-purple-400"
    v-if="isLoaded"
  >
    <li
      :class="[
        'active',
        state.selected == 0 ? 'border-l-2  border-primary -ml-0.5' : '',
      ]"
    >
      <a @click="goHome()">Info</a>
    </li>
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
                :class="[
                  'rounded-xl w-full flex flex-row justify-start',
                  state.selected == task.task_id ? 'bg-slate-600' : '',
                ]"
                @click="goTask(task.task_id)"
              >
                <p class="text-white w-full">
                  <span
                    :class="[
                      'size-2 rounded-full',
                      taskUtils.getColor(task.color),
                    ]"
                  ></span>
                  {{ shortName(task.title) }}
                </p>
              </li>
            </ul>
          </details>

          <details open>
            <summary>
              <span>Designated</span>
            </summary>
            <ul class="before:ring-offset-purple-400"></ul>
          </details>
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
  import { onBeforeMount, ref, reactive, watch } from "vue";
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

  const state = reactive({
    selected: 0,
  });

  watch(
    () => route.query.id,
    (newValue, oldValue) => {
      if (route.path != "/project/info")
        state.selected = newValue as unknown as number;
    }
  );

  watch(
    () => projectStore.current,
    (newValue, oldValue) => {
      if (newValue.workspace_id != oldValue.workspace_id) {
        project.value = projectStore.current;
      }
    }
  );

  function goHome() {
    state.selected = 0;
    router.push(`/project/info?id=${id.value}`);
  }

  function goTask(id: number) {
    state.selected = id;
    router.push(`/project/task?id=${id}`);
  }

  function shortName(name: string) {
    if (name.length > 15) {
      return name.slice(0, 15) + "(...)";
    } else return name;
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
