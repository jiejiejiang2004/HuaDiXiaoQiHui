# Swan 前端组件与文档

本目录存放 **Swan** 迭代相关的说明文档（与 `frontend/src/components/swan/` 下的 Vue 组件及 **Swan 风格对齐页面** 配套）。

## 命名

- **Swan**：与主业务代码区分的 UI 组件命名空间，便于按 Figma / 设计稿逐步替换页面，而不与历史页面强耦合。
- **Swan 风格对齐**：求职者个人中心等页面虽不在 `components/swan/` 下，但使用同一套主题变量（`jobpilot-theme.css`）、圆角与蓝灰主色，与首页职位卡、顶栏一致。

## 圆角与主题

- 全局圆角已收紧，见 `frontend/src/styles/jobpilot-theme.css` 中的 `--jb-radius-*`。
- 主色、顶栏色等 CSS 变量同文件；组件与求职者工作台内优先使用这些变量，避免写死色值。

## 文档索引

| 文档 | 说明 |
|------|------|
| [components.md](./components.md) | Swan 组件清单、求职者工作台 UI 说明、修改顺序建议 |

## 代码位置

### Swan 组件（`frontend/src/components/swan/`）

```
frontend/src/components/swan/
├── types.ts               # 组件间共享类型
├── SwanTopNav.vue         # 全站顶栏
├── SwanHeroSection.vue    # 首页 Hero + 搜索区
├── SwanJobListSection.vue # 首页职位列表区块
├── SwanJobCard.vue        # 单条职位卡片
└── SwanHowItWorks.vue     # 首页「如何使用」等说明区块
```

页脚等区域若由 `AppShell.vue` 内联实现，以仓库当前代码为准；文档描述的是 **Swan 视觉体系**，不限于单一目录。

### 求职者工作台（Swan 风格对齐）

| 路径 | 说明 |
|------|------|
| `frontend/src/views/CandidateCenterView.vue` | 求职者个人中心：概览热力图、**个人资料**、**我的简历**、投递记录列表、消息卡片、收藏职位列表 |

详见 [components.md §7](./components.md#7-求职者个人中心candidatecenterview--swan-风格对齐)。

后续新增 Swan 文档或设计说明，请继续放在 **`doc/swan/`** 下。
