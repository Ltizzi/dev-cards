<template>
  <div>
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
      <div class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto">
        <div
          v-if="!props.isSpecial"
          class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto"
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
        <div
          v-else
          class="flex flex-row flex-wrap justify-start w-full gap-1 my-auto"
        >
          <div v-for="tag in special_tags">
            <p
              :class="[
                'rounded-lg  text-white text-sm font-semibold py-0.5   capitalize px-3 transition-all ease-in-out duration-300 hover:scale-110 bg-gradient-to-r from-primary via-secondary to-accent',
                removeTagActive
                  ? 'hover:bg-error hover:cursor-not-allowed'
                  : 'hover:cursor-pointer', //TODO: ELEGIR COLOR PARA LAS SPECIAL TAGS
                ,
              ]"
              @click="removeTagActive ? removeTag(tag.name) : goToTag(tag.name)"
            >
              <!-- getColor(tag) -->
              {{ tag.name }}
            </p>
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
  import { defineProps, onMounted, ref } from "vue";
  import { SpecialTag, UITag } from "../../utils/types";

  const props = defineProps<{
    isSpecial: boolean;
    tags: UITag[] | SpecialTag[];
    canModify: boolean;
  }>();

  const emit = defineEmits(["removeTag", "removeSpecialTag", "navigate"]);

  const mouseOverTag = ref<boolean>(false);
  const removeTagActive = ref<boolean>(false);

  const special_tags = ref<SpecialTag[]>();
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

  function removeSpecialTag(id: number) {
    if (special_tags.value && special_tags.value.length > 0) {
      special_tags.value = special_tags.value.filter(
        (sp: SpecialTag) => sp.id != id
      );
      emit("removeSpecialTag", id);
    }
  }

  function goToTag(name: string) {
    emit("navigate", name);
  }

  onMounted(() => {
    if (props.tags && props.tags.length > 0) {
      if (props.isSpecial) {
        special_tags.value = props.tags as SpecialTag[];
      } else {
        normal_tags.value = props.tags as UITag[];
      }
      isLoaded.value = true;
    }
  });
</script>
