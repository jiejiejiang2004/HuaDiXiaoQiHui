<template>
  <header class="swan-top-nav">
    <div class="swan-top-nav__inner">
      <router-link to="/" class="swan-top-nav__brand" aria-label="校企慧首页">
        <span
          class="material-symbols-outlined swan-top-nav__mark"
          aria-hidden="true"
          >business_center</span
        >
        <span class="swan-top-nav__title">校企慧</span>
      </router-link>

      <nav class="swan-top-nav__links" aria-label="主导航">
        <router-link
          to="/"
          class="swan-top-nav__link"
          :class="{ 'swan-top-nav__link--active': isHomeActive }"
        >
          首页
        </router-link>
        <router-link
          to="/jobs"
          class="swan-top-nav__link"
          :class="{ 'swan-top-nav__link--active': isJobsActive }"
        >
          求职
        </router-link>
        <router-link
          :to="profileTarget"
          class="swan-top-nav__link"
          :class="{ 'swan-top-nav__link--active': isProfileActive }"
        >
          个人主页
        </router-link>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { getAccessToken, getUserType } from "@/utils/auth";

const route = useRoute();
const token = ref("");
const userType = ref<ReturnType<typeof getUserType>>("");

function syncAuth() {
  token.value = getAccessToken();
  userType.value = getUserType();
}

const isAuthed = computed(() => Boolean(token.value));

const dashboardPath = computed(() => {
  switch (userType.value) {
    case "CANDIDATE":
      return "/candidate";
    case "ENTERPRISE":
      return "/enterprise";
    case "ADMIN":
      return "/admin";
    default:
      return "/login";
  }
});

/** 未登录时个人主页进入登录页 */
const profileTarget = computed(() =>
  isAuthed.value ? dashboardPath.value : "/login"
);

const isHomeActive = computed(() => route.path === "/");

const isJobsActive = computed(() => route.path === "/jobs");

const isProfileActive = computed(() => {
  if (!isAuthed.value) {
    return route.path === "/login";
  }
  return (
    route.path === "/candidate" ||
    route.path === "/enterprise" ||
    route.path === "/admin"
  );
});

onMounted(() => {
  syncAuth();
  window.addEventListener("storage", syncAuth);
});

watch(
  () => route.fullPath,
  () => {
    syncAuth();
  }
);
</script>

<style scoped>
.swan-top-nav {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--jp-nav-bg, #f1f2f4);
  border-bottom: 1px solid var(--jp-nav-border, #e5e7eb);
  box-shadow: 0 1px 0 rgba(255, 255, 255, 0.8) inset;
}

.swan-top-nav__inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  gap: 24px;
  min-height: 56px;
}

.swan-top-nav__brand {
  align-self: center;
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: var(--jb-text, #111827);
  font-weight: 800;
  letter-spacing: -0.02em;
  flex-shrink: 0;
}

.swan-top-nav__mark {
  font-size: 2rem;
  line-height: 1;
  color: var(--jb-primary, #0a65cc);
  font-variation-settings: "FILL" 0, "wght" 500, "GRAD" 0, "opsz" 48;
  flex-shrink: 0;
  user-select: none;
}

.swan-top-nav__title {
  font-size: 1.125rem;
}

.swan-top-nav__links {
  display: flex;
  align-items: stretch;
  gap: 4px;
  margin-left: auto;
}

.swan-top-nav__link {
  position: relative;
  display: inline-flex;
  align-items: center;
  align-self: stretch;
  padding: 0 18px;
  text-decoration: none;
  color: var(--jp-nav-text, #5e6670);
  font-size: 0.9375rem;
  font-weight: 500;
  transition: color 0.15s ease;
}

.swan-top-nav__link:hover {
  color: var(--jp-nav-active, #0a65cc);
}

.swan-top-nav__link--active {
  color: var(--jp-nav-active, #0a65cc);
  font-weight: 600;
}

.swan-top-nav__link--active::after {
  content: "";
  position: absolute;
  left: 10px;
  right: 10px;
  bottom: 0;
  height: var(--jp-nav-underline, 3px);
  border-radius: var(--jb-radius-sm, 4px) var(--jb-radius-sm, 4px) 0 0;
  background: var(--jp-nav-active, #0a65cc);
  pointer-events: none;
}

@media (max-width: 640px) {
  .swan-top-nav__inner {
    flex-wrap: wrap;
    padding: 10px 16px 12px;
  }

  .swan-top-nav__links {
    width: 100%;
    justify-content: flex-end;
    border-top: 1px solid var(--jp-nav-border);
    padding-top: 6px;
  }

  .swan-top-nav__link {
    padding: 10px 12px;
  }
}
</style>
