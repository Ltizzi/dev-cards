import { defineStore } from "pinia";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import {
  Effort,
  Priority,
  Progress,
  ProgressItem,
  Status,
  Task,
  TaskType,
  TaskUpdate,
} from "../utils/types";

const apiCall = useApiCall();

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
      const response = await apiCall.get(EndpointType.TASK_GET_ALL);
      this.tasks = response as Array<Task>;
      return response;
    },

    async fetchTaskById(id: number) {
      const response = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
        params: {
          id,
        },
      })) as Task;
      this.taskById = response;
      this.setCurrentTask(response);
      return response;
    },

    setCurrentTask(task: Task) {
      localStorage.setItem("currentTask", JSON.stringify(task));
      this.currentTask = task;
    },

    setCurrentProjectTasks(list: Array<Task>) {
      this.currentProjectTasks = list;
    },

    addTaskToCurrentProject(task: Task) {
      this.currentProjectTasks.push(task);
    },

    removeCurrent() {
      this.currentTask = {} as Task;
    },
    async updateTitle(title: string, id: number) {
      return await apiCall.patch(
        EndpointType.TASK_UPDATE_TITLE,
        {},
        { params: { task_id: id, title: title } }
      );
    },
    async updateSubtitle(subtitle: string, id: number) {
      return await apiCall.patch(
        EndpointType.TASK_UPDATE_SUBTITLE,
        {},
        { params: { task_id: id, subtitle: subtitle } }
      );
    },
    async updateTaskProgress(progress: Progress) {
      return (await apiCall.patch(
        EndpointType.TASK_UPDATE_PROGRESS,
        {},
        { params: { task_id: this.currentTask.task_id, progress: progress } }
      )) as Task;
    },
    async updatePriority(priority: Priority) {
      return (await apiCall.patch(
        EndpointType.TASK_UPDATE_PRIORITY,
        {},
        { params: { priority: priority } }
      )) as Task;
    },
    async updateTaskStatus(option: Status) {
      return (await apiCall.patch(
        EndpointType.TASK_UPDATE_STATUS,
        {},
        { params: { task_id: this.currentTask.task_id, status: option } }
      )) as Task;
    },
    async updateTaskEffort(effort: Effort) {
      return (await apiCall.patch(
        EndpointType.TASK_UPDATE_EFFORT,
        {},
        { params: { task_id: this.currentTask.task_id, effort: effort } }
      )) as Task;
    },
    async updateTaskType(type: TaskType) {
      return (await apiCall.patch(
        EndpointType.TASK_UPDATE_TYPE,
        {},
        { params: { task_id: this.currentTask.task_id, type: type } }
      )) as Task;
    },
    async removeTag(tag: string) {
      return (await apiCall.patch(
        EndpointType.TASK_REMOVE_TAG,
        {},
        { params: { task_id: this.currentTask.task_id, tag: tag } }
      )) as Task;
    },
    async addDependency(id: number, parent_id: number) {
      return (await apiCall.post(EndpointType.TASK_ADD_DEPENDENCY, null, {
        params: { task_id: id, parent_id: parent_id },
      })) as Task;
    },
    async addIssue(id: number, issue: ProgressItem) {
      return await apiCall.post(EndpointType.TASK_CREATE_ISSUE, issue, {
        params: { task_id: id },
      });
    },
    async updateIssue(issue: ProgressItem, id: number) {
      return await apiCall.patch(EndpointType.TASK_UPDATE_ISSUE, issue, {
        params: { task_id: id },
      });
    },
    async deleteIssue(id: number, task_id: number) {
      return await apiCall.del(EndpointType.TASK_REMOVE_ISSUE, {
        params: { task_id: task_id, issue_id: id },
      });
    },
    async saveTag(tag: string) {
      return await apiCall.patch(
        EndpointType.TASK_ADD_TAG,
        {},
        { params: { task_id: this.currentTask.task_id, tag: tag } }
      );
    },
    async addTaskUpdate(update: TaskUpdate) {
      return await apiCall.post(EndpointType.TASK_ADD_UPDATE, update, {
        params: { task_id: this.currentTask.task_id },
      });
    },
    async updateDescription(newDes: any, id: number) {
      return await apiCall.patch(EndpointType.TASK_UPDATE_DESCRIPTION, newDes, {
        params: { task_id: id },
      });
    },
    async assignUserToTask(user_id: number, task_id?: number) {
      const id = task_id ? task_id : this.currentTask.task_id;
      return await apiCall.post(EndpointType.TASK_ASSIGN_USER, null, {
        params: { task_id: id, user_id: user_id },
      });
    },
    async unassignUserFromTask(user_id: number, task_id?: number) {
      const id = task_id ? task_id : this.currentTask.task_id;
      return await apiCall.post(EndpointType.TASK_UNASSIGN_USER, null, {
        params: { task_id: id, user_id: user_id },
      });
    },
    async autoAssignTask(task_id: number, ws_id: number) {
      return await apiCall.post(
        EndpointType.TASK_AUTOASSIGN,
        {},
        { params: { task_id: task_id, ws_id: ws_id } }
      );
    },
    async createTask(task: Task) {
      return await apiCall.post(EndpointType.TASK_NEW, task);
    },
    async deleteTask(id: number) {
      return await apiCall.del(EndpointType.TASK_DELETE, {
        params: { id: id },
      });
    },
  },
});
