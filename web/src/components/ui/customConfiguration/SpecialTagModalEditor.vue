<template>
  <BaseModal :is-active="showModal" v-if="showModal" @closeModal="closeModal">
    <div class="p-5">
      <h1 class="text-center text-2xl pb-4 font-medium">
        {{ isEditor ? "Edit Special Tag" : "Create new Special Tag" }}
      </h1>
      <p class="pb-5 font-light">
        Special tags are tags with more functionality. Can be used to group
        tasks, divide them by groups, flag tasks, etc.
      </p>
    </div>
    <div class="py-5 flex flex-row flex-wrap max-w-96 mx-auto gap-y-3">
      <label class="form-control">
        <div class="label"><span class="label-text text-xs"></span>Value</div>
        <input
          type="text"
          :placeholder="props.isEditor ? tagToEdit.value : 'Value'"
          v-model="tag.value"
          class="input input-bordered focus:input-primary input-secondary input-sm w-44"
        />
      </label>
      <label class="form-control">
        <div class="label"><span class="label-text text-xs"></span>Name</div>
        <input
          type="text"
          :placeholder="props.isEditor ? tagToEdit.name : 'Name'"
          v-model="tag.name"
          class="input input-bordered focus:input-primary input-secondary input-sm w-44"
        />
      </label>
      <label class="form-control">
        <div class="label">
          <span class="label-text text-xs">Description</span>
        </div>
        <textarea
          class="textarea textarea-primary"
          :placeholder="
            props.isEditor ? tagToEdit.description : 'Enter a description'
          "
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
  import { ref } from "vue";
  import { useConfigStore } from "../../../store/config.store";
  import { SpecialTag } from "../../../utils/types";
  import BaseModal from "../../common/BaseModal.vue";

  const props = defineProps<{
    showModal: boolean;
    config_id: number;
    ws_id: number;
    isEditor: boolean;
    tagToEdit: SpecialTag;
  }>();

  const tag = ref<SpecialTag>({} as SpecialTag);

  const emit = defineEmits(["updateList", "close"]);

  const confnigStore = useConfigStore();

  function addNewTag() {}

  function closeModal() {
    emit("close");
  }
</script>
