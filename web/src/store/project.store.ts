import { defineStore } from "pinia";
import { Workspace } from "../utils/types";
import { useUserStore } from "./user.store";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";

const apiCall = useApiCall();

export const useProjectStore = defineStore("projects", {
  state: () => ({
    owned: [] as Array<Workspace>,
    member: [] as Array<Workspace>,
    byId: {} as Workspace,
    current: {} as Workspace,
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
    setMember(list: Array<Workspace>) {
      this.member = list;
    },
    setById(project: Workspace) {
      this.byId = project;
    },
    setCurrent(project: Workspace) {
      this.current = project;
    },
    async updateCurrent() {
      const response = (await apiCall.get(EndpointType.WORKSPACE_GET_BY_ID, {
        params: { id: this.current.workspace_id },
      })) as Workspace;
      if (response.workspace_id == this.current.workspace_id)
        this.current = response;
    },
  },
});
