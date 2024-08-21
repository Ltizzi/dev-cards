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
    member: [] as Array<Workspace>,
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
    setMember(list: Array<Workspace>) {
      this.member = list;
    },
    setCurrent(project: Workspace) {
      this.current = project;
      this.currentName = project.project_name;
      this.currentAvatar = project.avatar;
    },
    async fetchProjectById(id: number) {
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: id },
      })) as Workspace;
      if (response.workspace_id == id) {
        this.setCurrent(response);
        // this.current = response;
        // this.currentName = response.project_name;
        // this.currentAvatar = response.avatar;
        if (!this.justUpdated) {
          this.justUpdated = true;
          setTimeout(() => {
            this.justUpdated = false;
          }, 1000);
        }

        return this.current;
      }
    },
    async updateCurrent() {
      return await this.fetchProjectById(this.current.workspace_id);
    },
    async updateCurrentById(id: number) {
      return await this.fetchProjectById(id);
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
