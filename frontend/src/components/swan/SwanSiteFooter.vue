<template>
  <footer class="swan-site-footer">
    <p>
      界面视觉参考
      <a
        href="https://www.figma.com/community/file/1225308519419319279/jobpilot-job-portal-figma-ui-template-community"
        target="_blank"
        rel="noopener noreferrer"
        >JobPilot · Figma Community</a
      >
    </p>
  </footer>
</template>

<script setup lang="ts"></script>

<style scoped>
.swan-site-footer {
  padding: 20px;
  text-align: center;
  font-size: 0.8125rem;
  color: var(--jb-text-muted);
  border-top: 1px solid var(--jb-border);
  background: var(--jb-bg-elevated);
}

.swan-site-footer a {
  color: var(--jb-primary);
  text-decoration: none;
}

.swan-site-footer a:hover {
  text-decoration: underline;
}
</style>
