import {
  Color,
  Priority,
  Progress,
  TagPool,
  Task,
  TaskLite,
  Workspace,
} from "./types";

function calcProgress(progress: Progress) {
  switch (progress) {
    case Progress.NULL:
      return 0;
    case Progress.NOT_FUNCTIONAL:
      return 1;

    case Progress.BASIC:
      return 2;

    case Progress.INTERMEDIATE:
      return 3;

    case Progress.ADVANCE:
      return 4;
  }
}

function calcPriorityColor(priority: Priority) {
  switch (priority) {
    case Priority.VERY_LOW:
      return "text-info"; //"font_emerald";
    case Priority.LOW:
      return "text-success"; //"font_green";
    case Priority.MEDIUM:
      return "text-accent"; //"font_lime";
    case Priority.HIGH:
      return "text-warning"; //"font_amber";
    case Priority.VERY_HIGH:
      return "text-error"; //"font_red";
  }
}

function getColor(color: Color) {
  return color.toLocaleLowerCase();
}

function stringShortener(value: string) {
  if (value.length > 15) {
    return value.slice(0, 15) + "...";
  }
  return value;
}

function searchTasks(searchValue: string, tasks: TaskLite[]): TaskLite[] {
  let returned_tasks: TaskLite[] = [];
  searchValue = searchValue.toLowerCase();
  if (!searchValue) {
    return tasks;
  }
  tasks.forEach((task: TaskLite) => {
    if (task.title.toLowerCase().includes(searchValue)) {
      returned_tasks.push(task);
      return;
    }
    task.task_tags.forEach((tag: string) => {
      if (tag.toLowerCase().includes(searchValue)) {
        returned_tasks.push(task);
        return;
      }
    });
  });
  return returned_tasks;
}

function searchTasksByFilters(options: any[], tasks: TaskLite[]): TaskLite[] {
  // console.log("Searching...");
  // console.log(tasks);
  return tasks.filter((t: TaskLite) => {
    return options.some((opt: any) => {
      if (t.color == opt) return true;
      else if (t.status == opt) return true;
      else if (t.effort == opt) return true;
      else if (t.task_type == opt) return true;
      else if (t.progress == opt) return true;
      else if (t.priority == opt) return true;
    });
  });
}

function getProjectUserDesignatedTasks(
  tasks: TaskLite[],
  designated_tasks: TaskLite[]
) {
  let project_designated_tasks = [] as TaskLite[];
  tasks.forEach((task: TaskLite) => {
    designated_tasks.forEach((t: TaskLite) => {
      if (task.task_id == t.task_id) {
        project_designated_tasks.push(t);
      }
    });
  });
  return project_designated_tasks;
}

function addTagToTagsPool(tag: string, ws_id: number) {
  let tag_pools = [] as TagPool[];
  let tags = [] as string[];
  if (localStorage.getItem("tags")) {
    tag_pools = JSON.parse(localStorage.getItem("tags") as string);
    const tp = getTagPoolById(tag_pools, ws_id);
    if (tp.length > 0) {
      tp[0].tags.push(tag);
      tags = tp[0].tags;
      tag_pools = tag_pools.map((t: TagPool) => {
        if (t.workspace_id === ws_id) {
          t.tags = tags;
        }
        return t;
      });
    } else {
      tag_pools = addNewTagPool(tags, tag_pools, ws_id, tag);
    }
  } else {
    tag_pools = addNewTagPool(tags, tag_pools, ws_id, tag);
  }
  localStorage.setItem("tags", JSON.stringify(tag_pools));
}

function getTagPoolById(tag_pools: TagPool[], id: number) {
  return tag_pools.filter((tp: TagPool) => tp.workspace_id === id);
}

function addNewTagPool(
  tags: string[],
  pools: TagPool[],
  ws_id: number,
  tag: string
) {
  tags.push(tag);
  const newTagPool: TagPool = {
    workspace_id: ws_id,
    tags: tags,
  };
  pools.push(newTagPool);
  return pools;
}

export const taskUtils = {
  calcPriorityColor,
  calcProgress,
  stringShortener,
  getColor,
  searchTasks,
  getProjectUserDesignatedTasks,
  addTagToTagsPool,
  searchTasksByFilters,
};
