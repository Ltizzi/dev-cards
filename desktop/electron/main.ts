// import { app, BrowserWindow } from "electron";
// import { createRequire } from "node:module";
// import { fileURLToPath } from "node:url";
// import path from "node:path";

// const require = createRequire(import.meta.url);
// const __dirname = path.dirname(fileURLToPath(import.meta.url));

// // The built directory structure
// //
// // ├─┬─┬ dist
// // │ │ └── index.html
// // │ │
// // │ ├─┬ dist-electron
// // │ │ ├── main.js
// // │ │ └── preload.mjs
// // │
// process.env.APP_ROOT = path.join(__dirname, "..");

// // 🚧 Use ['ENV_NAME'] avoid vite:define plugin - Vite@2.x
// export const VITE_DEV_SERVER_URL = process.env["VITE_DEV_SERVER_URL"];
// export const MAIN_DIST = path.join(process.env.APP_ROOT, "dist-electron");
// export const RENDERER_DIST = path.join(process.env.APP_ROOT, "dist");

// process.env.VITE_PUBLIC = VITE_DEV_SERVER_URL
//   ? path.join(process.env.APP_ROOT, "public")
//   : RENDERER_DIST;

// const iconPath = path.join(__dirname, "..", "public", "card.png");

// let win: BrowserWindow | null;

// function createWindow() {
//   win = new BrowserWindow({
//     icon: iconPath,
//     webPreferences: {
//       preload: path.join(__dirname, "preload.mjs"),
//     },
//   });

//   // Test active push message to Renderer-process.
//   win.webContents.on("did-finish-load", () => {
//     win?.webContents.send("main-process-message", new Date().toLocaleString());
//   });

//   if (VITE_DEV_SERVER_URL) {
//     win.loadURL(VITE_DEV_SERVER_URL);
//   } else {
//     // win.loadFile('dist/index.html')
//     win.loadFile(path.join(RENDERER_DIST, "index.html"));
//   }
// }

// // Quit when all windows are closed, except on macOS. There, it's common
// // for applications and their menu bar to stay active until the user quits
// // explicitly with Cmd + Q.
// app.on("window-all-closed", () => {
//   if (process.platform !== "darwin") {
//     app.quit();
//     win = null;
//   }
// });

// app.on("activate", () => {
//   // On OS X it's common to re-create a window in the app when the
//   // dock icon is clicked and there are no other windows open.
//   if (BrowserWindow.getAllWindows().length === 0) {
//     createWindow();
//   }
// });

// app.whenReady().then(createWindow);

import { app, BrowserWindow } from "electron";
import path from "node:path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const RENDERER_DIST = path.join(__dirname, "../dist"); // <-- apunta a dist de Vite

function createWindow() {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    icon: path.join(__dirname, "../public/card.png"), // o donde esté tu PNG
    webPreferences: {
      preload: path.join(__dirname, "preload.mjs"),
    },
  });

  if (process.env.VITE_DEV_SERVER_URL) {
    win.loadURL(process.env.VITE_DEV_SERVER_URL); // dev
  } else {
    win.loadFile(path.join(RENDERER_DIST, "index.html")); // build
  }
}

app.whenReady().then(createWindow);
