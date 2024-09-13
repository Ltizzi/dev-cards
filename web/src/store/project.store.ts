import { defineStore } from "pinia";
import { Workspace } from "../utils/types";
import { useUserStore } from "./user.store";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";

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
  }),
  actions: {
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
    setCurrent(project: Workspace) {
      if (project) {
        this.current = project;
        this.currentName = project.project_name;
        this.currentAvatar = project.avatar;
        localStorage.setItem(
          "current_workspace_id",
          project.workspace_id.toString()
        );
      }
    },
    async fetchProjectById(id: number) {
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
    },
    async fetchProjectsByUser(userId: number) {
      if (userId) {
        const workspaces = (await apiCall.get(EndpointType.USER_MEMBER, {
          params: { user_id: userId },
        })) as Array<Workspace>;
        this.setMemberOf(workspaces);
        return workspaces;
      }
    },
    async createWorkspace(ws: Workspace) {
      return await apiCall.post(EndpointType.WORKSPACE_NEW, ws);
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
      return await apiCall.patch(
        EndpointType.WORKSPACE_UPDATE_NAME,
        {},
        { params: { ws_id: ws_id, name: name } }
      );
    },
    async updateWorkspaceAvatar(ws_id: number, url: string) {
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
    checkIsLocal() {
      return this.isLocal;
    },
    loadLocal(ws: Workspace) {
      this.local = ws;
      this.localList.push(ws);
      this.isLocal = true;
    },
    clean() {
      this.current = {} as Workspace;
      this.currentName = "";
      this.currentAvatar = "";
    },
  },
});
