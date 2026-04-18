<template>
  <section :id="sectionId" class="swan-job-list">
    <div class="swan-job-list__head">
      <h2>职位列表</h2>
      <p v-if="!loading">共 {{ total }} 个匹配结果</p>
    </div>
    <div class="swan-job-list__grid" v-loading="loading">
      <SwanJobCard
        v-for="job in jobs"
        :key="job.jobId"
        :job="job"
        :is-candidate="isCandidate"
        :selected="selectedJobIds.includes(job.jobId)"
        @open-detail="emit('open-detail', job.jobId)"
        @toggle-collect="emit('toggle-collect', job)"
        @toggle-select="(v) => emit('toggle-select', job.jobId, v)"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import SwanJobCard from "./SwanJobCard.vue";
import type { SwanJobSummary } from "./types";

withDefaults(
  defineProps<{
    jobs: SwanJobSummary[];
    loading: boolean;
    total: number;
    isCandidate: boolean;
    selectedJobIds: number[];
    sectionId?: string;
  }>(),
  {
    sectionId: "job-list",
  }
);

const emit = defineEmits<{
  "open-detail": [jobId: number];
  "toggle-collect": [job: SwanJobSummary];
  "toggle-select": [jobId: number, checked: string | number | boolean];
}>();
</script>

<style scoped>
.swan-job-list {
  margin-bottom: 24px;
}

.swan-job-list__head {
  margin-bottom: 16px;
}

.swan-job-list__head h2 {
  margin: 0 0 6px;
  font-size: 1.35rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
}

.swan-job-list__head p {
  margin: 0;
  font-size: 0.9375rem;
  color: var(--jb-text-muted);
}

.swan-job-list__grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}
</style>
