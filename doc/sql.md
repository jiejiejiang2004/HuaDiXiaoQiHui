



我需要读取 xiaoqihui.sql 文件的内容，分析其中的表结构和字段含义，以便详细解释每个表和字段的内容。


toolName: view_files

status: success


filePath: D:\Project\HuaDi_Temp\xiaoqihui.sql

# xiaoqihui.sql 数据库结构详解

## 1. 公司相关表

### 1.1 company_auth（公司认证表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| auth_id | bigint | 认证ID，自增主键 |
| company_id | bigint | 公司ID，外键关联 company_info.enterprise_id |
| credit_code | varchar(50) | 企业信用代码 |
| legal_person | varchar(50) | 法定代表人 |
| license_file_id | varchar(64) | 营业执照文件ID |
| license_image | varchar(255) | 营业执照图片URL |
| logo_file_id | varchar(64) | LOGO文件ID |
| logo_url | varchar(255) | LOGO图片URL |
| apply_time | datetime | 申请时间，默认当前时间 |
| audit_time | datetime | 审核时间 |
| audit_status | varchar(20) | 审核状态，默认 PENDING |
| audit_remark | varchar(500) | 审核备注 |
| auditor_id | bigint | 审核人ID |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 1.2 company_info（公司信息表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| enterprise_id | bigint | 企业ID，自增主键 |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| company_name | varchar(100) | 公司名称 |
| industry | varchar(50) | 行业 |
| scale | varchar(30) | 公司规模 |
| address | varchar(255) | 公司地址 |
| introduction | text | 公司介绍 |
| website | varchar(255) | 公司网站 |
| logo | varchar(255) | 公司LOGO |
| auth_status | varchar(20) | 认证状态，默认 PASS |
| auth_remark | varchar(500) | 认证备注 |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

## 2. 职位相关表

### 2.1 job_position（职位表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| job_id | bigint | 职位ID，自增主键 |
| enterprise_id | bigint | 企业ID，外键关联 company_info.enterprise_id |
| job_name | varchar(100) | 职位名称 |
| job_category | varchar(50) | 职位分类 |
| responsibility | text | 工作职责 |
| requirement_text | text | 任职要求 |
| salary_min | int | 最低薪资 |
| salary_max | int | 最高薪资 |
| location | varchar(100) | 工作地点 |
| head_count | int | 招聘人数 |
| education | varchar(20) | 学历要求 |
| experience | varchar(20) | 经验要求 |
| welfare | json | 福利待遇，JSON格式 |
| contact_name | varchar(50) | 联系人 |
| contact_mobile | varchar(20) | 联系电话 |
| view_count | int | 浏览次数，默认 0 |
| status | varchar(20) | 职位状态，默认 RECRUITING |
| audit_remark | varchar(500) | 审核备注 |
| publish_time | datetime | 发布时间，默认当前时间 |
| refresh_time | datetime | 刷新时间，默认当前时间 |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 2.2 job_category（职位分类表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| category_id | bigint | 分类ID，自增主键 |
| parent_id | bigint | 父分类ID，默认 0 |
| name | varchar(50) | 分类名称 |
| code | varchar(50) | 分类代码 |
| sort | int | 排序，默认 0 |
| status | varchar(20) | 状态，默认 ACTIVE |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 2.3 job_collection（职位收藏表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| collection_id | bigint | 收藏ID，自增主键 |
| user_id | bigint | 用户ID |
| job_id | bigint | 职位ID |
| create_time | datetime | 创建时间，默认当前时间 |

## 3. 简历相关表

### 3.1 resume（简历表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| resume_id | bigint | 简历ID，自增主键 |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| title | varchar(100) | 简历标题 |
| basic_info | json | 基本信息，JSON格式 |
| job_intention | json | 求职意向，JSON格式 |
| education_list | json | 教育经历，JSON格式 |
| work_list | json | 工作经历，JSON格式 |
| skill_list | json | 技能列表，JSON格式 |
| self_evaluation | text | 自我评价 |
| privacy | varchar(20) | 隐私设置，默认 ENTERPRISE_ONLY |
| is_default | tinyint(1) | 是否默认简历，默认 0 |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 3.2 resume_attachment（简历附件表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| attachment_id | bigint | 附件ID，自增主键 |
| resume_id | bigint | 简历ID，外键关联 resume.resume_id |
| file_id | varchar(64) | 文件ID，外键关联 sys_file_record.file_id |
| file_name | varchar(255) | 文件名 |
| file_url | varchar(255) | 文件URL |
| uploader_id | bigint | 上传者ID |
| create_time | datetime | 创建时间，默认当前时间 |

### 3.3 enterprise_resume_favorite（企业简历收藏表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| favorite_id | bigint | 收藏ID，自增主键 |
| enterprise_id | bigint | 企业ID |
| resume_id | bigint | 简历ID |
| create_time | datetime | 创建时间，默认当前时间 |

## 4. 申请与面试相关表

### 4.1 job_application（职位申请表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| apply_id | bigint | 申请ID，自增主键 |
| job_id | bigint | 职位ID，外键关联 job_position.job_id |
| resume_id | bigint | 简历ID，外键关联 resume.resume_id |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| enterprise_id | bigint | 企业ID，外键关联 company_info.enterprise_id |
| cover_letter | varchar(500) | 求职信 |
| status | varchar(20) | 申请状态，默认 PENDING |
| apply_time | datetime | 申请时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 4.2 enterprise_interview（企业面试表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| interview_id | bigint | 面试ID，自增主键 |
| apply_id | bigint | 申请ID |
| enterprise_id | bigint | 企业ID |
| user_id | bigint | 用户ID |
| resume_id | bigint | 简历ID |
| job_id | bigint | 职位ID |
| interview_time | datetime | 面试时间 |
| interview_type | varchar(20) | 面试类型 |
| interview_place | varchar(255) | 面试地点 |
| interview_link | varchar(255) | 面试链接 |
| contact_name | varchar(50) | 联系人 |
| contact_mobile | varchar(30) | 联系电话 |
| remark | varchar(500) | 备注 |
| status | varchar(20) | 状态，默认 PENDING |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

## 5. 消息相关表

### 5.1 job_message（职位消息表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| message_id | bigint | 消息ID，自增主键 |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| type | varchar(20) | 消息类型 |
| title | varchar(100) | 消息标题 |
| content | text | 消息内容 |
| biz_id | bigint | 业务ID |
| read_status | varchar(20) | 阅读状态，默认 UNREAD |
| create_time | datetime | 创建时间，默认当前时间 |
| read_time | datetime | 阅读时间 |

### 5.2 job_notice（职位公告表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| notice_id | bigint | 公告ID，自增主键 |
| title | varchar(200) | 公告标题 |
| content | text | 公告内容 |
| type | varchar(30) | 公告类型 |
| status | varchar(20) | 状态，默认 PENDING |
| audit_remark | varchar(500) | 审核备注 |
| publish_time | datetime | 发布时间 |
| create_by | bigint | 创建人ID，外键关联 sys_user.user_id |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 5.3 sys_message_template（系统消息模板表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| template_id | bigint | 模板ID，自增主键 |
| type | varchar(50) | 模板类型 |
| title_template | varchar(200) | 标题模板 |
| content_template | text | 内容模板 |
| channels | varchar(100) | 发送渠道，默认 INSITE,EMAIL |
| enabled | varchar(20) | 是否启用，默认 ACTIVE |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

## 6. 系统相关表

### 6.1 sys_user（用户表，文件中未直接显示，但被多个表引用）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| user_id | bigint | 用户ID，自增主键 |
| mobile | varchar(20) | 手机号 |
| password | varchar(100) | 密码 |
| user_type | varchar(20) | 用户类型 |
| real_name | varchar(50) | 真实姓名 |
| status | varchar(20) | 状态 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

### 6.2 sys_file_record（系统文件记录表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| file_id | varchar(64) | 文件ID，主键 |
| biz_type | varchar(30) | 业务类型 |
| original_name | varchar(255) | 原始文件名 |
| file_url | varchar(255) | 文件URL |
| file_path | varchar(255) | 文件路径 |
| file_size | bigint | 文件大小 |
| file_type | varchar(20) | 文件类型 |
| uploader_id | bigint | 上传者ID，外键关联 sys_user.user_id |
| create_time | datetime | 创建时间，默认当前时间 |

### 6.3 sys_audit_log（系统审核日志表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| audit_id | bigint | 审核ID，自增主键 |
| biz_type | varchar(20) | 业务类型 |
| biz_id | bigint | 业务ID |
| before_status | varchar(20) | 审核前状态 |
| after_status | varchar(20) | 审核后状态 |
| audit_result | varchar(20) | 审核结果 |
| audit_remark | varchar(500) | 审核备注 |
| auditor_id | bigint | 审核人ID，外键关联 sys_user.user_id |
| auditor_name | varchar(50) | 审核人姓名 |
| create_time | datetime | 创建时间，默认当前时间 |

### 6.4 sys_banner（系统横幅表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| banner_id | bigint | 横幅ID，自增主键 |
| title | varchar(100) | 横幅标题 |
| image_file_id | varchar(64) | 图片文件ID |
| image_url | varchar(255) | 图片URL |
| link_url | varchar(255) | 链接URL |
| sort | int | 排序，默认 0 |
| status | varchar(20) | 状态，默认 ONLINE |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |
| create_time | datetime | 创建时间，默认当前时间 |
| update_time | datetime | 更新时间，默认当前时间并自动更新 |

### 6.5 sys_export_task（系统导出任务表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| task_id | varchar(64) | 任务ID，主键 |
| task_type | varchar(30) | 任务类型 |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| file_id | varchar(64) | 文件ID |
| download_url | varchar(255) | 下载URL |
| status | varchar(20) | 状态，默认 SUCCESS |
| create_time | datetime | 创建时间，默认当前时间 |

### 6.6 sys_operation_log（系统操作日志表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| log_id | bigint | 日志ID，自增主键 |
| user_id | bigint | 用户ID，外键关联 sys_user.user_id |
| user_name | varchar(50) | 用户名 |
| action | varchar(50) | 操作动作 |
| resource | varchar(100) | 操作资源 |
| detail | text | 操作详情 |
| ip | varchar(64) | 操作IP |
| create_time | datetime | 创建时间，默认当前时间 |

### 6.7 enterprise_job_refresh_log（企业职位刷新日志表）
| 字段名 | 数据类型 | 含义 |
|-------|---------|------|
| log_id | bigint | 日志ID，自增主键 |
| enterprise_id | bigint | 企业ID |
| job_id | bigint | 职位ID |
| refresh_date | date | 刷新日期 |
| create_time | datetime | 创建时间，默认当前时间 |
