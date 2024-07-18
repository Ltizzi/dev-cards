import { defineStore } from "pinia";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { Task } from "../utils/types";

export const useTaskStore = defineStore("tasks", {
  state: () => ({
    tasks: [] as Array<Task>,
    taskById: {} as Task,
    currentTask: {} as Task,
    previousTask: {} as Task,
    currentProjectTasks: [] as Array<Task>,
  }),
  actions: {
    async fetchAllTasks() {
      const response = await useApiCall().get(EndpointType.TASK_GET_ALL);
      this.tasks = response as Array<Task>;
      return response;
    },

    async fetchTaskById(id: number) {
      const response = await useApiCall().get(EndpointType.TASK_GET_BY_ID, {
        params: {
          id,
        },
      });
      this.taskById = response as Task;
      return response;
    },

    setCurrentTask(task: Task) {
      this.currentTask = task;
    },

    setCurrentProjectTasks(list: Array<Task>) {
      this.currentProjectTasks = list;
    },

    addTaskToCurrentProject(task: Task) {
      this.currentProjectTasks.push(task);
    },
  },
});
