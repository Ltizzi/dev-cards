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
      "bumblebee",
      "emerald",
      "corporate",
      "synthwave",
      "valentine",
      "aqua",
      "lofi",
      "fantasy",
      "autumn",
      "acid",
      "lemonade",
      "night",
      "coffee",
      "winter",
      "nord",
      "sunset",
    ],
  },
};
