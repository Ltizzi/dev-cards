import { defineStore } from "pinia";
import {
  CustomConfiguration,
  Glosary,
  SpecialTag,
  TagPool,
  TaskSlim,
  UITag,
} from "../utils/types";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { useProjectStore } from "./project.store";

const apiCall = useApiCall();

export const useConfigStore = defineStore("configs", {
  state: () => ({
    current: {} as CustomConfiguration,
  }),
  actions: {
    setCurrent(config: CustomConfiguration) {
      this.current = config;
    },
    async getCurrent() {
      if (this.current.config_id != null) return this.current;
      else {
        const projectStore = useProjectStore();
        const ws_id = projectStore.current.workspace_id;
        const response = await this.fetchByWorkspaceId(ws_id);
        this.setCurrent(response);
        return response;
      }
    },
    async getTags(): Promise<UITag[]> {
      if (this.current.config_id != null) return this.current.tagPool.tags;
      else {
        return (await this.getCurrent()).tagPool.tags;
      }
    },
    async getSpecialTags(): Promise<SpecialTag[]> {
      if (this.current.config_id != null)
        return this.current.tagPool.specialTags;
      else {
        return (await this.getCurrent()).tagPool.specialTags;
      }
    },
    async getFlaggedTags(): Promise<TaskSlim[]> {
      if (this.current.config_id != null) return this.current.flagged_tasks;
      else {
        return (await this.getCurrent()).flagged_tasks;
      }
    },
    async getGLosaries(): Promise<Glosary[]> {
      if (this.current.config_id != null) return this.current.customGlosaries;
      else {
        return (await this.getCurrent()).customGlosaries;
      }
    },
    async getGlosaryByType(type: string): Promise<Glosary | undefined> {
      if (this.current.config_id != null)
        return this.current.customGlosaries.find(
          (g: Glosary) => g.type.toLowerCase() === type.toLowerCase()
        );
      else {
        return (await this.getCurrent()).customGlosaries.find(
          (g: Glosary) => g.type.toLowerCase() === type.toLowerCase()
        );
      }
    },

    async fetchById(id: number, ws_id: number) {
      return await apiCall.get(EndpointType.CONFIG_GET_BY_ID, {
        params: {
          id: id,
          ws_id: ws_id,
        },
      });
    },
    async fetchByWorkspaceId(id: number) {
      const response = (await apiCall.get(EndpointType.CONFIG_GET_BY_WS_ID, {
        params: {
          ws_id: id,
        },
      })) as CustomConfiguration;
      this.setCurrent(response);
      return response;
    },
    async saveConfig(id: number, config: CustomConfiguration) {
      return await apiCall.post(EndpointType.CONFIG_SAVE, config, {
        params: { ws_id: id },
      });
    },
    async updateConfig(id: number, ws_id: number, config: CustomConfiguration) {
      return await apiCall.patch(EndpointType.CONFIG_UPDATE, config, {
        params: { ws_id: ws_id, config_id: id },
      });
    },
    async addGlosary(id: number, ws_id: number, glosary: Glosary) {
      return await apiCall.post(EndpointType.CONFIG_ADD_GLOSARY, glosary, {
        params: {
          ws_id: ws_id,
          config_id: id,
        },
      });
    },
    async removeGlosary(ws_id: number, config_id: number, glosary: Glosary) {
      return await apiCall.del(EndpointType.CONFIG_REMOVE_GLOSARY, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
        },
        data: glosary,
      });
    },
    async updateGlosary(
      ws_id: number,
      config_id: number,
      glosary: Glosary,
      id: number
    ) {
      return await apiCall.patch(EndpointType.CONFIG_UPDATE_GLOSARY, glosary, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
          id: id,
        },
      });
    },
    async addSpecialTag(ws_id: number, config_id: number, tag: SpecialTag) {
      return await apiCall.post(EndpointType.CONFIG_ADD_SPECIAL_TAG, tag, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
        },
      });
    },
    async removeSpecialTag(ws_id: number, config_id: number, id: number) {
      return await apiCall.del(EndpointType.CONFIG_REMOVE_SPECIAL_TAG, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
          id: id,
        },
      });
    },
    async updateSpecialTag(
      ws_id: number,
      config_id: number,
      id: number,
      tag: SpecialTag
    ) {
      return await apiCall.patch(EndpointType.CONFIG_UPDATE_SPECIAL_TAG, tag, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
          id: id,
        },
      });
    },
    async checkTagInPool(tag: UITag) {
      if (this.current) return this.current.tagPool.tags.includes(tag);
      else {
        return (await this.getCurrent()).tagPool.tags.includes(tag);
      }
    },
    async addTagToPool(ws_id: number, config_id: number, tag: UITag) {
      if (!this.checkTagInPool(tag))
        return await apiCall.post(EndpointType.CONFIG_ADD_TAG, tag, {
          params: {
            ws_id: ws_id,
            config_id: config_id,
          },
        });
    },
    async deleteTagFromPool(ws_id: number, config_id: number, tag: UITag) {
      return await apiCall.del(EndpointType.CONFIG_REMOVE_TAG, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
        },
        data: tag,
      });
    },
    async updateTagFromPool(ws_id: number, config_id: number, tag: UITag) {
      return await apiCall.patch(EndpointType.CONFIG_UPDATE_TAG, tag, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
        },
      });
    },
    async saveTagPool(ws_id: number, config_id: number, pool: TagPool) {
      return await apiCall.post(EndpointType.CONFIG_SAVE_TAG_POOL, pool, {
        params: {
          ws_id: ws_id,
          config_id: config_id,
        },
      });
    },
  },
});
