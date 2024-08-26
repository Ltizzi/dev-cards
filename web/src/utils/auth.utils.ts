import { jwtDecode, JwtPayload } from "jwt-decode";
import { Role, TaskLite, User, UserWorkspaceRoles } from "./types";

interface CustomJwtPayload extends JwtPayload {
  scope: Array<string> | string;
  roles: UserWorkspaceRoles[];
}

function saveToken(token: string) {
  const decodedToken = jwtDecode(token);
  if (decodedToken && decodedToken.exp) {
    const expirationTime = decodedToken.exp * 1000;
    localStorage.setItem("token", token);
    localStorage.setItem("jwtExp", expirationTime.toString());
  }
}

function isTokenExpired(): boolean {
  const expiration = localStorage.getItem("jwtExp");
  if (!expiration) {
    return true;
  }
  const now = Date.now();
  const isExpired = now > parseInt(expiration, 10);
  if (isExpired) removeToken();
  return isExpired;
}

function removeToken(): void {
  localStorage.removeItem("token");
  localStorage.removeItem("jwtExp");
}

function logout() {
  localStorage.removeItem("user");
  removeToken();
}

function getRoles(): UserWorkspaceRoles[] {
  const token = localStorage.getItem("token");
  //console.log(token);
  if (token) {
    const decoded = jwtDecode(token) as CustomJwtPayload;
    return decoded.roles;
  } else {
    throw new Error("Something went wrong, couldn't get roles");
  }
}

function checkIsOwner(workspace_id: number): boolean {
  return (
    getRoles()?.filter(
      (uwr: UserWorkspaceRoles) =>
        uwr.workspace_id == workspace_id && uwr.role == Role.ROLE_OWNER
    ).length > 0
  );
}

function checkIsModerator(workspace_id: number): boolean {
  return (
    getRoles().filter(
      (uwr: UserWorkspaceRoles) =>
        uwr.workspace_id == workspace_id && uwr.role == Role.ROLE_MODERATOR
    ).length > 0
  );
}

function checkIsModOrOwner(workspace_id: number): boolean {
  return (
    getRoles().filter(
      (uwr: UserWorkspaceRoles) =>
        uwr.workspace_id == workspace_id &&
        (uwr.role == Role.ROLE_MODERATOR || uwr.role == Role.ROLE_OWNER)
    ).length > 0
  );
}

function checkIsCollaborator(workspace_id: number): boolean {
  return (
    getRoles().filter(
      (uwr: UserWorkspaceRoles) =>
        uwr.workspace_id == workspace_id && uwr.role == Role.ROLE_COLLABORATOR
    ).length > 0
  );
}

function checkIsDesignated(workspace_id: number, task_id: number) {
  return (
    getRoles().filter(
      (uwr: UserWorkspaceRoles) =>
        uwr.workspace_id == workspace_id &&
        uwr.assigned_tasks_ids?.includes(task_id)
    ).length > 0
  );
}

function checkIfUserisTaskOwner(task_id: number) {
  const user = JSON.parse(localStorage.getItem("user") as string) as User;
  return (
    user.created_tasks.filter((t: TaskLite) => t.task_id == task_id).length > 0
  );
}

export {
  saveToken,
  isTokenExpired,
  removeToken,
  getRoles,
  logout,
  checkIsDesignated,
  checkIsModOrOwner,
  checkIsOwner,
  checkIsModerator,
  checkIfUserisTaskOwner,
  checkIsCollaborator,
};
