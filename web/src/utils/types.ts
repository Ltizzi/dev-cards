export enum HttpMethod {
  GET,
  POST,
  PATCH,
  DELETE,
}

export interface Endpoint {
  [key: string]: string;
}

export enum Color {
  RED = "RED",
  BLUE = "BLUE",
  GREEN = "GREEN",
  ORANGE = "ORANGE",
  YELLOW = "YELLOW",
  PURPLE = "PURPLE",
  BROWN = "BROWN",
  GRAY = "GRAY",
  BLACK = "BLACK",
  PINK = "PINK",
  CYAN = "CYAN",
}

export enum Priority {
  VERY_LOW = "VERY_LOW",
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
  VERY_HIGH = "VERY_HIGH",
}

export enum Effort {
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
}

export enum Status {
  PENDING = "PENDING",
  PROGRESS = "PROGRESS",
  COMPLETED = "COMPLETED",
  BLOCKED = "BLOCKED",
}

export enum Progress {
  NULL = "NULL",
  NOT_FUNCTIONAL = "NOT_FUNCTIONAL",
  BASIC = "TESTING",
  INTERMEDIATE = "INTERMEDIATE",
  ADVANCE = "ADVANCE",
}

export enum TaskType {
  CODE = "CODE",
  ART = "ART",
  DOCUMENTATION = "DOCUMENTATION",
  BUG = "BUG",
  TESTING = "TESTING",
  MARKETING = "MARKETING",
}

export interface Task {
  task_id: number;
  title: string;
  subtitle: string;
  description: string;
  color: Color;
  priority: Priority;
  effort: Effort;
  status: Status;
  progress: Progress;
  project: Workspace;
  dependencies: Array<TaskLite>;
  task_tags: Array<string>;
  updates: Array<TaskUpdate>;
  blocked_by: UserLite;
  owner: UserLite;
  designated_to: Array<UserLite>;
  created_at: Date;
  updated_at: Date;
}

export interface TaskUpdate {
  update_id: number;
  creater_user_id: number;
  creator_username: string;
  editors_id: Array<number>;
  editors_usernames: Array<string>;
  description: String;
  old_descriptions: Array<string>;
  created_at: Date;
  updated_at: Date;
}

export interface TaskLite {
  task_id: number;
  title: string;
  subtitle: string;
  color: Color;
  priority: Priority;
  effort: Effort;
  status: Status;
  progress: Progress;
  project: WorkspaceLite;
  task_tags: Array<string>;
  owner: UserLite;
}

export interface Workspace {
  workspace_id: number;
  project_name: string;
  description: string;
  owner: UserLite;
  tasks: Array<TaskLite>;
  moderators: Array<UserLite>;
  users: Array<UserLite>;
  created_at: Date;
  updated_at: Date;
}

export interface WorkspaceLite {
  workspace_id: number;
  project_Name: string;
  owner: UserLite;
}

export interface User {
  user_id: number;
  username: string;
  email: string;
  avatar: string;
  about: string;
  githubProfile: string;
  workspaces: Array<WorkspaceLite>;
  created_tasks: Array<TaskLite>;
  designated_tasks: Array<TaskLite>;
  created_at: Date;
  updated_at: Date;
}
export interface UserLite {
  user_id: number;
  username: string;
  email: string;
  avatar: string;
}
