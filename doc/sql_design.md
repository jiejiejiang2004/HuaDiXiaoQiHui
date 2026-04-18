# 校企慧公共服务平台数据库设计说明书
**项目编号**: HD20221101SR005
**分 类**: 模板
**Version**: 1.2
**项目承担部门**: 智慧教育研教部
**撰写人（签名）**: 丁盈盈
**完成日期**: 2026-4-10
**本文档使用部门**: ☑项目组 ☑维护人员

---

## 文档信息
|标题|校企慧公共服务平台数据库设计说明书|
|---|---|
|作者|丁盈盈|
|创建日期|2026-04-09|
|上次更新日期|2026-04-10|
|版本|Build1.0|
|部门名称|智慧教育研教部|

---

## 修订文档历史记录
|日期|版本|说明|作者|
|---|---|---|---|
|2026-04-09|0.5|草稿，完成招聘模块数据库设计|丁盈盈|
|2026-04-09|1.0|添加Mermaid ER模型图（概念模型+物理模型）|丁盈盈|
|2026-04-10|1.1|精简表结构：移除sys_dict, sys_config；修复Mermaid物理模型字段类型语法|丁盈盈|
|2026-4-13|1.2|补充sys_role,sys_user_role,sys_banner,sys_audit_log|丁盈盈|
|2026-4-17|1.3|补充sys_permission,sys_role_permission,sys_message_template，并同步邮件验证码与系统管理实现说明|项目组|
|2026-4-17|1.4|补充resume_attachment、enterprise_job_refresh_log，并同步简历附件与职位刷新扩展设计|项目组|
|2026-4-17|1.5|补充job_collection、enterprise_interview，并同步职位收藏与面试邀约扩展设计|项目组|

---

## 目 录
1. 引言
    1.1 编写目的
    1.2 背景
    1.3 定义
    1.4 参考资料
2. 数据库环境说明
3. 逻辑结构设计
    3.1 ER图概述
    3.2 ER模型图
    3.3 实体关系说明
    3.4 实体关系矩阵
4. 物理结构设计
    4.1 表清单
    4.2 表结构设计
5. 数据库关系图
    5.1 概念模型 (Conceptual Model)
    5.2 物理模型 (Physical Model)
6. 数据字典
7. 数据库安全设计

---

# 1. 引言
## 1.1 编写目的
本文档描述了校企慧招聘模块的数据库设计方案，包括逻辑结构设计、物理结构设计、数据字典等内容，为系统开发和维护提供数据层面的技术依据。

## 1.2 背景
校企慧公共服务平台招聘模块旨在搭建校企人才对接桥梁，为高校学生提供就业服务，为企业提供招聘渠道，实现人才供需精准匹配。

## 1.3 定义
|缩写|全称|说明|
|---|---|---|
|DB|Database|数据库|
|DDL|Data Definition Language|数据定义语言|
|ER|Entity-Relationship|实体-关系|
|PK|Primary Key|主键|
|FK|Foreign Key|外键|
|IDX|Index|索引|
|UK|Unique Key|唯一键|

## 1.4 参考资料
《校企慧公共服务平台》软件需求规约

---

# 2. 数据库环境说明
|配置项|说明|
|---|---|
|数据库类型|MySQL 8.0+|
|字符集|utf8mb4|
|排序规则|utf8mb4_unicode_ci|
|存储引擎|InnoDB|
|时区|Asia/Shanghai|

---

# 3. 逻辑结构设计
## 3.1 ER图概述
数据库包含以下核心实体：
- 用户体系：用户表、操作日志表
- 求职者模块：求职者简历表、教育经历表、工作经历表、项目经验表、技能表、求职意向表
- 企业模块：企业信息表、企业认证表、职位分类表、职位信息表
- 招聘流程：简历投递表、面试邀约表
- 平台管理：消息通知表、收藏表

## 3.2 ER模型图
详见5.1，包含：
- 概念模型：业务实体关系，不涉具体实现
- 物理模型：包含完整字段、类型、索引的数据库结构

## 3.3 实体关系说明
- 用户(1) ←→ (N) 求职者简历
- 用户(1) ←→ (N) 企业信息
- 求职者简历(1) ←→ (N) 教育经历
- 求职者简历(1) ←→ (N) 工作经历
- 求职者简历(1) ←→ (N) 项目经验
- 求职者简历(1) ←→ (N) 技能
- 企业信息(1) ←→ (N) 职位信息
- 职位分类(1) ←→ (N) 职位信息
- 求职者简历(N) ←→ 投递 ←→ (N) 职位信息 → 简历投递表
- 企业(N) ←→ 邀约 ←→ (N) 求职者 → 面试邀约表
- 用户(1) ←→ (N) 消息通知
- 用户(1) ←→ (N) 操作日志

## 3.4 实体关系矩阵
|关系类型|主表|从表|关联字段|说明|
|---|---|---|---|---|
|1:1|sys_user|job_seeker|user_id|用户-求职者|
|1:1|sys_user|company_info|user_id|用户-企业|
|1:1|job_application|job_interview|application_id|投递-面试|
|1:N|job_seeker|job_education|seeker_id|求职者-教育经历|
|1:N|job_seeker|job_experience|seeker_id|求职者-工作经历|
|1:N|job_seeker|job_skill|seeker_id|求职者-技能|
|1:N|job_seeker|job_project|seeker_id|求职者-项目|
|1:N|job_seeker|job_intention|seeker_id|求职者-意向|
|1:N|company_info|job_position|company_id|企业-职位|
|1:N|company_info|company_auth|company_id|企业-认证|
|1:N|job_category|job_position|category_id|分类-职位|
|1:N|job_category|job_intention|category_id|分类-意向|
|N:N|job_seeker|job_position|job_application|求职者-职位投递|

---

# 4. 物理结构设计
## 4.1 表清单
|序号|表名|中文名|说明|
|---|---|---|---|
|1|sys_user|用户表|系统用户基础信息|
|2|sys_log|操作日志表|用户操作记录|
|3|job_seeker|求职者简历表|求职者基本信息|
|4|job_education|教育经历表|求职者教育背景|
|5|job_experience|工作经历表|求职者工作经历|
|6|job_project|项目经验表|求职者项目经历|
|7|job_skill|技能表|求职者技能标签|
|8|job_intention|求职意向表|求职者求职意向|
|9|company_info|企业信息表|企业基本信息|
|10|company_auth|企业认证表|企业认证信息|
|11|job_category|职位分类表|行业/职位分类|
|12|job_position|职位信息表|职位发布信息|
|13|job_application|简历投递表|投递记录|
|14|job_interview|面试邀约表|面试安排|
|15|job_message|消息通知表|系统消息|
|16|job_favorite|收藏表|职位/企业收藏|
|17|job_notice|公告信息表|公告内容|
|18|sys_role|角色表|系统角色定义|
|19|sys_user_role|用户角色关联表|用户与角色的关联关系|
|20|sys_banner|轮播图表|轮播图信息|
|21|sys_audit_log|审核日志表|审核行为历史记录|
|22|sys_permission|权限表|后台菜单与权限点定义|
|23|sys_role_permission|角色权限关联表|角色与权限绑定关系|
|24|sys_message_template|消息模板表|站内信/邮件模板维护|
|25|resume_attachment|简历附件表|简历与上传附件的关联关系|
|26|enterprise_job_refresh_log|职位刷新日志表|记录企业职位每日刷新次数|
|27|job_collection|职位收藏表|求职者收藏职位关系|
|28|enterprise_interview|面试邀约表|企业向求职者发起的面试记录|

## 4.2 表结构设计
### 4.2.1 用户表 (sys_user)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|user_id|BIGINT|20|否|自增|主键，用户ID|
|username|VARCHAR|50|否|-|用户名|
|password|VARCHAR|100|否|-|加密密码|
|real_name|VARCHAR|50|是|NULL|真实姓名|
|phone|VARCHAR|20|是|NULL|手机号|
|email|VARCHAR|100|是|NULL|邮箱|
|avatar|VARCHAR|200|是|NULL|头像URL|
|gender|TINYINT|1|是|0|性别：0未知 1男 2女|
|birthday|DATE|-|是|NULL|出生日期|
|user_type|TINYINT|1|否|1|用户类型：1求职者 2企业 3管理员|
|status|TINYINT|1|否|1|状态：0禁用 1启用|
|last_login_time|DATETIME|-|是|NULL|最后登录时间|
|last_login_ip|VARCHAR|50|是|NULL|最后登录IP|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: user_id
- UK: username
- IDX: phone
- IDX: email
- IDX: user_type
- IDX: status

### 4.2.2 操作日志表 (sys_log)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|log_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|是|NULL|操作用户ID|
|username|VARCHAR|50|是|NULL|用户名|
|operation|VARCHAR|100|否|-|操作描述|
|method|VARCHAR|200|是|NULL|请求方法|
|params|TEXT|-|是|NULL|请求参数|
|ip|VARCHAR|50|是|NULL|IP地址|
|status|TINYINT|1|否|1|状态：0失败 1成功|
|error_msg|VARCHAR|2000|是|NULL|错误信息|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: log_id
- IDX: user_id
- IDX: create_time
- IDX: operation

### 4.2.3 公告信息表 (job_notice)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|notice_id|BIGINT|20|否|自增|主键|
|notice_title|VARCHAR|200|否|-|公告标题|
|notice_content|TEXT|-|是|NULL|公告内容|
|notice_type|TINYINT|1|否|1|类型：1通知 2公告|
|target_type|TINYINT|1|否|0|目标：0全部 1求职者 2企业|
|status|TINYINT|1|否|0|状态：0草稿 1已发布 2已下架|
|publish_time|DATETIME|-|是|NULL|发布时间|
|sort|INT|11|否|0|排序|
|create_by|BIGINT|20|是|NULL|创建人|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: notice_id
- IDX: status + sort
- IDX: target_type + status

### 4.2.4 求职者简历表 (job_seeker)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|seeker_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|否|-|关联用户ID|
|real_name|VARCHAR|50|否|-|真实姓名|
|gender|TINYINT|1|是|0|性别|
|phone|VARCHAR|20|否|-|联系电话|
|email|VARCHAR|100|是|NULL|邮箱|
|id_card|VARCHAR|18|是|NULL|身份证号(加密)|
|birth_date|DATE|-|是|NULL|出生日期|
|residence|VARCHAR|200|是|NULL|现居地|
|hometown|VARCHAR|200|是|NULL|籍贯|
|political_status|VARCHAR|20|是|NULL|政治面貌|
|marital_status|TINYINT|1|是|0|婚姻状况|
|self_intro|TEXT|-|是|NULL|个人介绍|
|job_status|TINYINT|1|否|1|求职状态：1在职 2离职 3在校|
|work_years|DECIMAL|3,1|是|NULL|工作年限|
|expected_city|VARCHAR|100|是|NULL|期望城市|
|expected_salary_min|DECIMAL|10,2|是|NULL|期望薪资下限|
|expected_salary_max|DECIMAL|10,2|是|NULL|期望薪资上限|
|job_type|TINYINT|1|是|1|工作性质：1全职 2兼职 3实习|
|resume_file|VARCHAR|200|是|NULL|附件简历URL|
|is_public|TINYINT|1|否|0|是否公开：0否 1是|
|view_count|INT|11|否|0|浏览次数|
|completeness|INT|3|否|0|简历完整度(%)|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: seeker_id
- UK: user_id
- IDX: is_public
- IDX: expected_city
- IDX: job_status

### 4.2.5 教育经历表 (job_education)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|edu_id|BIGINT|20|否|自增|主键|
|seeker_id|BIGINT|20|否|-|求职者ID|
|school_name|VARCHAR|100|否|-|学校名称|
|major|VARCHAR|50|否|-|专业|
|degree|TINYINT|1|否|-|学历：1高中 2大专 3本科 4硕士 5博士|
|edu_type|TINYINT|1|是|1|教育类型：1统招 2自考 3成教|
|start_date|DATE|-|否|-|开始日期|
|end_date|DATE|-|是|NULL|结束日期|
|is_graduate|TINYINT|1|否|1|是否毕业：0在读 1已毕业|
|description|VARCHAR|500|是|NULL|在校经历描述|
|sort|INT|11|否|0|排序|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: edu_id
- FK: seeker_id → job_seeker(seeker_id)
- IDX: seeker_id + sort

### 4.2.6 工作经历表 (job_experience)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|exp_id|BIGINT|20|否|自增|主键|
|seeker_id|BIGINT|20|否|-|求职者ID|
|company_name|VARCHAR|100|否|-|公司名称|
|position|VARCHAR|50|否|-|职位名称|
|department|VARCHAR|50|是|NULL|部门|
|industry|VARCHAR|50|是|NULL|所属行业|
|start_date|DATE|-|否|-|开始日期|
|end_date|DATE|-|是|NULL|结束日期|
|is_current|TINYINT|1|否|0|是否在职：0否 1是|
|work_desc|TEXT|-|是|NULL|工作描述|
|achievement|TEXT|-|是|NULL|工作业绩|
|salary|DECIMAL|10,2|是|NULL|薪资(元/月)|
|sort|INT|11|否|0|排序|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: exp_id
- FK: seeker_id → job_seeker(seeker_id)
- IDX: seeker_id + sort

### 4.2.7 项目经验表 (job_project)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|project_id|BIGINT|20|否|自增|主键|
|seeker_id|BIGINT|20|否|-|求职者ID|
|project_name|VARCHAR|100|否|-|项目名称|
|role|VARCHAR|50|否|-|担任角色|
|start_date|DATE|-|否|-|开始日期|
|end_date|DATE|-|是|NULL|结束日期|
|project_desc|TEXT|-|是|NULL|项目描述|
|responsibility|TEXT|-|是|NULL|项目职责|
|project_link|VARCHAR|200|是|NULL|项目链接|
|sort|INT|11|否|0|排序|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: project_id
- FK: seeker_id → job_seeker(seeker_id)

### 4.2.8 技能表 (job_skill)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|skill_id|BIGINT|20|否|自增|主键|
|seeker_id|BIGINT|20|否|-|求职者ID|
|skill_name|VARCHAR|50|否|-|技能名称|
|proficiency|TINYINT|1|否|3|熟练度：1入门 2熟悉 3掌握 4精通 5专家|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: skill_id
- FK: seeker_id → job_seeker(seeker_id)
- IDX: skill_name

### 4.2.9 求职意向表 (job_intention)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|intention_id|BIGINT|20|否|自增|主键|
|seeker_id|BIGINT|20|否|-|求职者ID|
|category_id|BIGINT|20|否|-|职位分类ID|
|position_name|VARCHAR|50|否|-|意向职位名称|
|industry|VARCHAR|50|是|NULL|意向行业|
|city|VARCHAR|50|是|NULL|意向城市|
|salary_min|DECIMAL|10,2|是|NULL|期望薪资下限|
|salary_max|DECIMAL|10,2|是|NULL|期望薪资上限|
|job_type|TINYINT|1|否|1|工作性质|
|is_primary|TINYINT|1|否|0|是否主要意向|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: intention_id
- FK: seeker_id → job_seeker(seeker_id)
- FK: category_id → job_category(category_id)

### 4.2.10 企业信息表 (company_info)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|company_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|否|-|关联用户ID|
|company_name|VARCHAR|100|否|-|企业名称|
|short_name|VARCHAR|50|是|NULL|企业简称|
|company_logo|VARCHAR|200|是|NULL|企业Logo|
|company_cover|VARCHAR|200|是|NULL|企业封面图|
|industry|VARCHAR|50|是|NULL|所属行业|
|company_scale|TINYINT|1|是|NULL|企业规模：1-49人 2 50-149人 3 150-499人 4 500-999人 5 1000人以上|
|company_type|TINYINT|1|是|NULL|企业性质：1民营 2国企 3外企 4合资 5上市公司 6事业单位 7其他|
|found_date|DATE|-|是|NULL|成立日期|
|registered_capital|DECIMAL|15,2|是|NULL|注册资本(万元)|
|business_license|VARCHAR|100|是|NULL|营业执照号|
|license_image|VARCHAR|200|是|NULL|营业执照图片|
|website|VARCHAR|100|是|NULL|企业官网|
|province|VARCHAR|50|是|NULL|省|
|city|VARCHAR|50|是|NULL|市|
|district|VARCHAR|50|是|NULL|区/县|
|address|VARCHAR|200|是|NULL|详细地址|
|longitude|DECIMAL|10,7|是|NULL|经度|
|latitude|DECIMAL|10,7|是|NULL|纬度|
|contact_name|VARCHAR|50|是|NULL|联系人|
|contact_phone|VARCHAR|20|是|NULL|联系人电话|
|contact_email|VARCHAR|100|是|NULL|联系人邮箱|
|company_intro|TEXT|-|是|NULL|企业介绍|
|company_welfare|VARCHAR|500|是|NULL|企业福利(逗号分隔)|
|work_time|VARCHAR|100|是|NULL|工作时间|
|work_address|VARCHAR|200|是|NULL|办公地址|
|auth_status|TINYINT|1|否|0|认证状态：0未认证 1审核中 2已认证 3认证失败|
|auth_time|DATETIME|-|是|NULL|认证通过时间|
|view_count|INT|11|否|0|浏览次数|
|position_count|INT|11|否|0|在招职位数|
|status|TINYINT|1|否|1|状态：0禁用 1启用|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: company_id
- UK: user_id
- IDX: company_name
- IDX: industry
- IDX: city
- IDX: auth_status + status

### 4.2.11 企业认证表 (company_auth)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|auth_id|BIGINT|20|否|自增|主键|
|company_id|BIGINT|20|否|-|企业ID|
|auth_type|TINYINT|1|否|1|认证类型：1企业认证|
|legal_person|VARCHAR|50|是|NULL|法人姓名|
|id_card|VARCHAR|18|是|NULL|法人身份证号|
|id_card_front|VARCHAR|200|是|NULL|身份证正面|
|id_card_back|VARCHAR|200|是|NULL|身份证反面|
|business_license|VARCHAR|100|是|NULL|营业执照号|
|license_image|VARCHAR|200|是|NULL|营业执照图片|
|apply_time|DATETIME|-|否|CURRENT_TIMESTAMP|申请时间|
|audit_time|DATETIME|-|是|NULL|审核时间|
|audit_status|TINYINT|1|否|0|审核状态：0待审核 1通过 2驳回|
|audit_remark|VARCHAR|500|是|NULL|审核备注|
|auditor_id|BIGINT|20|是|NULL|审核人ID|

**索引**：
- PK: auth_id
- FK: company_id → company_info(company_id)
- IDX: company_id + audit_status

### 4.2.12 职位分类表 (job_category)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|category_id|BIGINT|20|否|自增|主键|
|parent_id|BIGINT|20|否|0|父分类ID，0为顶级|
|category_name|VARCHAR|50|否|-|分类名称|
|category_code|VARCHAR|50|否|-|分类编码|
|category_type|TINYINT|1|否|1|类型：1行业 2职位|
|category_level|TINYINT|1|否|1|层级：1一级 2二级 3三级|
|category_sort|INT|11|否|0|排序|
|icon|VARCHAR|100|是|NULL|图标|
|status|TINYINT|1|否|1|状态|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: category_id
- IDX: parent_id
- IDX: category_type + status

### 4.2.13 职位信息表 (job_position)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|position_id|BIGINT|20|否|自增|主键|
|company_id|BIGINT|20|否|-|企业ID|
|category_id|BIGINT|20|是|NULL|职位分类ID|
|position_name|VARCHAR|100|否|-|职位名称|
|position_type|TINYINT|1|否|1|职位类型：1全职 2兼职 3实习 4校招|
|recruit_num|INT|5|是|1|招聘人数|
|salary_min|DECIMAL|10,2|是|NULL|薪资下限|
|salary_max|DECIMAL|10,2|是|NULL|薪资上限|
|salary_negotiable|TINYINT|1|否|0|是否面议：0否 1是|
|city|VARCHAR|50|否|-|工作城市|
|district|VARCHAR|50|是|NULL|工作区域|
|address|VARCHAR|200|是|NULL|详细地址|
|experience_min|INT|2|是|0|经验要求下限(年)|
|experience_max|INT|2|是|NULL|经验要求上限(年)|
|education|TINYINT|1|是|0|学历要求：0不限 1高中 2大专 3本科 4硕士 5博士|
|job_desc|TEXT|-|是|NULL|职位描述|
|job_requirement|TEXT|-|是|NULL|任职要求|
|job_welfare|VARCHAR|500|是|NULL|职位福利|
|skill_required|VARCHAR|500|是|NULL|技能要求(逗号分隔)|
|keywords|VARCHAR|200|是|NULL|搜索关键词|
|contact_name|VARCHAR|50|是|NULL|联系人|
|contact_phone|VARCHAR|20|是|NULL|联系人电话|
|contact_email|VARCHAR|100|是|NULL|联系人邮箱|
|view_count|INT|11|否|0|浏览次数|
|apply_count|INT|11|否|0|投递次数|
|status|TINYINT|1|否|0|状态：0待审核 1招聘中 2已暂停 3已结束 4审核驳回|
|is_urgent|TINYINT|1|否|0|是否急招|
|is_recommend|TINYINT|1|否|0|是否推荐|
|publish_time|DATETIME|-|是|NULL|发布时间|
|refresh_time|DATETIME|-|是|NULL|刷新时间|
|end_time|DATETIME|-|是|NULL|截止时间|
|create_by|BIGINT|20|是|NULL|创建人|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|
|audit_time|DATETIME|-|是|NULL|审核时间|
|audit_remark|VARCHAR|500|是|NULL|审核备注|

**索引**：
- PK: position_id
- FK: company_id → company_info(company_id)
- FK: category_id → job_category(category_id)
- IDX: company_id + status
- IDX: status + is_recommend + publish_time
- IDX: city + category_id + status
- IDX: position_name (FULLTEXT)
- IDX: keywords (FULLTEXT)

### 4.2.14 简历投递表 (job_application)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|application_id|BIGINT|20|否|自增|主键|
|position_id|BIGINT|20|否|-|职位ID|
|company_id|BIGINT|20|否|-|企业ID|
|seeker_id|BIGINT|20|否|-|求职者ID|
|resume_id|BIGINT|20|否|-|简历ID|
|apply_status|TINYINT|1|否|1|投递状态：1已投递 2已查看 3感兴趣 4不合适 5已邀约 6已录用 7已拒绝|
|apply_time|DATETIME|-|否|CURRENT_TIMESTAMP|投递时间|
|view_time|DATETIME|-|是|NULL|查看时间|
|handle_time|DATETIME|-|是|NULL|处理时间|
|handle_remark|VARCHAR|500|是|NULL|处理备注|
|source|TINYINT|1|否|1|来源：1主动投递 2企业邀请|

**索引**：
- PK: application_id
- FK: position_id → job_position(position_id)
- FK: company_id → company_info(company_id)
- FK: seeker_id → job_seeker(seeker_id)
- UK: position_id + seeker_id (唯一约束)
- IDX: seeker_id + apply_status
- IDX: company_id + apply_status
- IDX: apply_time

### 4.2.15 面试邀约表 (job_interview)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|interview_id|BIGINT|20|否|自增|主键|
|application_id|BIGINT|20|否|-|投递记录ID|
|position_id|BIGINT|20|否|-|职位ID|
|company_id|BIGINT|20|否|-|企业ID|
|seeker_id|BIGINT|20|否|-|求职者ID|
|interview_type|TINYINT|1|否|1|面试类型：1现场 2视频 3电话|
|interview_date|DATE|-|否|-|面试日期|
|interview_time|TIME|-|是|NULL|面试时间|
|interview_address|VARCHAR|200|是|NULL|面试地点|
|contact_name|VARCHAR|50|是|NULL|联系人|
|contact_phone|VARCHAR|20|是|NULL|联系人电话|
|remark|VARCHAR|500|是|NULL|备注|
|status|TINYINT|1|否|1|状态：1待确认 2已接受 3已拒绝 4已完成 5已取消|
|reply_content|VARCHAR|500|是|NULL|回复内容|
|invite_time|DATETIME|-|否|CURRENT_TIMESTAMP|邀约时间|
|reply_time|DATETIME|-|是|NULL|回复时间|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: interview_id
- FK: application_id → job_application(application_id)
- FK: position_id → job_position(position_id)
- FK: company_id → company_info(company_id)
- FK: seeker_id → job_seeker(seeker_id)
- IDX: seeker_id + status
- IDX: company_id + status
- IDX: interview_date

### 4.2.16 消息通知表 (job_message)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|message_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|否|-|接收用户ID|
|sender_id|BIGINT|20|是|NULL|发送者ID，NULL为系统|
|sender_type|TINYINT|1|否|0|发送者类型：0系统 1企业 2管理员|
|message_type|TINYINT|1|否|1|消息类型：1系统 2投递反馈 3面试邀请 4账号通知|
|message_title|VARCHAR|100|否|-|消息标题|
|message_content|TEXT|-|是|NULL|消息内容|
|related_id|BIGINT|20|是|NULL|关联业务ID|
|related_type|TINYINT|1|是|NULL|关联类型：1职位 2投递 3面试 4企业|
|is_read|TINYINT|1|否|0|是否已读|
|read_time|DATETIME|-|是|NULL|阅读时间|
|is_push|TINYINT|1|否|0|是否推送|
|push_time|DATETIME|-|是|NULL|推送时间|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: message_id
- FK: user_id → sys_user(user_id)
- IDX: user_id + is_read
- IDX: create_time
- IDX: message_type

### 4.2.17 收藏表 (job_favorite)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|favorite_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|否|-|用户ID|
|target_id|BIGINT|20|否|-|目标ID|
|target_type|TINYINT|1|否|1|类型：1职位 2企业|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|收藏时间|

**索引**：
- PK: favorite_id
- FK: user_id → sys_user(user_id)
- UK: user_id + target_id + target_type

### 4.2.18 角色表 (sys_role)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|role_id|BIGINT|20|否|自增|主键|
|role_name|VARCHAR|50|否|-|角色名称|
|role_key|VARCHAR|50|否|-|角色标识（如admin）|
|role_sort|INT|11|否|0|显示顺序，默认0|
|status|TINYINT|1|否|0|状态：0禁用 1启用|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: role_id
- UK: role_name
- IDX: role_key

### 4.2.19 用户角色关联表 (sys_user_role)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|user_id|BIGINT|20|否|-|用户ID，关联 sys_user|
|role_id|BIGINT|20|否|-|角色ID，关联 sys_role|

**索引**：
- PK: user_id, role_id
- FK: user_id -> sys_user(user_id), role_id -> sys_role(role_id)

### 4.2.20 轮播图表 (sys_banner)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|banner_id|BIGINT|20|否|自增|主键|
|banner_title|VARCHAR|100|是|-|标题|
|banner_img|VARCHAR|200|否|-|图片URL|
|banner_link|VARCHAR|200|是|-|跳转链接|
|banner_sort|INT|11|否|0|排序|
|status|TINYINT|1|否|0|状态：0禁用 1启用|
|start_time|DATETIME|-|是|-|展示开始时间|
|end_time|DATETIME|-|是|-|展示结束时间|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: banner_id
- IDX: banner_sort

### 4.2.21 审核日志表 (sys_audit_log)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|audit_id|BIGINT|20|否|自增|主键|
|biz_type|TINYINT|1|否|1|业务类型：1企业认证 2职位审核 3求职者实名|
|biz_id|BIGINT|20|否|-|业务记录ID|
|before_status|TINYINT|1|是|-|审核前状态|
|after_status|TINYINT|1|否|-|审核后状态|
|audit_result|TINYINT|1|否|1|审核结果：1通过 2驳回|
|audit_remark|VARCHAR|500|是|-|审核意见|
|auditor_id|BIGINT|20|否|-|审核人ID，关联sys_user|
|auditor_name|VARCHAR|50|是|-|审核人姓名|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|审核时间|

**索引**：
- PK: audit_id
- IDX: biz_type
- IDX: biz_id
- FK: auditor_id -> sys_user(user_id)

### 4.2.22 权限表 (sys_permission)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|permission_id|BIGINT|20|否|自增|主键|
|permission_name|VARCHAR|100|否|-|权限名称|
|permission_code|VARCHAR|100|否|-|权限编码|
|menu_key|VARCHAR|100|是|NULL|对应前端菜单标识|
|description|VARCHAR|255|是|NULL|权限说明|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: permission_id
- UK: permission_code
- IDX: menu_key

### 4.2.23 角色权限关联表 (sys_role_permission)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|role_id|BIGINT|20|否|-|角色ID|
|permission_id|BIGINT|20|否|-|权限ID|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: role_id + permission_id
- FK: role_id -> sys_role(role_id)
- FK: permission_id -> sys_permission(permission_id)

### 4.2.24 消息模板表 (sys_message_template)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|template_id|BIGINT|20|否|自增|主键|
|type|VARCHAR|50|否|-|模板类型|
|title_template|VARCHAR|200|否|-|标题模板|
|content_template|TEXT|-|否|-|内容模板|
|channels|VARCHAR|100|否|INSITE,EMAIL|默认发送通道|
|enabled|VARCHAR|20|否|ACTIVE|状态|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: template_id
- UK: type

### 4.2.25 简历附件表 (resume_attachment)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|attachment_id|BIGINT|20|否|自增|主键|
|resume_id|BIGINT|20|否|-|简历ID|
|file_id|VARCHAR|64|否|-|文件ID|
|file_name|VARCHAR|255|否|-|附件名称|
|file_url|VARCHAR|255|否|-|附件访问地址|
|uploader_id|BIGINT|20|否|-|上传人|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: attachment_id
- FK: resume_id -> resume(resume_id)
- FK: file_id -> sys_file_record(file_id)

### 4.2.26 职位刷新日志表 (enterprise_job_refresh_log)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|log_id|BIGINT|20|否|自增|主键|
|enterprise_id|BIGINT|20|否|-|企业ID|
|job_id|BIGINT|20|否|-|职位ID|
|refresh_date|DATE|-|否|-|刷新日期|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: log_id
- IDX: job_id + refresh_date
- IDX: enterprise_id + refresh_date

### 4.2.27 职位收藏表 (job_collection)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|collection_id|BIGINT|20|否|自增|主键|
|user_id|BIGINT|20|否|-|求职者ID|
|job_id|BIGINT|20|否|-|职位ID|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|

**索引**：
- PK: collection_id
- UK: user_id + job_id
- IDX: user_id
- IDX: job_id

### 4.2.28 面试邀约表 (enterprise_interview)
|字段名|数据类型|长度|是否为空|默认值|说明|
|---|---|---|---|---|---|
|interview_id|BIGINT|20|否|自增|主键|
|apply_id|BIGINT|20|否|-|投递ID|
|enterprise_id|BIGINT|20|否|-|企业ID|
|user_id|BIGINT|20|否|-|求职者ID|
|resume_id|BIGINT|20|否|-|简历ID|
|job_id|BIGINT|20|否|-|职位ID|
|interview_time|DATETIME|-|否|-|面试时间|
|interview_type|VARCHAR|20|否|-|ONLINE/OFFLINE|
|interview_place|VARCHAR|255|是|NULL|线下面试地点|
|interview_link|VARCHAR|255|是|NULL|线上会议链接|
|contact_name|VARCHAR|50|否|-|联系人|
|contact_mobile|VARCHAR|30|否|-|联系电话|
|remark|VARCHAR|500|是|NULL|备注|
|status|VARCHAR|20|否|PENDING|邀约状态|
|create_time|DATETIME|-|否|CURRENT_TIMESTAMP|创建时间|
|update_time|DATETIME|-|否|CURRENT_TIMESTAMP|更新时间|

**索引**：
- PK: interview_id
- IDX: enterprise_id
- IDX: user_id
- IDX: apply_id

---

# 5. 数据库关系图
## 5.1 概念模型 (Conceptual Model)
展示业务实体及其关系，不包含具体数据库实现细节

## 5.2 物理模型 (Physical Model)
按模块划分：用户权限、求职者、企业、招聘流程、平台功能
包含完整字段定义、数据类型、约束条件
标注主键(PK)、外键(FK)、唯一键(UK)、索引(IDX)、全文索引(FT)

### 5.2.1 用户权限模块
### 5.2.2 求职者模块
### 5.2.3 企业模块
### 5.2.4 招聘流程模块
### 5.2.5 平台功能模块

---

# 6. 数据字典
## 6.1 通用状态定义
|字段名|值|含义|
|---|---|---|
|status|0|禁用/否/失败|
|status|1|启用/是/成功|
|user_type|1|求职者|
|user_type|2|企业用户|
|user_type|3|平台管理员|
|gender|0|未知|
|gender|1|男|
|gender|2|女|

## 6.2 学历定义
|值|含义|
|---|---|
|0|不限|
|1|高中/中专|
|2|大专|
|3|本科|
|4|硕士|
|5|博士|

## 6.3 投递状态定义
|值|含义|
|---|---|
|1|已投递|
|2|已查看|
|3|感兴趣|
|4|不合适|
|5|已邀约|
|6|已录用|
|7|已拒绝|

## 6.4 认证状态定义
|值|含义|
|---|---|
|0|未认证/待审核|
|1|审核中|
|2|已通过/已认证|
|3|认证失败/驳回|

## 6.5 职位状态定义
|值|含义|
|---|---|
|0|待审核|
|1|招聘中|
|2|已暂停|
|3|已结束|
|4|审核驳回|

## 6.6 面试类型定义
|值|含义|
|---|---|
|1|现场面试|
|2|视频面试|
|3|电话面试|

## 6.7 面试状态定义
|值|含义|
|---|---|
|1|待确认|
|2|已接受|
|3|已拒绝|
|4|已完成|
|5|已取消|

---

# 7. 数据库安全设计
## 7.1 敏感信息加密
|信息类型|加密方式|说明|
|---|---|---|
|用户密码|BCrypt|单向哈希，强度10|
|身份证号|AES-256|对称加密，密钥存储在配置中心|
|手机号|部分脱敏|显示为 138xxxx8888|

## 7.2 数据访问控制
1. 行级权限：用户只能访问自己的数据
2. 列级脱敏：敏感字段按需脱敏展示
3. 操作审计：关键操作记录到sys_log表

## 7.3 SQL注入防护
1. 使用预编译语句(PreparedStatement)
2. 输入参数校验和过滤
3. Mybatis参数化查询
