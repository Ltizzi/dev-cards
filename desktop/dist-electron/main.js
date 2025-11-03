var __defProp = Object.defineProperty;
var __defNormalProp = (obj, key, value) => key in obj ? __defProp(obj, key, { enumerable: true, configurable: true, writable: true, value }) : obj[key] = value;
var __publicField = (obj, key, value) => __defNormalProp(obj, typeof key !== "symbol" ? key + "" : key, value);
import { app, BrowserWindow } from "electron";
import path$1 from "node:path";
import { fileURLToPath as fileURLToPath$1 } from "node:url";
import { spawn } from "child_process";
import path from "path";
import fs from "fs";
import { fileURLToPath } from "url";
const __filename$1 = fileURLToPath(import.meta.url);
const __dirname$1 = path.dirname(__filename$1);
class JavaBackend {
  constructor() {
    __publicField(this, "process", null);
  }
  start() {
    var _a, _b;
    const isDev = process.env.NODE_ENV === "development";
    let backendDir;
    let jarPath;
    if (isDev) {
      backendDir = path.join(__dirname$1, "../backend");
      jarPath = path.join(backendDir, "dev-cards-0.0.1-SNAPSHOT.jar");
    } else {
      backendDir = path.join(process.resourcesPath, "backend");
      jarPath = path.join(backendDir, "dev-cards-0.0.1-SNAPSHOT.jar");
    }
    console.log("🔍 Checking backend structure...");
    console.log("   Backend dir:", backendDir);
    console.log("   JAR path:", jarPath);
    console.log("   JAR exists:", fs.existsSync(jarPath));
    console.log("🚀 Starting Java backend...");
    this.process = spawn("java", ["-jar", jarPath], {
      //path.basename(jarPath)
      //   cwd: backendDir,
      env: {
        ...process.env,
        SPRING_PROFILES_ACTIVE: isDev ? "development" : "production"
      }
    });
    (_a = this.process.stdout) == null ? void 0 : _a.on("data", (data) => {
      console.log(`[Java]: ${data.toString().trim()}`);
    });
    (_b = this.process.stderr) == null ? void 0 : _b.on("data", (data) => {
      console.error(`[Java Error]: ${data.toString().trim()}`);
    });
    this.process.on("error", (error) => {
      console.error("❌ Failed to start Java process:", error);
    });
    this.process.on("close", (code) => {
      console.log(`Java backend exited with code ${code}`);
      this.process = null;
    });
  }
  stop() {
    if (this.process) {
      console.log("🛑 Stopping Java backend...");
      this.process.kill("SIGTERM");
      setTimeout(() => {
        if (this.process) {
          console.log("⚠️ Force killing Java backend...");
          this.process.kill("SIGKILL");
        }
      }, 5e3);
    }
    this.process = null;
  }
}
const __filename = fileURLToPath$1(import.meta.url);
const __dirname = path$1.dirname(__filename);
const RENDERER_DIST = path$1.join(__dirname, "../dist");
const backend = new JavaBackend();
function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 720,
    icon: path$1.join(__dirname, "../public/card.png"),
    // o donde esté tu PNG
    webPreferences: {
      preload: path$1.join(__dirname, "preload.mjs")
    }
  });
  if (process.env.VITE_DEV_SERVER_URL) {
    win.loadURL(process.env.VITE_DEV_SERVER_URL);
  } else {
    win.loadFile(path$1.join(RENDERER_DIST, "index.html"));
  }
}
app.whenReady().then(async () => {
  backend.start();
  await new Promise((resolve) => setTimeout(resolve, 3e3));
  setTimeout(() => {
    createWindow();
  }, 6e3);
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
