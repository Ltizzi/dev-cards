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
  //necesita theme
  return darkThemes.includes(theme).toString();
}

export function checkThemeIsDark(): boolean {
  //no usa la variable en el localstorage
  return darkThemes.includes(getActualTheme());
}

export function getActualTheme() {
  return localStorage.getItem("theme") != null
    ? (localStorage.getItem("theme") as string)
    : "dracula";
}

export function checkIsDark(): boolean {
  return JSON.parse(localStorage.getItem("darkTheme") as string);
}
