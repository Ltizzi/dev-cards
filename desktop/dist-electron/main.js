import { app as i, BrowserWindow as r } from "electron";
import e from "node:path";
import { fileURLToPath as t } from "node:url";
const a = t(import.meta.url), o = e.dirname(a), d = e.join(o, "../dist");
function m() {
  const n = new r({
    width: 800,
    height: 600,
    icon: e.join(o, "../public/card.png"),
    // o donde esté tu PNG
    webPreferences: {
      preload: e.join(o, "preload.mjs")
    }
  });
  process.env.VITE_DEV_SERVER_URL ? n.loadURL(process.env.VITE_DEV_SERVER_URL) : n.loadFile(e.join(d, "index.html"));
}
i.whenReady().then(m);
