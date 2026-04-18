# 校企慧公共服务平台-招聘模块 软件系统构架设计说明书
**项目编号**：HD20221101SR005
**文档编号**：HD20221101SR005
**版本**：V1.0
**项目承担部门**：智慧教育研教部
**撰写人**：林梦如
**完成日期**：2026-04-11
**评审日期**：2026-04-14
**使用部门**：项目组、维护人员
**公司**：四川华迪信息技术有限公司

---

## 文档信息
| 标题 | 校企慧公共服务平台软件系统架构说明书 |
| ---- | ---- |
| 作者 | 林梦如 |
| 创建日期 | 2026-04-09 |
| 上次更新日期 | — |
| 版本 | V1.0 |
| 部门名称 | 智慧教育研教部 |

---

## 修订文档历史记录
| 日期 | 版本 | 说明 | 作者 |
| ---- | ---- | ---- | ---- |
| — | — | — | — |

---

## 目录
1. [简介](#1-简介)
2. [构架表示方式](#2-构架表示方式)
3. [构架目标和约束](#3-构架目标和约束)
4. [关键用例视图](#4-关键用例视图)
5. [层次结构](#5-层次结构)
6. [逻辑视图](#6-逻辑视图)
7. [部署视图](#7-部署视图)

---

# 1 简介
本文档用于对整个系统的软件架构进行初步的简要描述。

## 1.1 目的
本文档从构架方面对校企慧公共服务平台-招聘模块进行综合概述，使用多种构架视图描述系统的各个方面，用于记录并表述已对系统构架做出的重要决策，作为后续详细设计、编码实现、测试验证和系统维护的依据。

## 1.2 范围
本文档作用于招聘模块的分析设计、实施、测试及运维阶段，将影响与上述活动相关的所有角色。

## 1.3 定义、首字母缩写词和缩略语
| 缩写/术语 | 全称/说明 |
| -------- | -------- |
| 校企慧 | 郫都区校企人力资源合作暨高技能人才培训联盟工作平台 |
| SRS | 软件需求规约 |
| UI | 用户界面 |
| B/S | 浏览器/服务器架构 |
| XQH-ZP | 校企慧公共服务平台-招聘模块软件缩称 |
| SpringBoot/SpringMVC/Mybatis | 系统采用的主要开发框架 |

## 1.4 参考资料
《校企慧公共服务平台-招聘模块需求规格说明书》

## 1.5 概述
略

---

# 2 构架表示方式
本文档采用UML分析设计语言对软件备选构架进行描述，使用Rational Rose工具生成软件构架的用例视图、逻辑视图和部署视图。对于进程视图和实施视图，由于在本软件备选构架中作用不明显，因而略去。

---

# 3 构架目标和约束

## 3.1 架构目标
- **业务目标**：消除区域人才就业信息孤岛，实现人才招聘、求职、审核、统计的全流程线上化管理。
- **技术目标**：用户操作简单，系统稳定性高、可扩展性高、构件重用性好，支持PC端与移动端自适应适配。

## 3.2 约束条件
- **技术栈约束**：基于SpringBoot + SpringMVC + MyBatis框架体系，使用Maven构建，Git版本管理。
- **过程约束**：遵循CMMI3及RUP开发过程规范。

---

# 4 关键用例视图
本部分展示系统核心角色的关键业务用例。根据SRS定义，系统划分为**个人求职者、企业用户、平台管理员**三大角色。

## 4.1 个人求职者用例图
主要包含注册登录、管理简历（增删改查）、搜索职位、投递简历、查看消息中心等用例。
> 图1 个人求职者用例图

## 4.2 企业用户用例图
主要包含企业认证、管理职位（上下架、刷新）、搜索人才、筛选接收简历、发送面试邀约等用例。
> 图2 企业用户用例图

## 4.3 平台管理员用例图
### 4.3.1 用户管理
- 个人用户管理：查看/编辑/禁用/启用求职者账号
- 企业用户管理：企业认证审核、企业信息管理、账号禁用/启用

### 4.3.2 信息审核
- 职位信息审核
- 简历信息审核
- 公告审核

### 4.3.3 系统管理
- 职位管理：添加/编辑/删除行业、职位分类
- 轮播图公告管理：发布/编辑/下架首页招聘相关内容
- 日志管理：用户操作日志
- 权限管理：按角色分配功能权限

> 图3 平台管理员用例图

## 4.4 首页管理
职位展示、搜索筛选、公告轮播、登录入口

## 4.5 消息通知
系统消息、业务消息

## 4.6 统计数据
- 个人数据统计：投递数量、面试邀约数量、收藏职位数量
- 企业统计：职位浏览量、简历接收量、面试邀约量
- 平台数据统计：总职位数、总用户数、总投递量

---

# 5 层次结构

| 包名 | 功能 |
| ---- | ---- |
| commonController | 首页展示 |
| personalController | 个人求职者功能 |
| enterpriseController | 企业用户功能 |
| adminController | 平台管理员功能 |
| statisticsController | 统计分析功能 |
| messageController | 消息中心功能 |

*表1 用户服务层(Controller层)功能*

> 图4 层次结构图

---

# 6 逻辑视图

## 6.1 概述
本章节从逻辑层面详细描述校企慧公共服务平台-招聘模块的类组织结构、包划分以及各层之间的协作关系。系统采用经典的**三层架构模式**，将功能按职责分配到不同的包和类中，确保系统的可维护性和可扩展性。

校企慧招聘模块的逻辑视图由以下三层组成：
1. **用户服务层（Controller）**：用户与系统交互的层面，包含用例分析中产生的边界类，负责接收HTTP请求、参数校验、请求分发，并返回视图或JSON数据。
2. **业务逻辑层（Service）**：响应用户操作，组织和管理系统的业务规则，包含在用例分析中产生的控制类，是系统的核心业务处理层，提供声明式事务管理。
3. **数据服务层（Dao）**：负责内部数据结构与外部数据存储（MySQL数据库）的存取操作，封装所有数据库访问逻辑，采用MyBatis框架实现。

系统采用MyBatis ORM设计实施数据模型，数据实体层（Entity）与数据库表结构一一对应，用于各层间的数据传输。

## 6.2 用户服务层
> 图5 用户服务层结构图

### 6.2.1 Package commonController
该包包含处理首页展示、全局搜索、用户登录注册等通用功能的类。

| 类名 | 功能 |
| ---- | ---- |
| IndexController | 处理首页热门职位、最新职位、推荐职位及公告轮播图的展示 |
| SearchController | 处理基于关键词和筛选条件的职位搜索请求 |
| LoginController | 处理个人/企业用户的登录、注册、忘记密码等请求 |

*表2 Package commonController*

### 6.2.2 Package personalController
该包包含处理个人求职者自助服务的类。

| 类名 | 功能 |
| ---- | ---- |
| ResumeController | 管理简历的在线创建、编辑、删除、预览、导出 |
| JobController | 处理职位搜索、详情查看 |
| DeliveryController | 投递简历 |
| PersonalController | 处理个人用户的消息中心 |

*表3 Package personalController*

### 6.2.3 Package enterpriseController
该包包含处理企业用户招聘管理全流程的类。

| 类名 | 功能 |
| ---- | ---- |
| CompanyController | 处理企业注册、认证 |
| JobManageController | 处理职位发布、编辑、刷新、删除 |
| ResumeManageController | 筛选简历 |
| InterviewController | 发送面试邀约 |
| TalentSearchController | 搜索人才库中的公开简历 |

*表4 Package enterpriseController*

### 6.2.4 Package adminController
| 类名 | 功能 |
| ---- | ---- |
| UserManageController | 处理个人用户和企业的账号状态管理 |
| MessageAuditController | 处理职位信息、简历信息、招聘公告发布前的合规性审核 |
| SystemController | 处理职位分类、首页轮播图及公告管理、权限管理、日志管理 |

*表5 Package adminController*

### 6.2.5 Package statistcsController
| 类名 | 功能 |
| ---- | ---- |
| PersonalStatController | 为个人用户提供投递数量、面试邀约数量等统计数据 |
| EnterpriseStatController | 为企业用户提供简历接收量等统计数据 |
| PlatformStatController | 为平台管理员提供总职位数、总用户数、总投递量等平台级数据 |

*表6 Package statistcsController*

### 6.2.6 Package messageController
| 类名 | 功能 |
| ---- | ---- |
| NoticeController | 处理系统级和业务级消息的获取、标记已读和删除操作 |

*表7 Package messageController*

## 6.3 业务逻辑层
> 图6 业务逻辑层结构图

### 6.3.1 Package commonService
| 类名 | 功能 |
| ---- | ---- |
| IndexService | 聚合来自职位、公告等Dao的数据，组装成首页所需的数据模型 |
| SearchService | 实现搜索的业务逻辑 |
| LoginService | 处理登录验证、密码加密、与平台统一用户接口的对接，生成并校验Token |

*表8 Package commonService*

### 6.3.2 Package personalService
| 类名 | 功能 |
| ---- | ---- |
| ResumeService | 管理简历的在线创建、编辑、删除、预览、导出 |
| JobService | 实现职位搜索、投递的业务逻辑 |
| DeliveryService | 管理用户的投递记录 |
| PersonalMessageService | 处理个人用户的消息中心 |

*表9 Package personalService*

### 6.3.3 Package enterpriseService
| 类名 | 功能 |
| ---- | ---- |
| CompanyService | 实现企业注册和认证的业务流程 |
| JobManageService | 实现职位发布的业务逻辑 |
| ResumeManageService | 实现接收简历的业务逻辑，处理HR对简历的筛选操作 |
| InterviewService | 实现面试邀约的业务流程 |
| TalentSearchService | 实现人才库的复杂搜索业务逻辑 |

*表10 Package enterpriseService*

### 6.3.4 Package adminService
| 类名 | 功能 |
| ---- | ---- |
| UserManageService | 实现管理员对用户账号的禁用/启用、企业认证审核的业务流程 |
| MessageAuditService | 实现职位、简历、公告的审核业务流程 |
| SystemService | 实现职位分类、轮播图等系统配置项的维护业务逻辑 |

*表11 Package adminService*

### 6.3.5 Package statistcsService
| 类名 | 功能 |
| ---- | ---- |
| PersonalStatService | 为个人用户提供投递数量、面试邀约数量等统计数据 |
| EnterpriseStatService | 为企业用户提供简历接收量等统计数据 |
| PlatformStatService | 为平台管理员提供总职位数、总用户数、总投递量等平台级数据 |

*表12 Package statistcsService*

### 6.3.6 Package messageService
| 类名 | 功能 |
| ---- | ---- |
| NoticeService | 实现消息的生成、持久化，并提供根据用户ID查询消息列表、更新消息状态的服务 |

*表13 Package messageService*

## 6.4 数据服务层
> 图7 数据服务层结构图

### 6.4.1 Package commonDao
| 类名 | 功能 |
| ---- | ---- |
| IndexDao | 首页热门职位、最新职位、推荐职位、轮播图公告的数据查询、更新、删除 |
| SearchDao | 职位关键词检索、多条件筛选、分页查询的数据访问 |
| LoginDao | 用户账号验证、密码查询与更新、Token 相关数据读写 |

*表14 Package commonDao*

### 6.4.2 Package personalDao
| 类名 | 功能 |
| ---- | ---- |
| ResumeDao | 简历信息的新增、编辑、删除、查询、导出数据访问 |
| JobDao | 个人用户职位查询、收藏、浏览记录的数据操作 |
| DeliveryDao | 管理用户的投递记录 |
| PersonalMessageDao | 处理个人用户的消息中心 |

*表15 Package personalDao*

### 6.4.3 Package enterpriseDao
| 类名 | 功能 |
| ---- | ---- |
| CompanyDao | 企业信息新增、编辑、认证状态更新、信息查询 |
| JobManageDao | 职位发布、编辑、刷新、删除、查询的数据访问 |
| ResumeManageDao | 企业接收简历查询 |
| InterviewDao | 面试邀约 |
| TalentSearchDao | 人才库简历多条件检索 |

*表16 Package enterpriseDao*

### 6.4.4 Package adminDao
| 类名 | 功能 |
| ---- | ---- |
| UserManageDao | 个人/企业账号状态更新、信息查询、企业认证审核数据操作 |
| MessageAuditDao | 职位、简历、公告审核结果更新、待审核数据查询 |
| SystemDao | 职位分类增删改查、首页轮播图/公告管理、日志管理、权限配置数据访问 |

*表17 Package adminDao*

### 6.4.5 Package statistcsDao
| 类名 | 功能 |
| ---- | ---- |
| PersonalStatDao | 个人投递数、面试邀约数、收藏数等数据统计查询 |
| EnterpriseStatDao | 企业职位浏览量、简历接收量、面试邀约量统计查询 |
| PlatformStatDao | 平台总职位数、总用户数、总投递量、总认证企业数等全局数据统计 |

*表18 Package statistcsDao*

### 6.4.6 Package messageDao
| 类名 | 功能 |
| ---- | ---- |
| NoticeDao | 消息新增、列表查询、状态标记 |

*表19 Package messageDao*