import { app, BrowserWindow } from "electron";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { JavaBackend } from "./backend";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const RENDERER_DIST = path.join(__dirname, "../dist");

const backend = new JavaBackend();

function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 720,
    icon: path.join(__dirname, "../public/card.png"),
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

app.whenReady().then(async () => {
  backend.start();
  await new Promise((resolve) => setTimeout(resolve, 3000));

  setTimeout(() => {
    createWindow();
  }, 6000);
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
