<template>
  <div class="job-detail-page" v-loading="loading">
    <div v-if="job" class="job-detail-page__inner">
      <router-link class="job-detail-page__back" to="/jobs">
        ← 返回职位列表
      </router-link>

      <header class="job-detail-page__hero">
        <div class="job-detail-page__hero-main">
          <div class="job-detail-page__logo" aria-hidden="true">
            {{ companyInitial(job.companyName) }}
          </div>
          <div class="job-detail-page__hero-text">
            <h1 class="job-detail-page__title">{{ job.jobName }}</h1>
            <p class="job-detail-page__company">
              <span class="job-detail-page__company-at">就职于</span>
              {{ job.companyName }}
            </p>
            <div class="job-detail-page__tags">
              <span class="job-detail-page__pill job-detail-page__pill--type">
                {{ jobTypeLabel }}
              </span>
              <span
                v-if="job.featured"
                class="job-detail-page__pill job-detail-page__pill--feat"
              >
                精选
              </span>
            </div>
          </div>
        </div>
        <div class="job-detail-page__hero-actions">
          <button
            type="button"
            class="job-detail-page__icon-btn"
            :class="{ 'job-detail-page__icon-btn--on': job.collected }"
            :title="job.collected ? '已收藏' : '收藏'"
            aria-label="收藏职位"
            @click="toggleCollect"
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
          <button
            type="button"
            class="job-detail-page__apply"
            @click="onApplyClick"
          >
            立即投递
            <span class="job-detail-page__apply-arrow" aria-hidden="true"
              >→</span
            >
          </button>
        </div>
      </header>

      <div class="job-detail-page__layout">
        <main class="job-detail-page__main">
          <section class="job-detail-page__section">
            <h2 class="job-detail-page__h2">职位描述</h2>
            <div
              v-for="(p, i) in descriptionParagraphs"
              :key="i"
              class="job-detail-page__para"
            >
              {{ p }}
            </div>
          </section>
          <section
            v-if="requirementItems.length"
            class="job-detail-page__section"
          >
            <h2 class="job-detail-page__h2">任职要求</h2>
            <ul class="job-detail-page__list">
              <li v-for="(item, i) in requirementItems" :key="i">
                {{ item }}
              </li>
            </ul>
          </section>
          <section
            v-if="desirableItems.length"
            class="job-detail-page__section"
          >
            <h2 class="job-detail-page__h2">加分项</h2>
            <ul class="job-detail-page__list">
              <li v-for="(item, i) in desirableItems" :key="i">
                {{ item }}
              </li>
            </ul>
          </section>
          <section v-if="benefitItems.length" class="job-detail-page__section">
            <h2 class="job-detail-page__h2">福利待遇</h2>
            <ul class="job-detail-page__list">
              <li v-for="(item, i) in benefitItems" :key="i">
                {{ item }}
              </li>
            </ul>
          </section>
        </main>

        <aside class="job-detail-page__aside">
          <div class="job-detail-page__card">
            <h3 class="job-detail-page__card-title">薪资范围（税前）</h3>
            <p class="job-detail-page__salary">{{ salaryRange }}</p>
            <h3
              class="job-detail-page__card-title job-detail-page__card-title--sp"
            >
              工作地点
            </h3>
            <p class="job-detail-page__loc">
              <span class="job-detail-page__pin" aria-hidden="true" />
              {{ job.location }}
            </p>
          </div>

          <div class="job-detail-page__card">
            <h3 class="job-detail-page__card-title">职位概览</h3>
            <div class="job-detail-page__overview">
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">发布于</span>
                <span class="job-detail-page__ov-value">{{
                  job.jobPostedAt || "—"
                }}</span>
              </div>
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">招聘截止</span>
                <span class="job-detail-page__ov-value">{{
                  job.jobExpireAt || "—"
                }}</span>
              </div>
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">职级</span>
                <span class="job-detail-page__ov-value">{{
                  job.jobLevel || job.experience || "—"
                }}</span>
              </div>
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">经验要求</span>
                <span class="job-detail-page__ov-value">{{
                  job.experience || "—"
                }}</span>
              </div>
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">学历</span>
                <span class="job-detail-page__ov-value">{{
                  job.education || "—"
                }}</span>
              </div>
              <div class="job-detail-page__ov-item">
                <span class="job-detail-page__ov-label">工作类型</span>
                <span class="job-detail-page__ov-value">{{
                  job.jobType || "全职"
                }}</span>
              </div>
            </div>
          </div>

          <div class="job-detail-page__card">
            <h3 class="job-detail-page__card-title">分享职位</h3>
            <div class="job-detail-page__share-row" aria-label="分享职位">
              <button
                type="button"
                class="job-detail-page__copy"
                @click="copyShareLink"
              >
                <span class="job-detail-page__copy-ico" aria-hidden="true" />
                复制链接
              </button>
              <button
                type="button"
                class="job-detail-page__share-brand"
                title="微信"
                aria-label="分享到微信"
                @click="shareWeChatHint"
              >
                <svg
                  class="job-detail-page__share-svg"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="#07C160"
                    d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 01.213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 00.167-.054l1.903-1.114a.864.864 0 01.717-.098 10.16 10.16 0 002.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348zM5.785 5.991c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178A1.17 1.17 0 014.623 7.17c0-.651.52-1.18 1.162-1.18zm5.813 0c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178 1.17 1.17 0 01-1.162-1.178c0-.651.52-1.18 1.162-1.18zm5.34 2.867c-1.797-.052-3.746.512-5.28 1.786-1.72 1.428-2.687 3.72-1.78 6.22.942 2.453 3.666 4.229 6.884 4.229.826 0 1.622-.12 2.361-.336a.722.722 0 01.598.082l1.584.926a.272.272 0 00.14.047c.134 0 .24-.111.24-.247 0-.06-.023-.12-.038-.177l-.327-1.233a.582.582 0 01-.023-.156.49.49 0 01.201-.398C23.024 18.48 24 16.82 24 14.98c0-3.21-2.731-5.837-6.061-6.122zm-2.074 2.984c.535 0 .969.44.969.983a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.543.434-.983.97-.983zm4.844 0c.535 0 .969.44.969.983a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.543.434-.983.969-.983z"
                  />
                </svg>
              </button>
              <button
                type="button"
                class="job-detail-page__share-brand"
                title="微博"
                aria-label="分享到微博"
                @click="openWeiboShare"
              >
                <svg
                  class="job-detail-page__share-svg"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="#E6162D"
                    d="M10.098 20.323c-3.977.391-7.414-1.406-7.672-4.02-.259-2.609 2.759-5.047 6.74-5.441 3.979-.394 7.413 1.404 7.671 4.018.259 2.6-2.759 5.049-6.737 5.439l-.002.004zM9.05 17.219c-.384.616-1.208.884-1.829.602-.612-.279-.793-.991-.406-1.593.379-.595 1.176-.861 1.793-.601.622.263.82.972.442 1.592zm1.27-1.627c-.141.237-.449.353-.689.253-.236-.09-.313-.361-.177-.586.138-.227.436-.346.672-.24.239.09.315.36.18.601l.014-.028zm.176-2.719c-1.893-.493-4.033.45-4.857 2.118-.836 1.704-.026 3.591 1.886 4.21 1.983.64 4.318-.341 5.132-2.179.8-1.793-.201-3.642-2.161-4.149zm7.563-1.224c-.346-.105-.57-.18-.405-.615.375-.977.42-1.804 0-2.404-.781-1.112-2.915-1.053-5.364-.03 0 0-.766.331-.571-.271.376-1.217.315-2.224-.27-2.809-1.338-1.337-4.869.045-7.888 3.08C1.309 10.87 0 13.273 0 15.348c0 3.981 5.099 6.395 10.086 6.395 6.536 0 10.888-3.801 10.888-6.82 0-1.822-1.547-2.854-2.915-3.284v.01zm1.908-5.092c-.766-.856-1.908-1.187-2.96-.962-.436.09-.706.511-.616.932.09.42.511.691.932.602.511-.105 1.067.044 1.442.465.376.421.466.977.316 1.473-.136.406.089.856.51.992.405.119.857-.105.992-.512.33-1.021.12-2.178-.646-3.035l.03.045zm2.418-2.195c-1.576-1.757-3.905-2.419-6.054-1.968-.496.104-.812.587-.706 1.081.104.496.586.813 1.082.707 1.532-.331 3.185.15 4.296 1.383 1.112 1.246 1.429 2.943.947 4.416-.165.48.106 1.007.586 1.157.479.165.991-.104 1.157-.586.675-2.088.241-4.478-1.338-6.235l.03.045z"
                  />
                </svg>
              </button>
              <button
                type="button"
                class="job-detail-page__share-brand"
                title="LinkedIn"
                aria-label="分享到 LinkedIn"
                @click="openLinkedInShare"
              >
                <svg
                  class="job-detail-page__share-svg job-detail-page__share-svg--linkedin"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="#0A66C2"
                    d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </aside>
      </div>

      <section
        v-if="relatedJobs.length"
        class="job-detail-page__related"
        aria-labelledby="related-heading"
      >
        <h2 id="related-heading" class="job-detail-page__related-title">
          相关职位
        </h2>
        <div class="job-detail-page__related-grid">
          <SwanJobCard
            v-for="j in relatedJobs"
            :key="j.jobId"
            :job="j"
            :is-candidate="isCandidate"
            :selected="false"
            :selectable="false"
            @open-detail="() => goJob(j.jobId)"
            @toggle-collect="() => onRelatedCollect(j)"
          />
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import SwanJobCard from "@/components/swan/SwanJobCard.vue";
import type { SwanJobSummary } from "@/components/swan/types";
import {
  applyJob,
  collectJob,
  getJobDetail,
  listResumes,
  searchJobs,
  uncollectJob,
} from "@/api/recruit";
import { getUserType } from "@/utils/auth";
import { openAuthModal } from "@/composables/useAuthModal";

const route = useRoute();
const router = useRouter();

const loading = ref(true);
const job = ref<SwanJobSummary | null>(null);
const relatedJobs = ref<SwanJobSummary[]>([]);

const isCandidate = computed(() => getUserType() === "CANDIDATE");

const jobTypeLabel = computed(() => {
  const t = job.value?.jobType?.trim() || "全职";
  return t.toUpperCase();
});

const salaryRange = computed(() => {
  if (!job.value) {
    return "";
  }
  const a = new Intl.NumberFormat("zh-CN").format(job.value.salaryMin);
  const b = new Intl.NumberFormat("zh-CN").format(job.value.salaryMax);
  return `¥${a} - ¥${b} / 月`;
});

function splitParagraphs(text: string | undefined): string[] {
  if (!text?.trim()) {
    return [];
  }
  return text
    .split(/\n+/)
    .map((s) => s.trim())
    .filter(Boolean);
}

function splitBullets(text: string | undefined): string[] {
  if (!text?.trim()) {
    return [];
  }
  const parts = text
    .split(/[;；\n]/)
    .map((s) => s.trim())
    .filter(Boolean);
  return parts.length ? parts : [text.trim()];
}

const descriptionParagraphs = computed(() =>
  splitParagraphs(job.value?.responsibility)
);
const requirementItems = computed(() => splitBullets(job.value?.requirement));
const desirableItems = computed(() => splitBullets(job.value?.desirable));
const benefitItems = computed(() => {
  const w = job.value?.welfare;
  if (!w?.length) {
    return [];
  }
  return w.map((x) => String(x).trim()).filter(Boolean);
});

function companyInitial(name: string) {
  const s = (name || "公").trim();
  return s.charAt(0) || "企";
}

function currentJobId(): number {
  return Number(route.params.jobId);
}

const RELATED_PAGE_SIZE = 30;

async function loadRelated(source: SwanJobSummary, excludeId: number) {
  try {
    const companyKw = source.companyName?.trim() || "";
    const jobName = source.jobName?.trim() || "";
    const tailKw =
      jobName.length >= 2
        ? jobName.slice(-2)
        : jobName.length === 1
        ? jobName
        : "";

    const base = {
      education: "",
      experience: "",
      location: "",
      pageNum: 1,
      pageSize: RELATED_PAGE_SIZE,
    };

    const byId = new Map<number, SwanJobSummary>();

    const mergeSearch = async (keyword: string) => {
      if (!keyword) {
        return;
      }
      const data = await searchJobs({ ...base, keyword });
      for (const j of data.list || []) {
        if (j.jobId !== excludeId && !byId.has(j.jobId)) {
          byId.set(j.jobId, j);
        }
      }
    };

    await mergeSearch(companyKw);
    await mergeSearch(tailKw);

    if (byId.size < 6) {
      const data = await searchJobs({
        ...base,
        keyword: "",
        sortBy: "recent",
        pageSize: 20,
      });
      for (const j of data.list || []) {
        if (j.jobId !== excludeId && !byId.has(j.jobId)) {
          byId.set(j.jobId, j);
        }
        if (byId.size >= 12) {
          break;
        }
      }
    }

    const currentCompany = source.companyName?.trim() || "";
    const currentType = source.jobType?.trim() || "";

    const merged = [...byId.values()].sort((a, b) => {
      const sameCo = (n: string) => (n === currentCompany ? 0 : 1);
      const sc =
        sameCo((a.companyName || "").trim()) -
        sameCo((b.companyName || "").trim());
      if (sc !== 0) {
        return sc;
      }
      const sameTy = (t: string) => ((t || "").trim() === currentType ? 0 : 1);
      const st = sameTy(a.jobType || "") - sameTy(b.jobType || "");
      if (st !== 0) {
        return st;
      }
      return b.jobId - a.jobId;
    });

    relatedJobs.value = merged.slice(0, 6);
  } catch {
    relatedJobs.value = [];
  }
}

async function load() {
  const id = currentJobId();
  if (!Number.isFinite(id) || id <= 0) {
    ElMessage.warning("职位不存在");
    router.replace("/jobs");
    return;
  }
  loading.value = true;
  try {
    const detail = await getJobDetail(id);
    job.value = detail;
    await loadRelated(detail, id);
  } catch {
    ElMessage.error("职位加载失败");
    router.replace("/jobs");
  } finally {
    loading.value = false;
  }
}

function goJob(jobId: number) {
  router.push({ name: "job-detail", params: { jobId: String(jobId) } });
}

watch(
  () => route.params.jobId,
  () => {
    load();
  }
);

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

async function onApplyClick() {
  if (!job.value) {
    return;
  }
  if (!isCandidate.value) {
    openAuthModal({ tab: "candidate" });
    return;
  }
  try {
    const resumeId = await getDefaultResumeId();
    await applyJob({ jobId: job.value.jobId, resumeId });
    ElMessage.success("职位投递成功");
  } catch {
    // 拦截器或缺简历
  }
}

async function toggleCollect() {
  if (!job.value) {
    return;
  }
  if (!isCandidate.value) {
    openAuthModal({ tab: "candidate" });
    return;
  }
  try {
    if (job.value.collected) {
      await uncollectJob(job.value.jobId);
      job.value.collected = false;
      ElMessage.success("已取消收藏");
    } else {
      await collectJob(job.value.jobId);
      job.value.collected = true;
      ElMessage.success("已收藏职位");
    }
    syncRelatedCollectState(job.value.jobId, Boolean(job.value.collected));
  } catch {
    // 拦截器
  }
}

function syncRelatedCollectState(jobId: number, collected: boolean) {
  const j = relatedJobs.value.find((x) => x.jobId === jobId);
  if (j) {
    j.collected = collected;
  }
}

async function onRelatedCollect(j: SwanJobSummary) {
  if (!isCandidate.value) {
    openAuthModal({ tab: "candidate" });
    return;
  }
  try {
    if (j.collected) {
      await uncollectJob(j.jobId);
      j.collected = false;
      ElMessage.success("已取消收藏");
    } else {
      await collectJob(j.jobId);
      j.collected = true;
      ElMessage.success("已收藏职位");
    }
    if (job.value?.jobId === j.jobId) {
      job.value.collected = j.collected;
    }
  } catch {
    // 拦截器
  }
}

function jobShareUrl(): string {
  const path = router.resolve({
    name: "job-detail",
    params: { jobId: String(currentJobId()) },
  }).href;
  return `${window.location.origin}${path}`;
}

function jobShareTitle(): string {
  if (!job.value) {
    return "职位分享";
  }
  return `${job.value.jobName} · ${job.value.companyName}`;
}

function copyShareLink() {
  const url = jobShareUrl();
  navigator.clipboard.writeText(url).then(
    () => ElMessage.success("链接已复制"),
    () => ElMessage.info("复制失败，请手动复制地址栏链接")
  );
}

function shareWeChatHint() {
  const url = jobShareUrl();
  navigator.clipboard.writeText(url).then(
    () => ElMessage.success("链接已复制，可在微信中粘贴发送给好友"),
    () => ElMessage.info("请先点击「复制链接」，再到微信中粘贴分享")
  );
}

function openWeiboShare() {
  const url = jobShareUrl();
  const title = jobShareTitle();
  const href = `https://service.weibo.com/share/share.php?url=${encodeURIComponent(
    url
  )}&title=${encodeURIComponent(title)}`;
  window.open(href, "_blank", "noopener,noreferrer");
}

function openLinkedInShare() {
  const url = jobShareUrl();
  const href = `https://www.linkedin.com/sharing/share-offsite/?url=${encodeURIComponent(
    url
  )}`;
  window.open(href, "_blank", "noopener,noreferrer");
}

onMounted(() => {
  load();
});
</script>

<style scoped>
.job-detail-page {
  flex: 1;
  width: 100%;
  min-height: 100%;
  background: #fff;
}

.job-detail-page__inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px clamp(16px, 3vw, 28px) 64px;
}

.job-detail-page__back {
  display: inline-block;
  margin-bottom: 20px;
  font-size: 0.875rem;
  color: #64748b;
  text-decoration: none;
  font-weight: 500;
}

.job-detail-page__back:hover {
  color: var(--jb-primary, #0a65cc);
}

.job-detail-page__hero {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  padding-bottom: 28px;
  margin-bottom: 28px;
  border-bottom: 1px solid #e5e7eb;
}

.job-detail-page__hero-main {
  display: flex;
  gap: 18px;
  min-width: 0;
  flex: 1 1 280px;
}

.job-detail-page__logo {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(145deg, #f1f5f9, #e2e8f0);
  color: #475569;
  font-weight: 800;
  font-size: 1.35rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border: 1px solid #e2e8f0;
}

.job-detail-page__title {
  margin: 0 0 6px;
  font-size: clamp(1.35rem, 2.5vw, 1.75rem);
  font-weight: 800;
  letter-spacing: -0.02em;
  color: #0f172a;
  line-height: 1.25;
}

.job-detail-page__company {
  margin: 0 0 10px;
  font-size: 0.9375rem;
  color: #64748b;
}

.job-detail-page__company-at {
  margin-right: 6px;
  color: #94a3b8;
  font-size: 0.8125rem;
}

.job-detail-page__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.job-detail-page__pill {
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  padding: 5px 10px;
  border-radius: 6px;
  line-height: 1;
}

.job-detail-page__pill--type {
  color: #15803d;
  background: #dcfce7;
}

.job-detail-page__pill--feat {
  color: #be185d;
  background: #fce7f3;
}

.job-detail-page__hero-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.job-detail-page__icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  background: #fff;
  color: #94a3b8;
  cursor: pointer;
  transition: color 0.15s ease, border-color 0.15s ease, background 0.15s ease;
}

.job-detail-page__icon-btn:hover {
  color: var(--jb-primary, #0a65cc);
  border-color: rgba(10, 101, 204, 0.35);
  background: #f8fafc;
}

.job-detail-page__icon-btn--on {
  color: var(--jb-primary, #0a65cc);
  border-color: rgba(10, 101, 204, 0.4);
  background: #eff6ff;
}

.job-detail-page__apply {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 22px;
  height: 44px;
  border: none;
  border-radius: 10px;
  background: var(--jb-primary, #0a65cc);
  color: #fff;
  font-size: 0.9375rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s ease, transform 0.1s ease;
}

.job-detail-page__apply:hover {
  background: var(--jb-primary-hover, #084f9f);
}

.job-detail-page__apply-arrow {
  font-size: 1.1rem;
  line-height: 1;
}

.job-detail-page__layout {
  display: grid;
  grid-template-columns: minmax(0, 2fr) minmax(260px, 1fr);
  gap: 32px;
  align-items: start;
}

.job-detail-page__main {
  min-width: 0;
}

.job-detail-page__section {
  margin-bottom: 32px;
}

.job-detail-page__h2 {
  margin: 0 0 14px;
  font-size: 1.125rem;
  font-weight: 800;
  color: #0f172a;
}

.job-detail-page__para {
  margin: 0 0 12px;
  font-size: 0.9375rem;
  line-height: 1.75;
  color: #334155;
}

.job-detail-page__list {
  margin: 0;
  padding-left: 1.15rem;
  font-size: 0.9375rem;
  line-height: 1.7;
  color: #334155;
}

.job-detail-page__list li {
  margin-bottom: 8px;
}

.job-detail-page__aside {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.job-detail-page__card {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 18px 18px 16px;
  background: #fff;
}

.job-detail-page__card-title {
  margin: 0 0 10px;
  font-size: 0.8125rem;
  font-weight: 700;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.job-detail-page__card-title--sp {
  margin-top: 16px;
}

.job-detail-page__salary {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 800;
  color: #15803d;
  letter-spacing: -0.02em;
}

.job-detail-page__loc {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9375rem;
  color: #334155;
  font-weight: 500;
}

.job-detail-page__pin {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
  background: #64748b;
  opacity: 0.75;
  mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Cpath d='M12 21s7-4.35 7-10a7 7 0 1 0-14 0c0 5.65 7 10 7 10z'/%3E%3Ccircle cx='12' cy='11' r='2.5'/%3E%3C/svg%3E")
    center / contain no-repeat;
}

.job-detail-page__overview {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px 12px;
}

.job-detail-page__ov-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.job-detail-page__ov-label {
  font-size: 0.75rem;
  color: #94a3b8;
  font-weight: 500;
}

.job-detail-page__ov-value {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1e293b;
}

.job-detail-page__share-row {
  display: flex;
  align-items: stretch;
  gap: 8px;
}

.job-detail-page__copy {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 0;
  justify-content: center;
  height: 40px;
  margin-bottom: 0;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #f8fafc;
  font-size: 0.875rem;
  font-weight: 600;
  color: #334155;
  cursor: pointer;
  transition: border-color 0.15s ease, background 0.15s ease;
}

.job-detail-page__copy:hover {
  border-color: var(--jb-primary, #0a65cc);
  background: #eff6ff;
  color: var(--jb-primary, #0a65cc);
}

.job-detail-page__copy-ico {
  width: 16px;
  height: 16px;
  background: currentColor;
  opacity: 0.85;
  mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Crect x='9' y='9' width='13' height='13' rx='2'/%3E%3Cpath d='M5 15H4a2 2 0 01-2-2V4a2 2 0 012-2h9a2 2 0 012 2v1'/%3E%3C/svg%3E")
    center / contain no-repeat;
}

.job-detail-page__share-brand {
  flex-shrink: 0;
  width: 46px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
  transition: border-color 0.15s ease, background 0.15s ease;
}

.job-detail-page__share-brand:hover {
  border-color: var(--jb-primary, #0a65cc);
  background: #f8fafc;
}

.job-detail-page__share-svg {
  width: 22px;
  height: 22px;
  display: block;
}

.job-detail-page__share-svg--linkedin {
  width: 20px;
  height: 20px;
}

.job-detail-page__related {
  margin-top: 48px;
  padding-top: 36px;
  border-top: 1px solid #e5e7eb;
}

.job-detail-page__related-title {
  margin: 0 0 22px;
  font-size: 1.25rem;
  font-weight: 800;
  color: #0f172a;
}

.job-detail-page__related-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

@media (max-width: 1024px) {
  .job-detail-page__layout {
    grid-template-columns: 1fr;
  }

  .job-detail-page__related-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .job-detail-page__hero {
    flex-direction: column;
  }

  .job-detail-page__hero-actions {
    width: 100%;
    justify-content: stretch;
  }

  .job-detail-page__apply {
    flex: 1;
    justify-content: center;
  }

  .job-detail-page__related-grid {
    grid-template-columns: 1fr;
  }
}
</style>
