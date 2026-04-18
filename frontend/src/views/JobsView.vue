<template>
  <div class="jobs-page">
    <section class="jobs-page__toolbar">
      <h1 class="jobs-page__title">求职</h1>
      <p class="jobs-page__subtitle">搜索与筛选岗位，支持批量投递</p>
      <el-form
        :inline="true"
        :model="filters"
        class="jobs-page__form"
        @submit.prevent
      >
        <el-form-item label="关键词">
          <el-input
            v-model="filters.keyword"
            placeholder="职位、公司"
            clearable
            style="width: 180px"
            @keyup.enter="runSearch"
          />
        </el-form-item>
        <el-form-item label="城市">
          <el-input
            v-model="filters.location"
            placeholder="如：成都"
            clearable
            style="width: 140px"
            @keyup.enter="runSearch"
          />
        </el-form-item>
        <el-form-item label="学历">
          <el-select
            v-model="filters.education"
            placeholder="不限"
            clearable
            style="width: 120px"
          >
            <el-option label="大专" value="大专" />
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
          </el-select>
        </el-form-item>
        <el-form-item label="经验">
          <el-select
            v-model="filters.experience"
            placeholder="不限"
            clearable
            style="width: 120px"
          >
            <el-option label="应届" value="应届" />
            <el-option label="1-3年" value="1-3年" />
            <el-option label="3-5年" value="3-5年" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="runSearch">搜索</el-button>
        </el-form-item>
        <el-form-item v-if="isCandidate">
          <el-button
            type="success"
            :disabled="selectedJobIds.length === 0"
            @click="batchApplySelectedJobs"
          >
            批量投递
          </el-button>
        </el-form-item>
      </el-form>
    </section>

    <SwanJobListSection
      heading="职位列表"
      :jobs="jobs"
      :loading="loading"
      :total="total"
      :is-candidate="isCandidate"
      :selected-job-ids="selectedJobIds"
      layout="list"
      :show-summary="true"
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
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
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
const isCandidate = computed(() => getUserType() === "CANDIDATE");
const filters = reactive({
  keyword: "",
  education: "",
  experience: "",
  location: "",
});

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
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 20px 56px;
}

.jobs-page__toolbar {
  margin-bottom: 28px;
}

.jobs-page__title {
  margin: 0 0 8px;
  font-size: 1.65rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
}

.jobs-page__subtitle {
  margin: 0 0 18px;
  font-size: 0.9375rem;
  color: var(--jb-text-muted);
}

.jobs-page__form {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 4px 8px;
  padding: 18px 20px;
  background: var(--jb-bg-elevated);
  border: 1px solid var(--jb-border);
  border-radius: var(--jb-radius-md);
  box-shadow: var(--jb-shadow-sm);
}

.jobs-page__form :deep(.el-form-item) {
  margin-bottom: 0;
}

.jobs-page__form :deep(.el-form-item__label) {
  color: var(--jb-text-muted);
  font-weight: 500;
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
</style>
