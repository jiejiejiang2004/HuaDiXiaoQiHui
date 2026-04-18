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
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
}
</style>
