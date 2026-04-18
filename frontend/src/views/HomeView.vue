<template>
  <div class="home-page">
    <SwanHeroSection
      :filters="filters"
      :platform-brief="platformBrief"
      :stats-loading="statsLoading"
      @update:filters="(p) => Object.assign(filters, p)"
      @search="goJobsSearch"
    />

    <SwanHowItWorks />

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
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import SwanHeroSection from "@/components/swan/SwanHeroSection.vue";
import SwanHowItWorks from "@/components/swan/SwanHowItWorks.vue";
import SwanJobListSection from "@/components/swan/SwanJobListSection.vue";
import type { SwanJobSummary } from "@/components/swan/types";
import {
  collectJob,
  getPlatformPublicBrief,
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

function openDetail(jobId: number) {
  router.push({ name: "job-detail", params: { jobId: String(jobId) } });
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
</style>
