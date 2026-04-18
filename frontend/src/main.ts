import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import App from "./App.vue";
import router from "./router";
import http from "./api/http";
import { setupMocks } from "./mock/setup";

if (process.env.VUE_APP_USE_MOCK === "true") {
  setupMocks(http);
}

const NativeResizeObserver = window.ResizeObserver;
const nativeConsoleError = window.console.error;

window.console.error = (...args: unknown[]) => {
  const joined = args.map((item) => String(item)).join(" ");
  if (
    joined.includes(
      "ResizeObserver loop completed with undelivered notifications"
    ) ||
    joined.includes("ResizeObserver loop limit exceeded")
  ) {
    return;
  }
  nativeConsoleError(...args);
};

if (NativeResizeObserver) {
  window.ResizeObserver = class ResizeObserver extends NativeResizeObserver {
    constructor(callback: ResizeObserverCallback) {
      super((entries, observer) => {
        window.requestAnimationFrame(() => callback(entries, observer));
      });
    }
  };
}

window.addEventListener(
  "error",
  (event) => {
    const message = event.message || "";
    if (
      message.includes(
        "ResizeObserver loop completed with undelivered notifications"
      ) ||
      message.includes("ResizeObserver loop limit exceeded")
    ) {
      event.preventDefault();
      event.stopImmediatePropagation();
    }
  },
  true
);

window.addEventListener(
  "unhandledrejection",
  (event) => {
    const handledReason = event.reason as { handled?: boolean } | undefined;
    if (handledReason?.handled) {
      event.preventDefault();
      return;
    }
    const reason = String(event.reason || "");
    if (
      reason.includes(
        "ResizeObserver loop completed with undelivered notifications"
      ) ||
      reason.includes("ResizeObserver loop limit exceeded")
    ) {
      event.preventDefault();
    }
  },
  true
);

const app = createApp(App);

app.config.errorHandler = (error) => {
  const handledError = error as { handled?: boolean } | undefined;
  if (handledError?.handled) {
    return;
  }
  nativeConsoleError(error);
};

app.use(router).use(ElementPlus).mount("#app");
