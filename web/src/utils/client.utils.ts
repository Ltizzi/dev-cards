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
  return darkThemes.includes(theme).toString();
}

export function checkThemeIsDark(): boolean {
  return darkThemes.includes(getActualTheme());
}

export function getActualTheme() {
  return localStorage.getItem("theme") != null
    ? (localStorage.getItem("theme") as string)
    : "dracula";
}
