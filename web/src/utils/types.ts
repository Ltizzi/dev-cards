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
  VIOLET = "VIOLET",
  INDIGO = "INDIGO",
  FUCHSIA = "FUCHSIA",
  ROSE = "ROSE",
  SKY = "SKY",
  TEAL = "TEAL",
  EMERALD = "EMERALD",
  LIME = "LIME",
  AMBER = "AMBER",
  SLATE = "SLATE",
}

export const ColorEnumArray = [
  Color.RED,
  Color.BLUE,
  Color.GREEN,
  Color.ORANGE,
  Color.YELLOW,
  Color.PURPLE,
  Color.BROWN,
  Color.GRAY,
  Color.BLACK,
  Color.PINK,
  Color.CYAN,
  Color.VIOLET,
  Color.INDIGO,
  Color.FUCHSIA,
  Color.ROSE,
  Color.SKY,
  Color.TEAL,
  Color.EMERALD,
  Color.LIME,
  Color.AMBER,
  Color.SLATE,
];

export enum Priority {
  VERY_LOW = "VERY_LOW",
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
  VERY_HIGH = "VERY_HIGH",
}
export const PriorityEnumArray = [
  Priority.VERY_LOW,
  Priority.LOW,
  Priority.MEDIUM,
  Priority.HIGH,
  Priority.VERY_HIGH,
];

export enum Effort {
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
}

export const EffortEnumArray = [Effort.LOW, Effort.MEDIUM, Effort.HIGH];

export enum Status {
  PENDING = "PENDING",
  PROGRESS = "PROGRESS",
  TESTING = "TESTING",
  COMPLETED = "COMPLETED",
  BLOCKED = "BLOCKED",
}

export const StatusEnumArray = [
  Status.PENDING,
  Status.PROGRESS,
  Status.TESTING,
  Status.COMPLETED,
  Status.BLOCKED,
];

export enum Progress {
  NULL = "NULL",
  NOT_FUNCTIONAL = "NOT_FUNCTIONAL",
  BASIC = "BASIC",
  INTERMEDIATE = "INTERMEDIATE",
  ADVANCE = "ADVANCE",
}

export const ProgressEnumArray = [
  Progress.NULL,
  Progress.NOT_FUNCTIONAL,
  Progress.BASIC,
  Progress.INTERMEDIATE,
  Progress.ADVANCE,
];

export enum TaskType {
  CODE = "CODE",
  ART = "ART",
  DOCUMENTATION = "DOCUMENTATION",
  BUG = "BUG",
  TESTING = "TESTING",
  MARKETING = "MARKETING",
}

export const TaskTypeEnumArray = [
  TaskType.CODE,
  TaskType.ART,
  TaskType.DOCUMENTATION,
  TaskType.BUG,
  TaskType.TESTING,
  TaskType.MARKETING,
];

export interface Task {
  task_id?: number;
  title: string;
  subtitle: string;
  description: string;
  color: Color;
  priority: Priority;
  effort: Effort;
  status: Status;
  progress: Progress;
  workspace: WorkspaceLite;
  task_type: TaskType;
  progressItems: Array<ProgressItem>;
  dependencies?: Array<TaskLite>;
  task_tags?: Array<string>;
  updates?: Array<TaskUpdate>;
  blocked_by?: UserLite;
  owner: UserLite;
  designated_to?: Array<UserLite>;
  created_at?: Date;
  updated_at?: Date;
}

export interface ProgressItem {
  issue_id?: number;
  sentence: string;
  isCompleted: boolean;
}

export interface TaskUpdate {
  update_id?: number;
  creator_user_id: number;
  creator_username: string;
  editors_id?: Array<number>;
  editors_usernames?: Array<string>;
  description: String;
  old_descriptions?: Array<string>;
  created_at?: Date;
  updated_at?: Date;
}

export interface TaskLite {
  task_id: number;
  title: string;
  //subtitle: string;
  color: Color;
  priority: Priority;
  effort: Effort;
  status: Status;
  progress: Progress;
  workspace: WorkspaceLite;
  task_tags: Array<string>;
  hasUsers: boolean;
  // owner: UserLite;
}

export interface Workspace {
  workspace_id: number;
  project_name: string;
  description: string;
  avatar: string;
  owner: UserLite;
  tasks: Array<TaskLite>;
  moderators: Array<UserLite>;
  users: Array<UserLite>;

  created_at: Date;
  updated_at: Date;
}

export interface WorkspaceLite {
  workspace_id: number;
  project_name: string;
  owner: UserLite;
}

export enum Role {
  ADMIN,
  OWNER,
  MODERATOR,
  USER,
}

export interface User {
  user_id: number;
  username: string;
  email: string;
  avatar: string | undefined;
  about: string | undefined;
  githubProfile: string | undefined;
  workspaces: Array<WorkspaceLite>;
  created_tasks: Array<TaskLite>;
  designated_tasks: Array<TaskLite>;
  roles: Array<Role>;
  created_at: Date;
  updated_at: Date;
}
export interface UserLite {
  user_id: number;
  username: string;
  email: string;
  avatar?: string;
}

export interface AuthResponse {
  user: User;
  token: string;
}
