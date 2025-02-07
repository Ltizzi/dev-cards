import { Workspace, WorkspaceLite } from "./types";

function sortByOldestUpdate(array: Workspace[]) {
  return array.sort((a, b) => {
    const dateA = new Date(a.updated_at).getTime();
    const dateB = new Date(b.updated_at).getTime();
    return dateA != dateB ? (dateA < dateB ? -1 : 1) : 0;
  });
}

function sortByNewbestUpdate(array: Workspace[]) {
  return array.sort((a, b) => {
    const dateA = new Date(a.updated_at).getTime();
    const dateB = new Date(b.updated_at).getTime();
    return dateA != dateB ? (dateA > dateB ? -1 : 1) : 0;
  });
}

function mapWsToWsLite(ws: Workspace) {
  return {
    workspace_id: ws.workspace_id,
    project_name: ws.project_name,
    owner: ws.owner,
  } as WorkspaceLite;
}

export const workspaceUtils = {
  sortByOldestUpdate,
  sortByNewbestUpdate,
  mapWsToWsLite,
};
