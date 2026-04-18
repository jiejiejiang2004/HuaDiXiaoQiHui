<template>
  <div class="app-shell">
    <SwanTopNav />
    <main class="app-main">
      <router-view />
    </main>
    <SwanSiteFooter v-if="showFooter" />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import SwanSiteFooter from "./swan/SwanSiteFooter.vue";
import SwanTopNav from "./swan/SwanTopNav.vue";

const route = useRoute();

const showFooter = computed(
  () => route.path === "/" || route.path === "/login"
);
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
