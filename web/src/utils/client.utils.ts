import {
  CustomConfiguration,
  SpecialTag,
  TagPool,
  TaskLite,
  TaskSlim,
  UITag,
  Workspace,
  WorkspaceLite,
} from "./types";
import { utils } from "./utils";

const darkThemes = [
  "dracula",
  "dark",
  "synthwave",
  "night",
  "dim",
  "forest",
  "aqua",
];

export function changeTheme(theme: string) {
  const htmlElement = document.documentElement;
  htmlElement.setAttribute("data-theme", theme);
  localStorage.setItem("darkTheme", isDarkTheme(theme) as string);
  localStorage.setItem("theme", theme);
  return darkThemes.includes(theme as string);
}

function isDarkTheme(theme: string): String {
  //necesita theme
  return darkThemes.includes(theme).toString();
}

export function checkThemeIsDark(theme: string): boolean {
  //no usa la variable en el localstorage
  return darkThemes.includes(theme);
}

export function getActualTheme() {
  return localStorage.getItem("theme") != null
    ? (localStorage.getItem("theme") as string)
    : "dracula";
}

export function checkIsDark(): boolean {
  return JSON.parse(localStorage.getItem("darkTheme") as string);
}

export function isDarkerCardsActive(): boolean {
  return JSON.parse(localStorage.getItem("darkerCards") as string);
}

export function createCustomConfiguration(ws: Workspace) {
  const ws_lite = mapWorkspaceToWorkspaceLite(ws);
  const newConfig: CustomConfiguration = {
    config_id: utils.generateRandomId(),
    customGlosaries: [],
    tagPool: {
      tag_pool_id: utils.generateRandomId(),
      tags: [] as UITag[],
      specialTags: [] as SpecialTag[],
    } as TagPool,
    workspace: ws_lite,
    flagged_tasks: [] as TaskSlim[],
  };
  return newConfig;
}

export function mapWorkspaceToWorkspaceLite(ws: Workspace) {
  return {
    workspace_id: ws.workspace_id,
    project_name: ws.project_name,
    owner: ws.owner,
  } as WorkspaceLite;
}
