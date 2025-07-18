<template lang="">
  <div class="flex flex-col justify-start">
    <input
      type="file"
      class="file-input file-input-bordered file-input-primary w-full max-w-xs file-input-xs lg:file-input-sm xl:file-lg"
      accept=".json"
      @change="handleInputChange"
    />
    <button class="btn btn-outline btn-primary" @click="importWorkspace">
      Submit
    </button>
  </div>
</template>
<script setup lang="ts">
  import { reactive, ref } from "vue";
  import {
    ImportProcess,
    JSONWorkspace,
    LevelBatch,
    Task,
    TaskBatch,
    User,
    UserLite,
    Workspace,
  } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useProjectStore } from "../../store/project.store";
  import { mapUserToUserLite } from "../../utils/auth.utils";
  import { useTaskStore } from "../../store/task.store";
  import { useConfigStore } from "../../store/config.store";

  const userStore = useUserStore();
  const wsStore = useProjectStore();
  const taskStore = useTaskStore();
  const configStore = useConfigStore();

  const importState: ImportProcess = reactive({
    phase: "analyzing",
    total: 0,
    processed: 0,
    errors: [],
    currentLevel: 0,
    totalLevels: 0,
    globalMapping: {},
    levelBatches: [],
  });

  const fileContent = ref<JSONWorkspace>();

  function handleInputChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) return;

    const reader = new FileReader();

    reader.onload = (event) => {
      const content = event.target?.result as string;
      try {
        fileContent.value = JSON.parse(content);
      } catch (err: any) {
        console.error("Invalid JSON file: ", err);
      }
    };

    reader.onerror = () => {
      console.error("An Error ocurred while reading file: ", reader.error);
    };
    reader.readAsText(file);
  }

  async function importWorkspace() {
    const jsonData = fileContent.value as JSONWorkspace;
    const user = userStore.self;

    importState.total = jsonData.tasks.length;

    try {
      const updatedData = updateLocalReferences(jsonData, user);

      const dependencyLevels = analyzeDependencies(jsonData.tasks);

      importState.phase = "workspace";

      const wsToImport = jsonData.workspace;
      const wsToCreate = {} as Workspace;
      wsToCreate.owner = mapUserToUserLite(user);
      wsToCreate.project_name = wsToImport.project_name;
      wsToCreate.description = wsToImport.description;
      wsToCreate.avatar = wsToImport.avatar;
      wsToCreate.created_at = wsToImport.created_at;

      // const ws = (await wsStore.createWorkspace(wsToCreate)) as Workspace;
      const ws = {} as Workspace;
      ws.workspace_id = 999;
      importState.workspace = ws;

      importState.levelBatches = dependencyLevels.map((levelTasks, index) => ({
        level: index,
        batches: createBatches(levelTasks, 10),
        status: "pending",
      }));

      importState.phase = "level_processing";

      for (let level = 0; level < importState.levelBatches.length; level++) {
        importState.currentLevel = level;
        await processLevel(
          importState.levelBatches[level],
          ws.workspace_id as number
        );
      }
      importState.phase = "customConfig";

      console.log(importState);

      //TODO: upload customConfig

      //const custConfig = await configStore.importCustomConfig(jsonData.customConfiguration      );
    } catch (err: any) {
      importState.phase = "error";
      importState.errors.push({ type: "critical", message: err.message });
    }
  }

  function updateLocalReferences(jsonData: JSONWorkspace, user: User) {
    const updatedData = { ...jsonData };

    updatedData.user = {
      user_id: user.user_id,
      username: user.username,
      email: user.email,
      avatar: user.avatar,
    };

    //NOTE: VER Q PASA ACA
    updatedData.tasks = updatedData.tasks.map((task) => ({
      ...task,
      owner: updatedData.user as UserLite,
      blocked_by: task.blocked_by
        ? (updatedData.user as UserLite)
        : ({} as UserLite),
      designated_to:
        (task.designated_to?.map((u: any) =>
          u.local ? updatedData.user : u
        ) as UserLite[]) || ([updatedData.user] as UserLite[]),
    }));
    return updatedData;
  }

  function analyzeDependencies(tasks: Task[]): Task[][] {
    const taskMap = new Map(tasks.map((t) => [t.task_id, t]));
    const levels: Task[][] = [];
    const processed = new Set<number>();

    while (processed.size < tasks.length) {
      const currentLevel: Task[] = [];

      for (const task of tasks) {
        if (processed.has(task.task_id as number)) continue;

        const dependencyIds =
          task.dependencies?.map((dep) => dep.task_id) || [];
        const childTaskIds =
          task.child_tasks?.map((child) => child.task_id) || [];
        const allDependencies = [...dependencyIds, ...childTaskIds];

        const validDependencies = allDependencies.filter((depId) =>
          taskMap.has(depId)
        );

        const canProcess =
          validDependencies.length === 0 ||
          validDependencies.every((depId) => processed.has(depId));

        if (canProcess) currentLevel.push(task);

        // const allDependenciesResolved = allDependencies.every(
        //   (depId) => processed.has(depId) || taskMap.has(depId)
        // );

        // if (allDependencies.length === 0 || allDependenciesResolved) {
        //   currentLevel.push(task);
        // }
      }

      if (currentLevel.length === 0) {
        const remaining = tasks.filter(
          (t) => !processed.has(t.task_id as number)
        );

        if (remaining.length > 0) {
          console.warn(
            "Dependencias ciruclares o no resuletas detectadas. Procesando:",
            remaining[0]
          );
          currentLevel.push(remaining[0]);
        } else {
          break;
        }

        //currentLevel.push(...remaining);
      }

      currentLevel.forEach((task) => processed.add(task.task_id as number));
      levels.push([...currentLevel]);
    }

    console.log(
      "Niveles de dependencias:",
      levels.map((lvl, i) => ({
        nivel: i,
        tareas: lvl.length,
        ids: lvl.map((t) => t.task_id),
      }))
    );

    return levels;
  }

  async function processLevel(levelBatch: LevelBatch, ws_id: number) {
    levelBatch.status = "processing";

    for (const batch of levelBatch.batches) {
      await processBatch(batch, ws_id);
    }

    levelBatch.status = "completed";

    return levelBatch;
  }

  async function processBatch(batch: TaskBatch, ws_id: number) {
    batch.status = "processing";
    try {
      const resolvedTasks = batch.tasks.map((t) => resolveTaskReferences(t));

      const tasksForAPI = resolvedTasks.map((task) => ({
        ...task,
        workspace_id: ws_id,
      }));

      //TODO: API y store methods to upload tasks batches
      console.log("TASKS FOR API: ");
      console.log(tasksForAPI);
      console.log("**************");
      const createdTasks = tasksForAPI; //[] as Task[];

      batch.tasks.forEach((localTask, index) => {
        const remoteTask = createdTasks[index];
        const i = localTask.task_id as number;
        batch.idMapping[i] = remoteTask.task_id as number;
        importState.globalMapping[i] = remoteTask.task_id as number;
      });

      batch.status = "completed";
      importState.processed += batch.tasks.length;
    } catch (err) {
      batch.status = "error";
      batch.retryCount++;

      if (batch.retryCount < 3) {
        await processBatch(batch, ws_id);
      } else {
        console.error(err);
        throw err;
      }
    }
  }

  function createBatches(tasks: Task[], batchSize: number): TaskBatch[] {
    const batches: TaskBatch[] = [];

    for (let i = 0; i < tasks.length; i += batchSize) {
      batches.push({
        id: `batch-${i / batchSize}`,
        tasks: tasks.slice(i, i + batchSize),
        status: "pending",
        retryCount: 0,
        idMapping: {},
      });
    }
    return batches;
  }

  function resolveTaskReferences(task: Task) {
    return {
      ...task,
      dependencies:
        task.dependencies?.map(
          (depTask) =>
            importState.globalMapping[depTask.task_id] || depTask.task_id
        ) || [],
      child_tasks:
        task.child_tasks?.map(
          (childTask) =>
            importState.globalMapping[childTask.task_id] || childTask.task_id
        ) || [],
      task_tags: task.task_tags || [],
      updates: task.updates || [],
      progressItems: task.progressItems || [],
    };
  }
</script>
