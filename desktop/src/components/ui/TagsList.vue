<template>
  <div v-if="isLoaded">
    <!-- "normal_tags && normal_tags.length > 0" -->
    <div
      v-if="props.tags && props.tags.length > 0"
      :class="[
        'flex flex-row justify-between gap-2 lg:w-2/4 py-5 h-20   my-auto',
        removeTagActive ? 'hover:cursor-not-allowed' : '',
      ]"
      @mouseover="props.tags.length > 0 ? (mouseOverTag = true) : ''"
      @mouseleave="mouseOverTag = false"
      @keydown.esc="removeTagActive ? (removeTagActive = false) : ''"
    >
      <div
        class="flex flex-row flex-nowrap justify-between w-full gap-1 my-auto"
      >
        <!--  -->
        <div
          class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto"
          v-if="!props.isSpecial"
        >
          <div v-for="tag in normal_tags">
            <p
              :class="[
                'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 ',
                removeTagActive
                  ? 'hover:bg-error hover:cursor-not-allowed'
                  : `hover:cursor-pointer ${tag.color}`,
                `${tag.color}`,
              ]"
              @click="removeTagActive ? removeTag(tag.name) : goToTag(tag.name)"
            >
              <!-- getColor(tag) -->
              {{ tag.name }}
            </p>
          </div>
        </div>
        <!-- TODO:FIXME: pensar si uso ese componente para renderizar la lista de special tags -->
        <div
          v-else
          class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto"
        >
          <div v-for="tag in special_tags">
            <SpecialTagElement
              :tag="tag"
              :fromControl="false"
              @click="goToTag(tag)"
            />
          </div>
        </div>
      </div>
      <div class="tooltip" data-tip="Click to remove tags!">
        <button
          :class="[
            'btn btn-error',
            removeTagActive
              ? 'hover:cursor-not-allowed'
              : 'hover:cursor:pointer ',
          ]"
          v-if="mouseOverTag && props.canModify"
          @click="activeRemoveTag()"
        >
          <font-awesome-icon
            :icon="['fas', 'trash']"
            class="text-white size-5 my-auto"
          />
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { defineProps, onBeforeMount, ref } from "vue";
  import { SpecialTag, UITag } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";
  import SpecialTagElement from "./customConfiguration/SpecialTagElement.vue";

  const props = defineProps<{
    isSpecial: boolean;
    tags: string[];
    canModify: boolean;
  }>();

  const emit = defineEmits(["removeTag", "removeSpecialTag", "navigate"]);

  const mouseOverTag = ref<boolean>(false);
  const removeTagActive = ref<boolean>(false);

  const special_tags = ref<string[]>();
  const normal_tags = ref<UITag[]>();

  const isLoaded = ref<boolean>(false);

  function activeRemoveTag() {
    removeTagActive.value = !removeTagActive.value;
  }

  function removeTag(name: string) {
    if (normal_tags.value && normal_tags.value.length > 0) {
      normal_tags.value = normal_tags.value.filter(
        (t: UITag) => t.name.toLowerCase() != name.toLowerCase()
      );
      emit("removeTag", name);
    }
  }

  function goToTag(name: string) {
    emit("navigate", name);
  }

  onBeforeMount(async () => {
    if (props.tags && props.tags.length > 0) {
      if (props.isSpecial) {
        special_tags.value = props.tags as string[];
      } else {
        const preNormalTags = taskUtils.getNormalTags(props.tags);
        normal_tags.value = await taskUtils.getUITags(
          preNormalTags as string[]
        );
      }
      isLoaded.value = true;
    }
  });
</script>
