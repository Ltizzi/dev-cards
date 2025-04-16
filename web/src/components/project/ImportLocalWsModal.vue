<template>
  <div></div>
</template>
<script setup lang="ts">
  import { reactive } from "vue";
  import { useConfigStore } from "../../store/config.store";
  import { useProjectStore } from "../../store/project.store";
  import { useTaskStore } from "../../store/task.store";
  import { useUserStore } from "../../store/user.store";

  import {
    CustomConfiguration,
    JSONWorkspace,
    Task,
    TaskLite,
    UserLite,
    Workspace,
    WorkspaceWithJwt,
  } from "../../utils/types";
  import { workspaceUtils } from "../../utils/workspace.utils";

  const state = reactive({
    error: false,
    errorMsg: "",
    showedMsg: "",
    ws_saved: false,
    workspace: {} as Workspace,
    ws_id: 0,
    config_saved: false,
  });

  const projectStore = useProjectStore();
  const configStore = useConfigStore();
  const taskStore = useTaskStore();
  const userStore = useUserStore();

  function changeMsg(msg: string) {
    state.showedMsg = msg + "...";
  }

  async function importJSONWorkspace(jsw: JSONWorkspace) {
    //WORKSPACE
    changeMsg("Preparing Workspace");
    const ws = prepareWs(jsw.workspace);
    await importWorkspace(ws);
    if (!state.ws_saved) return;

    //CONFIGURATION
    changeMsg("Preparing Custom Configuration");
    const config = prepareConfiguration(
      jsw.customConfiguration,
      state.workspace
    );
    await importConfigurations(config);
    if (!state.config_saved) return;

    changeMsg("Preparing tasks");
    const tasks = jsw.tasks;
  }

  async function importWorkspace(ws: Workspace) {
    changeMsg("Saving Project in the DB");
    const ws_response = (await projectStore.createWorkspace(
      ws
    )) as WorkspaceWithJwt;
    if (ws_response.workspace) {
      changeMsg("Project saved");
      state.ws_saved = true;
      state.workspace = ws_response.workspace;
      state.ws_id = state.workspace.workspace_id as number;
    } else {
      console.error(ws_response);
      state.error = true;
      state.errorMsg = "Something went wrong importing the workspace";
    }
  }

  function prepareWs(ws: Workspace) {
    ws.workspace_id = undefined;
    ws.owner = userStore.getSelfAsUserLite();
    ws.tasks = [] as TaskLite[];
    return ws;
  }

  function prepareConfiguration(config: CustomConfiguration, ws: Workspace) {
    config.config_id = undefined;
    config.workspace = workspaceUtils.mapWsToWsLite(ws);
    return config;
  }

  async function importConfigurations(config: CustomConfiguration) {
    changeMsg("Saving Custom Configuration in the database");
    const config_response = (await configStore.saveConfig(
      state.ws_id,
      config
    )) as CustomConfiguration;

    if (
      config_response.config_id &&
      config_response.workspace.workspace_id == state.ws_id
    ) {
      changeMsg("Custom Configuration Saved");
      state.config_saved = true;
    } else {
      console.error(config_response);
      state.error = true;
      state.errorMsg =
        "Something went wrong importing the Custom Configuration";
    }
  }

  function prepareTasks(tasks: Task[]) {
    return tasks.map((t: Task) => {
      t.owner = userStore.getSelfAsUserLite();
      if (t.designated_to && t.designated_to?.length > 0) {
        t.designated_to = [] as UserLite[];
        t.designated_to.push(t.owner);
      }
      if (t.blocked_by?.user_id) t.blocked_by = t.owner;

      return t;
    });
  }

  async function importTasks(tasks: Task[]) {
    let parent_tasks = [] as TaskLite[];
    let saved_parent_tasks = [] as Task[];
    let saved_tasks = [] as Task[];

    changeMsg("Resolving dependencies");
    tasks.sort((a, b) => (a.task_id as number) - (b.task_id as number));
    tasks.forEach((t: Task) => {
      if (t.dependencies && t.dependencies.length > 0) {
        t.dependencies.forEach((pt: TaskLite) => parent_tasks.push(pt));
      }
    });
  }
</script>
