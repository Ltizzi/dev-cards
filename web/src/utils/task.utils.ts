import { useConfigStore } from "../store/config.store";
import {
  Color,
  Priority,
  Progress,
  SpecialTag,
  Status,
  TagPool,
  Task,
  TaskLite,
  UITag,
  Workspace,
} from "./types";

function mapTaskToTaskLite(task: Task) {
  return {
    task_id: task.task_id,
    title: task.title,
    color: task.color,
    priority: task.priority,
    status: task.status,
    progress: task.progress,
    task_type: task.task_type,
    workspace: task.workspace,
    task_tags: task.task_tags,
    hasUsers: task.designated_to ? task.designated_to.length > 0 : false,
  } as TaskLite;
}

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

function filterTags(tags: string[], tag: string) {
  return tags.filter((t: string) => t.toLowerCase() == tag).length > 0;
}

function searchTasksByTag(tag: string, tasks: TaskLite[]): TaskLite[] {
  if (!tag) {
    return tasks;
  }
  tag = tag.toLowerCase();
  //return tasks.filter((t: TaskLite) => t.task_tags.includes(tag));
  return tasks.filter((t: TaskLite) => filterTags(t.task_tags, tag));
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

function searchBlockedTasks(tasks: TaskLite[]): TaskLite[] {
  return tasks.filter((t: TaskLite) => t.status == Status.BLOCKED);
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

const colors = [
  // "bg-slate-500",
  // "bg-red-500",
  // "bg-orange-500",
  // "bg-emerald-500",
  // "bg-teal-500",
  // "bg-sky-500",
  // "bg-indigo-500",
  // "bg-violet-500",
  // "bg-pink-500",
  // "bg-rose-600",
  "red",
  "orange",
  "purple",
  "pink",
  "indigo",
  "fuchsia",
  "cyan",
  "sky",
  "teal",
  "emerald",
  "amber",
]; //  "neutral","base-100",

function getLastColor() {
  return localStorage.getItem("last-color")
    ? localStorage.getItem("last-color")
    : "";
}

function getRandomColor() {
  let color = colors[Math.floor(Math.random() * colors.length)];

  if (color != getLastColor()) {
    localStorage.setItem("last-color", color);
    return color;
  } else return getRandomColor();
}

function createTagPool(ws_id: number) {
  let tag_pools = [] as TagPool[];
  if (localStorage.getItem("tags")) {
    tag_pools = JSON.parse(localStorage.getItem("tags") as string);
  }
  const newTagPool: TagPool = {
    workspace_id: ws_id,
    tags: [],
    specialTags: [] as SpecialTag[],
  };
  tag_pools.push(newTagPool);
  localStorage.setItem("tags", JSON.stringify(tag_pools));
}

function addTagToTagsPool(tag: string, ws_id: number) {
  let tag_pools = [] as TagPool[];
  let tags = [] as UITag[];
  if (localStorage.getItem("tags")) {
    tag_pools = JSON.parse(localStorage.getItem("tags") as string);
    const tp = getTagPoolById(tag_pools, ws_id);
    if (tp.length > 0) {
      const newTag: UITag = {
        name: tag,
        color: getRandomColor(),
      };
      tp[0].tags.push(newTag);
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
  tags: UITag[],
  pools: TagPool[],
  ws_id: number,
  tag: string
) {
  const newTag: UITag = {
    name: tag,
    color: getRandomColor(),
  };
  tags.push(newTag);
  const newTagPool: TagPool = {
    workspace_id: ws_id,
    tags: tags,
    specialTags: [] as SpecialTag[],
  };
  pools.push(newTagPool);
  return pools;
}

function getTagColor(ws_id: number, tag_name: string) {
  if (localStorage.getItem("tags")) {
    const tagPools = JSON.parse(localStorage.getItem("tags") as string);
    const tagPool = getTagPoolById(tagPools, ws_id) as TagPool[];
    if (tagPool[0] && tagPool[0].tags.length > 0)
      return tagPool[0].tags.filter(
        (tag: UITag) => tag.name.toLowerCase() === tag_name.toLowerCase()
      );
    else return [{ name: "", color: "red" }];
  }
}

function getTags(ws_id: number) {
  const tagPools = JSON.parse(localStorage.getItem("tags") as string);
  if (tagPools) {
    const TagPool = getTagPoolById(tagPools, ws_id) as TagPool[];
    if (TagPool[0]) return TagPool[0].tags;
  } else return [];
}

//Method and interface used to convert old tag pool format in the new Tag pool
// interface OldTagPool {
//   workspace_id: number;
//   tags: string[];
// }
// function convertTagPool() {
//   let tags = JSON.parse(localStorage.getItem("tags") as string);
//   let newTagPools = [] as TagPool[];
//   tags.forEach((tp: OldTagPool) => {
//     const newTP: TagPool = {
//       workspace_id: tp.workspace_id,
//       tags: [] as UITag[],
//     };
//     const newTags = tp.tags.map((tag: string) => {
//       const newTag: UITag = {
//         name: tag,
//         color: getRandomColor(),
//       };
//       return newTag;
//     });
//     newTP.tags = newTags;
//     newTagPools.push(newTP);
//   });
//   localStorage.setItem("tags", JSON.stringify(newTagPools));
// }

//USED TO RESTORED BROKEN TAG POOL
function saveTagPool() {
  const newTagPools: TagPool[] = [
    {
      workspace_id: 1,
      tags: [
        // { name: "UI", color: "bg-accent" },
        // { name: "BTN", color: "bg-primary" },
        // { name: "CLIENT", color: "bg-secondary" },
        // { name: "STATE", color: "bg-accent" },
        // { name: "API", color: "bg-primary" },
        // { name: "JSON", color: "bg-secondary" },
        // { name: "BACKUP", color: "bg-success" },
        // { name: "JWT", color: getRandomColor() },
        // { name: "AUTHORIZATION", color: "bg-primary" },
        // { name: "BACKEND", color: getRandomColor() },
        // { name: "SECURITY", color: getRandomColor() },
      ],
      specialTags: [] as SpecialTag[],
    },
  ];
  localStorage.setItem("tags", JSON.stringify(newTagPools));
}

async function parseTags(tags: string[]) {
  const specialTags = getSpecialTagsByFilter(tags);
  const filtered_tags = getNormalTags(tags);

  return {
    tags: await getUITags(filtered_tags as string[]),
    specialTags: await getSpecialsTags(specialTags as string[]),
  };
}

async function parseAndGetSpecialTags(tags: string[]) {
  const filtered_tags = getSpecialTagsByFilter(tags);
  return filtered_tags ? await getSpecialsTags(filtered_tags as string[]) : [];
}

function getSpecialTagsByFilter(tags: string[]) {
  return tags.map((t: string) => {
    if (t[0] === "{" && t[t.length - 1] === "}") return t;
  });
}
function getNormalTags(tags: string[]) {
  return tags.map((t: string) => {
    if (t[0] !== "{" && t[t.length - 1] !== "}") return t;
  });
}

async function getUITags(tags: string[]) {
  const configStore = useConfigStore();
  const UITags = await configStore.getTags();
  // console.log("UITAGS FROM UTILS:");
  // console.log(tags);
  // console.log(UITags);
  return UITags.filter((ut: UITag) => {
    let parcial = tags.filter(
      (t: string) => t.toLowerCase() == ut.name.toLowerCase()
    );
    if (parcial.length > 0) return parcial;
  });
}

async function getSpecialsTags(tags: string[]) {
  const configStore = useConfigStore();
  const specialTags = await configStore.getSpecialTags();

  return specialTags.filter((st: SpecialTag) => {
    let parcial = tags.filter(
      (t: string) => t.toLowerCase() == st.name.toLowerCase()
    );
    if (parcial.length > 0) return parcial;
    // tags.forEach((t: string) => {
    //   if (st.value.toLowerCase() == t.toLowerCase()) return st;
    // });
  });
}

export const taskUtils = {
  calcPriorityColor,
  calcProgress,
  stringShortener,
  getColor,
  searchTasks,
  searchTasksByTag,
  getProjectUserDesignatedTasks,
  addTagToTagsPool,
  searchTasksByFilters,
  searchBlockedTasks,
  getRandomColor,
  getTagColor,
  getTags,
  saveTagPool,
  createTagPool,
  mapTaskToTaskLite,
  parseTags,
  parseAndGetSpecialTags,
  getNormalTags,
  getUITags,
};
