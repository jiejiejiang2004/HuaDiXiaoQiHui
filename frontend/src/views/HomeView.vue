<template>
  <div class="home-page">
    <SwanHeroSection
      :filters="filters"
      :platform-brief="platformBrief"
      :stats-loading="statsLoading"
      @update:filters="(p) => Object.assign(filters, p)"
      @search="goJobsSearch"
    />

    <SwanJobListSection
      heading="最新职位"
      :jobs="jobs"
      :loading="loading"
      :total="0"
      :is-candidate="isCandidate"
      :selected-job-ids="[]"
      layout="home-three-rows"
      view-all-to="/jobs"
      :show-summary="false"
      section-id="recent-jobs"
      @open-detail="openDetail"
      @toggle-collect="toggleCollect"
    />

    <el-empty v-if="!loading && !jobs.length" description="暂无职位展示" />

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
import { useRouter } from "vue-router";
import SwanHeroSection from "@/components/swan/SwanHeroSection.vue";
import SwanJobListSection from "@/components/swan/SwanJobListSection.vue";
import type { SwanJobSummary } from "@/components/swan/types";
import {
  applyJob,
  collectJob,
  getJobDetail,
  getPlatformPublicBrief,
  listResumes,
  searchJobs,
  uncollectJob,
  type PlatformPublicBrief,
} from "@/api/recruit";
import { getUserType } from "@/utils/auth";

const router = useRouter();

const loading = ref(false);
const platformBrief = ref<PlatformPublicBrief | null>(null);
const statsLoading = ref(true);
const jobs = ref<SwanJobSummary[]>([]);
const detailVisible = ref(false);
const jobDetail = ref<SwanJobSummary | null>(null);
const isCandidate = computed(() => getUserType() === "CANDIDATE");
const filters = reactive({
  keyword: "",
  education: "",
  experience: "",
  location: "",
});

function goJobsSearch() {
  const q: Record<string, string> = {};
  if (filters.keyword.trim()) {
    q.kw = filters.keyword.trim();
  }
  if (filters.location.trim()) {
    q.city = filters.location.trim();
  }
  router.push({
    path: "/jobs",
    query: Object.keys(q).length ? q : {},
  });
}

async function loadPlatformBrief() {
  statsLoading.value = true;
  try {
    platformBrief.value = await getPlatformPublicBrief();
  } catch {
    platformBrief.value = null;
  } finally {
    statsLoading.value = false;
  }
}

async function loadRecentJobs() {
  loading.value = true;
  try {
    const data = await searchJobs({
      keyword: "",
      education: "",
      experience: "",
      location: "",
      pageNum: 1,
      pageSize: 12,
      sortBy: "recent",
    });
    jobs.value = data.list || [];
  } finally {
    loading.value = false;
  }
}

async function openDetail(jobId: number) {
  jobDetail.value = await getJobDetail(jobId);
  detailVisible.value = true;
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

function goLogin(tab: "candidate" | "enterprise") {
  router.push({
    path: "/login",
    query: { tab },
  });
}

onMounted(() => {
  loadPlatformBrief();
  loadRecentJobs();
});
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 56px;
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
