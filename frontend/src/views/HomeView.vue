<template>
  <div class="home-page">
    <SwanHeroSection
      :filters="filters"
      :is-candidate="isCandidate"
      :batch-disabled="selectedJobIds.length === 0"
      @update:filters="(p) => Object.assign(filters, p)"
      @search="loadJobs"
      @batch-apply="batchApplySelectedJobs"
      @go-candidate="goLogin('candidate')"
      @go-enterprise="goLogin('enterprise')"
    />

    <SwanJobListSection
      :jobs="jobs"
      :loading="loading"
      :total="total"
      :is-candidate="isCandidate"
      :selected-job-ids="selectedJobIds"
      section-id="job-list"
      @open-detail="openDetail"
      @toggle-collect="toggleCollect"
      @toggle-select="toggleSelectedJob"
    />

    <el-empty v-if="!loading && !jobs.length" description="暂无匹配职位" />

    <el-pagination
      v-if="total > pageSize"
      class="pager"
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
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import SwanHeroSection from "@/components/swan/SwanHeroSection.vue";
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

function scrollToJobListIfNeeded() {
  if (route.hash !== "#job-list") {
    return;
  }
  nextTick(() => {
    document
      .getElementById("job-list")
      ?.scrollIntoView({ behavior: "smooth", block: "start" });
  });
}

watch(() => route.hash, scrollToJobListIfNeeded);

const loading = ref(false);
const jobs = ref<SwanJobSummary[]>([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = 10;
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
    query: {
      tab,
    },
  });
}

onMounted(() => {
  loadJobs();
  scrollToJobListIfNeeded();
});
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 56px;
}

.pager {
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
