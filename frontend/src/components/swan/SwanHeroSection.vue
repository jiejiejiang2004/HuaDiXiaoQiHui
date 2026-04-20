<template>
  <section class="swan-hero">
    <div class="swan-hero__wrap">
      <div class="swan-hero__grid">
        <div class="swan-hero__copy">
          <p class="swan-hero__eyebrow">校企慧 · 校园与产业人才对接</p>
          <h1 class="swan-hero__title">
            找到一份契合你
            <span class="swan-hero__accent">兴趣与技能</span>
            的工作
          </h1>
          <p class="swan-hero__desc">
            海量校招与社会岗位，支持关键词、城市与条件组合筛选。完善简历后即可一键投递，让合适的机会主动找到你。
          </p>

          <div class="swan-hero__search-bar">
            <div class="swan-hero__search-field">
              <span
                class="swan-hero__field-ico swan-hero__field-ico--search"
                aria-hidden="true"
              />
              <el-input
                :model-value="filters.keyword"
                placeholder="职位、关键词、公司名"
                clearable
                @update:model-value="
                  patchFilters({ keyword: String($event ?? '') })
                "
                @keyup.enter="emit('search')"
              />
            </div>
            <div class="swan-hero__search-field">
              <span
                class="swan-hero__field-ico swan-hero__field-ico--pin"
                aria-hidden="true"
              />
              <el-input
                :model-value="filters.location"
                placeholder="工作城市"
                clearable
                @update:model-value="
                  patchFilters({ location: String($event ?? '') })
                "
                @keyup.enter="emit('search')"
              />
            </div>
            <el-button
              class="swan-hero__search-btn"
              type="primary"
              @click="emit('search')"
            >
              搜索职位
            </el-button>
          </div>
        </div>

        <div class="swan-hero__visual" aria-hidden="true">
          <div class="swan-hero__illustration">
            <svg
              viewBox="0 0 420 320"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
            >
              <rect width="420" height="320" rx="16" fill="#F8FAFC" />
              <path
                d="M60 240h300"
                stroke="#CBD5E1"
                stroke-width="2"
                stroke-linecap="round"
              />
              <rect
                x="120"
                y="200"
                width="180"
                height="40"
                rx="6"
                fill="#E2E8F0"
              />
              <rect
                x="150"
                y="160"
                width="120"
                height="44"
                rx="6"
                fill="#FFFFFF"
                stroke="#0A65CC"
                stroke-width="2"
              />
              <circle
                cx="210"
                cy="120"
                r="36"
                fill="#EFF6FF"
                stroke="#0A65CC"
                stroke-width="2"
              />
              <path
                d="M198 118c4-10 20-10 24 0 3 8-2 16-12 16s-15-8-12-16z"
                fill="#0A65CC"
                opacity=".25"
              />
              <circle cx="204" cy="114" r="3" fill="#0A65CC" />
              <circle cx="216" cy="114" r="3" fill="#0A65CC" />
              <path
                d="M204 126c4 4 12 4 16 0"
                stroke="#0A65CC"
                stroke-width="2"
                stroke-linecap="round"
              />
              <rect
                x="176"
                y="152"
                width="68"
                height="48"
                rx="8"
                fill="#FFFFFF"
                stroke="#94A3B8"
              />
              <rect
                x="186"
                y="162"
                width="48"
                height="28"
                rx="4"
                fill="#E8F1FC"
              />
              <circle
                cx="96"
                cy="88"
                r="22"
                fill="#EFF6FF"
                stroke="#0A65CC"
                stroke-width="1.5"
              />
              <path
                d="M96 78v20M86 88h20"
                stroke="#0A65CC"
                stroke-width="2"
                stroke-linecap="round"
              />
              <circle
                cx="332"
                cy="72"
                r="20"
                fill="#FFF7ED"
                stroke="#F59E0B"
                stroke-width="1.5"
              />
              <path
                d="M332 66v14l8 8"
                stroke="#F59E0B"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <circle
                cx="340"
                cy="180"
                r="18"
                fill="#FEF2F2"
                stroke="#EF4444"
                stroke-width="1.5"
              />
              <path
                d="M340 174v10"
                stroke="#EF4444"
                stroke-width="2"
                stroke-linecap="round"
              />
              <path
                d="M336 188c2 2 8 2 8 0"
                stroke="#EF4444"
                stroke-width="2"
                stroke-linecap="round"
              />
              <circle
                cx="72"
                cy="200"
                r="16"
                fill="#ECFDF5"
                stroke="#10B981"
                stroke-width="1.5"
              />
              <path
                d="M68 200l4 4 8-10"
                stroke="#10B981"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
        </div>
      </div>

      <div
        v-if="statsLoading || platformBrief"
        class="swan-hero__stats"
        role="list"
        aria-label="平台数据概览"
      >
        <div
          v-for="item in statItems"
          :key="item.key"
          class="swan-hero__stat"
          role="listitem"
        >
          <div class="swan-hero__stat-icon">
            <span
              :class="`swan-hero__stat-glyph swan-hero__stat-glyph--${item.icon}`"
              aria-hidden="true"
            />
          </div>
          <div class="swan-hero__stat-body">
            <p class="swan-hero__stat-value">{{ item.value }}</p>
            <p class="swan-hero__stat-label">{{ item.label }}</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { PlatformPublicBrief } from "@/api/recruit";
import type { SwanJobFilters } from "./types";

const props = withDefaults(
  defineProps<{
    filters: SwanJobFilters;
    /** 来自 /home/platform-brief，与数据库统计一致 */
    platformBrief?: PlatformPublicBrief | null;
    statsLoading?: boolean;
  }>(),
  {
    platformBrief: null,
    statsLoading: false,
  }
);

const emit = defineEmits<{
  "update:filters": [patch: Partial<SwanJobFilters>];
  search: [];
}>();

function patchFilters(patch: Partial<SwanJobFilters>) {
  emit("update:filters", { ...props.filters, ...patch });
}

function formatStat(n: number | undefined, loading: boolean) {
  if (loading || n === undefined) {
    return "—";
  }
  return new Intl.NumberFormat("zh-CN").format(n);
}

const statItems = computed(() => {
  const brief = props.platformBrief;
  const loading = props.statsLoading && !brief;
  return [
    {
      key: "ent",
      label: "入驻企业",
      value: formatStat(brief?.enterpriseCount, loading),
      icon: "building" as const,
    },
    {
      key: "job",
      label: "岗位总数",
      value: formatStat(brief?.jobCount, loading),
      icon: "case" as const,
    },
    {
      key: "cand",
      label: "注册求职者",
      value: formatStat(brief?.candidateCount, loading),
      icon: "people" as const,
    },
  ];
});
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
    var(--jb-hero-tint, #eff6ff) 42%,
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
    ellipse 65% 50% at 82% 8%,
    rgba(10, 101, 204, 0.14),
    transparent 55%
  );
  pointer-events: none;
}

.swan-hero__wrap {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 44px 20px 36px;
}

.swan-hero__grid {
  display: grid;
  grid-template-columns: minmax(0, 1.05fr) minmax(260px, 0.95fr);
  gap: 36px 40px;
  align-items: center;
}

.swan-hero__title {
  margin: 12px 0 16px;
  font-size: clamp(1.85rem, 4vw, 2.75rem);
  font-weight: 800;
  letter-spacing: -0.03em;
  line-height: 1.18;
  color: var(--jb-text);
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
  margin: 0 0 24px;
  max-width: 520px;
  line-height: 1.75;
  color: var(--jb-text-muted);
  font-size: 1.02rem;
}

.swan-hero__search-bar {
  --hero-search-row-height: 48px;
  display: flex;
  flex-wrap: wrap;
  align-items: stretch;
  gap: 0;
  background: var(--jb-bg-elevated);
  border-radius: var(--jb-radius-md);
  border: 1px solid var(--jb-border);
  box-shadow: var(--jb-shadow-md);
  overflow: hidden;
  max-width: 640px;
}

.swan-hero__search-field {
  box-sizing: border-box;
  flex: 1 1 148px;
  min-width: 0;
  display: flex;
  align-items: stretch;
  gap: 10px;
  padding: 0 14px;
  border-right: 1px solid var(--jb-border);
  height: var(--hero-search-row-height);
  min-height: var(--hero-search-row-height);
  max-height: var(--hero-search-row-height);
}

.swan-hero__search-field:last-of-type {
  border-right: none;
}

.swan-hero__search-field :deep(.el-input) {
  flex: 1;
  width: 0;
  min-width: 0;
  display: flex;
  align-items: stretch;
}

.swan-hero__search-field :deep(.el-input__wrapper) {
  flex: 1;
  box-shadow: none !important;
  background: transparent;
  padding-left: 0;
  min-height: 0 !important;
  height: 100% !important;
  align-items: center;
}

.swan-hero__field-ico {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  align-self: center;
  opacity: 0.5;
  background: var(--jb-text-muted);
  mask-size: contain;
  mask-repeat: no-repeat;
  mask-position: center;
}

.swan-hero__field-ico--search {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Ccircle cx='11' cy='11' r='7'/%3E%3Cpath d='M20 20l-3-3'/%3E%3C/svg%3E");
}

.swan-hero__field-ico--pin {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Cpath d='M12 21s7-4.35 7-10a7 7 0 1 0-14 0c0 5.65 7 10 7 10z'/%3E%3Ccircle cx='12' cy='11' r='2.5'/%3E%3C/svg%3E");
}

.swan-hero__search-btn {
  box-sizing: border-box;
  flex: 0 0 auto;
  align-self: stretch;
  height: var(--hero-search-row-height);
  min-height: var(--hero-search-row-height);
  max-height: var(--hero-search-row-height);
  padding: 0 24px;
  border-radius: 0 !important;
  font-weight: 700;
  font-size: 0.9375rem;
  letter-spacing: 0.02em;
  line-height: 1.2;
}

.swan-hero__visual {
  justify-self: end;
  width: 100%;
  max-width: 400px;
}

.swan-hero__illustration :deep(svg) {
  width: 100%;
  height: auto;
  display: block;
}

.swan-hero__stats {
  margin-top: 36px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.swan-hero__stat {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 20px;
  background: var(--jb-bg-elevated);
  border: 1px solid var(--jb-border);
  border-radius: var(--jb-radius-md);
  box-shadow: var(--jb-shadow-sm);
}

.swan-hero__stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--jb-radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  background: #eff6ff;
  border: 1px solid #dbeafe;
  cursor: default;
  transition: background 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}

.swan-hero__stat:hover .swan-hero__stat-icon {
  background: var(--jb-primary);
  border-color: var(--jb-primary);
  box-shadow: 0 4px 12px rgba(10, 101, 204, 0.22);
}

.swan-hero__stat-glyph {
  width: 22px;
  height: 22px;
  background: var(--jb-primary);
  mask-size: contain;
  mask-repeat: no-repeat;
  mask-position: center;
  transition: background 0.2s ease;
}

.swan-hero__stat:hover .swan-hero__stat-glyph {
  background: #fff;
}

.swan-hero__stat-glyph--case {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Crect x='4' y='7' width='16' height='12' rx='2'/%3E%3Cpath d='M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'/%3E%3C/svg%3E");
}

.swan-hero__stat-glyph--building {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Cpath d='M4 21V8l8-4 8 4v13'/%3E%3Cpath d='M9 21v-4h6v4'/%3E%3Cpath d='M9 13h2'/%3E%3Cpath d='M13 13h2'/%3E%3Cpath d='M9 17h2'/%3E%3Cpath d='M13 17h2'/%3E%3C/svg%3E");
}

.swan-hero__stat-glyph--people {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='black' stroke-width='2'%3E%3Ccircle cx='9' cy='7' r='3'/%3E%3Cpath d='M3 21v-2a4 4 0 0 1 4-4h4a4 4 0 0 1 4 4v2'/%3E%3Ccircle cx='17' cy='10' r='2.5'/%3E%3Cpath d='M21 21v-1.5a3.5 3.5 0 0 0-3.5-3.5h-1'/%3E%3C/svg%3E");
}

.swan-hero__stat-value {
  margin: 0;
  font-size: 1.35rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
}

.swan-hero__stat-label {
  margin: 4px 0 0;
  font-size: 0.875rem;
  color: var(--jb-text-muted);
}

@media (max-width: 960px) {
  .swan-hero__grid {
    grid-template-columns: 1fr;
  }

  .swan-hero__visual {
    justify-self: center;
    order: -1;
    max-width: 320px;
  }

  .swan-hero__stats {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 720px) {
  .swan-hero__wrap {
    padding: 32px 16px 28px;
  }

  .swan-hero__search-bar {
    --hero-search-row-height: 44px;
    max-width: none;
    gap: 0;
  }

  .swan-hero__search-field {
    padding: 0 10px;
    gap: 8px;
  }

  .swan-hero__field-ico {
    width: 18px;
    height: 18px;
  }

  .swan-hero__search-btn {
    padding: 0 16px;
    font-size: 0.875rem;
  }
}

@media (max-width: 520px) {
  .swan-hero__search-bar {
    --hero-search-row-height: 44px;
    flex-direction: column;
    align-items: stretch;
  }

  .swan-hero__search-field {
    flex: 0 0 auto;
    width: 100%;
    border-right: none;
    border-bottom: 1px solid var(--jb-border);
  }

  .swan-hero__search-btn {
    width: 100%;
    flex: 0 0 auto;
  }
}
</style>
