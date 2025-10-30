import { Color } from "../utils/types";

export function useColor(color: Color | undefined): string {
  if (color) return `${color.toLocaleLowerCase()}-500`;
  else return "";
}
