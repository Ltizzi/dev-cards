import { Color, Priority, Progress } from "./types";

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
      return "font_emerald";
    case Priority.LOW:
      return "font_green";
    case Priority.MEDIUM:
      return "font_lime";
    case Priority.HIGH:
      return "font_amber";
    case Priority.VERY_HIGH:
      return "font_red";
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

export const taskUtils = {
  calcPriorityColor,
  calcProgress,
  stringShortener,
  getColor,
};
