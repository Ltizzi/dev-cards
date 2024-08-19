import { Color, Priority, Progress, TaskLite } from "./types";

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

export const taskUtils = {
  calcPriorityColor,
  calcProgress,
  stringShortener,
  getColor,
  searchTasks,
  getProjectUserDesignatedTasks,
};
