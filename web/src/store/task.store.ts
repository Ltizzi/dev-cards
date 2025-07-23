import { defineStore } from "pinia";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import {
  Effort,
  JSONWorkspace,
  Priority,
  Progress,
  ProgressItem,
  Status,
  Task,
  TaskLite,
  TaskType,
  TaskUpdate,
  TaskWithReference,
  UITag,
  Workspace,
} from "../utils/types";
import { taskUtils } from "../utils/task.utils";
import { useConfigStore } from "./config.store";
import { useUIStore } from "./ui.store";
import { useProjectStore } from "./project.store";
import { utils } from "../utils/utils";
import { useUserStore } from "./user.store";
import { mapLocalUserToUserLite, mapUserToUserLite } from "../utils/auth.utils";

const apiCall = useApiCall();

export const useTaskStore = defineStore("tasks", {
  state: () => ({
    tasks: [] as Array<Task>,
    taskById: {} as Task,
    currentTask: {} as Task,
    previousTask: {} as Task,
    currentProjectTasks: [] as Array<Task>,
    offlineMode: false,
  }),
  actions: {
    checkOfflineMode() {
      const UIStore = useUIStore();
      this.offlineMode = UIStore.checkOfflineMode();
      return this.offlineMode;
    },
    async fetchAllTasks() {
      const response = await apiCall.get(EndpointType.TASK_GET_ALL);
      this.tasks = response as Array<Task>;
      return response;
    },
    getLocalTask(id: string) {
      const projectStore = useProjectStore();
      const ws = projectStore.getCurrent() as Workspace;
      const jws = projectStore.getLocalStorageWorkspaceById(
        ws.workspace_id as number
      );
      const tasks = jws?.tasks;
      let task = tasks?.find((t: Task) => t.task_id === id);
      if (task?.task_id === id) {
        this.setCurrentTask(task);
        return task;
      }
    },

    async fetchTaskById(id: string) {
      this.checkOfflineMode();
      if (this.offlineMode) {
        return this.getLocalTask(id);
      } else {
        const response = (await apiCall.get(EndpointType.TASK_GET_BY_ID, {
          params: {
            id,
          },
        })) as Task;
        this.taskById = response;
        this.setCurrentTask(response);
        return response;
      }
    },
    getCurrent() {
      this.checkOfflineMode();
      return this.currentTask;
    },

    setCurrentTask(task: Task) {
      this.checkOfflineMode();
      localStorage.setItem("currentTask", JSON.stringify(task));
      this.currentTask = task;
    },
    saveLocalTask(task: Task) {
      // console.log("TASK: ");
      // console.log(task);
      const projectStore = useProjectStore();
      const ws = projectStore.getCurrent() as Workspace;
      // console.log("WORKSPACE: ");
      // console.log(ws);
      const jws = projectStore.getLocalStorageWorkspaceById(
        ws.workspace_id as number
      ) as unknown as JSONWorkspace;
      // console.log("JWS:");
      // console.log(jws);
      let tasks = jws.tasks ? jws.tasks : ([] as Task[]);
      if (task.task_id && this.checkTaskSavedLocal(task.task_id)) {
        tasks = tasks.map((t: Task) => {
          if (t.task_id === task.task_id) t = task;
          return t;
        });
      } else tasks.push(task);
      jws.tasks = tasks;
      projectStore.saveJSONWStoLocalStorage(jws);
    },
    checkTaskSavedLocal(id: string) {
      const projectStore = useProjectStore();
      const ws = projectStore.getCurrent() as Workspace;
      const jws = projectStore.getLocalStorageWorkspaceById(
        ws.workspace_id as number
      ) as unknown as JSONWorkspace;
      return jws.tasks.filter((t: Task) => t.task_id == id).length > 0;
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
    async updateTitle(title: string, id: string) {
      if (this.offlineMode) {
        this.currentTask.title = title;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return await apiCall.patch(
          EndpointType.TASK_UPDATE_TITLE,
          {},
          { params: { task_id: id, title: title } }
        );
    },
    async updateSubtitle(subtitle: string, id: string) {
      if (this.offlineMode) {
        this.currentTask.subtitle = subtitle;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return await apiCall.patch(
          EndpointType.TASK_UPDATE_SUBTITLE,
          {},
          { params: { task_id: id, subtitle: subtitle } }
        );
    },
    async updateTaskProgress(progress: Progress) {
      if (this.offlineMode) {
        this.currentTask.progress = progress;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return (await apiCall.patch(
          EndpointType.TASK_UPDATE_PROGRESS,
          {},
          { params: { task_id: this.currentTask.task_id, progress: progress } }
        )) as Task;
    },
    async updatePriority(priority: Priority) {
      if (this.offlineMode) {
        this.currentTask.priority = priority;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return (await apiCall.patch(
          EndpointType.TASK_UPDATE_PRIORITY,
          {},
          { params: { priority: priority, task_id: this.currentTask.task_id } }
        )) as Task;
    },
    async updateTaskStatus(option: Status) {
      if (this.offlineMode) {
        this.currentTask.status = option;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return (await apiCall.patch(
          EndpointType.TASK_UPDATE_STATUS,
          {},
          { params: { task_id: this.currentTask.task_id, status: option } }
        )) as Task;
    },
    async updateTaskEffort(effort: Effort) {
      if (this.offlineMode) {
        this.currentTask.effort = effort;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return (await apiCall.patch(
          EndpointType.TASK_UPDATE_EFFORT,
          {},
          { params: { task_id: this.currentTask.task_id, effort: effort } }
        )) as Task;
    },
    async updateTaskType(type: TaskType) {
      if (this.offlineMode) {
        this.currentTask.task_type = type;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return (await apiCall.patch(
          EndpointType.TASK_UPDATE_TYPE,
          {},
          { params: { task_id: this.currentTask.task_id, type: type } }
        )) as Task;
    },

    async addDependency(id: string, parent_id: string) {
      if (this.offlineMode) {
        let parent = await this.fetchTaskById(parent_id);
        if (parent && parent.child_tasks) {
          parent.child_tasks.push(
            taskUtils.mapTaskToTaskLite(this.currentTask)
          );
          this.saveLocalTask(parent);
          this.currentTask.dependencies?.push(
            taskUtils.mapTaskToTaskLite(parent)
          );
          this.saveLocalTask(this.currentTask);
          return this.currentTask;
        }
      } else
        return (await apiCall.post(EndpointType.TASK_ADD_DEPENDENCY, null, {
          params: { task_id: id, parent_id: parent_id },
        })) as Task;
    },
    async addIssue(id: string, issue: ProgressItem) {
      console.log("double pre");
      console.log(issue);
      if (this.offlineMode) {
        const task = this.getLocalTask(
          this.currentTask.task_id as string
        ) as Task;

        task?.progressItems.push(issue);

        // this.saveLocalTask(task);
        // this.setCurrentTask(task);
        // console.log(this.currentTask.progressItems);
        setTimeout(() => {
          return this.getCurrent();
        }, 200);
      } else
        return await apiCall.post(EndpointType.TASK_CREATE_ISSUE, issue, {
          params: { task_id: id },
        });
      return this.currentTask;
    },
    async updateIssue(issue: ProgressItem, id: string) {
      if (this.offlineMode) {
        let issues = this.currentTask.progressItems;
        if (
          issues.filter((i: any) => (i.issue_id = issue.issue_id)).length > 0
        ) {
          issues = issues.map((i: ProgressItem) => {
            if (i.issue_id === issue.issue_id) i = issue;
            return i;
          });
          this.currentTask.progressItems = issues;
          this.saveLocalTask(this.currentTask);
          return this.currentTask;
        }
      } else
        return await apiCall.patch(EndpointType.TASK_UPDATE_ISSUE, issue, {
          params: { task_id: id },
        });
    },
    async deleteIssue(id: number, task_id: string) {
      if (this.offlineMode) {
        let issues = this.currentTask.progressItems;
        issues = issues.filter((i: ProgressItem) => i.issue_id !== id);
        this.currentTask.progressItems = issues;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return await apiCall.del(EndpointType.TASK_REMOVE_ISSUE, {
          params: { task_id: task_id, issue_id: id },
        });
    },
    async saveTag(tag: string) {
      console.log("FROM STORE");
      console.log(this.currentTask.task_tags);
      this.checkOfflineMode();
      //TODO: add id here or in the API?

      const newTag = {} as UITag;

      const configStore = useConfigStore();

      if (!taskUtils.parseSpecialTag(tag)) {
        newTag.color = taskUtils.getRandomColor();
        newTag.name = tag;
        await configStore.addTagToPool(
          this.currentTask.workspace.workspace_id,
          configStore.current.config_id as number,
          newTag
        );
      }

      if (this.offlineMode) {
        console.log("DEBUG 1");
        console.log(this.currentTask.task_tags);
        console.log("TAG TO ADD: ", tag);
        let tags = this.currentTask.task_tags as string[];
        console.log(tags);
        const alreadyAddedToTask =
          tags.filter((t: string) => t.toLowerCase() == tag.toLowerCase())
            .length > 0;
        console.log(
          tags.filter((t: string) => t.toLowerCase() == tag.toLowerCase())
        );

        if (!alreadyAddedToTask) {
          console.log("DEBUG 2");
          tags.push(tag);
          this.currentTask.task_tags = tags;
          this.saveLocalTask(this.currentTask);
          return this.currentTask;
        } else return this.currentTask;
      } else {
        const response = (await apiCall.patch(
          EndpointType.TASK_ADD_TAG,
          {},
          { params: { task_id: this.currentTask.task_id, tag: tag } }
        )) as Task;
        this.setCurrentTask(response);
        //taskUtils.addTagToTagsPool(tag, response.workspace.workspace_id);
        return response;
      }
    },
    async removeTag(tag: string) {
      const configStore = useConfigStore();
      if (this.offlineMode) {
        let tags = this.currentTask.task_tags as string[];
        tags = tags.filter((t: string) => t.toLowerCase() != tag.toLowerCase());
        this.currentTask.task_tags = tags;
        this.saveLocalTask(this.currentTask);
        await configStore.removeTag(tag);
        return this.currentTask;
      } else {
        const response = (await apiCall.patch(
          EndpointType.TASK_REMOVE_TAG,
          {},
          { params: { task_id: this.currentTask.task_id, tag: tag } }
        )) as Task;
        this.setCurrentTask(response);
        await configStore.removeTag(tag);
        return response;
      }
    },
    async addTaskUpdate(update: TaskUpdate) {
      if (this.offlineMode) {
        update.update_id = utils.generateRandomId();
        update.created_at = new Date(Date.now());
        update.updated_at = new Date(Date.now());
        this.currentTask.updates?.push(update);
        this.saveLocalTask(this.currentTask);
        return this.currentTask.updates;
      } else
        return await apiCall.post(EndpointType.TASK_ADD_UPDATE, update, {
          params: { task_id: this.currentTask.task_id },
        });
    },
    async updateDescription(newDes: string, id: number) {
      if (this.offlineMode) {
        this.currentTask.description = newDes;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return await apiCall.patch(
          EndpointType.TASK_UPDATE_DESCRIPTION,
          newDes,
          {
            params: { task_id: id },
          }
        );
    },
    async assignUserToTask(user_id: number, task_id?: string) {
      const id = task_id ? task_id : this.currentTask.task_id;
      return await apiCall.post(EndpointType.TASK_ASSIGN_USER, null, {
        params: { task_id: id, user_id: user_id },
      });
    },
    async unassignUserFromTask(user_id: number, task_id?: string) {
      const id = task_id ? task_id : this.currentTask.task_id;
      return await apiCall.post(EndpointType.TASK_UNASSIGN_USER, null, {
        params: { task_id: id, user_id: user_id },
      });
    },
    async autoAssignTask(task_id: string, ws_id: number) {
      if (this.offlineMode) {
        const userStore = useUserStore();
        const user = mapLocalUserToUserLite(userStore.local);
        const task = taskUtils.mapTaskToTaskLite(this.currentTask);
        userStore.local.designated_tasks?.push(task);
        userStore.saveLocal();
        this.currentTask.designated_to?.push(user);
        this.currentTask.status = Status.PROGRESS;
        this.saveLocalTask(this.currentTask);
        return this.currentTask;
      } else
        return await apiCall.post(
          EndpointType.TASK_AUTOASSIGN,
          {},
          { params: { task_id: task_id, ws_id: ws_id } }
        );
    },
    async createTask(task: Task) {
      this.offlineMode = this.checkOfflineMode();
      if (this.offlineMode) {
        task.task_id = utils.generateUUID(); //NOTE: GENERATE UUID
        task.child_tasks = [];
        task.dependencies = [];
        task.designated_to = [];
        task.task_tags = [];
        task.updates = [];
        this.saveLocalTask(task);
        const projectStore = useProjectStore();
        let current = projectStore.getCurrent() as Workspace;
        const taskLite = taskUtils.mapTaskToTaskLite(task);
        if (current.tasks) current?.tasks.push(taskLite);
        else {
          current.tasks = [];
          current.tasks.push(taskLite);
        }
        projectStore.saveWorkspaceToLocalStorage(current as Workspace);
        const userStore = useUserStore();
        userStore.local.created_tasks?.push(taskLite);
        userStore.saveLocal();
        return task;
      } else return await apiCall.post(EndpointType.TASK_NEW, task);
    },
    async importTasks(tasks: Task[], ws_id: number) {
      const res = (await apiCall.post(EndpointType.TASK_IMPORT, tasks, {
        params: { ws_id: ws_id },
      })) as TaskWithReference[];
      return res;
    },
    async deleteTask(id: string) {
      if (this.offlineMode) {
        const projectStore = useProjectStore();
        const ws = projectStore.getCurrent() as Workspace;
        let project = projectStore.getLocalStorageWorkspaceById(
          ws.workspace_id as number
        ) as unknown as JSONWorkspace;
        let tasks = project.tasks;
        tasks = tasks?.filter((t: Task) => t.task_id != id);
        project.tasks = tasks;
        projectStore.saveJSONWStoLocalStorage(project);
        const userStore = useUserStore();
        userStore.local.created_tasks = userStore.local.created_tasks?.filter(
          (t: TaskLite) => t.task_id !== id
        );
        userStore.local.designated_tasks =
          userStore.local.designated_tasks?.filter(
            (t: TaskLite) => t.task_id !== id
          );
        userStore.saveLocal();
        return { message: "task deleted" };
      } else
        return await apiCall.del(EndpointType.TASK_DELETE, {
          params: { id: id },
        });
    },
  },
});
