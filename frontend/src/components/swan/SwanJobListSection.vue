<template>
  <section :id="sectionId" class="swan-job-list">
    <div class="swan-job-list__head">
      <div class="swan-job-list__titles">
        <h2>{{ heading }}</h2>
        <p v-if="showSummary && !loading" class="swan-job-list__summary">
          共 {{ total }} 个匹配结果
        </p>
      </div>
      <router-link
        v-if="viewAllTo"
        :to="viewAllTo"
        class="swan-job-list__view-all"
      >
        查看全部
        <span class="swan-job-list__arrow" aria-hidden="true" />
      </router-link>
    </div>
    <div
      class="swan-job-list__grid"
      :class="{
        'swan-job-list__grid--home-rows': layout === 'home-three-rows',
      }"
      v-loading="loading"
    >
      <SwanJobCard
        v-for="(job, index) in jobs"
        :key="job.jobId"
        :job="job"
        :is-candidate="isCandidate"
        :selected="selectedJobIds.includes(job.jobId)"
        :compact="layout === 'home-three-rows'"
        :warm-accent="layout === 'home-three-rows' && index % 4 === 1"
        :selectable="selectable"
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
    heading?: string;
    /** 首页：容器内自适应列换行、略缩小卡片；求职页：自适应列 */
    layout?: "home-three-rows" | "list";
    /** 首页「查看全部」链接 */
    viewAllTo?: string;
    showSummary?: boolean;
    selectable?: boolean;
  }>(),
  {
    sectionId: "job-list",
    heading: "职位列表",
    layout: "list",
    showSummary: true,
    selectable: false,
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
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.swan-job-list__titles {
  min-width: 0;
}

.swan-job-list__titles h2 {
  margin: 0 0 6px;
  font-size: 1.35rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
}

.swan-job-list__titles h2:only-child {
  margin-bottom: 0;
}

.swan-job-list__view-all {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--jb-primary);
  text-decoration: none;
  white-space: nowrap;
}

.swan-job-list__view-all:hover {
  text-decoration: underline;
  text-underline-offset: 3px;
}

.swan-job-list__arrow {
  width: 8px;
  height: 8px;
  border-right: 2px solid currentColor;
  border-bottom: 2px solid currentColor;
  transform: rotate(-45deg);
  margin-top: -2px;
}

.swan-job-list__summary {
  margin: 0;
  font-size: 0.9375rem;
  color: var(--jb-text-muted);
}

.swan-job-list__grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.swan-job-list__grid--home-rows {
  grid-template-columns: repeat(auto-fill, minmax(min(100%, 236px), 1fr));
  gap: 14px;
}
</style>
