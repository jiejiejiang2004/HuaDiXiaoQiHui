<template>
  <header class="swan-top-nav">
    <div class="swan-top-nav__inner">
      <router-link to="/" class="swan-top-nav__brand" aria-label="校企慧首页">
        <span class="swan-top-nav__mark" aria-hidden="true" />
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
          :to="{ path: '/', hash: '#job-list' }"
          class="swan-top-nav__link"
          :class="{ 'swan-top-nav__link--active': isJobsActive }"
        >
          求职
        </router-link>
      </nav>
      <div class="swan-top-nav__actions">
        <router-link v-if="!isAuthed" to="/login" class="swan-top-nav__auth"
          >登录</router-link
        >
        <router-link v-else :to="dashboardPath" class="swan-top-nav__auth"
          >我的主页</router-link
        >
      </div>
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

const isHomeActive = computed(
  () => route.path === "/" && route.hash !== "#job-list"
);

const isJobsActive = computed(
  () => route.path === "/" && route.hash === "#job-list"
);

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
  min-height: 56px;
}

.swan-top-nav__brand {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: var(--jb-text, #111827);
  font-weight: 800;
  letter-spacing: -0.02em;
  margin-right: 40px;
  flex-shrink: 0;
}

.swan-top-nav__mark {
  width: 32px;
  height: 32px;
  border-radius: var(--jb-radius-sm, 4px);
  background: linear-gradient(145deg, #0a65cc, #2563eb);
  flex-shrink: 0;
  box-shadow: 0 1px 4px rgba(10, 101, 204, 0.22);
}

.swan-top-nav__title {
  font-size: 1.125rem;
}

.swan-top-nav__links {
  display: flex;
  align-items: stretch;
  gap: 4px;
  flex: 1;
}

.swan-top-nav__link {
  position: relative;
  display: inline-flex;
  align-items: center;
  padding: 0 20px;
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
  left: 12px;
  right: 12px;
  bottom: 0;
  height: var(--jp-nav-underline, 3px);
  border-radius: var(--jb-radius-sm, 4px) var(--jb-radius-sm, 4px) 0 0;
  background: var(--jp-nav-active, #0a65cc);
}

.swan-top-nav__actions {
  margin-left: auto;
  display: flex;
  align-items: center;
}

.swan-top-nav__auth {
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--jp-nav-active, #0a65cc);
  text-decoration: none;
  padding: 8px 4px;
}

.swan-top-nav__auth:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .swan-top-nav__inner {
    flex-wrap: wrap;
    padding: 8px 16px 12px;
    gap: 8px;
  }

  .swan-top-nav__links {
    order: 3;
    width: 100%;
    border-top: 1px solid var(--jp-nav-border);
    padding-top: 4px;
  }

  .swan-top-nav__link {
    padding: 10px 14px;
  }
}
</style>
