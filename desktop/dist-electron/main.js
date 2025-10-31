var h = Object.defineProperty;
var f = (o, e, s) => e in o ? h(o, e, { enumerable: !0, configurable: !0, writable: !0, value: s }) : o[e] = s;
var m = (o, e, s) => f(o, typeof e != "symbol" ? e + "" : e, s);
import { app as a, BrowserWindow as v } from "electron";
import i from "node:path";
import { fileURLToPath as R } from "node:url";
import { spawn as _ } from "child_process";
import n from "path";
import g from "fs";
import { fileURLToPath as u } from "url";
const E = u(import.meta.url), w = n.dirname(E);
class S {
  constructor() {
    m(this, "process", null);
  }
  start() {
    var p, d;
    const e = process.env.NODE_ENV === "development";
    let s, r;
    e ? (s = n.join(w, "../backend"), r = n.join(s, "dev-cards-0.0.1-SNAPSHOT.jar")) : (s = n.join(process.resourcesPath, "backend"), r = n.join(s, "dev-cards-0.0.1-SNAPSHOT.jar")), console.log("🔍 Checking backend structure..."), console.log("   Backend dir:", s), console.log("   JAR path:", r), console.log("   JAR exists:", g.existsSync(r)), console.log("🚀 Starting Java backend..."), this.process = _("java", ["-jar", r], {
      //path.basename(jarPath)
      //   cwd: backendDir,
      env: {
        ...process.env,
        SPRING_PROFILES_ACTIVE: e ? "development" : "production"
      }
    }), (p = this.process.stdout) == null || p.on("data", (t) => {
      console.log(`[Java]: ${t.toString().trim()}`);
    }), (d = this.process.stderr) == null || d.on("data", (t) => {
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
const k = R(import.meta.url), c = i.dirname(k), j = i.join(c, "../dist"), l = new S();
function T() {
  const o = new v({
    width: 1280,
    height: 720,
    icon: i.join(c, "../public/card.png"),
    // o donde esté tu PNG
    webPreferences: {
      preload: i.join(c, "preload.mjs")
    }
  });
  process.env.VITE_DEV_SERVER_URL ? o.loadURL(process.env.VITE_DEV_SERVER_URL) : o.loadFile(i.join(j, "index.html"));
}
a.whenReady().then(async () => {
  l.start(), await new Promise((o) => setTimeout(o, 3e3)), setTimeout(() => {
    T();
  }, 6e3);
});
a.on("before-quit", () => {
  l.stop();
});
a.on("window-all-closed", () => {
  l.stop(), process.platform !== "darwin" && a.quit();
});
