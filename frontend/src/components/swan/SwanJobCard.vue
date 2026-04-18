<template>
  <article class="swan-job-card">
    <div class="swan-job-card__top">
      <div class="swan-job-card__avatar" aria-hidden="true">
        {{ companyInitial(job.companyName) }}
      </div>
      <div class="swan-job-card__titles">
        <h3>{{ job.jobName }}</h3>
        <p>{{ job.companyName }}</p>
      </div>
      <div
        class="swan-job-card__salary"
        :title="`月薪 ${job.salaryMin} - ${job.salaryMax}`"
      >
        ¥{{ job.salaryMin }}–{{ job.salaryMax }}
      </div>
    </div>
    <div class="swan-job-card__meta">
      <span>{{ job.location }}</span>
      <span class="swan-job-card__dot" />
      <span>{{ job.education }}</span>
      <span class="swan-job-card__dot" />
      <span>{{ job.experience }}</span>
    </div>
    <div class="swan-job-card__tags">
      <span
        v-for="tag in job.welfare || []"
        :key="tag"
        class="swan-job-card__tag"
        >{{ tag }}</span
      >
    </div>
    <div class="swan-job-card__actions">
      <el-checkbox
        v-if="isCandidate"
        :model-value="selected"
        @change="(v: string | number | boolean) => emit('toggle-select', v)"
        @click.stop
      >
        加入投递
      </el-checkbox>
      <div class="swan-job-card__actions-right">
        <el-button
          v-if="isCandidate"
          text
          :type="job.collected ? 'warning' : 'default'"
          @click.stop="emit('toggle-collect')"
        >
          {{ job.collected ? "已收藏" : "收藏" }}
        </el-button>
        <el-button type="primary" plain @click.stop="emit('open-detail')">
          详情
        </el-button>
      </div>
    </div>
  </article>
</template>

<script setup lang="ts">
import type { SwanJobSummary } from "./types";

defineProps<{
  job: SwanJobSummary;
  isCandidate: boolean;
  selected: boolean;
}>();

const emit = defineEmits<{
  "open-detail": [];
  "toggle-collect": [];
  "toggle-select": [value: string | number | boolean];
}>();

function companyInitial(name: string) {
  const s = (name || "公").trim();
  return s.charAt(0) || "企";
}
</script>

<style scoped>
.swan-job-card {
  border-radius: var(--jb-radius-md);
  background: var(--jb-bg-elevated);
  border: 1px solid var(--jb-border);
  box-shadow: var(--jb-shadow-sm);
  padding: 20px;
  transition: box-shadow 0.2s ease, border-color 0.2s ease;
}

.swan-job-card:hover {
  box-shadow: var(--jb-shadow-md);
  border-color: rgba(10, 101, 204, 0.28);
}

.swan-job-card__top {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}

.swan-job-card__avatar {
  width: 48px;
  height: 48px;
  border-radius: var(--jb-radius-sm);
  background: linear-gradient(
    145deg,
    var(--el-color-primary-light-7),
    var(--el-color-primary-light-9)
  );
  color: var(--jb-primary);
  font-weight: 800;
  font-size: 1.125rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.swan-job-card__titles {
  flex: 1;
  min-width: 0;
}

.swan-job-card__titles h3 {
  margin: 0 0 6px;
  font-size: 1.0625rem;
  font-weight: 700;
  color: var(--jb-text);
  line-height: 1.35;
}

.swan-job-card__titles p {
  margin: 0;
  font-size: 0.875rem;
  color: var(--jb-text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.swan-job-card__salary {
  flex-shrink: 0;
  padding: 6px 10px;
  border-radius: var(--jb-radius-sm);
  font-size: 0.8125rem;
  font-weight: 700;
  color: var(--jb-salary);
  background: var(--el-color-primary-light-9);
  border: 1px solid var(--el-color-primary-light-7);
  white-space: nowrap;
}

.swan-job-card__meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  margin: 16px 0 12px;
  font-size: 0.8125rem;
  color: var(--jb-text-muted);
}

.swan-job-card__dot {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #cbd5e1;
}

.swan-job-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 28px;
}

.swan-job-card__tag {
  font-size: 0.75rem;
  padding: 3px 8px;
  border-radius: var(--jb-radius-sm);
  background: var(--el-color-primary-light-9);
  color: #1e40af;
  font-weight: 500;
}

.swan-job-card__actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--jb-border);
}

.swan-job-card__actions-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
