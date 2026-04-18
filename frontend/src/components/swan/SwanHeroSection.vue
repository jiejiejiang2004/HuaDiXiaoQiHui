<template>
  <section class="swan-hero">
    <div class="swan-hero__wrap">
      <p class="swan-hero__eyebrow">校企慧 · 校园与产业人才对接</p>
      <h1 class="swan-hero__title">
        找到你的
        <span class="swan-hero__accent">理想职位</span>
      </h1>
      <p class="swan-hero__desc">
        职位搜索、在线简历与投递一站完成。按关键词、学历、经验与城市快速筛选岗位。
      </p>
      <div class="swan-hero__search">
        <p class="swan-hero__search-title">搜索职位</p>
        <el-form
          :inline="true"
          :model="filters"
          class="swan-hero__form"
          @submit.prevent
        >
          <el-form-item label="关键词">
            <el-input
              :model-value="filters.keyword"
              placeholder="职位名称、公司名"
              clearable
              style="width: 200px"
              @update:model-value="
                patchFilters({ keyword: String($event ?? '') })
              "
            />
          </el-form-item>
          <el-form-item label="学历">
            <el-select
              :model-value="filters.education"
              placeholder="不限"
              clearable
              style="width: 120px"
              @update:model-value="
                patchFilters({ education: String($event ?? '') })
              "
            >
              <el-option label="大专" value="大专" />
              <el-option label="本科" value="本科" />
              <el-option label="硕士" value="硕士" />
            </el-select>
          </el-form-item>
          <el-form-item label="经验">
            <el-select
              :model-value="filters.experience"
              placeholder="不限"
              clearable
              style="width: 120px"
              @update:model-value="
                patchFilters({ experience: String($event ?? '') })
              "
            >
              <el-option label="应届" value="应届" />
              <el-option label="1-3年" value="1-3年" />
              <el-option label="3-5年" value="3-5年" />
            </el-select>
          </el-form-item>
          <el-form-item label="地点">
            <el-input
              :model-value="filters.location"
              placeholder="如：成都、重庆"
              clearable
              style="width: 140px"
              @update:model-value="
                patchFilters({ location: String($event ?? '') })
              "
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="emit('search')"
              >搜索职位</el-button
            >
          </el-form-item>
          <el-form-item v-if="isCandidate">
            <el-button
              type="success"
              :disabled="batchDisabled"
              @click="emit('batch-apply')"
            >
              批量投递
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="swan-hero__cta">
        <el-button size="large" @click="emit('go-candidate')"
          >求职者入口</el-button
        >
        <el-button size="large" type="primary" @click="emit('go-enterprise')"
          >企业发布职位</el-button
        >
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import type { SwanJobFilters } from "./types";

const props = defineProps<{
  filters: SwanJobFilters;
  isCandidate: boolean;
  batchDisabled: boolean;
}>();

const emit = defineEmits<{
  "update:filters": [patch: Partial<SwanJobFilters>];
  search: [];
  "batch-apply": [];
  "go-candidate": [];
  "go-enterprise": [];
}>();

function patchFilters(patch: Partial<SwanJobFilters>) {
  emit("update:filters", { ...props.filters, ...patch });
}
</script>

<style scoped>
.swan-hero {
  position: relative;
  margin-left: calc(-50vw + 50%);
  margin-right: calc(-50vw + 50%);
  width: 100vw;
  max-width: 100vw;
  margin-bottom: 40px;
  padding: 0;
  border-radius: 0;
  background: linear-gradient(
    165deg,
    #f8fafc 0%,
    var(--jb-hero-tint, #eff6ff) 45%,
    #e0f2fe 100%
  );
  overflow: hidden;
  border-bottom: 1px solid var(--jb-border);
}

.swan-hero::after {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(
    ellipse 70% 55% at 75% 0%,
    rgba(10, 101, 204, 0.12),
    transparent 58%
  );
  pointer-events: none;
}

.swan-hero__wrap {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px 48px;
}

.swan-hero__title {
  margin: 12px 0 16px;
  font-size: clamp(1.85rem, 4.2vw, 2.65rem);
  font-weight: 800;
  letter-spacing: -0.03em;
  line-height: 1.15;
  color: var(--jb-text);
  max-width: 720px;
}

.swan-hero__accent {
  color: var(--jb-primary);
}

.swan-hero__eyebrow {
  margin: 0;
  font-size: 0.8125rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--jb-primary);
}

.swan-hero__desc {
  margin: 0 0 28px;
  max-width: 560px;
  line-height: 1.75;
  color: var(--jb-text-muted);
  font-size: 1.05rem;
}

.swan-hero__search {
  background: var(--jb-bg-elevated);
  border-radius: var(--jb-radius-md);
  padding: 22px 22px 12px;
  box-shadow: var(--jb-shadow-md);
  border: 1px solid var(--jb-border);
  margin-bottom: 24px;
  max-width: 100%;
}

.swan-hero__search-title {
  margin: 0 0 16px;
  font-size: 1.05rem;
  font-weight: 800;
  color: var(--jb-text);
  letter-spacing: -0.02em;
}

.swan-hero__search :deep(.el-form-item__label) {
  color: var(--jb-text-muted);
  font-weight: 500;
}

.swan-hero__form {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 4px 8px;
}

.swan-hero__cta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.swan-hero__cta :deep(.el-button--default) {
  --el-button-bg-color: #fff;
  --el-button-border-color: var(--jb-border);
  --el-button-text-color: var(--jb-text);
}

.swan-hero__cta :deep(.el-button--default:hover) {
  --el-button-border-color: var(--jb-primary);
  --el-button-text-color: var(--jb-primary);
}

@media (max-width: 900px) {
  .swan-hero__wrap {
    padding-top: 28px;
  }

  .swan-hero__search {
    padding: 16px 16px 8px;
  }
}
</style>
