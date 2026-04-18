<template>
  <article
    class="swan-job-card"
    :class="{
      'swan-job-card--warm': warmAccent,
      'swan-job-card--compact': isCompact,
    }"
    role="button"
    tabindex="0"
    @click="emit('open-detail')"
    @keydown.enter.prevent="emit('open-detail')"
  >
    <h3 class="swan-job-card__title">{{ job.jobName }}</h3>

    <div class="swan-job-card__meta-row">
      <span class="swan-job-card__badge">{{ jobTypeLabel }}</span>
      <span class="swan-job-card__salary">{{ salaryText }}</span>
    </div>

    <ul
      v-if="welfareTags.length"
      class="swan-job-card__tags"
      aria-label="岗位标签"
    >
      <li v-for="tag in welfareTags" :key="tag" class="swan-job-card__tag">
        {{ tag }}
      </li>
    </ul>

    <div class="swan-job-card__company">
      <div class="swan-job-card__logo" aria-hidden="true">
        {{ companyInitial(job.companyName) }}
      </div>
      <div class="swan-job-card__company-text">
        <p class="swan-job-card__company-name">{{ job.companyName }}</p>
        <p class="swan-job-card__location">
          <span class="swan-job-card__pin-badge" aria-hidden="true">
            <span class="swan-job-card__pin" />
          </span>
          {{ job.location }}
        </p>
      </div>
      <button
        type="button"
        class="swan-job-card__bookmark"
        :class="{ 'swan-job-card__bookmark--on': job.collected }"
        :title="job.collected ? '已收藏' : '收藏'"
        aria-label="收藏职位"
        @click.stop="emit('toggle-collect')"
      >
        <svg viewBox="0 0 24 24" width="22" height="22" aria-hidden="true">
          <path
            :fill="job.collected ? 'currentColor' : 'none'"
            stroke="currentColor"
            stroke-width="1.75"
            d="M6 4h12v16l-6-4-6 4V4z"
          />
        </svg>
      </button>
    </div>

    <div v-if="selectable && isCandidate" class="swan-job-card__batch">
      <el-checkbox
        :model-value="selected"
        @change="(v: string | number | boolean) => emit('toggle-select', v)"
        @click.stop
      >
        加入投递
      </el-checkbox>
    </div>
  </article>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { SwanJobSummary } from "./types";

const props = defineProps<{
  job: SwanJobSummary;
  isCandidate: boolean;
  selected: boolean;
  /** 浅色暖底，接近参考稿中的高亮卡片 */
  warmAccent?: boolean;
  /** 首页等窄栅格：略缩小内边距与字号 */
  compact?: boolean;
  /** 求职页批量投递：显示勾选框 */
  selectable?: boolean;
}>();

const emit = defineEmits<{
  "open-detail": [];
  "toggle-collect": [];
  "toggle-select": [value: string | number | boolean];
}>();

const warmAccent = computed(() => props.warmAccent === true);
const isCompact = computed(() => props.compact === true);
const selectable = computed(() => props.selectable === true);

const jobTypeLabel = computed(() => props.job.jobType?.trim() || "全职");

const welfareTags = computed(() =>
  (props.job.welfare ?? []).map((t) => String(t).trim()).filter(Boolean)
);

const salaryText = computed(() => {
  const a = formatMoney(props.job.salaryMin);
  const b = formatMoney(props.job.salaryMax);
  return `薪资：¥${a} - ¥${b} / 月`;
});

function formatMoney(n: number) {
  return new Intl.NumberFormat("zh-CN").format(n);
}

function companyInitial(name: string) {
  const s = (name || "公").trim();
  return s.charAt(0) || "企";
}
</script>

<style scoped>
.swan-job-card {
  position: relative;
  border-radius: var(--jb-radius-md);
  background: var(--jb-bg-elevated);
  border: 1px solid var(--jb-border);
  box-shadow: var(--jb-shadow-sm);
  padding: 22px 20px 18px;
  text-align: left;
  cursor: pointer;
  transition: box-shadow 0.2s ease, border-color 0.2s ease, transform 0.15s ease;
}

.swan-job-card:hover {
  box-shadow: var(--jb-shadow-md);
  border-color: rgba(10, 101, 204, 0.18);
  background: #fff;
}

.swan-job-card:focus-visible {
  outline: 2px solid var(--jb-primary);
  outline-offset: 2px;
}

.swan-job-card--warm {
  background: linear-gradient(145deg, #fff9f2 0%, #fffefb 42%, #ffffff 100%);
  border-color: #fde8d4;
}

.swan-job-card--compact {
  padding: 16px 14px 14px;
}

.swan-job-card--compact .swan-job-card__title {
  margin-bottom: 10px;
  font-size: 0.98rem;
}

.swan-job-card--compact .swan-job-card__meta-row {
  gap: 8px 10px;
  margin-bottom: 14px;
}

.swan-job-card--compact .swan-job-card__badge {
  font-size: 0.6875rem;
  padding: 4px 8px;
}

.swan-job-card--compact .swan-job-card__salary {
  font-size: 0.75rem;
}

.swan-job-card--compact .swan-job-card__tags {
  margin-bottom: 12px;
  gap: 5px 6px;
}

.swan-job-card--compact .swan-job-card__tag {
  font-size: 0.6875rem;
  padding: 3px 7px;
}

.swan-job-card--compact .swan-job-card__company {
  gap: 10px;
}

.swan-job-card--compact .swan-job-card__logo {
  width: 38px;
  height: 38px;
  font-size: 1rem;
}

.swan-job-card--compact .swan-job-card__company-name {
  font-size: 0.875rem;
}

.swan-job-card--compact .swan-job-card__location {
  font-size: 0.75rem;
}

.swan-job-card--compact .swan-job-card__bookmark {
  width: 36px;
  height: 36px;
}

.swan-job-card--compact .swan-job-card__bookmark svg {
  width: 19px;
  height: 19px;
}

.swan-job-card--compact .swan-job-card__pin-badge {
  width: 24px;
  height: 24px;
}

.swan-job-card__title {
  margin: 0 0 12px;
  font-size: 1.0625rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
  line-height: 1.35;
  padding-right: 8px;
}

.swan-job-card__meta-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px 14px;
  margin-bottom: 18px;
}

.swan-job-card__badge {
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.02em;
  color: #15803d;
  background: #dcfce7;
  padding: 5px 10px;
  border-radius: var(--jb-radius-sm);
  line-height: 1;
}

.swan-job-card__salary {
  font-size: 0.8125rem;
  color: var(--jb-text-muted);
}

.swan-job-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px 8px;
  list-style: none;
  margin: 0 0 16px;
  padding: 0;
}

.swan-job-card__tag {
  font-size: 0.75rem;
  font-weight: 500;
  padding: 4px 8px;
  border-radius: var(--jb-radius-sm);
  color: #1e40af;
  background: var(--el-color-primary-light-9, #e8f1fc);
  border: 1px solid var(--el-color-primary-light-7, #9fc2ea);
  line-height: 1.3;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.swan-job-card__company {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-top: 4px;
  border-top: 1px solid var(--jb-border);
  margin: 0 -4px 0 0;
}

.swan-job-card__logo {
  width: 44px;
  height: 44px;
  border-radius: var(--jb-radius-sm);
  background: linear-gradient(145deg, #f1f5f9, #e2e8f0);
  color: #475569;
  font-weight: 800;
  font-size: 1.125rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border: 1px solid var(--jb-border);
}

.swan-job-card__company-text {
  flex: 1;
  min-width: 0;
}

.swan-job-card__company-name {
  margin: 0 0 4px;
  font-size: 0.9375rem;
  font-weight: 600;
  color: var(--jb-text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.swan-job-card__location {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.8125rem;
  color: var(--jb-text-muted);
}

.swan-job-card__pin-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: var(--jb-radius-sm);
  flex-shrink: 0;
  transition: background 0.2s ease;
}

.swan-job-card:hover .swan-job-card__pin-badge {
  background: var(--jb-primary);
}

.swan-job-card__pin {
  width: 14px;
  height: 14px;
  flex-shrink: 0;
  opacity: 0.65;
  background: currentColor;
  mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Cpath d='M12 21s7-4.35 7-10a7 7 0 1 0-14 0c0 5.65 7 10 7 10z'/%3E%3Ccircle cx='12' cy='11' r='2.5'/%3E%3C/svg%3E")
    center / contain no-repeat;
  transition: background 0.2s ease, opacity 0.2s ease;
}

.swan-job-card:hover .swan-job-card__pin {
  background: #fff;
  opacity: 1;
}

.swan-job-card__bookmark {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  margin: 0;
  padding: 0;
  border: none;
  border-radius: var(--jb-radius-sm);
  background: transparent;
  color: #94a3b8;
  cursor: pointer;
  transition: color 0.15s ease, background 0.15s ease;
}

.swan-job-card:hover .swan-job-card__bookmark {
  color: #fff;
  background: var(--jb-primary);
}

.swan-job-card:hover .swan-job-card__bookmark svg path {
  stroke: #fff;
}

.swan-job-card__bookmark:hover {
  color: var(--jb-primary);
  background: rgba(10, 101, 204, 0.08);
}

.swan-job-card:hover .swan-job-card__bookmark:hover {
  color: #fff;
  background: var(--jb-primary-hover);
}

.swan-job-card__bookmark--on {
  color: var(--jb-primary);
}

.swan-job-card__bookmark--on svg path {
  opacity: 0.92;
}

.swan-job-card__batch {
  margin-top: 14px;
  padding-top: 12px;
  border-top: 1px dashed var(--jb-border);
}
</style>
