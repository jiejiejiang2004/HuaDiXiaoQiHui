import { ref } from "vue";

const visible = ref(false);
const initialTab = ref<"candidate" | "enterprise" | "admin">("candidate");
const pendingRedirect = ref<string | null>(null);

export function openAuthModal(options?: { tab?: string; redirect?: string }) {
  const t = options?.tab;
  if (t === "enterprise" || t === "admin" || t === "candidate") {
    initialTab.value = t;
  } else {
    initialTab.value = "candidate";
  }
  pendingRedirect.value =
    typeof options?.redirect === "string" && options.redirect
      ? options.redirect
      : null;
  visible.value = true;
}

export function closeAuthModal() {
  visible.value = false;
  pendingRedirect.value = null;
}

export function useAuthModal() {
  return {
    visible,
    initialTab,
    pendingRedirect,
    openAuthModal,
    closeAuthModal,
  };
}
