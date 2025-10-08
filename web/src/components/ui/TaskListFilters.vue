<template lang="">
  <div class="flex flex-col justify-center">
    <div
      class="flex flex-row xl:flex-nowrap flex-wrap lg:mx-0 mx-7 xl:gap-y-0 gap-y-5 justify-between py-5"
    >
      <TaskFilterInput @search="defineSearch" class="ml-0" />
      <div
        class="flex flex-row flex-wrap lg:flex-nowrap gap-y-3 lg:gap-y-0 lg:mt-0 gap-1 mt-1 items-center"
      >
        <div
          class="flex flex-row justify-center items-center my-auto gap-2 mr-2"
        >
          <div class="tooltip" data-tip="Show/Hide Special Tags">
            <button
              class="btn btn-outline btn-info 2xl:text-sm text-xs btn-sm"
              @click="handleSpecialTags"
            >
              {{
                !state.showingSpecialTags
                  ? "Show Special Tags"
                  : "Hide Special Tags"
              }}
            </button>
          </div>
          <div class="tooltip" data-tip="Show/Hidetags">
            <button
              class="btn btn-outline btn-info 2xl:text-sm text-xs btn-sm"
              @click="handleTags"
            >
              {{ !state.showingTags ? "Show Tags" : "Hide Tags" }}
            </button>
            <!-- <button
              class="btn h-2 bg-info hover:bg-indigo-400"
              @click="handleTags"
            >
              <font-awesome-icon
                :icon="['fas', 'magnifying-glass']"
                class="size-5"
              />
            </button> -->
          </div>
          <div class="tooltip" data-tip="Clear Search">
            <button
              :class="[
                'btn btn-outline btn-info 2xl:text-sm text-xs btn-sm',
                props.tagSearch || state.tagSearch ? '' : 'btn-disabled',
              ]"
              @click="clearSearch"
            >
              Clear Tag Search
            </button>
            <!-- <button
              class="btn bg-error h-2 hover:bg-red-800"
              @click="clearSearch"
            >
              <font-awesome-icon :icon="['fas', 'trash']" class="size-5" />
            </button> -->
          </div>
        </div>
        <TaskPropFilters @selected="filterByOptions" />
        <ChangeCardSizeBtn
          @changeIconSize="changeIconSize"
          class="mr-16 mt-1 2xl:text-sm text-xs"
        />
      </div>
    </div>
    <SpecialTagNavigation
      :showTags="state.showingSpecialTags"
      @update="searchTasks"
      class="pb-5"
    />
    <TagNavigationPanel
      :ws_id="props.ws_id"
      :show="state.showingTags"
      @update="searchTasks"
    />
  </div>
</template>
<script setup lang="ts">
  import TaskFilterInput from "./TaskFilterInput.vue";
  import TaskPropFilters from "./TaskPropFilters.vue";
  import ChangeCardSizeBtn from "./ChangeCardSizeBtn.vue";
  import TagNavigationPanel from "./TagNavigationPanel.vue";
  import SpecialTagNavigation from "./customConfiguration/SpecialTagNavigation.vue";
  import { TaskLite, UITag } from "../../utils/types";
  import { onBeforeMount, reactive, ref, watch } from "vue";
  import { taskUtils } from "../../utils/task.utils";
  import { useRoute, useRouter } from "vue-router";

  const props = defineProps<{
    tasks: TaskLite[];
    tagSearch: boolean;
    ws_id: number;
  }>();

  const filteredTasks = ref<TaskLite[]>();

  const search = ref<string>();

  const route = useRoute();
  const router = useRouter();

  const state = reactive({
    selectedColors: [] as any[],
    selectedStatus: [] as any[],
    selectedProgress: [] as any[],
    selectedPriority: [] as any[],
    selectedTypes: [] as any[],
    selectedEffort: [] as any[],
    selectedTags: [] as any[],
    selectedValues: [] as any[],
    lastSelected: "" as string,
    lastSearchResultsCount: 0,
    noResults: false,
    showingTags: false,
    showingSpecialTags: false,
    tagSearch: false,
  });

  const emit = defineEmits([
    "changeSize",
    "filterTasks",
    "noResults",
    "handleTags",
    "clearSearch",
  ]);

  watch(
    () => route.query.tag,
    (newValue, oldValue) => {
      if (newValue) {
        state.tagSearch = true;
      } else state.tagSearch = false;
    }
  );

  watch(
    () => route.query.tag,
    (newValue, oldValue) => {
      if (newValue) {
        console.log("tag:", newValue);
        state.tagSearch = true;
        newValue = newValue as string;
        const tags = newValue.split(" ");
        console.log(tags);

        filterByOptions(tags, "tag");
        if (filteredTasks.value) emit("filterTasks", filteredTasks.value);
      } else state.tagSearch = false;
    }
  );

  watch(
    () => search.value,
    (newValue, oldValue) => {
      let tasksToFilter =
        filteredTasks.value &&
        filteredTasks.value?.length > 0 &&
        newValue != "" &&
        newValue != oldValue
          ? filteredTasks.value
          : props.tasks;
      filteredTasks.value = taskUtils.searchTasks(
        newValue as string,
        tasksToFilter as TaskLite[]
      );
      state.noResults = checkNoResults();
      filterByOptions([], "");
    }
  );

  watch(
    () => state.noResults,
    (newValue, oldValue) => {
      if (newValue) {
        emit("noResults");
        setTimeout(() => {
          state.noResults = false;
        }, 1500);
      }
    }
  );

  function handleTags() {
    state.showingTags = !state.showingTags;
    emit("handleTags");
  }

  function handleSpecialTags() {
    state.showingSpecialTags = !state.showingSpecialTags;
    emit("handleTags");
  }

  function clearSearch() {
    //    emit("clearSearch");
    searchTasks(null);
  }

  function defineSearch(value: string) {
    search.value = value;
  }

  function filterByOptions(opts: any[], type: string) {
    setSelectedOptions(opts, type);
    if (
      filteredTasks.value &&
      filteredTasks.value.length > 0 &&
      type == state.lastSelected &&
      !search.value
    ) {
      //suma tasks del mismo tipo de filtro si no hay search (input de texto)
      filterTasks(opts, type, props.tasks, true);
    } else if (filteredTasks.value && filteredTasks.value.length > 0) {
      //filtra a lo ya filtrado
      filterTasks(opts, type, filteredTasks.value);
    } else {
      filterTasks(opts, type, props.tasks);
    }
    if (state.selectedValues.length > 0) state.noResults = checkNoResults();
    state.lastSelected = type;
    state.lastSearchResultsCount = filteredTasks.value
      ? filteredTasks.value.length
      : 0;
    state.selectedValues = [];
    if (state.lastSearchResultsCount == 0 && search.value) {
      filteredTasks.value = taskUtils.searchTasks(
        search.value,
        props.tasks as TaskLite[]
      );
    }
    if (filteredTasks.value) emit("filterTasks", filteredTasks.value);
    // else emit("noResults");
  }

  function setSelectedOptions(opts: any[], type: string) {
    state.selectedValues = opts;
    switch (type.toLowerCase()) {
      case "color":
        state.selectedColors = opts;

        break;
      case "status":
        state.selectedStatus = opts;
        break;
      case "progress":
        state.selectedProgress = opts;
        break;
      case "priority":
        state.selectedPriority = opts;
        break;
      case "type":
        state.selectedTypes = opts;
        break;
      case "effort":
        state.selectedEffort = opts;
        break;
      case "tag":
        state.selectedTags = opts;
        break;
    }
    if (opts.length == 0) {
      prepareCheckedValues();
    }
  }

  //método q llama a taskUtils para filtrar segun sea por tags o por filtros comunes
  function filterTasks(
    opts: any[],
    type: string,
    list: TaskLite[],
    sameCategory?: boolean
  ) {
    console.log("OPTS: " + opts);
    if (type == "tag") {
      filteredTasks.value = fetchTasks(opts[0], list);

      if (opts.length > 1) {
        let listToFilter = sameCategory ? props.tasks : filteredTasks.value;
        for (let i = 1; i < opts.length; i++) {
          filteredTasks.value = filteredTasks.value?.concat(
            fetchTasks(opts[i], listToFilter)
          );
        }
      }
    } else
      filteredTasks.value = taskUtils.searchTasksByFilters(
        state.selectedValues,
        list as TaskLite[]
      );
    filteredTasks.value = eraseDuplicates(filteredTasks.value);
  }

  function eraseDuplicates(arr: TaskLite[]) {
    return Array.from(new Set(arr));
  }

  //método q filtra tasks de una lista a partir de un tag llamando a método de taskutils
  function fetchTasks(value: string, tasks_list: TaskLite[]) {
    return taskUtils.searchTasksByTag(
      value as string,
      tasks_list as TaskLite[]
    );
  }

  function checkUItag(obj: unknown): obj is UITag {
    return (
      typeof obj === "object" &&
      obj !== null &&
      "name" in obj &&
      typeof obj.name === "string" &&
      "color" in obj &&
      typeof obj.color === "string"
    );
  }

  function searchTasks(tag: UITag | string | null) {
    //este método NO busca/filtra tasks por tags, solo agrega tags a la ruta de la web, el filtro viene después

    if (tag) {
      const search_value = checkUItag(tag) ? tag.name : tag;
      if (!route.query.tag) router.push(`/project/tasks?tag=${search_value}`);
      else {
        if (!route.fullPath.includes(search_value))
          router.push(`${route.fullPath}+${search_value}`);
        else {
          let queryTags = route.query.tag as string;
          const tags = queryTags.split(" ");
          console.log(tags);
          let filtered_tags = tags
            .filter(
              (t: string) => t.toLowerCase() != search_value.toLowerCase()
            )
            .toString()
            .replace(",", "+");
          if (filtered_tags.length > 0)
            router.push(`/project/tasks?tag=${filtered_tags}`);
          else {
            router.push("/project/tasks");
            filterByOptions([], "");
          }
        }
      }
    } else {
      router.push("/project/tasks");
      state.tagSearch = false;
      filteredTasks.value = props.tasks;
      state.noResults = checkNoResults();
      emit("filterTasks", filteredTasks.value);
    }
  }

  function changeIconSize() {
    emit("changeSize");
  }

  function prepareCheckedValues() {
    state.selectedValues = state.selectedValues.concat(
      state.selectedColors,
      state.selectedEffort,
      state.selectedPriority,
      state.selectedProgress,
      state.selectedStatus,
      state.selectedTypes,
      state.selectedTags
    );
  }

  function checkNoResults() {
    return (
      filteredTasks.value?.length == 0 ||
      filteredTasks.value?.length == state.lastSearchResultsCount
    );
  }

  onBeforeMount(() => {
    if (route.query.tag) {
      state.tagSearch = true;
      filteredTasks.value = taskUtils.searchTasksByTag(
        route.query.tag as string,
        props.tasks as TaskLite[]
      );
      if (filteredTasks.value) emit("filterTasks", filteredTasks.value);
      // filteredByTags.value = filteredTasks.value;
    }
  });
</script>
<style lang=""></style>
