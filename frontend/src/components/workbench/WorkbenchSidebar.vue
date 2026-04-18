<template>
  <aside
    class="workbench-sidebar"
    :class="{
      'workbench-sidebar--mobile': isNarrowScreen,
      'workbench-sidebar--mobile-open': isNarrowScreen && mobileNavExpanded,
    }"
    :aria-label="ariaLabel"
  >
    <template v-if="isNarrowScreen">
      <div class="workbench-sidebar__mobile-head">
        <p class="workbench-sidebar__label workbench-sidebar__label--mobile">
          {{ brandLabel }}
        </p>
        <button
          type="button"
          class="workbench-sidebar__toggle"
          :aria-expanded="mobileNavExpanded"
          :aria-controls="navPanelId"
          @click="mobileNavExpanded = !mobileNavExpanded"
        >
          <span class="workbench-sidebar__toggle-text">
            <span class="workbench-sidebar__toggle-prefix">当前</span>
            {{ activeLabel }}
          </span>
          <span
            class="workbench-sidebar__chevron"
            :class="{
              'workbench-sidebar__chevron--open': mobileNavExpanded,
            }"
            aria-hidden="true"
          />
        </button>
      </div>
    </template>
    <p v-else class="workbench-sidebar__label">{{ brandLabel }}</p>

    <nav
      :id="navPanelId"
      class="workbench-nav"
      :aria-hidden="isNarrowScreen && !mobileNavExpanded ? true : undefined"
    >
      <button
        v-for="item in tabs"
        :key="item.key"
        type="button"
        class="workbench-nav__item"
        :class="{ 'workbench-nav__item--active': activeKey === item.key }"
        @click="onSelect(item.key)"
      >
        {{ item.label }}
      </button>
    </nav>
    <div class="workbench-sidebar__foot">
      <div v-if="$slots['footer-extra']" class="workbench-sidebar__foot-extra">
        <slot name="footer-extra" />
      </div>
      <button type="button" class="workbench-nav__logout" @click="emitLogout">
        {{ logoutLabel }}
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, watch } from "vue";

export interface WorkbenchTabItem {
  key: string;
  label: string;
}

const MOBILE_BREAKPOINT = "(max-width: 960px)";

const props = withDefaults(
  defineProps<{
    brandLabel: string;
    tabs: WorkbenchTabItem[];
    activeKey: string;
    navPanelId?: string;
    ariaLabel?: string;
    logoutLabel?: string;
  }>(),
  {
    navPanelId: "workbench-nav-panel",
    ariaLabel: "工作台导航",
    logoutLabel: "退出登录",
  }
);

const emit = defineEmits<{
  (e: "select", key: string): void;
  (e: "logout"): void;
}>();

const isNarrowScreen = ref(
  typeof window !== "undefined" && window.matchMedia(MOBILE_BREAKPOINT).matches
);
const mobileNavExpanded = ref(false);
let mq: MediaQueryList | null = null;

function syncMq() {
  isNarrowScreen.value = mq?.matches ?? false;
}

function bindMq() {
  mq = window.matchMedia(MOBILE_BREAKPOINT);
  syncMq();
  if (typeof mq.addEventListener === "function") {
    mq.addEventListener("change", syncMq);
  } else {
    mq.addListener(syncMq);
  }
}

function unbindMq() {
  if (!mq) {
    return;
  }
  if (typeof mq.removeEventListener === "function") {
    mq.removeEventListener("change", syncMq);
  } else {
    mq.removeListener(syncMq);
  }
  mq = null;
}

const activeLabel = computed(
  () => props.tabs.find((t) => t.key === props.activeKey)?.label ?? "菜单"
);

watch(isNarrowScreen, (narrow) => {
  if (!narrow) {
    mobileNavExpanded.value = false;
  }
});

watch(
  () => props.activeKey,
  () => {
    if (isNarrowScreen.value) {
      mobileNavExpanded.value = false;
    }
  }
);

function onSelect(key: string) {
  emit("select", key);
  if (isNarrowScreen.value) {
    mobileNavExpanded.value = false;
  }
}

function emitLogout() {
  emit("logout");
}

onMounted(() => {
  bindMq();
});

onUnmounted(() => {
  unbindMq();
});
</script>

<style scoped>
.workbench-sidebar {
  position: sticky;
  top: 0;
  z-index: 20;
  width: 248px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-self: flex-start;
  height: calc(100vh - var(--app-header-height, 56px));
  min-height: calc(100vh - var(--app-header-height, 56px));
  max-height: calc(100vh - var(--app-header-height, 56px));
  overflow: hidden;
  background: #fff;
  border-right: 1px solid #d8dde3;
  padding: 20px 0 16px;
  box-sizing: border-box;
}

.workbench-sidebar__label {
  flex-shrink: 0;
  margin: 0 20px 14px;
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  color: #94a3b8;
  text-transform: uppercase;
}

.workbench-nav {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  gap: 0;
  padding: 0;
  overflow-y: auto;
  scrollbar-gutter: stable;
}

.workbench-nav__item {
  display: block;
  width: 100%;
  padding: 12px 16px;
  border: none;
  border-radius: 0;
  background: transparent;
  color: #334155;
  font-size: 0.9375rem;
  font-weight: 500;
  text-align: left;
  cursor: pointer;
  transition: background 0.12s ease, color 0.12s ease;
  font-family: inherit;
  border-left: 3px solid transparent;
}

.workbench-nav__item:hover {
  background: #f3f5f7;
  color: #146bce;
}

.workbench-nav__item--active {
  background: #e7f0fa;
  color: #146bce;
  font-weight: 600;
  border-left-color: #146bce;
}

.workbench-sidebar__foot {
  flex-shrink: 0;
  padding: 12px 0 0;
  margin-top: auto;
  border-top: 1px solid #d8dde3;
}

.workbench-sidebar__foot-extra {
  padding: 0 12px 10px;
}

.workbench-sidebar__foot-extra :deep(.el-button) {
  width: 100%;
  margin-bottom: 6px;
}

.workbench-nav__logout {
  display: block;
  width: 100%;
  padding: 12px 16px;
  border: none;
  border-radius: 0;
  background: transparent;
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  font-family: inherit;
  transition: background 0.12s ease, color 0.12s ease;
  text-align: left;
}

.workbench-nav__logout:hover {
  background: #fef2f2;
  color: #dc2626;
}

.workbench-sidebar__mobile-head {
  width: 100%;
  box-sizing: border-box;
}

.workbench-sidebar__label--mobile {
  margin: 0 0 8px;
}

.workbench-sidebar__toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #e2e8f0;
  border-radius: var(--jb-radius-md, 4px);
  background: #f8fafc;
  color: #0f172a;
  font-size: 0.9375rem;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  text-align: left;
  transition: background 0.15s ease, border-color 0.15s ease;
}

.workbench-sidebar__toggle:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.workbench-sidebar__toggle-text {
  display: flex;
  align-items: baseline;
  gap: 8px;
  min-width: 0;
  flex: 1;
}

.workbench-sidebar__toggle-prefix {
  font-size: 0.6875rem;
  font-weight: 700;
  color: #94a3b8;
  letter-spacing: 0.08em;
  flex-shrink: 0;
}

.workbench-sidebar__chevron {
  width: 9px;
  height: 9px;
  flex-shrink: 0;
  border-right: 2px solid #64748b;
  border-bottom: 2px solid #64748b;
  transform: rotate(45deg) translateY(-2px);
  transition: transform 0.2s ease;
}

.workbench-sidebar__chevron--open {
  transform: rotate(-135deg) translateY(2px);
}

.workbench-sidebar--mobile:not(.workbench-sidebar--mobile-open) .workbench-nav,
.workbench-sidebar--mobile:not(.workbench-sidebar--mobile-open)
  .workbench-sidebar__foot {
  display: none;
}

@media (max-width: 960px) {
  .workbench-sidebar {
    position: static;
    top: auto;
    z-index: auto;
    height: auto;
    min-height: 0;
    max-height: none;
    overflow: visible;
    align-self: stretch;
    width: 100%;
    flex-direction: column;
    align-items: stretch;
    padding: 12px 0 8px;
    border-right: none;
    border-bottom: 1px solid #d8dde3;
  }

  .workbench-sidebar__mobile-head {
    padding: 0 16px 4px;
  }

  .workbench-sidebar__label--mobile {
    margin: 0 0 8px;
  }

  .workbench-nav {
    flex: none;
    flex-direction: column;
    flex-wrap: nowrap;
    width: 100%;
    padding: 4px 16px 8px;
    box-sizing: border-box;
    overflow: visible;
    gap: 0;
  }

  .workbench-nav__item {
    flex: none;
    width: 100%;
    min-width: 0;
    text-align: left;
    padding: 12px 14px;
    font-size: 0.875rem;
    border-left: 3px solid transparent;
    border-radius: var(--jb-radius-sm, 4px);
  }

  .workbench-nav__item--active {
    border-left-color: #146bce;
    background: #e7f0fa;
  }

  .workbench-sidebar__foot {
    width: 100%;
    margin-top: 0;
    padding: 8px 16px 0;
    border-top: 1px solid #d8dde3;
    flex-shrink: 0;
  }

  .workbench-sidebar__foot-extra {
    padding: 0 0 10px;
  }

  .workbench-nav__logout {
    text-align: center;
  }
}
</style>
