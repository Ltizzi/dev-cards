/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      "dracula",
      "light",
      "dark",
      "cupcake",
      "retro",
      "emerald",
      "synthwave",
      "valentine",
      "lofi",
      "fantasy",
      "autumn",
      "acid",
      "night",
      "nord",
      "dim",
      "forest",
      "aqua",
    ],
  },
};
