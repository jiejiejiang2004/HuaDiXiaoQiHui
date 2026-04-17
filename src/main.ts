import { createApp } from "vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import App from "./App.vue";
import router from "./router";

window.addEventListener("error", (event) => {
  const message = event.message || "";
  if (
    message.includes(
      "ResizeObserver loop completed with undelivered notifications"
    ) ||
    message.includes("ResizeObserver loop limit exceeded")
  ) {
    event.stopImmediatePropagation();
  }
});

createApp(App).use(router).use(ElementPlus).mount("#app");
