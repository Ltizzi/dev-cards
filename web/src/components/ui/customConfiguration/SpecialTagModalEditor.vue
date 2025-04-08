<template>
  <BaseModal :is-active="showModal" v-if="showModal" @closeModal="closeModal">
    <div class="pt-5 pb-2 flex flex-col justify-center text-center">
      <h1 class="text-center text-2xl pb-4 font-medium">
        {{ isEditor ? "Edit Special Tag" : "Create new Special Tag" }}
      </h1>
      <p class="pt-5 text-sm font-extralight w-3/4 mx-auto">
        Special tags are tags with more functionality. Can be used to group
        tasks, divide them by groups, flag tasks, etc.
      </p>
    </div>
    <div class="py-5 flex flex-col max-w-96 mx-auto gap-y-3">
      <label class="form-control">
        <div class="label"><span class="label-text text-xs">Name</span></div>
        <input
          type="text"
          :placeholder="props.isEditor ? tagToEdit.name : 'Name'"
          v-model="tag.name"
          class="input input-bordered focus:input-primary input-secondary input-sm w-auto"
        />
      </label>
      <label class="form-control">
        <div class="label">
          <span class="label-text text-xs">Description</span>
        </div>
        <textarea
          class="textarea textarea-primary"
          rows="4"
          :placeholder="
            props.isEditor ? tagToEdit.description : 'Enter a description'
          "
          v-model="tag.description"
        ></textarea>
      </label>
    </div>
    <div class="flex flex-row gap-5 justify-center w-full py-3">
      <button class="btn btn-outline btn-primary" @click="addNewTag">
        Send
      </button>
      <button class="btn btn-outline btn-error" @click="closeModal">
        Cancel
      </button>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { ref, watch } from "vue";
  import { useConfigStore } from "../../../store/config.store";
  import { SpecialTag } from "../../../utils/types";
  import BaseModal from "../../common/BaseModal.vue";
  import { taskUtils } from "../../../utils/task.utils";

  const props = defineProps<{
    showModal: boolean;
    config_id: number;
    ws_id: number;
    isEditor: boolean;
    tagToEdit: SpecialTag;
  }>();

  const tag = ref<SpecialTag>({} as SpecialTag);

  const emit = defineEmits(["updateList", "close"]);

  const configStore = useConfigStore();

  watch(
    () => props.tagToEdit,
    (newValue, oldValue) => {
      if (newValue && newValue != oldValue) {
        tag.value.name = props.tagToEdit.name;
        tag.value.description = props.tagToEdit.description;
      }
    }
  );

  async function addNewTag() {
    if (!props.isEditor) {
      const new_tag: SpecialTag = {
        value: taskUtils.generateSpecialTag(tag.value.name.toUpperCase()),
        name: tag.value.name,
        description: tag.value.description,
      };
      const response = (await configStore.addSpecialTag(
        props.ws_id,
        props.config_id,
        new_tag
      )) as SpecialTag[];
      console.log(response);
      if (response.length > 0) {
        emit("updateList");
        emit("close");
      } else console.error("ERROR SAVING TAG");
    } else {
      const updatedTag: SpecialTag = {
        id: props.tagToEdit.id as number,
        value: taskUtils.updateSpecialTagValue(
          props.tagToEdit.value,
          tag.value.value
        ),
        name: tag.value.name,
        description: tag.value.description,
      };

      const response = (await configStore.updateSpecialTag(
        props.ws_id,
        props.config_id,
        updatedTag.id as number,
        updatedTag
      )) as SpecialTag[];
      if (response.length > 0) {
        emit("updateList");
        emit("close");
      } else console.error("ERROR UPDATING TAG");
    }
  }

  function closeModal() {
    emit("close");
  }
</script>
