<template lang="">
  <div class="flex flex-col justify-start gap-20 py-5">
    <h1 class="text-3xl text-center font-bold">Import Workspaces</h1>
    <div class="flex flex-col justify-start gap-5" v-if="!isOfflineEnv">
      <h1 class="font-semibold card-title text-xl">
        Save Local Workspace's JSON to the cloud (only accept valid .json files)
      </h1>
      <div class="flex flex-row justify-start gap-5 align-middle">
        <input
          type="file"
          class="file-input file-input-bordered file-input-primary w-full max-w-xs file-input-xs lg:file-input-sm xl:file-lg"
          accept=".json"
          @change="handleInputChange"
        />
        <button
          class="btn btn-outline btn-primary btn-sm"
          @click="importWorkspace"
        >
          Submit
        </button>
      </div>
      <div
        :class="[
          'bg-base-content flex flex-col justify-center gap-3 text-center py-5 items-center text-base-300 duration-500 transition-all ',
          importState.working
            ? 'opacity-100 translate-y-0 '
            : 'opacity-0 -translate-y-40',
        ]"
        v-if="importState.working"
      >
        <h1 class="font-semibold text-lg">
          {{
            importState.phase[0].toUpperCase() +
            importState.phase.slice(1) +
            "..."
          }}
        </h1>
        <p class="text-base italic">
          Tasks: {{ importState.processed + " / " + importState.total }}
        </p>
        <p class="text-base italic">Errors: {{ importState.errors.length }}</p>
        <p class="text-base italic">
          Hierarchy level:
          {{ importState.currentLevel + " / " + importState.totalLevels }}
        </p>
        <div
          class="flex flex-col justify-start gap-2 text-sm"
          v-if="importState.errors.length > 0"
        >
          <p v-for="err in importState.errors">{{ err }}</p>
        </div>
      </div>
    </div>
    <div class="flex flex-col justify-start gap-5" v-else>
      <h1 class="text-xl font-semibold">
        Import Workspace's JSON to the local storage
      </h1>
      <LoadWorkspaceButton />
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onBeforeMount, reactive, ref } from "vue";
  import {
    ImportProcess,
    JSONWorkspace,
    LevelBatch,
    Task,
    TaskBatch,
    TaskLite,
    TaskWithReference,
    User,
    UserLite,
    Workspace,
    WorkspaceLite,
    WorkspaceWithJwt,
  } from "../../utils/types";
  import { useUserStore } from "../../store/user.store";
  import { useProjectStore } from "../../store/project.store";
  import { mapUserToUserLite, saveToken } from "../../utils/auth.utils";
  import { useTaskStore } from "../../store/task.store";
  import { useConfigStore } from "../../store/config.store";
  import { utils } from "../../utils/utils";
  import { taskUtils } from "../../utils/task.utils";
  import { AnyCnameRecord } from "node:dns";
  import { TaskSlim } from "../../utils/types";
  import { workspaceUtils } from "../../utils/workspace.utils";
  import LoadWorkspaceButton from "../ui/LoadWorkspaceButton.vue";

  const userStore = useUserStore();
  const wsStore = useProjectStore();
  const taskStore = useTaskStore();
  const configStore = useConfigStore();

  const tasksMap = ref<Map<string, Task>>(new Map());

  const onlineExportSource = ref<boolean>(false);
  const isOfflineEnv = ref<boolean>(false);
  const userLite = ref<UserLite>();

  const importState: ImportProcess = reactive({
    phase: "analyzing",
    total: 0,
    processed: 0,
    errors: [],
    currentLevel: 0,
    totalLevels: 0,
    globalMapping: {},
    levelBatches: [],
    working: false,
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
    importState.working = true;
    const jsonData = fileContent.value as JSONWorkspace;
    onlineExportSource.value = jsonData.onlineExport as boolean;
    const user = userStore.getSelf();
    userLite.value = mapUserToUserLite(user);
    importState.total = jsonData.tasks.length;

    try {
      const updatedData = updateLocalReferences(jsonData, user);

      const dependencyLevels = analyzeDependencies(jsonData.tasks);

      importState.phase = "workspace";

      const wsToImport = jsonData.workspace;
      const wsToCreate = {} as Workspace;
      wsToCreate.owner = userLite.value;
      wsToCreate.project_name = wsToImport.project_name;
      wsToCreate.description = wsToImport.description;
      wsToCreate.avatar = wsToImport.avatar;
      wsToCreate.created_at = new Date(
        utils.fixDateFormat(wsToImport.created_at) as string
      );
      //NOTE: API CALL

      const res = (await wsStore.createWorkspace(
        wsToCreate
      )) as WorkspaceWithJwt;
      // const res = {
      //   workspace: jsonData.workspace,
      //   token:
      //     "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiVGVzdGluZ1VwbG9hZCIsImV4cCI6MTc1Mzc5OTYxMywiaWF0IjoxNzUzMTk0ODEzLCJzY29wZSI6IlJPTEVfVVNFUiIsInJvbGVzIjpbeyJ3b3Jrc3BhY2VfaWQiOjQ0LCJyb2xlIjoiUk9MRV9PV05FUiIsImFzc2lnbmVkX3Rhc2tzX2lkcyI6W119XX0.EQS07fEVZfKW6pYmAQUUUkIk4pt0JDuxigQ6AnB65J_FOBWiLlwD4TkjLBGA8Qwk69DXxsJdLPak21ptA-JMrsnB-5V1zOO5tCy0X2HAcZDivk-4k_L39P1WmeHbgEhN824Uii_rMOEN0-Q6K5CoGPviHDv53iSNegVhebRm21GlnUAdpj7YiSCzxYA9E9VCSY3Dtfvl8et3gQ22uVjaWDnsg7awHlZlx_-7cPSRtH6jx7wEIDB7PBEyZeMabGn2usE_lFHZC2dSfKJvkEmhX7pm1K3spVCRLLpH3r4Hu5jwnKfDZPwpyjXrHI0YowCrfEEdfZg0vOATOrhWAMFgoA",
      // } as WorkspaceWithJwt;
      wsStore.addProjectToOwner(res.workspace);
      const ws = res.workspace;
      saveToken(res.token);
      wsStore.setCurrent(res.workspace);
      wsStore.setMemberOf([...wsStore.memberOf, res.workspace]);

      importState.workspace = ws;

      importState.levelBatches = dependencyLevels.map((levelTasks, index) => ({
        level: index,
        batches: createBatches(levelTasks, 10),
        status: "pending",
      }));

      importState.phase = "level_processing";

      const ws_lite = workspaceUtils.mapWsToWsLite(ws);
      for (let level = 0; level < importState.levelBatches.length; level++) {
        importState.currentLevel = level;
        await processLevel(importState.levelBatches[level], ws_lite);
      }
      //importState.phase = "customConfig";

      console.log(importState);

      //TODO: upload customConfig

      //const custConfig = await configStore.importCustomConfig(jsonData.customConfiguration      );
      importState.phase = "completed";
      setTimeout(() => {
        importState.working = false;
      }, 10000);
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
    debugTaskDependencies(tasks);
    const taskMap = new Map(tasks.map((t) => [t.task_id, t]));
    const levels: Task[][] = [];
    const processed = new Set<string>();

    console.log(
      "Iniciando análisis de dependencias para ",
      tasks.length,
      " tasks"
    );

    let levelIndex = 0;

    while (processed.size < tasks.length && levelIndex < 100) {
      const currentLevel: Task[] = [];

      console.log(`\n--- Procesando nivel ${levelIndex} ---`);
      console.log("Tasks ya procesadas: ", processed.size);

      for (const task of tasks) {
        if (processed.has(task.task_id as string)) continue;

        const dependencyIds = (task.dependencies || []).map(
          (dep: TaskLite) => dep.task_id
        );
        console.log(`Task ${task.task_id} tiene dependencias: `, dependencyIds);

        const validDependencies = dependencyIds.filter((depId) =>
          taskMap.has(depId)
        );

        const canProcess =
          validDependencies.length === 0 ||
          validDependencies.every((depId) => {
            const isProcessed = processed.has(depId);
            console.log(` Dependencia ${depId} procesada: ${isProcessed}`);
            return isProcessed;
          });

        if (canProcess) {
          console.log(
            ` ✓ Task ${task.task_id} puede ser procesada en nivel ${levelIndex}`
          );
          currentLevel.push(task);
        } else {
          console.log(`✗ Task ${task.task_id} no puede ser procesada aún`);
        }
      }

      if (currentLevel.length === 0) {
        const remaining = tasks.filter(
          (t) => !processed.has(t.task_id as string)
        );

        if (remaining.length > 0) {
          console.error("⚠️ Posible dependencia circular detectada");
          console.error(
            "Tasks restantes: ",
            remaining.map((t) => ({
              id: t.task_id,
              dependencies: t.dependencies?.map((dep) =>
                typeof dep === "string" ? dep : dep.task_id
              ),
            }))
          );
          console.warn("Forzando procesamiento de: ", remaining[0].task_id);
          currentLevel.push(remaining[0]);
        } else {
          break;
        }
      }

      currentLevel.forEach((task) => {
        processed.add(task.task_id as string);
        console.log(`Marcando como procesada: ${task.task_id}`);
      });

      levels.push([...currentLevel]);
      console.log(
        `Nivel ${levelIndex} completado con ${currentLevel.length} tasks`
      );
      levelIndex++;
    }

    if (levelIndex >= 100) {
      console.error(
        "⚠️ Se alcanzó el límite de niveles. Posible bucle infinito."
      );
    }

    console.log(
      "\n====Resumen de niveles====",
      levels.forEach((lvl, i) => {
        console.log(
          `Nivel ${i}: ${lvl.length} tasjs`,
          lvl.map((t) => t.task_id)
        );
      })
    );

    return levels;
  }

  async function processLevel(levelBatch: LevelBatch, ws: WorkspaceLite) {
    levelBatch.status = "processing";

    for (const batch of levelBatch.batches) {
      await processBatch(batch, ws);
    }

    levelBatch.status = "completed";

    return levelBatch;
  }

  async function processBatch(batch: TaskBatch, ws: WorkspaceLite) {
    batch.status = "processing";
    try {
      const resolvedTasks = batch.tasks.map((t) => resolveTaskReferences(t));
      console.log("🔄 Procesando batch con", resolvedTasks.length, " tasks");
      console.log(
        "Tasks en batch:",
        resolvedTasks.map((t) => t.task_id)
      );

      const tasksForAPI = resolvedTasks.map((task) => {
        // Resolver dependencias usando el mapa de tasks ya creadas
        task.workspace = ws;
        task.owner = userLite.value as UserLite;
        const resolvedDependencies = (task.dependencies || [])
          .map((dep: TaskLite) => {
            const depId = dep.task_id;

            // Buscar en tasks ya creadas
            if (tasksMap.value.has(depId)) {
              const dependentTask = tasksMap.value.get(depId);
              console.log(
                `✓ Resolviendo dependencia ${depId} para task ${task.task_id}`
              );
              console.log(dependentTask?.workspace.workspace_id);
              return taskUtils.mapTaskToTaskLite(dependentTask as Task);
            } else {
              console.warn(
                `⚠️ No se encontró dependencia ${depId} para task ${task.task_id}`
              );

              console.log(`  Usando TaskLite original:`, dep);
              console.log(dep.workspace.workspace_id);
              dep.workspace = ws;
              console.log(dep.workspace.workspace_id);
              return dep;
            }
          })
          .filter((dep): dep is TaskLite => dep !== null);

        console.log(
          `Task ${task.task_id} tendrá ${resolvedDependencies.length} dependencias resueltas`
        );

        return {
          ...task,
          owner: userLite.value as UserLite,
          task_id: utils.validateUUID(task.task_id as any)
            ? task.task_id
            : utils.generateUUID(),
          workspace: ws,
          dependencies: resolvedDependencies.map((t) => ({
            ...t,
            workspace: ws,
          })),
          child_tasks: [],
          updated_at: new Date(
            utils.fixDateFormat(task.updated_at as Date) as string
          ),
          created_at: new Date(
            utils.fixDateFormat(task.created_at as Date) as string
          ),
        };
      }) as unknown as Task[];

      console.log("📤 Enviando a API:", tasksForAPI.length, "tasks");

      //TODO: API y store methods to upload tasks batches

      const createdTasks = await taskStore.importTasks(
        tasksForAPI,
        ws.workspace_id
      );

      createdTasks.forEach((task) => {
        tasksMap.value.set(task.task_id as string, task);
        console.log(`✓ Task creada y mapeada: ${task.task_id}`);
      });

      addTasksWithReferencesToMap(createdTasks);

      batch.status = "completed";
      importState.processed += batch.tasks.length;

      console.log("✅ Batch completado exitosamente");
    } catch (error: any) {
      console.error("❌ Error procesando batch:", error);
      batch.status = "error";
      batch.retryCount++;
      importState.errors.push({
        type: "batch_error",
        message: `Error en batch: ${error.message}`,
      });
      throw error;
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
      dependencies: task.dependencies || [],
      child_tasks: task.child_tasks || [],
    };
  }

  function getTaskLiteById(id: any) {
    const tasks = fileContent.value?.tasks as Task[];
    return taskUtils.mapTaskToTaskLite(tasks.find((t) => t == id) as Task);
  }

  function addTasksWithReferencesToMap(tasks: Task[]) {
    tasks.forEach((t: Task) => {
      console.log("Añadiendo...");
      tasksMap.value?.set(t.task_id as string, t);
    });
  }

  function debugTaskDependencies(tasks: Task[]) {
    console.log("\n=== DEBUG: Estructura de dependencias ===");
    tasks.forEach((task) => {
      const deps = (task.dependencies || []).map(
        (dep: TaskLite) => dep.task_id
      );
      const childTasks = (task.child_tasks || []).map((child: any) =>
        typeof child === "string" ? child : child.task_id
      );
      console.log(`${task.task_id}:`);
      console.log(`  └─ depende de: [${deps.join(", ") || "ninguna"}]`);
      console.log(`  └─ hijos: [${childTasks.join(", ") || "ninguno"}]`);
    });
    console.log("==========================================\n");
  }

  onBeforeMount(() => {
    isOfflineEnv.value = wsStore.offlineMode;
  });
</script>
