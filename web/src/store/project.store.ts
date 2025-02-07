import { defineStore } from "pinia";
import {
  CustomConfiguration,
  JSONWorkspace,
  Task,
  TaskLite,
  UserLocal,
  Workspace,
  WorkspaceLite,
} from "../utils/types";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { useUIStore } from "./ui.store";
import { useConfigStore } from "./config.store";
import { useUserStore } from "./user.store";
import { workspaceUtils } from "../utils/workspace.utils";
import { utils } from "../utils/utils";
import { taskUtils } from "../utils/task.utils";

const apiCall = useApiCall();

export const useProjectStore = defineStore("projects", {
  state: () => ({
    isLocal: false,
    owned: [] as Array<Workspace>,
    localList: [] as Array<Workspace>,
    local: {} as Workspace,
    memberOf: [] as Array<Workspace>,
    current: {} as Workspace,
    currentName: "" as string,
    currentAvatar: "" as string,
    justCreated: false,
    justUpdated: false,
    offlineMode: false,
  }),
  actions: {
    checkOfflineMode() {
      const UIStore = useUIStore();
      this.offlineMode = UIStore.getOfflineMode();
      return this.offlineMode;
    },
    setOwned(list: Array<Workspace>) {
      this.owned = list;
    },
    addProjectToOwner(project: Workspace) {
      const user = JSON.parse(localStorage.getItem("user") as string);
      if (user && project.owner.user_id == user.user_id) {
        this.owned.push(project);
      }
    },
    setJustCreated() {
      this.justCreated = true;
      setTimeout(() => {
        this.justCreated = false;
      }, 1000 * 60);
    },
    setMemberOf(list: Array<Workspace>) {
      this.memberOf = list;
    },
    async setCurrent(project: Workspace) {
      this.checkOfflineMode();
      if (project) {
        this.current = project;
        if (this.offlineMode) {
          const configStore = useConfigStore();
          const config =
            (await configStore.getCurrent()) as CustomConfiguration;
        } //this.setLocal(project); else
        this.current = project;
        this.currentName = project.project_name;
        this.currentAvatar = project.avatar;
        localStorage.setItem(
          "current_workspace_id",
          project.workspace_id.toString()
        );
      }
    },
    getCurrent() {
      this.checkOfflineMode();
      return this.current;
      // if (this.offlineMode) return this.local;
      // else {
      //   if (this.current) this.current;
      // }
    },
    setLocal(ws: Workspace) {
      //this.local = ws;
      this.setCurrent(ws);
    },
    saveWorkspacesToLocalStorage(arr: JSONWorkspace[]) {
      localStorage.setItem("localWorkspaces", JSON.stringify(arr));
    },
    async saveWorkspaceToLocalStorage(ws: Workspace) {
      let workspaces = this.getLocalStorageWorkspaces();
      if (workspaces && this.getLocalStorageWorkspaceById(ws.workspace_id)) {
        workspaces = workspaces.map((jws: JSONWorkspace) => {
          if (jws.workspace.workspace_id === ws.workspace_id) {
            jws.workspace = ws;
            jws.update_at = new Date(Date.now());
          }
          return jws;
        });
        this.saveWorkspacesToLocalStorage(workspaces);
      } else {
        this.saveJSONWStoLocalStorage(await this.createJSONWorkspace(ws));
      }
    },
    checkJWSSavedInLocalStorage(jws: JSONWorkspace) {
      return (
        this.getLocalStorageWorkspaces().filter(
          (ljws: JSONWorkspace) =>
            ljws.workspace.workspace_id === jws.workspace.workspace_id
        ).length > 0
      );
    },
    saveJSONWStoLocalStorage(jws: JSONWorkspace) {
      let workspaces = this.getLocalStorageWorkspaces();
      if (this.checkJWSSavedInLocalStorage(jws)) {
        workspaces = workspaces.map((ljws: JSONWorkspace) => {
          if (ljws.workspace.workspace_id === jws.workspace.workspace_id) {
            ljws = jws;
            let tasks = ljws.tasks;
            let lite_tasks: TaskLite[] = tasks.map((t: Task) =>
              taskUtils.mapTaskToTaskLite(t)
            );
            ljws.workspace.tasks = lite_tasks;
          }
          return ljws;
        });
      } else {
        if (workspaces != null && workspaces.length > 0) workspaces.push(jws);
        else {
          workspaces = [] as JSONWorkspace[];
          workspaces.push(jws);
        }
      }
      this.saveWorkspacesToLocalStorage(workspaces);
    },
    async createJSONWorkspace(ws: Workspace) {
      const newJSON: JSONWorkspace = {
        user: {
          nickname: ws.owner.username,
          avatar: ws.owner.avatar,
        } as UserLocal,
        workspace: ws,
        tasks: this.getWorkspaceTasks(ws.workspace_id),
        customConfiguration: await this.getCustomConfiguration(ws.workspace_id),
        created_at: new Date(Date.now()),
        update_at: new Date(Date.now()),
      };
      return newJSON;
    },
    async getCustomConfiguration(ws_id: number) {
      const configStore = useConfigStore();
      const config = (await configStore.getCurrent()) as CustomConfiguration;
      return config && config.workspace.workspace_id === ws_id
        ? config
        : ({} as CustomConfiguration);
    },
    getWorkspaceTasks(ws_id: number): Task[] {
      let arr = [] as Task[];
      //TODO: implement taskFetcher
      if (this.offlineMode) {
      } else {
      }
      return arr;
    },
    getLocalStorageWorkspaces() {
      const ljws = JSON.parse(
        localStorage.getItem("localWorkspaces") as string
      ) as JSONWorkspace[];
      return ljws != null ? ljws : ([] as JSONWorkspace[]);
    },
    getLocalStorageWorkspaceById(id: number) {
      const localProjects = this.getLocalStorageWorkspaces();
      //console.log(localProjects, "....", id);
      return localProjects != null
        ? localProjects.find(
            (ws: JSONWorkspace) => ws.workspace.workspace_id == id
          )
        : ({} as JSONWorkspace);
    },
    getUserLocalStorageWorkspaces(): Workspace[] {
      const localProjects = this.getLocalStorageWorkspaces();
      return localProjects
        ? localProjects.map((ws: JSONWorkspace) => ws.workspace)
        : [];
    },
    async fetchProjectById(id: number) {
      if (this.offlineMode) {
        const ws = this.getLocalStorageWorkspaceById(id)?.workspace;
        //  console.log(ws);
        if (ws?.workspace_id) {
          this.setCurrent(ws);
          return ws;
        }
      } else {
        const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
          params: { id: id },
        })) as Workspace;
        if (response.workspace_id == id) {
          this.setCurrent(response);
          if (!this.justUpdated) {
            this.justUpdated = true;
            setTimeout(() => {
              this.justUpdated = false;
            }, 1000);
          }

          return this.current;
        }
      }
    },
    async fetchProjectsByUser(userId: number) {
      this.offlineMode = this.checkOfflineMode();
      if (this.offlineMode) {
        const workspaces = this.getUserLocalStorageWorkspaces();
        this.setMemberOf(workspaces);
        return workspaces;
      } else {
        if (userId) {
          const workspaces = (await apiCall.get(EndpointType.USER_MEMBER, {
            params: { user_id: userId },
          })) as Array<Workspace>;
          this.setMemberOf(workspaces);
          return workspaces;
        }
      }
    },
    async createWorkspace(ws: Workspace) {
      this.offlineMode = this.checkOfflineMode();
      if (this.offlineMode) {
        ws.workspace_id = utils.generateRandomId();
        // ws.tasks = [];
        ws.users.push(ws.owner);

        //console.log("WS_ID: ", ws.workspace_id);
        this.setCurrent(ws);

        const configStore = useConfigStore();
        configStore.createConfig(ws);
        //const jws = this.createJSONWorkspace(ws);
        this.saveWorkspaceToLocalStorage(ws);
        const userStore = useUserStore();
        userStore.local.workspaces?.push(workspaceUtils.mapWsToWsLite(ws));
        userStore.saveLocal();
        return { workspace: this.current, token: "" };
      } else return await apiCall.post(EndpointType.WORKSPACE_NEW, ws);
    },
    async updateCurrent() {
      return await this.fetchProjectById(this.current.workspace_id);
    },
    async updateCurrentById(id: number) {
      return await this.fetchProjectById(id);
    },
    //INVITE USER TO WS
    async addUserToProjectByEmail(email: string, ws_id: number) {
      return await apiCall.post(
        EndpointType.WORKSPACE_ADD_USER_BY_EMAIL,
        {},
        { params: { ws_id: ws_id, email: email } }
      );
    },
    async deleteWorkspace(id: number) {
      if (this.offlineMode) {
        let workspaces = this.getLocalStorageWorkspaces();
        workspaces = workspaces.filter(
          (ws: JSONWorkspace) => ws.workspace.workspace_id !== id
        );
        this.saveWorkspacesToLocalStorage(workspaces);
        const userStore = useUserStore();
        userStore.local.workspaces = userStore.local.workspaces?.filter(
          (wsl: WorkspaceLite) => wsl.workspace_id != id
        );
        userStore.saveLocal();
      } else
        return await apiCall.del(EndpointType.WORKSPACE_DELETE, {
          params: { id: id },
        });
    },
    async addUserAsMod(ws_id: number, user_id: number) {
      return await apiCall.patch(
        EndpointType.WORKSPACE_ADD_MOD,
        {},
        { params: { ws_id: ws_id, user_id: user_id } }
      );
    },
    async removeMod(ws_id: number, user_id: number) {
      return await apiCall.patch(
        EndpointType.WORKSPACE_REMOVE_MOD,
        {},
        { params: { ws_id: ws_id, user_id: user_id } }
      );
    },
    async updateWorkspaceName(ws_id: number, name: string) {
      if (this.offlineMode && this.current.workspace_id === ws_id) {
        this.current.project_name = name;
        this.saveWorkspaceToLocalStorage(this.current);
        this.justUpdated = true;
        setTimeout(() => {
          this.justUpdated = false;
        }, 1000);
      } else
        return await apiCall.patch(
          EndpointType.WORKSPACE_UPDATE_NAME,
          {},
          { params: { ws_id: ws_id, name: name } }
        );
    },
    async updateWorkspaceAvatar(ws_id: number, url: string) {
      if (this.offlineMode && this.current.workspace_id === ws_id) {
        this.current.avatar = url;
        this.saveWorkspaceToLocalStorage(this.current);
        this.justUpdated = true;
        return this.current;
        setTimeout(() => {
          this.justUpdated = false;
        }, 1000);
      } else
        return await apiCall.patch(
          EndpointType.WORKSPACE_UPDATE_AVATAR,
          {},
          { params: { ws_id: ws_id, url: url } }
        );
    },
    async removeUserFromWorkspace(ws_id: number, user_id: number) {
      return await apiCall.patch(
        EndpointType.WORKSPACE_REMOVE_USER,
        {},
        { params: { ws_id: ws_id, user_id: user_id } }
      );
    },
    async addUserAsCollaborator(ws_id: number, id: number) {
      return await apiCall.patch(
        EndpointType.WORKSPACE_ADD_COLLABORATOR,
        {},
        { params: { ws_id: ws_id, user_id: id } }
      );
    },
    async removeUserAsCollaborator(ws_id: number, id: number) {
      return await apiCall.patch(
        EndpointType.WORKSPACE_REMOVE_COLLABORATOR,
        {},
        { params: { ws_id: ws_id, user_id: id } }
      );
    },
    async updateProjectDescription(id: number, description: string) {
      const newDes = { description: description };
      if (this.offlineMode) {
        this.current.description = description;
        this.saveWorkspaceToLocalStorage(this.current);
      } else {
        const response = (await apiCall.patch(
          EndpointType.WORKSPACE_UPDATE_DESCRIPTION,
          newDes,
          { params: { ws_id: id } }
        )) as Workspace;
        this.setCurrent(response);
      }
      this.justUpdated = true;
      setTimeout(() => (this.justUpdated = false), 1000);
      return this.current;
    },

    checkIsLocal() {
      return this.offlineMode;
    },
    loadLocal(ws: JSONWorkspace) {
      //this.local = ws.workspace;
      this.current = ws.workspace;
      //  let projects = this.getLocalStorageWorkspaces();
      let project = this.getLocalStorageWorkspaceById(
        ws.workspace.workspace_id
      );
      if (project?.workspace.workspace_id === ws.workspace.workspace_id) {
        if (ws.update_at.getTime() > project.update_at.getTime()) project = ws;
      } else {
        this.saveJSONWStoLocalStorage(ws);
      }
      this.offlineMode = true;
      this.isLocal = true;
    },
    clean() {
      this.current = {} as Workspace;
      this.currentName = "";
      this.currentAvatar = "";
      this.owned = [] as Workspace[];
      //this.memberOf = [] as Workspace[];
      const uiStore = useUIStore();
      uiStore.clean();
      const configStore = useConfigStore();
      configStore.clean();
    },
  },
});
