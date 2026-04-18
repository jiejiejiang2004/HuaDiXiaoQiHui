# Swan 组件说明

> 最后更新：与 `frontend/src/components/swan/` 目录结构一致，便于逐个改 UI。

---

## 1. 公共类型 `types.ts`

**路径**：`frontend/src/components/swan/types.ts`

| 导出 | 用途 |
|------|------|
| `SwanJobSummary` | 职位卡片 / 列表所需字段（与接口返回对齐） |
| `SwanJobFilters` | 首页搜索表单四项筛选条件 |

**修改提示**：若后端字段变更，先改此处类型，再同步 `SwanJobCard` / `HomeView` 中的接口调用。

---

## 2. `SwanTopNav` 全站顶栏

**路径**：`frontend/src/components/swan/SwanTopNav.vue`  
**使用位置**：`AppShell.vue`

### 职责

- 品牌区（Logo +「校企慧」）
- 主导航：**首页** `/`、**求职** `/#job-list`（激活态与底部蓝条）
- 右侧：**登录** 或 **我的主页**（按登录态与 `userType` 跳转）

### Props / Emits

无 Props；内部使用 `vue-router` + `@/utils/auth`，与原先写在 `AppShell` 中的逻辑一致。

### 单独修改时建议关注

- 链接文案、路由、`isHomeActive` / `isJobsActive` 判定
- 顶栏高度、下划线粗细：`--jp-nav-underline`
- 背景与边框：`--jp-nav-bg`、`--jp-nav-border`

---

## 3. `SwanSiteFooter` 全站页脚

**路径**：`frontend/src/components/swan/SwanSiteFooter.vue`  
**使用位置**：`AppShell.vue`（`v-if="showFooter"`）

### 职责

- 展示 Figma 参考链接等轻量说明

### 单独修改

- 直接改模板与 scoped 样式即可；是否展示仍由 `AppShell` 的 `showFooter` 控制。

---

## 4. `SwanHeroSection` 首页 Hero + 搜索

**路径**：`frontend/src/components/swan/SwanHeroSection.vue`  
**使用位置**：`HomeView.vue`

### Props

| 名称 | 类型 | 说明 |
|------|------|------|
| `filters` | `SwanJobFilters` | 筛选条件（只读展示，通过事件回写） |
| `isCandidate` | `boolean` | 是否显示「批量投递」 |
| `batchDisabled` | `boolean` | 「批量投递」是否禁用 |

### Events

| 事件名 | 载荷 | 说明 |
|--------|------|------|
| `update:filters` | `Partial<SwanJobFilters>` 合并后的完整快照 | 任一筛选项变更时由子组件发出；父级 `Object.assign` 到自身 `filters` |
| `search` | — | 点击「搜索职位」 |
| `batch-apply` | — | 点击「批量投递」 |
| `go-candidate` | — | 「求职者入口」 |
| `go-enterprise` | — | 「企业发布职位」 |

### 单独修改时建议关注

- 标题、副文案、渐变背景（`.swan-hero`）
- 搜索白卡片圆角：使用 `var(--jb-radius-md)`
- 表单项布局：`.swan-hero__form`

---

## 5. `SwanJobListSection` 职位列表区块

**路径**：`frontend/src/components/swan/SwanJobListSection.vue`  
**使用位置**：`HomeView.vue`

### Props

| 名称 | 类型 | 说明 |
|------|------|------|
| `jobs` | `SwanJobSummary[]` | 当前页职位数据 |
| `loading` | `boolean` | 加载中（`v-loading`） |
| `total` | `number` | 总条数（标题旁展示） |
| `isCandidate` | `boolean` | 是否展示投递相关操作 |
| `selectedJobIds` | `number[]` | 已选职位 id |
| `sectionId` | `string`，默认 `job-list` | 锚点 id，供顶栏「求职」滚动 |

### Events

| 事件名 | 载荷 | 说明 |
|--------|------|------|
| `open-detail` | `jobId: number` | 打开详情 |
| `toggle-collect` | `job: SwanJobSummary` | 收藏/取消收藏 |
| `toggle-select` | `jobId`, `checked` | 多选投递勾选 |

### 单独修改

- 区块标题、统计文案
- 网格列宽：`.swan-job-list__grid` 的 `minmax`

---

## 6. `SwanJobCard` 单条职位卡片

**路径**：`frontend/src/components/swan/SwanJobCard.vue`  
**使用位置**：`SwanJobListSection.vue`

### Props

| 名称 | 类型 | 说明 |
|------|------|------|
| `job` | `SwanJobSummary` | 单条数据 |
| `isCandidate` | `boolean` | 是否求职者 |
| `selected` | `boolean` | 是否已加入批量投递 |

### Events

| 事件名 | 载荷 | 说明 |
|--------|------|------|
| `open-detail` | — | 由父级带上当前 `jobId` |
| `toggle-collect` | — | 父级传入当前 `job` |
| `toggle-select` | `checked` | `el-checkbox` 变更值 |

### 单独修改时建议关注

- 公司首字头像、薪资块、标签 chips、底部操作区
- 圆角统一用 `var(--jb-radius-md)` / `var(--jb-radius-sm)`，避免过大圆角

---

## 7. 求职者个人中心（`CandidateCenterView` · Swan 风格对齐）

**路径**：`frontend/src/views/CandidateCenterView.vue`  
**路由**：`/candidate`（`AppShell` 内全屏内容区，无顶栏重复）

### 职责与 Tab

| Tab | 内容要点 |
|-----|----------|
| 概览 | 投递/收藏统计卡片、子指标、投递热力图（`GET /statistics/candidate` → `applyTrend`） |
| 个人资料 | 分区表单：**账号信息**（姓名、手机只读）、**学业与联络**（邮箱、城市、学校、专业）；说明文案 + `candidate-form-section` 分隔 |
| 我的简历 | 卡片头：默认 / PDF / 删除 / 保存；正文分区：**简历标题**、**基本信息**（含性别、出生日期、现居城市）、**求职意向**（含期望薪资数字）、**教育经历**（含起止时间）、**隐私与附件**、**自我评价** |
| 投递记录 | 表格式自定义列表：公司首字、职位、类型标签、地点与薪资、投递时间、状态（灰叹号 / 绿勾 / 红叉）、查看详情；行 hover 高亮 |
| 消息中心 | 消息卡片：类型标签（面试邀约 / 投递通知 / 系统通知等）、关联职位块（可选字段）、正文；未读为带描边强调；详见接口说明（`GET /messages` 可扩展 `job*`） |
| 收藏职位 | 与投递记录同构列表 |

### 设计约定（与 Swan 一致）

- 主标题：`#0f172a`、字重 800；副文案：`#64748b`（`.candidate-panel__desc`、`.candidate-form-section__hint`）。
- 卡片容器：`candidate-card` + Element `el-card`，与首页职位区同样使用 `--jb-border`、`--jb-shadow-sm`、`--jb-radius-md`。
- 表单分区：`.candidate-form-stack` / `.candidate-form-section` / `__title` / `__hint`，浅分割线 `#f1f5f9`，避免在卡片内再套一层粗边框（与消息列表「仅外层一层框」原则一致）。
- 职位相关展示（投递/收藏/消息内职位块）：与 `SwanJobCard` 同类信息层级（标题、公司、地点、薪资、小标签）。
- **窄屏（≤960px）**：左侧 Tab 默认收起，仅展示「当前」Tab 与展开按钮；点击后展开完整导航与「退出登录」；切换 Tab 或断点变宽后自动收起。

### 相关接口（摘录）

- 资料：`GET/PATCH /profile`（或项目内 `getProfile` / `updateProfile`）
- 简历：`listResumes`、`getResumeDetail`、`updateResume`、`createResume` 等
- 投递：`GET /apply/my`
- 收藏：`GET /jobs/favorites`
- 消息：`GET /messages`、`GET /messages/:id`（列表项含 `type`、`bizId`；职位扩展字段见后端/Mock 约定）

---

## 建议的迭代顺序（「一个一个慢慢改」）

1. **`jobpilot-theme.css`**：色板、圆角、阴影 — 影响全站  
2. **`SwanTopNav.vue`**：与 Figma 顶栏像素对齐  
3. **`SwanHeroSection.vue`**：着陆区与搜索卡  
4. **`SwanJobCard.vue`**：列表中最显眼单元  
5. **`SwanJobListSection.vue`**：栅格与区块标题  
6. **`SwanSiteFooter.vue`**：最后收尾  

每改完一个组件，在本文档对应小节补一行 **变更记录（日期 + 摘要）** 即可。

---

## 变更记录

| 日期 | 摘要 |
|------|------|
| 2026-04 | 新增 **§7 求职者个人中心**：投递/收藏列表、消息卡片、个人资料与简历分区表单与 Swan 主题对齐；更新 `README.md` 目录说明。 |
| 2026-04 | 个人中心窄屏：侧栏 Tab 折叠/展开（`matchMedia` 与 `@media (max-width: 960px)` 对齐）。 |
