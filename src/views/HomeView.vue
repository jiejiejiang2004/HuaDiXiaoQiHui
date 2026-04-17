<template>
  <div class="home-page">
    <section class="hero">
      <div>
        <p class="hero-tag">校企慧招聘平台 MVP</p>
        <h1>连接求职者、企业与校园招聘场景</h1>
        <p class="hero-desc">
          已接入真实 Spring Boot
          接口，当前支持职位搜索、个人登录注册、简历维护、职位投递、企业职位管理与简历处理。
        </p>
      </div>
      <div class="hero-actions">
        <el-button type="primary" size="large" @click="goLogin('candidate')"
          >求职者登录</el-button
        >
        <el-button size="large" @click="goLogin('enterprise')"
          >企业登录</el-button
        >
      </div>
    </section>

    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="filters" @submit.prevent>
        <el-form-item label="关键词">
          <el-input
            v-model="filters.keyword"
            placeholder="职位 / 企业 / 分类"
            clearable
          />
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="filters.education" placeholder="不限" clearable>
            <el-option label="大专" value="大专" />
            <el-option label="本科" value="本科" />
            <el-option label="硕士" value="硕士" />
          </el-select>
        </el-form-item>
        <el-form-item label="经验">
          <el-select v-model="filters.experience" placeholder="不限" clearable>
            <el-option label="应届" value="应届" />
            <el-option label="1-3年" value="1-3年" />
            <el-option label="3-5年" value="3-5年" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点">
          <el-input
            v-model="filters.location"
            placeholder="如：成都"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadJobs">搜索职位</el-button>
        </el-form-item>
        <el-form-item v-if="isCandidate">
          <el-button
            type="success"
            :disabled="selectedJobIds.length === 0"
            @click="batchApplySelectedJobs"
          >
            批量投递已选职位
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="job-grid" v-loading="loading">
      <el-card
        v-for="job in jobs"
        :key="job.jobId"
        class="job-card"
        shadow="hover"
      >
        <div class="job-head">
          <div>
            <h3>{{ job.jobName }}</h3>
            <p>{{ job.companyName }}</p>
          </div>
          <span class="salary">{{ job.salaryMin }} - {{ job.salaryMax }}</span>
        </div>
        <div class="job-meta">
          <span>{{ job.location }}</span>
          <span>{{ job.education }}</span>
          <span>{{ job.experience }}</span>
        </div>
        <div class="job-tags">
          <el-tag v-for="tag in job.welfare || []" :key="tag" effect="plain">{{
            tag
          }}</el-tag>
        </div>
        <div class="job-actions">
          <el-checkbox
            v-if="isCandidate"
            :model-value="selectedJobIds.includes(job.jobId)"
            @change="toggleSelectedJob(job.jobId, $event)"
          >
            选择
          </el-checkbox>
          <el-button
            v-if="isCandidate"
            text
            :type="job.collected ? 'warning' : 'default'"
            @click="toggleCollect(job)"
          >
            {{ job.collected ? "取消收藏" : "收藏职位" }}
          </el-button>
          <el-button text type="primary" @click="openDetail(job.jobId)"
            >查看详情</el-button
          >
        </div>
      </el-card>
    </div>

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
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
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

interface JobSummary {
  jobId: number;
  jobName: string;
  companyName: string;
  salaryMin: number;
  salaryMax: number;
  location: string;
  education: string;
  experience: string;
  welfare?: string[];
  responsibility?: string;
  requirement?: string;
  collected?: boolean;
}

const router = useRouter();
const loading = ref(false);
const jobs = ref<JobSummary[]>([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = 10;
const detailVisible = ref(false);
const jobDetail = ref<JobSummary | null>(null);
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

async function toggleCollect(job: JobSummary) {
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

onMounted(loadJobs);
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 20px 48px;
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 24px;
  padding: 32px;
  border-radius: 20px;
  background: linear-gradient(135deg, #1d4ed8, #0f766e);
  color: #fff;
}

.hero h1 {
  margin: 8px 0 12px;
  font-size: 36px;
}

.hero-tag {
  margin: 0;
  font-weight: 600;
  opacity: 0.9;
}

.hero-desc {
  max-width: 720px;
  line-height: 1.8;
  opacity: 0.95;
}

.hero-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.search-card {
  margin-bottom: 24px;
}

.job-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 16px;
}

.job-card {
  border-radius: 16px;
}

.job-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.job-head h3 {
  margin: 0 0 8px;
}

.job-head p {
  margin: 0;
  color: #6b7280;
}

.salary {
  color: #dc2626;
  font-weight: 700;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin: 16px 0;
  color: #4b5563;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  min-height: 32px;
}

.job-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.pager {
  display: flex;
  justify-content: center;
  margin-top: 24px;
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
  color: #374151;
}
</style>
