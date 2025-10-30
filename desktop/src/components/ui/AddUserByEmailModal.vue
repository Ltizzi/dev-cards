<template lang="">
  <BaseModal
    @closeModal="cancel"
    :is-active="props.showModal"
    v-if="props.showModal"
  >
    <div
      class="flex flex-col gap-3 justify-center bg-base-100 text-base-content w-72"
    >
      <h1
        class="bg-secondary py-2 text-center text-neutral-content text-xl px-2 font-bold"
      >
        Invite user to project
      </h1>
      <div class="flex flex-col justify-center px-5 py-5">
        <div
          role="alert"
          class="alert alert-success"
          v-if="isSearching && !showError"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6 shrink-0 stroke-current"
            fill="none"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <span class="text-white">{{ successMsg }}</span>
        </div>
        <div
          role="alert"
          class="alert alert-error"
          v-if="isSearching && showError"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6 shrink-0 stroke-current"
            fill="none"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          <span class="text-white">Error! User not found</span>
        </div>

        <label class="input input-bordered flex items-center gap-2">
          <input
            type="email"
            class="grow"
            v-model="input_text"
            placeholder="Search user by e-mail"
            @keydown.enter="search"
          />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 16 16"
            fill="currentColor"
            class="h-4 w-4 opacity-70"
          >
            <path
              fill-rule="evenodd"
              d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
              clip-rule="evenodd"
            />
          </svg>
        </label>
      </div>

      <div class="flex flex-row gap-5 pb-5 px-10 mx-auto">
        <button class="btn btn-success text-white" @click="addUser">
          Search
        </button>
        <button class="btn btn-active btn-error text-white" @click="cancel">
          Cancel
        </button>
      </div>
    </div>
  </BaseModal>
</template>
<script setup lang="ts">
  import { ref, watch } from "vue";
  //import { useApiCall } from "../../composables/useAPICall";
  //import { EndpointType } from "../../utils/endpoints";
  import BaseModal from "../common/BaseModal.vue";
  import { UserLite } from "../../utils/types";
  import { useProjectStore } from "../../store/project.store";
  import { useUserStore } from "../../store/user.store";

  const input_text = ref<string>();

  const isSearching = ref<boolean>();
  const successMsg = ref<string>();

  const showError = ref<boolean>();

  const props = defineProps<{
    showModal: boolean;
    ws_id: number;
  }>();
  const emit = defineEmits(["update", "cancel"]);

  //const apiCall = useApiCall();
  const projectStore = useProjectStore();
  const userStore = useUserStore();

  async function addUser() {
    isSearching.value = true;
    const response = (await projectStore.addUserToProjectByEmail(
      input_text.value as string,
      props.ws_id
    )) as UserLite[];
    // (await apiCall.post(
    //   EndpointType.WORKSPACE_ADD_USER_BY_EMAIL,
    //   {},
    //   {
    //     params: {
    //       ws_id: props.ws_id,
    //       email: input_text.value,
    //     },
    //   }
    // )) as UserLite[];
    if (response[0].user_id) {
      successMsg.value = "User added to Workspace!";
      emit("update");
      setTimeout(() => {
        cancel();
      }, 1000);
    } else {
      console.error(response);
    }
  }

  function cancel() {
    input_text.value = "";
    isSearching.value = false;
    emit("cancel");
  }

  function isEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(input_text.value as string);
  }

  watch(
    () => input_text.value,
    async (newValue, oldValue) => {
      console.log(isEmail());
      if (newValue != oldValue && isEmail()) {
        isSearching.value = true;
        const response = (await userStore.checkUserExists(
          input_text.value as string
        )) as boolean;
        //  (await apiCall.get(EndpointType.USER_CHECK, {
        //   params: { email: input_text.value },
        // })) as boolean;
        if (!response) showError.value = true;
        else {
          showError.value = false;
          successMsg.value = "User found!";
        }
        setTimeout(() => {
          isSearching.value = false;
        }, 500);
      }
    }
  );
</script>
