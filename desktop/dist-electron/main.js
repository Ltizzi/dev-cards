var f = Object.defineProperty;
var v = (s, e, o) => e in s ? f(s, e, { enumerable: !0, configurable: !0, writable: !0, value: o }) : s[e] = o;
var m = (s, e, o) => v(s, typeof e != "symbol" ? e + "" : e, o);
import { app as c, ipcMain as g, BrowserWindow as R } from "electron";
import n from "node:path";
import { fileURLToPath as _ } from "node:url";
import { spawn as u } from "child_process";
import a from "path";
import E from "fs";
import { fileURLToPath as j } from "url";
const w = j(import.meta.url), S = a.dirname(w);
class k {
  constructor() {
    m(this, "process", null);
  }
  start() {
    var d, p;
    const e = process.env.NODE_ENV === "development";
    let o, i;
    e ? (o = a.join(S, "../backend"), i = a.join(o, "dev-cards-0.0.1-SNAPSHOT.jar")) : (o = a.join(process.resourcesPath, "backend"), i = a.join(o, "dev-cards-0.0.1-SNAPSHOT.jar")), console.log("🔍 Checking backend structure..."), console.log("   Backend dir:", o), console.log("   JAR path:", i), console.log("   JAR exists:", E.existsSync(i)), console.log("🚀 Starting Java backend..."), this.process = u("java", ["-jar", i], {
      env: {
        ...process.env,
        SPRING_PROFILES_ACTIVE: e ? "development" : "production"
      }
    }), (d = this.process.stdout) == null || d.on("data", (t) => {
      console.log(`[Java]: ${t.toString().trim()}`);
    }), (p = this.process.stderr) == null || p.on("data", (t) => {
      console.error(`[Java Error]: ${t.toString().trim()}`);
    }), this.process.on("error", (t) => {
      console.error("❌ Failed to start Java process:", t);
    }), this.process.on("close", (t) => {
      console.log(`Java backend exited with code ${t}`), this.process = null;
    });
  }
  stop() {
    this.process && (console.log("🛑 Stopping Java backend..."), this.process.kill("SIGTERM"), setTimeout(() => {
      this.process && (console.log("⚠️ Force killing Java backend..."), this.process.kill("SIGKILL"));
    }, 5e3)), this.process = null;
  }
}
const T = _(import.meta.url), h = n.dirname(T), P = n.join(h, "../dist"), l = new k();
let r;
function b() {
  r = new R({
    width: 1280,
    height: 720,
    icon: n.join(h, "../public/card.png"),
    webPreferences: {
      // preload: path.join(__dirname, "preload.js"),
      // preload: process.env.VITE_DEV_SERVER_URL
      //   ? path.join(process.cwd(), "dist-electron/preload.js") // DEV
      //   : path.join(__dirname, "preload.js"), // BUILD
      preload: n.join(process.cwd(), "dist-electron/preload.js"),
      contextIsolation: !0,
      nodeIntegration: !1
    }
  }), process.env.VITE_DEV_SERVER_URL ? r.loadURL(process.env.VITE_DEV_SERVER_URL) : r.loadFile(n.join(P, "index.html")), r.setTitle("Dev Cards");
}
c.whenReady().then(async () => {
  console.log(
    ">>> PRELOAD PATH:",
    n.join(process.cwd(), "dist-electron/preload.js")
  ), l.start(), await new Promise((s) => setTimeout(s, 3e3)), setTimeout(() => {
    b();
  }, 6e3);
});
g.on("set-project-title", (s, e) => {
  r && (e && e.length > 0 ? r.setTitle(`Dev Cards -  ${e}`) : r.setTitle("Dev Cards"));
});
c.on("before-quit", () => {
  l.stop();
});
c.on("window-all-closed", () => {
  l.stop(), process.platform !== "darwin" && c.quit();
});
