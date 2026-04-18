# Swan 前端组件与文档

本目录存放 **Swan** 迭代相关的说明文档（与 `frontend/src/components/swan/` 下的 Vue 组件配套）。

## 命名

- **Swan**：与主业务代码区分的 UI 组件命名空间，便于按 Figma / 设计稿逐步替换页面，而不与历史页面强耦合。

## 圆角与主题

- 全局圆角已收紧，见 `frontend/src/styles/jobpilot-theme.css` 中的 `--jb-radius-*`。
- 主色、顶栏色等 CSS 变量同文件；组件内优先使用这些变量，避免写死色值。

## 文档索引

| 文档 | 说明 |
|------|------|
| [components.md](./components.md) | Swan 组件清单、职责、Props/Events、修改顺序建议 |

## 代码位置

```
frontend/src/components/swan/
├── types.ts              # 组件间共享类型
├── SwanTopNav.vue        # 全站顶栏
├── SwanSiteFooter.vue    # 全站页脚（仅首页/登录展示由 AppShell 控制）
├── SwanHeroSection.vue   # 首页 Hero + 搜索区
├── SwanJobListSection.vue# 首页职位列表区块
└── SwanJobCard.vue       # 单条职位卡片
```

后续新增 Swan 文档或设计说明，请继续放在 **`doc/swan/`** 下。
