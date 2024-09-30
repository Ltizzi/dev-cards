import { defineStore } from "pinia";
import {
  CustomConfiguration,
  Glosary,
  SpecialTag,
  TagPool,
  TaskSlim,
  UITag,
  Workspace,
} from "../utils/types";
import { useApiCall } from "../composables/useAPICall";
import { EndpointType } from "../utils/endpoints";
import { useProjectStore } from "./project.store";
import { useUIStore } from "./ui.store";
import { JSONWorkspace } from "../utils/types";
import { utils } from "../utils/utils";
import { createCustomConfiguration } from "../utils/client.utils";

const apiCall = useApiCall();

export const useConfigStore = defineStore("configs", {
  state: () => ({
    current: {} as CustomConfiguration,
    offlineMode: false,
  }),
  actions: {
    setCurrent(config: CustomConfiguration) {
      this.current = config;
    },
    checkOfflineMode() {
      const UIStore = useUIStore();
      this.offlineMode = UIStore.getOfflineMode();
      return this.offlineMode;
    },

    async getCurrent() {
      this.checkOfflineMode();
      if (this.offlineMode) {
        if (!this.current.config_id) {
          const projectStore = useProjectStore();
          const id = projectStore.getCurrent()?.workspace_id as number;
          console.log(id);
          this.current = projectStore.getLocalStorageWorkspaceById(id)
            ?.customConfiguration as unknown as CustomConfiguration;
          console.log("CURRENT CONFIG:");
          console.log(this.current);
          return this.current;
        } else return this.current;
      }
      if (this.current.config_id != null) return this.current;
      else {
        const projectStore = useProjectStore();
        const ws = projectStore.getCurrent() as Workspace;

        const ws_id = ws
          ? ws.workspace_id
          : +JSON.parse(localStorage.getItem("current_workspace_id") as string);
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
      // if (this.offlineMode) {
      //   const projectStore = useProjectStore();
      //   const local_ws = projectStore.getLocalStorageWorkspaceById(id);
      //   this.current =
      // } else {
      const response = (await apiCall.get(EndpointType.CONFIG_GET_BY_WS_ID, {
        params: {
          ws_id: id,
        },
      })) as CustomConfiguration;
      this.setCurrent(response);
      return response;
      //}
    },
    createConfig(ws: Workspace) {
      const config = createCustomConfiguration(ws);
      this.current = config;
    },
    async saveConfig(id: number, config: CustomConfiguration) {
      if (this.offlineMode) {
        const projectStore = useProjectStore();
        const jws = projectStore.getLocalStorageWorkspaceById(
          id
        ) as unknown as JSONWorkspace;
        jws.customConfiguration = config;
        this.current = config;
        jws.update_at = new Date(Date.now());
        projectStore.saveJSONWStoLocalStorage(jws);
        return jws.customConfiguration;
      } else
        return await apiCall.post(EndpointType.CONFIG_SAVE, config, {
          params: { ws_id: id },
        });
    },
    async updateConfig(id: number, ws_id: number, config: CustomConfiguration) {
      if (this.offlineMode) {
        await this.saveConfig(ws_id, config);
      } else
        return await apiCall.patch(EndpointType.CONFIG_UPDATE, config, {
          params: { ws_id: ws_id, config_id: id },
        });
    },
    async addGlosary(id: number, ws_id: number, glosary: Glosary) {
      if (this.offlineMode) {
        glosary.id = utils.generateRandomId();
        this.current.customGlosaries.push(glosary);
        await this.saveConfig(ws_id, this.current);
        return this.current.customGlosaries;
      } else
        return await apiCall.post(EndpointType.CONFIG_ADD_GLOSARY, glosary, {
          params: {
            ws_id: ws_id,
            config_id: id,
          },
        });
    },
    async removeGlosary(ws_id: number, config_id: number, glosary: Glosary) {
      if (this.offlineMode) {
        let cg = this.current.customGlosaries;
        cg = cg.filter((g: Glosary) => g.id !== glosary.id);
        this.current.customGlosaries = cg;
        await this.saveConfig(ws_id, this.current);
        return cg;
      } else
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
      if (this.offlineMode) {
        let cg = this.current.customGlosaries;
        cg = cg.map((g: Glosary) => {
          if (g.id === id) {
            g = glosary;
          }
          return g;
        });
        this.current.customGlosaries = cg;
        this.saveConfig(ws_id, this.current);
        return cg;
      } else
        return await apiCall.patch(
          EndpointType.CONFIG_UPDATE_GLOSARY,
          glosary,
          {
            params: {
              ws_id: ws_id,
              config_id: config_id,
              id: id,
            },
          }
        );
    },
    async addSpecialTag(ws_id: number, config_id: number, tag: SpecialTag) {
      if (this.offlineMode) {
        let special_tags = this.current.tagPool.specialTags;
        if (!special_tags.includes(tag)) {
          special_tags.push(tag);
          this.current.tagPool.specialTags = special_tags;
          this.saveConfig(ws_id, this.current);
          return this.current.tagPool;
        }
      } else
        return await apiCall.post(EndpointType.CONFIG_ADD_SPECIAL_TAG, tag, {
          params: {
            ws_id: ws_id,
            config_id: config_id,
          },
        });
    },
    async removeSpecialTag(ws_id: number, config_id: number, id: number) {
      if (this.offlineMode) {
        let special_tags = this.current.tagPool.specialTags;
        special_tags = special_tags.filter((sp: SpecialTag) => sp.id !== id);
        this.current.tagPool.specialTags = special_tags;
        this.saveConfig(ws_id, this.current);
        return this.current.tagPool;
      } else
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
      if (this.offlineMode) {
        let special_tags = this.current.tagPool.specialTags;
        special_tags = special_tags.map((sp: SpecialTag) => {
          if (sp.id === tag.id) {
            sp = tag;
          }
          return sp;
        });
        this.current.tagPool.specialTags = special_tags;
        this.saveConfig(ws_id, this.current);
        return this.current.tagPool;
      } else
        return await apiCall.patch(
          EndpointType.CONFIG_UPDATE_SPECIAL_TAG,
          tag,
          {
            params: {
              ws_id: ws_id,
              config_id: config_id,
              id: id,
            },
          }
        );
    },
    async checkTagInPool(tag: UITag) {
      if (this.current) return this.current.tagPool.tags.includes(tag);
      else {
        return (await this.getCurrent()).tagPool.tags.includes(tag);
      }
    },
    async addTagToPool(ws_id: number, config_id: number, tag: UITag) {
      // console.log("ws:", ws_id);
      // console.log("config: ", config_id);
      // console.log("tag: ", tag);
      // console.log(await this.checkTagInPool(tag));
      const alreadySaved = await this.checkTagInPool(tag);
      if (!alreadySaved)
        if (this.offlineMode) {
          this.current.tagPool.tags.push(tag);
          this.saveConfig(ws_id, this.current);
          return this.current.tagPool;
        } else
          return await apiCall.post(EndpointType.CONFIG_ADD_TAG, tag, {
            params: {
              ws_id: ws_id,
              config_id: config_id,
            },
          });
    },
    async deleteTagFromPool(ws_id: number, config_id: number, tag: UITag) {
      if (this.offlineMode) {
        let tags = this.current.tagPool.tags;
        tags = tags.filter(
          (t: UITag) => t.name.toLowerCase() !== tag.name.toLowerCase()
        );
        this.current.tagPool.tags = tags;
        this.saveConfig(ws_id, this.current);
        return this.current.tagPool;
      } else
        return await apiCall.del(EndpointType.CONFIG_REMOVE_TAG, {
          params: {
            ws_id: ws_id,
            config_id: config_id,
          },
          data: tag,
        });
    },
    async updateTagFromPool(ws_id: number, config_id: number, tag: UITag) {
      if (this.offlineMode) {
        let tags = this.current.tagPool.tags;
        tags = tags.map((t: UITag) => {
          if (t.name.toLowerCase() === tag.name.toLowerCase()) t = tag;
          return t;
        });
        this.current.tagPool.tags = tags;
        this.saveConfig(ws_id, this.current);
        return this.current.tagPool;
      } else
        return await apiCall.patch(EndpointType.CONFIG_UPDATE_TAG, tag, {
          params: {
            ws_id: ws_id,
            config_id: config_id,
          },
        });
    },

    async saveTagPool(ws_id: number, config_id: number, pool: TagPool) {
      if (this.offlineMode) {
        this.current.tagPool = pool;
        await this.saveConfig(
          this.current.workspace.workspace_id,
          this.current
        );
        return this.current.tagPool;
      } else
        return await apiCall.post(EndpointType.CONFIG_SAVE_TAG_POOL, pool, {
          params: {
            ws_id: ws_id,
            config_id: config_id,
          },
        });
    },
  },
});
