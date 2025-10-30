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
  MANAGEMENT = "MANAGEMENT",
  REQUEST = "REQUEST",
}

export const TaskTypeEnumArray = [
  TaskType.CODE,
  TaskType.ART,
  TaskType.DOCUMENTATION,
  TaskType.BUG,
  TaskType.TESTING,
  TaskType.MARKETING,
  TaskType.MANAGEMENT,
  TaskType.REQUEST,
];

export interface Task {
  task_id?: string;
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
  child_tasks?: Array<TaskLite>;
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
  task_id: string;
  title: string;
  //subtitle: string;
  task_type: TaskType;
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
  workspace_id?: number;
  project_name: string;
  description: string;
  avatar: string;
  owner: UserLite;
  tasks: Array<TaskLite>;
  moderators: Array<UserLite>;
  collaborators: Array<UserLite>;
  users: Array<UserLite>;
  created_at: Date;
  updated_at: Date;
}

export interface WorkspaceWithJwt {
  workspace: Workspace;
  token: string;
}

export interface WorkspaceLite {
  workspace_id: number;
  project_name: string;
  owner: UserLite;
}

export enum Role {
  ROLE_ADMIN = "ROLE_ADMIN",
  ROLE_OWNER = "ROLE_OWNER",
  ROLE_MODERATOR = "ROLE_MODERATOR",
  ROLE_COLLABORATOR = "ROLE_COLLABORATOR",
  ROLE_USER = "ROLE_USER",
}

export interface UserWorkspaceRoles {
  workspace_id: number;
  role: Role;
  assigned_tasks_ids?: string[];
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

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  user: User;
  token: string;
}

export interface APIResponse {
  http_method: string;
  message: string;
}

export interface MenuOptionUI {
  title: string;
  path: string;
  needOwner: boolean;
  needOnline: boolean;
}

export interface DropdownCheckboxOption {
  text: Status | Effort | Color | Priority | TaskType | Progress;
  check: boolean;
}

export interface UITag {
  name: string;
  color: string;
}

export interface TagPool {
  workspace_id?: number;
  tag_pool_id?: number; //TODO: Sacar el ? una vez q esté implementado
  tags: UITag[];
  specialTags: SpecialTag[];
}

export interface CustomConfiguration {
  config_id?: number;
  workspace: WorkspaceLite;
  customGlosaries: Glosary[];
  tagPool: TagPool;
  flagged_tasks: TaskSlim[];
}

export interface Glosary {
  id?: number;
  type: string;
  items: GlosaryItem[];
}

export interface GlosaryItem {
  id?: number;
  key: string;
  value: string;
}

export interface SpecialTag {
  id?: number;
  value: string;
  name: string;
  description: string;
}

export interface TaskSlim {
  task_id: string;
  title: string;
  color: Color;
  workspace_id: number;
}

export interface UserLocal {
  user_id: number;
  username: string;
  avatar: string;
  designated_tasks?: TaskLite[];
  created_tasks?: TaskLite[];
  workspaces?: WorkspaceLite[];
  local: boolean;
}

export interface JSONWorkspace {
  user: UserLite | UserLocal;
  workspace: Workspace;
  tasks: Task[];
  customConfiguration: CustomConfiguration;
  onlineExport?: boolean;
  localExport?: boolean;
  created_at: Date;
  download_at?: Date;
  update_at: Date;
}

export interface ImportProcess {
  phase:
    | "analyzing"
    | "workspace"
    | "level_processing"
    | "customConfig"
    | "completed"
    | "error";
  total: number;
  processed: number;
  errors: ImportError[];
  currentLevel: number;
  totalLevels: number;
  globalMapping: Record<string, string>;
  levelBatches: LevelBatch[];
  workspace?: Workspace;
  customConfig?: CustomConfiguration;
  working: boolean;
}

export interface ImportError {
  type: string;
  message: string;
}

export interface LevelBatch {
  level: number;
  batches: TaskBatch[];
  status: "pending" | "processing" | "completed" | "error";
}

export interface TaskBatch {
  id: string;
  tasks: Task[];
  status: "pending" | "processing" | "completed" | "error";
  retryCount: number;
  idMapping: Record<string, string>;
}

export interface TaskWithReference {
  task: Task;
  reference: TaskSlim;
}

export interface UserCalendar {
  calendar_id: string;
  owner: UserLite | UserLocal;
  items: Map<string, CalendarDay>;
}

export interface WorkspaceCalendar {
  calendar_id: string;
  workspace: WorkspaceLite;
  items: Map<string, CalendarDay>;
}

export interface HourRange {
  start: string;
  end: string;
}

export type CalendarDay = Map<string, CalendarItem>;

export interface CalendarItem {
  id?: string;
  owner: UserLite | UserLocal;
  title: string;
  description: string;
  location: string;
  color: string;
  hourRange: HourRange;
  created_at: Date;
  updated_at: Date;
  date: Date;
}

export interface DateHelper {
  day: number;
  month: number;
  year: number;
}
