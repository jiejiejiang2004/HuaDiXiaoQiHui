<template>
  <div class="jobs-page">
    <div class="jobs-page__inner">
      <div class="jobs-page__search-bar">
        <div class="jobs-page__field jobs-page__field--kw">
          <span
            class="jobs-page__field-ico jobs-page__field-ico--search"
            aria-hidden="true"
          />
          <el-input
            v-model="filters.keyword"
            class="jobs-page__input"
            placeholder="职位、关键词、公司名…"
            clearable
            @keyup.enter="runSearch"
          />
        </div>
        <div class="jobs-page__field jobs-page__field--edu">
          <span
            class="jobs-page__field-ico jobs-page__field-ico--edu"
            aria-hidden="true"
          />
          <el-select v-model="filters.education" placeholder="学历" clearable>
            <el-option label="大专" value="大专" />
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
          </el-select>
        </div>
        <div class="jobs-page__field jobs-page__field--exp">
          <span
            class="jobs-page__field-ico jobs-page__field-ico--exp"
            aria-hidden="true"
          />
          <el-select v-model="filters.experience" placeholder="经验" clearable>
            <el-option label="应届" value="应届" />
            <el-option label="1-3年" value="1-3年" />
            <el-option label="3-5年" value="3-5年" />
          </el-select>
        </div>
        <div class="jobs-page__field jobs-page__field--loc">
          <span
            class="jobs-page__field-ico jobs-page__field-ico--pin"
            aria-hidden="true"
          />
          <el-input
            ref="locationInputRef"
            v-model="filters.location"
            class="jobs-page__input jobs-page__input--loc"
            placeholder="城市、区县"
            clearable
            @keyup.enter="runSearch"
          />
          <button
            type="button"
            class="jobs-page__loc-hit"
            title="填写城市"
            aria-label="聚焦城市输入"
            @click="focusLocationInput"
          >
            <span class="jobs-page__field-ico jobs-page__field-ico--target" />
          </button>
          <el-button
            type="primary"
            class="jobs-page__submit"
            @click="runSearch"
          >
            搜索
          </el-button>
        </div>
      </div>

      <div v-if="isCandidate" class="jobs-page__batch-row">
        <el-button
          class="jobs-page__batch"
          :disabled="selectedJobIds.length === 0"
          @click="batchApplySelectedJobs"
        >
          批量投递
        </el-button>
      </div>

      <SwanJobListSection
        :jobs="jobs"
        :loading="loading"
        :total="total"
        :is-candidate="isCandidate"
        :selected-job-ids="selectedJobIds"
        layout="list"
        grid-preset="three"
        :show-head="false"
        :show-summary="false"
        :selectable="true"
        section-id="job-list"
        @open-detail="openDetail"
        @toggle-collect="toggleCollect"
        @toggle-select="toggleSelectedJob"
      />

      <el-empty v-if="!loading && !jobs.length" description="暂无匹配职位" />

      <el-pagination
        v-if="total > pageSize"
        class="jobs-page__pager"
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="handlePageChange"
      />

      <el-dialog v-model="detailVisible" title="职位详情" width="720px">
        <template v-if="jobDetail">
          <div class="detail-section">
            <h2>{{ jobDetail.jobName }}</h2>
            <p>{{ jobDetail.companyName }} | {{ jobDetail.location }}</p>
            <p>
              {{ jobDetail.salaryMin }} - {{ jobDetail.salaryMax }} /
              {{ jobDetail.education }} / {{ jobDetail.experience }}
            </p>
          </div>
          <div class="detail-section">
            <h4>岗位职责</h4>
            <p>{{ jobDetail.responsibility }}</p>
          </div>
          <div class="detail-section">
            <h4>任职要求</h4>
            <p>{{ jobDetail.requirement }}</p>
          </div>
        </template>
        <template #footer>
          <el-button @click="detailVisible = false">关闭</el-button>
          <el-button
            v-if="!isCandidate"
            type="primary"
            @click="goLogin('candidate')"
            >登录后投递</el-button
          >
          <el-button v-else type="primary" @click="applyCurrentJob"
            >立即投递</el-button
          >
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import type { InputInstance } from "element-plus";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import SwanJobListSection from "@/components/swan/SwanJobListSection.vue";
import type { SwanJobSummary } from "@/components/swan/types";
import {
  applyJob,
  batchApplyJobs,
  collectJob,
  getJobDetail,
  listResumes,
  searchJobs,
  uncollectJob,
} from "@/api/recruit";
import { getUserType } from "@/utils/auth";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const jobs = ref<SwanJobSummary[]>([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = 15;
const detailVisible = ref(false);
const jobDetail = ref<SwanJobSummary | null>(null);
const selectedJobIds = ref<number[]>([]);
const locationInputRef = ref<InputInstance | null>(null);
const isCandidate = computed(() => getUserType() === "CANDIDATE");
const filters = reactive({
  keyword: "",
  education: "",
  experience: "",
  location: "",
});

function focusLocationInput() {
  locationInputRef.value?.focus?.();
}

function syncFiltersFromRoute() {
  if (route.path !== "/jobs") {
    return;
  }
  const kw = route.query.kw;
  const city = route.query.city;
  if (typeof kw === "string") {
    filters.keyword = kw;
  }
  if (typeof city === "string") {
    filters.location = city;
  }
}

function runSearch() {
  pageNum.value = 1;
  const q: Record<string, string> = {};
  if (filters.keyword.trim()) {
    q.kw = filters.keyword.trim();
  }
  if (filters.location.trim()) {
    q.city = filters.location.trim();
  }
  router.replace({
    path: "/jobs",
    query: Object.keys(q).length ? q : {},
  });
  loadJobs();
}

async function loadJobs() {
  loading.value = true;
  try {
    const data = await searchJobs({
      ...filters,
      pageNum: pageNum.value,
      pageSize,
    });
    jobs.value = data.list || [];
    total.value = data.total || 0;
  } finally {
    loading.value = false;
  }
}

watch(
  () => [filters.education, filters.experience],
  () => {
    if (route.path !== "/jobs") {
      return;
    }
    pageNum.value = 1;
    loadJobs();
  }
);

async function openDetail(jobId: number) {
  jobDetail.value = await getJobDetail(jobId);
  detailVisible.value = true;
}

function toggleSelectedJob(jobId: number, checked: string | number | boolean) {
  if (!checked) {
    selectedJobIds.value = selectedJobIds.value.filter((id) => id !== jobId);
    return;
  }
  if (!selectedJobIds.value.includes(jobId)) {
    selectedJobIds.value.push(jobId);
  }
}

async function getDefaultResumeId() {
  const data = await listResumes();
  const defaultResume =
    (data.list || []).find((item: Record<string, unknown>) => item.isDefault) ||
    data.list?.[0];
  if (!defaultResume) {
    throw new Error("请先在个人中心创建简历");
  }
  return Number(defaultResume.resumeId);
}

async function applyCurrentJob() {
  if (!jobDetail.value) {
    return;
  }
  const resumeId = await getDefaultResumeId();
  await applyJob({ jobId: jobDetail.value.jobId, resumeId });
  ElMessage.success("职位投递成功");
  await openDetail(jobDetail.value.jobId);
}

async function batchApplySelectedJobs() {
  if (!selectedJobIds.value.length) {
    ElMessage.warning("请先选择职位");
    return;
  }
  const resumeId = await getDefaultResumeId();
  const data = await batchApplyJobs({
    jobIds: selectedJobIds.value,
    resumeId,
  });
  ElMessage.success(`批量投递完成，成功 ${data.successCount} 个职位`);
  selectedJobIds.value = [];
  await loadJobs();
}

async function toggleCollect(job: SwanJobSummary) {
  if (!isCandidate.value) {
    goLogin("candidate");
    return;
  }
  if (job.collected) {
    await uncollectJob(job.jobId);
    job.collected = false;
    ElMessage.success("已取消收藏");
  } else {
    await collectJob(job.jobId);
    job.collected = true;
    ElMessage.success("已收藏职位");
  }
  if (jobDetail.value?.jobId === job.jobId) {
    jobDetail.value.collected = job.collected;
  }
}

function handlePageChange(page: number) {
  pageNum.value = page;
  loadJobs();
}

function goLogin(tab: "candidate" | "enterprise") {
  router.push({
    path: "/login",
    query: { tab },
  });
}

onMounted(() => {
  syncFiltersFromRoute();
  loadJobs();
});
</script>

<style scoped>
.jobs-page {
  flex: 1;
  width: 100%;
  min-height: 100%;
  background: #fff;
}

.jobs-page__inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 28px clamp(16px, 3vw, 28px) 56px;
}

.jobs-page__search-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: stretch;
  gap: 0;
  margin-bottom: 14px;
  background: #fff;
  border-radius: 6px;
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 6px 24px rgba(15, 23, 42, 0.06);
  overflow: hidden;
}

.jobs-page__search-bar > .jobs-page__field {
  flex: 1 1 0;
  min-width: 0;
  border-right: 1px solid rgba(15, 23, 42, 0.08);
}

.jobs-page__field {
  display: flex;
  align-items: stretch;
  gap: 10px;
  min-height: 46px;
  padding: 0 12px;
}

.jobs-page__field--loc {
  border-right: none;
}

.jobs-page__field--edu,
.jobs-page__field--exp {
  align-items: center;
}

.jobs-page__field--edu :deep(.el-select),
.jobs-page__field--exp :deep(.el-select) {
  flex: 1 1 0;
  min-width: 0;
}

.jobs-page__field--edu :deep(.el-select__wrapper),
.jobs-page__field--exp :deep(.el-select__wrapper) {
  box-shadow: none !important;
  background: transparent;
  min-height: 38px;
  padding-left: 0;
}

.jobs-page__field--edu :deep(.el-select__placeholder),
.jobs-page__field--exp :deep(.el-select__placeholder) {
  font-size: 0.9375rem;
}

.jobs-page__field--edu :deep(.el-select__selected-item),
.jobs-page__field--exp :deep(.el-select__selected-item) {
  font-size: 0.9375rem;
}

.jobs-page__field :deep(.el-input) {
  display: flex;
  align-items: center;
}

.jobs-page__field--kw :deep(.el-input) {
  flex: 1 1 auto;
  min-width: 0;
}

.jobs-page__field :deep(.el-input__wrapper) {
  box-shadow: none !important;
  background: transparent;
  padding-left: 0;
  min-height: 38px;
}

.jobs-page__field :deep(.el-input__inner) {
  font-size: 0.9375rem;
}

.jobs-page__input--loc {
  flex: 1 1 auto;
  min-width: 0;
}

.jobs-page__field-ico {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  align-self: center;
  background: var(--jb-primary);
  opacity: 0.88;
  mask-size: contain;
  mask-repeat: no-repeat;
  mask-position: center;
}

.jobs-page__field-ico--search {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Ccircle cx='11' cy='11' r='7'/%3E%3Cpath d='M20 20l-3-3'/%3E%3C/svg%3E");
}

.jobs-page__field-ico--pin {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Cpath d='M12 21s7-4.35 7-10a7 7 0 1 0-14 0c0 5.65 7 10 7 10z'/%3E%3Ccircle cx='12' cy='11' r='2.5'/%3E%3C/svg%3E");
}

.jobs-page__field-ico--edu {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2' stroke-linejoin='round'%3E%3Cpath d='M2 10l10-5 10 5-10 4-10-4z'/%3E%3Cpath d='M6 12v4c2 1.5 10 1.5 12 0v-4'/%3E%3C/svg%3E");
}

.jobs-page__field-ico--exp {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Crect x='2' y='7' width='20' height='14' rx='2'/%3E%3Cpath d='M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2'/%3E%3C/svg%3E");
}

.jobs-page__field-ico--target {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Ccircle cx='12' cy='12' r='3'/%3E%3Cpath d='M12 2v3M12 19v3M2 12h3M19 12h3'/%3E%3C/svg%3E");
  opacity: 0.55;
  background: var(--jb-text-muted);
}

.jobs-page__loc-hit {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  align-self: center;
  margin: 0;
  padding: 0;
  border: none;
  border-radius: 6px;
  background: transparent;
  cursor: pointer;
  color: inherit;
  transition: background 0.15s ease;
}

.jobs-page__loc-hit:hover {
  background: rgba(10, 101, 204, 0.08);
}

.jobs-page__loc-hit:hover .jobs-page__field-ico--target {
  background: var(--jb-primary);
  opacity: 0.95;
}

.jobs-page__field--loc :deep(.jobs-page__submit) {
  flex-shrink: 0;
  align-self: stretch;
  height: auto !important;
  min-height: 0;
  margin: 4px 4px 4px 6px;
  padding: 0 18px !important;
  border-radius: 5px !important;
  font-weight: 700;
  letter-spacing: 0.02em;
  font-size: 0.875rem;
}

.jobs-page__field--loc :deep(.jobs-page__submit span) {
  line-height: 1;
}

.jobs-page__batch-row {
  margin-bottom: 22px;
}

.jobs-page__batch {
  height: 32px !important;
  border-radius: 6px !important;
  padding: 0 14px !important;
}

.jobs-page__pager {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.detail-section {
  margin-bottom: 16px;
}

.detail-section h2,
.detail-section h4 {
  margin: 0 0 8px;
}

.detail-section p {
  margin: 0;
  line-height: 1.8;
  color: #475569;
}

@media (max-width: 900px) {
  .jobs-page__search-bar {
    flex-direction: column;
  }

  .jobs-page__search-bar > .jobs-page__field {
    flex: 1 1 auto;
    width: 100%;
    border-right: none;
    border-bottom: 1px solid rgba(15, 23, 42, 0.08);
  }

  .jobs-page__field--loc {
    border-bottom: none;
  }

  .jobs-page__field--loc :deep(.jobs-page__submit) {
    margin: 4px 4px 4px 4px;
  }

  .jobs-page__batch {
    width: 100%;
  }
}
</style>
