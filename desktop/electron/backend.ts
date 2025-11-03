import { spawn, ChildProcess } from "child_process";
import path from "path";
import fs from "fs";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export class JavaBackend {
  private process: ChildProcess | null = null;

  start() {
    const isDev = process.env.NODE_ENV === "development";

    let backendDir: string;
    let jarPath: string;

    if (isDev) {
      backendDir = path.join(__dirname, "../backend");
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
      env: {
        ...process.env,
        SPRING_PROFILES_ACTIVE: isDev ? "development" : "production",
      },
    });

    this.process.stdout?.on("data", (data) => {
      console.log(`[Java]: ${data.toString().trim()}`);
    });

    this.process.stderr?.on("data", (data) => {
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
      }, 5000);
    }

    this.process = null;
  }
}
