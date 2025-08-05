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
  safelist: [
    // "bg-slate-500",
    // "bg-red-500",
    // "bg-orange-500",
    // "bg-emerald-500",
    // "bg-teal-500",
    // "bg-sky-500",
    // "bg-indigo-500",
    // "bg-violet-500",
    // "bg-pink-500",
    // "bg-rose-500",
    {
      pattern:
        /bg-(slate|red|orange|emerald|teal|sky|indigo|violet|pink|rose)-(500)/,
    },

    {
      pattern:
        /from-(red|blue|green|orange|yellow|purple|brown|gray|black|pink|cyan|violet|indigo|fuchsia|rose|sky|teal|emerald|lime|amber|slate)-(400|600|950)/,
    },
    {
      pattern:
        /via-(red|blue|green|orange|yellow|purple|brown|gray|black|pink|cyan|violet|indigo|fuchsia|rose|sky|teal|emerald|lime|amber|slate)-(400|600|950)/,
    },
    {
      pattern:
        /to-(red|blue|green|orange|yellow|purple|brown|gray|black|pink|cyan|violet|indigo|fuchsia|rose|sky|teal|emerald|lime|amber|slate)-(400|600|950)/,
    },
  ],
  plugins: [require("daisyui"), require("tailwindcss-motion")],
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
