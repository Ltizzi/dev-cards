/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        roboto: ["Roboto"],
        raleway: ["Raleway"],
        ubuntuMono: ["Ubuntu Mono"],
      },
    },
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
      "winter",
      "lemonade",
      "pastel",
      "garden",
      "corporate",
      "wireframe",
    ],
  },
};
