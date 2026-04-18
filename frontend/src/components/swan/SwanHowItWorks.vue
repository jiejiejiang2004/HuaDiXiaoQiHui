<template>
  <section class="swan-how" aria-labelledby="swan-how-title">
    <div class="swan-how__inner">
      <h2 id="swan-how-title" class="swan-how__title">校企慧怎样帮你求职</h2>
      <div class="swan-how__track">
        <template v-for="(step, index) in steps" :key="step.key">
          <div class="swan-how__step">
            <div class="swan-how__icon-wrap" aria-hidden="true">
              <component :is="step.icon" class="swan-how__icon-svg" />
            </div>
            <h3 class="swan-how__step-title">{{ step.title }}</h3>
            <p class="swan-how__step-desc">{{ step.desc }}</p>
          </div>
          <div
            v-if="index < steps.length - 1"
            class="swan-how__connector"
            aria-hidden="true"
          >
            <svg
              class="swan-how__connector-svg swan-how__connector-svg--h"
              viewBox="0 0 72 36"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M4 28 C 22 6, 50 6, 68 28"
                fill="none"
                stroke="#93c5fd"
                stroke-width="1.5"
                stroke-dasharray="5 5"
                stroke-linecap="round"
              />
              <path
                d="M62 24 L68 28 L64 32"
                fill="none"
                stroke="#93c5fd"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            <svg
              class="swan-how__connector-svg swan-how__connector-svg--v"
              viewBox="0 0 36 48"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M18 4 C 6 16, 6 32, 18 44"
                fill="none"
                stroke="#93c5fd"
                stroke-width="1.5"
                stroke-dasharray="5 5"
                stroke-linecap="round"
              />
              <path
                d="M14 38 L18 44 L22 38"
                fill="none"
                stroke="#93c5fd"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
        </template>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { h } from "vue";

function IconPersonPlus() {
  return h(
    "svg",
    {
      class: "swan-how__glyph",
      viewBox: "0 0 48 48",
      xmlns: "http://www.w3.org/2000/svg",
    },
    [
      h("circle", {
        cx: "20",
        cy: "16",
        r: "7",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
      }),
      h("path", {
        d: "M8 40c0-8 5.4-12 12-12s12 4 12 12",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
      }),
      h("path", {
        d: "M34 10v8M30 14h8",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
      }),
    ]
  );
}

function IconCloudUpload() {
  return h(
    "svg",
    {
      class: "swan-how__glyph",
      viewBox: "0 0 48 48",
      xmlns: "http://www.w3.org/2000/svg",
    },
    [
      h("path", {
        d: "M12 32h-2a8 8 0 0 1 .3-15.9A10 10 0 0 1 36 20a6 6 0 0 1 1 11.9V32H22",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
      }),
      h("path", {
        d: "M24 26v12M18 30l6-6 6 6",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
      }),
    ]
  );
}

function IconSearchPlus() {
  return h(
    "svg",
    {
      class: "swan-how__glyph",
      viewBox: "0 0 48 48",
      xmlns: "http://www.w3.org/2000/svg",
    },
    [
      h("circle", {
        cx: "20",
        cy: "20",
        r: "8",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
      }),
      h("path", {
        d: "M26 26l8 8M32 14v6M29 17h6",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
      }),
    ]
  );
}

function IconBadgeCheck() {
  return h(
    "svg",
    {
      class: "swan-how__glyph",
      viewBox: "0 0 48 48",
      xmlns: "http://www.w3.org/2000/svg",
    },
    [
      h("path", {
        d: "M24 6l14 6v10c0 9-6 17-14 20-8-3-14-11-14-20V12l14-6z",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linejoin": "round",
      }),
      h("path", {
        d: "M17 24l5 5 10-11",
        fill: "none",
        stroke: "currentColor",
        "stroke-width": "2",
        "stroke-linecap": "round",
        "stroke-linejoin": "round",
      }),
    ]
  );
}

const steps = [
  {
    key: "register",
    title: "注册账号",
    desc: "使用手机号或邮箱完成注册，选择求职者身份，即可开启你的校招与社会招聘之旅。",
    icon: IconPersonPlus,
  },
  {
    key: "resume",
    title: "上传简历",
    desc: "在线填写或上传简历附件，支持多份简历管理，并可将常用简历设为默认投递。",
    icon: IconCloudUpload,
  },
  {
    key: "find",
    title: "发现职位",
    desc: "在首页浏览最新岗位，或前往求职页按城市、关键词、学历等条件筛选合适机会。",
    icon: IconSearchPlus,
  },
  {
    key: "apply",
    title: "投递岗位",
    desc: "登录后一键投递心仪职位，可在个人中心查看投递与面试进度，及时跟进反馈。",
    icon: IconBadgeCheck,
  },
];
</script>

<style scoped>
.swan-how {
  padding: 40px 0 48px;
}

.swan-how__inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.swan-how__title {
  margin: 0 0 40px;
  text-align: center;
  font-size: clamp(1.35rem, 3vw, 1.75rem);
  font-weight: 800;
  letter-spacing: -0.02em;
  color: var(--jb-text);
}

.swan-how__track {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0;
}

.swan-how__step {
  flex: 1 1 0;
  min-width: 0;
  padding: 20px 16px 22px;
  border-radius: var(--jb-radius-md);
  text-align: center;
  transition: background 0.2s ease, box-shadow 0.2s ease;
}

.swan-how__step:hover {
  background: var(--jb-bg-elevated);
  box-shadow: var(--jb-shadow-card);
}

.swan-how__icon-wrap {
  width: 72px;
  height: 72px;
  margin: 0 auto 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1px solid #dbeafe;
  color: var(--jb-primary);
  transition: background 0.2s ease, border-color 0.2s ease, color 0.2s ease,
    box-shadow 0.2s ease;
}

.swan-how__step:hover .swan-how__icon-wrap {
  background: var(--jb-primary);
  border-color: var(--jb-primary);
  color: #fff;
  box-shadow: 0 6px 16px rgba(10, 101, 204, 0.28);
}

.swan-how__icon-svg {
  width: 40px;
  height: 40px;
  display: block;
}

.swan-how__glyph {
  display: block;
  width: 100%;
  height: 100%;
}

.swan-how__step-title {
  margin: 0 0 10px;
  font-size: 1.0625rem;
  font-weight: 800;
  color: var(--jb-text);
  letter-spacing: -0.02em;
}

.swan-how__step-desc {
  margin: 0;
  font-size: 0.875rem;
  line-height: 1.65;
  color: var(--jb-text-muted);
  text-align: center;
}

.swan-how__connector {
  flex: 0 0 auto;
  width: 56px;
  align-self: center;
  padding: 0 4px;
  margin-top: 28px;
  opacity: 0.95;
}

.swan-how__connector-svg {
  width: 100%;
  height: auto;
  display: block;
}

.swan-how__connector-svg--v {
  display: none;
}

@media (max-width: 900px) {
  .swan-how__track {
    flex-direction: column;
    align-items: stretch;
  }

  .swan-how__connector {
    width: 100%;
    margin: 4px auto 8px;
    padding: 0;
    display: flex;
    justify-content: center;
    align-self: center;
  }

  .swan-how__connector-svg--h {
    display: none;
  }

  .swan-how__connector-svg--v {
    display: block;
    width: 36px;
    height: 40px;
  }

  .swan-how__step {
    max-width: 420px;
    margin-left: auto;
    margin-right: auto;
  }
}
</style>
