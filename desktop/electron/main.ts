import { app, BrowserWindow, ipcMain } from "electron";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { JavaBackend } from "./backend";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const RENDERER_DIST = path.join(__dirname, "../dist");

const backend = new JavaBackend();

let win: BrowserWindow;

function createWindow() {
  win = new BrowserWindow({
    width: 1280,
    height: 720,
    icon: path.join(__dirname, "../public/card.png"),
    webPreferences: {
      // preload: path.join(__dirname, "preload.js"),
      // preload: process.env.VITE_DEV_SERVER_URL
      //   ? path.join(process.cwd(), "dist-electron/preload.js") // DEV
      //   : path.join(__dirname, "preload.js"), // BUILD
      preload: path.join(process.cwd(), "dist-electron/preload.js"),
      contextIsolation: true,
      nodeIntegration: false,
    },
  });

  if (process.env.VITE_DEV_SERVER_URL) {
    win.loadURL(process.env.VITE_DEV_SERVER_URL); // dev
  } else {
    win.loadFile(path.join(RENDERER_DIST, "index.html")); // build
  }
  win.setTitle("Dev Cards");
}

app.whenReady().then(async () => {
  console.log(
    ">>> PRELOAD PATH:",
    path.join(process.cwd(), "dist-electron/preload.js")
  );
  backend.start();
  await new Promise((resolve) => setTimeout(resolve, 3000));

  setTimeout(() => {
    createWindow();
  }, 6000);
});

ipcMain.on("set-project-title", (_event, newTitle: string) => {
  if (win) {
    if (newTitle && newTitle.length > 0)
      win.setTitle(`Dev Cards -  ${newTitle}`);
    else win.setTitle("Dev Cards");
  }
});

app.on("before-quit", () => {
  backend.stop();
});

app.on("window-all-closed", () => {
  backend.stop();
  if (process.platform !== "darwin") {
    app.quit();
  }
});
