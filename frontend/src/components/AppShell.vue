<template>
  <div class="app-shell">
    <SwanTopNav />
    <main class="app-main">
      <router-view />
    </main>
    <AuthModal />
  </div>
</template>

<script setup lang="ts">
import SwanTopNav from "./swan/SwanTopNav.vue";
import AuthModal from "./AuthModal.vue";
</script>

<style scoped>
.app-shell {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  height: 100vh;
  max-height: 100vh;
  background: #fff;
  overflow: hidden;
}

.app-main {
  flex: 1;
  min-height: 0;
  overflow-x: hidden;
  overflow-y: auto;
  background: #fff;
  /* 主内容区滚动，窗口不出现纵向条，顶栏宽度与位置保持稳定 */
  scrollbar-gutter: stable;
}
</style>
