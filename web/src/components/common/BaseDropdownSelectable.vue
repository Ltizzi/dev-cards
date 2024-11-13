<template lang="">
  <div class="dropdown">
    <div
      tabindex="0"
      role="button"
      :class="[
        'btn btn-info  m-1 2xl:text-sm text-xs btn-sm',
        selected_options.length == 0 ? 'btn-outline' : '',
      ]"
    >
      {{ props.name }}
    </div>
    <ul
      tabindex="0"
      class="dropdown-content lg:text-sm text-xs menu bg-base-100 rounded-box z-[1] w-52 px-2 shadow py-5"
    >
      <li
        class="ml-1 hover:bg-accent pl-2 hover:text-accent-content flex flex-row justify-between h-7 my-auto align-middle"
        @click="clearSelected()"
        v-if="selected_options.length > 0"
      >
        Clear All
      </li>
      <li
        v-else
        class="ml-1 pl-2 flex flex-row justify-between h-7 my-auto align-middle"
      >
        Select options
      </li>
      <li
        v-for="opt in options"
        class="hover:bg-accent hover:text-accent-content flex flex-row justify-between h-8 my-auto align-middle"
        @click="handleLiClick(opt)"
      >
        <div class="flex flex-row justify-between w-3/4">
          <p>
            {{ opt.text[0] + opt.text.slice(1).toLowerCase() }}
          </p>
          <span
            v-if="props.name == 'Color'"
            :class="['size-3 rounded-full', `${taskUtils.getColor(opt.text)}`]"
          ></span>
        </div>

        <input
          type="checkbox"
          :checked="opt.check"
          class="checkbox checkbox-xs mt-2 w-1/4"
          @change="handleCheckbox(opt.text, $event)"
        />
        <!--         -->
      </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
  import { DropdownCheckboxOption } from "../../utils/types";
  import { taskUtils } from "../../utils/task.utils";
  import { onBeforeMount, ref } from "vue";

  const options = ref<DropdownCheckboxOption[]>();

  const props = defineProps<{
    options: DropdownCheckboxOption[];
    isDark: boolean;
    name: string;
  }>();

  const selected_options = ref<any[]>([]);

  const emit = defineEmits(["selected"]);

  function handleLiClick(opt: DropdownCheckboxOption) {
    opt.check = !opt.check;
    options.value = options.value?.map((op: any) => {
      if (op.text == opt.text) {
        op.check = opt.check;
      }
      return op;
    });
    if (opt.check) selected_options.value.push(opt.text);
    else
      selected_options.value = selected_options.value.filter(
        (s: any) => s !== opt.text
      );
    emit("selected", selected_options.value, props.name);
  }

  function handleCheckbox(label: string, event: Event) {
    // const checkbox = event.target as HTMLInputElement;
    // console.log("label: ", label + ", checked:" + checkbox.checked);
    // if (checkbox.checked) {
    //   console.log("added");
    //   selected_options.value?.push(label);
    // } else {
    //   selected_options.value = selected_options.value?.filter(
    //     (s: any) => s !== label
    //   );
    // }
    // emit("selected", selected_options.value, props.name);
  }

  function clearSelected() {
    options.value = options.value?.map((op: any) => {
      op.check = false;
      return op;
    });
    selected_options.value = [];
    emit("selected", selected_options.value, props.name);
  }

  function selectAll() {
    options.value = options.value?.map((op: any) => {
      op.check = true;
      return op;
    });
    selected_options.value = [];
    options.value?.forEach((opt: DropdownCheckboxOption) =>
      selected_options.value.push(opt.text)
    );
  }

  onBeforeMount(() => {
    options.value = props.options;
  });
</script>
