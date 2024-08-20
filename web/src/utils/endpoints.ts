export enum EndpointType {
  TASK_GET_ALL = "/task/all",
  TASK_GET_BY_ID = "/task/byId",
  TASK_NEW = "/task/new",
  TASK_UPDATE = "/task/update",
  TASK_DELETE = "/task/delete",
  TASK_ADD_DEPENDENCY = "/task/add_dependency",
  TASK_REMOVE_DEPENDENCY = "/task/remove_dependency",
  TASK_ASSIGN_USER = "/task/assign",
  TASK_UNASSIGN_USER = "/task/unassign",
  TASK_ADD_TAG = "/task/add_tag",
  TASK_REMOVE_TAG = "/task/remove_tag",
  TASK_ADD_UPDATE = "/task/add_update",
  TASK_REMOVE_UPDATE = "/task/remove_update",
  TASK_UPDATE_TASK_UPDATE = "/task/update_tu",
  TASKS_BY_WORKSPACE = "/task/byWorkspace",
  TASK_UPDATE_PROGRESS = "/task/updateProgress",
  TASK_UPDATE_PRIORITY = "/task/updatePriority",
  TASK_UPDATE_STATUS = "/task/updateStatus",
  TASK_UPDATE_EFFORT = "/task/updateEffort",
  TASK_UPDATE_TYPE = "/task/updateType",
  TASK_UPDATE_TITLE = "/task/updateTitle",
  TASK_UPDATE_SUBTITLE = "/task/updateSubtitle",
  TASK_UPDATE_DESCRIPTION = "/task/updateDescription",
  TASK_UPDATE_ISSUE = "/task/updateIssue",
  TASK_CREATE_ISSUE = "/task/addIssue",
  TASK_REMOVE_ISSUE = "/task/deleteIssue",

  USER_GET_ALL = "/user/all",
  USER_GET_BY_ID = "/user/byId",
  USER_NEW = "/user/new",
  USER_UPDATE = "/user/update",
  USER_DELETE = "/user/delete",
  USER_LOGIN = "/user/login",
  USER_REGISTER = "/user/register",
  USER_MEMBER = "/user/workspaces",
  USER_REFRESH = "/user/refresh",
  USER_CHECK = "/user/checkByMail",

  WORKSPACE_GET_ALL = "/workspace/all",
  WORKSPACE_GET_BY_ID = "/workspace/byId",
  WORKSPACE_NEW = "/workspace/new",
  WORKSPACE_UPDATE = "/workspace/update",
  WORKSPACE_DELETE = "/workspace/delete",
  WORKSPACE_ADD_USER = "/workspace/add_user",
  WORKSPACE_ADD_USER_BY_EMAIL = "/workspace/inviteByEmail",
  WORKSPACE_REMOVE_USER = "/workspace/remove_user",
  WORKSPACE_ADD_MOD = "/workspace/add_mod",
  WORKSPACE_REMOVE_MOD = "/workspace/remove_mod",
  WORKSPACE_JSON = "/workspace/json",
}
