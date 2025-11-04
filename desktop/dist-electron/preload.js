import { contextBridge as r, ipcRenderer as t } from "electron";
console.log("PRELOAD LOADED ✅✅✅");
r.exposeInMainWorld("ipcRenderer", {
  on(...e) {
    const [n, o] = e;
    return t.on(
      n,
      (c, ...i) => o(c, ...i)
    );
  },
  off(...e) {
    const [n, ...o] = e;
    return t.off(n, ...o);
  },
  send(...e) {
    const [n, ...o] = e;
    return t.send(n, ...o);
  },
  invoke(...e) {
    const [n, ...o] = e;
    return t.invoke(n, ...o);
  }
  // You can expose other APTs you need here.
  // ...
});
r.exposeInMainWorld("appControl", {
  setProjectTitle: (e) => t.send("set-project-title", e)
});
