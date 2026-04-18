# 校企慧公共服务平台招聘模块 API 接口文档
**版本**：V1.0
**项目编号**：HD20221101SR005
**编制日期**：2026-04-17
**编制部门**：智慧教育研教部

---

## 1 文档概述
### 1.1 文档目的
本文档是《校企慧公共服务平台-招聘模块软件需求规约》(V2.0)的配套接口文档，旨在规定招聘模块前端、后端、第三方系统之间的数据交互规范，作为前后端联调、系统集成测试、外部接入的技术依据。

### 1.2 适用范围
本文档适用于郫都区校企慧公共服务平台招聘模块的前端开发、后端开发、测试、集成、运维人员使用。

### 1.3 术语与缩略语
| 术语/缩写 | 说明 |
| -------- | ---- |
| 校企慧 | 郫都区校企人力资源合作暨高技能人才培训联盟工作平台 |
| XQH-ZP | 校企慧公共服务平台-招聘模块 |
| SRS | 软件需求规约(Software Requirements Specification) |
| JWT | JSON Web Token，用户身份鉴权令牌 |
| RESTful | 一种基于HTTP的Web服务架构风格 |
| HR | Human Resource，人力资源 |
| OSS | 对象存储服务(文件存储接口) |

### 1.4 参考文档
- 《校企慧公共服务平台-招聘模块软件需求规约》V2.0
- 《校企慧招聘模块数据库设计说明书》
- 《校企慧平台统一用户中心接口规范》
- 《校企慧平台统一权限体系规范》

---

## 2 通用约定
### 2.1 接口基础信息
| 项目 | 说明 |
| ---- | ---- |
| 协议 | HTTPS |
| 服务域名(开发) | https://dev-api.xqh.pidu.gov.cn |
| 服务域名(测试) | https://test-api.xqh.pidu.gov.cn |
| 服务域名(生产) | https://api.xqh.pidu.gov.cn |
| 接口前缀 | /recruit/api/v1 |
| 数据格式 | JSON(UTF-8编码) |
| 接口风格 | RESTful |
| 字符编码 | UTF-8 |
| 时间格式 | yyyy-MM-dd HH:mm:ss(UTC+8) |

### 2.2 请求规范
#### 2.2.1 HTTP 方法
| 方法 | 用途 |
| ---- | ---- |
| GET | 查询资源，参数通过Query String传递 |
| POST | 新增资源或复杂查询，参数通过Body传递(JSON) |
| PUT | 全量更新资源 |
| PATCH | 部分更新资源 |
| DELETE | 删除资源 |

#### 2.2.2 通用请求头
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| Content-Type | string | 是 | application/json;charset=UTF-8 |
| Authorization | string | 否 | Bearer {token}，需要登录的接口必传 |
| X-Device-Type | string | 否 | 设备类型：PC / H5 / IOS / ANDROID |
| X-Request-Id | string | 否 | 请求唯一ID，用于链路追踪 |
| Accept-Language | string | 否 | zh-CN(默认) / en-US |

### 2.3 响应规范
#### 2.3.1 统一响应结构
```json
{
  "code": 0,
  "message": "success",
  "data": { ... },
  "timestamp": 1745049600000
}
```

#### 2.3.2 响应字段说明
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| code | int | 是 | 状态码，0表示成功，非0表示失败 |
| message | string | 是 | 提示信息 |
| data | object | 否 | 返回数据，具体结构见各接口说明 |
| timestamp | long | 是 | 服务端时间戳(毫秒) |

#### 2.3.3 业务状态码
| 状态码 | 含义 |
| ------ | ---- |
| 0 | 成功 |
| 1001 | 参数校验失败 |
| 1002 | 请求参数缺失 |
| 2001 | 用户未登录或Token失效 |
| 2002 | 权限不足 |
| 2003 | 账号已被禁用 |
| 3001 | 用户不存在 |
| 3002 | 密码错误 |
| 3003 | 手机号已注册 |
| 3004 | 验证码错误或已过期 |
| 4001 | 简历不存在 |
| 4002 | 简历信息不完整 |
| 5001 | 职位不存在 |
| 5002 | 职位已下架 |
| 5003 | 重复投递 |
| 6001 | 企业未认证 |
| 6002 | 企业认证待审核 |
| 6003 | 企业认证已被驳回 |
| 7001 | 文件上传失败 |
| 7002 | 文件格式不支持 |
| 7003 | 文件大小超限 |
| 9001 | 系统内部错误 |
| 9002 | 数据库操作异常 |
| 9999 | 未知错误 |

### 2.4 分页规范
#### 请求参数
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| pageNum | int | 否 | 页码，从1开始，默认1 |
| pageSize | int | 否 | 每页数量，默认10，最大100 |
| orderBy | string | 否 | 排序字段，如 createTime |
| orderType | string | 否 | 排序方式：asc / desc，默认desc |

#### 响应数据结构
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 128,
    "totalPages": 13,
    "list": [ ... ]
  },
  "timestamp": 1745049600000
}
```

### 2.5 鉴权说明
- 用户登录成功后，服务端下发 accessToken(有效期2小时)和 refreshToken(有效期7天)
- 需登录的接口必须在请求头中携带 Authorization: Bearer {accessToken}
- accessToken 过期时，使用 refreshToken 调用刷新接口获取新令牌
- refreshToken 过期时，需引导用户重新登录
- 服务端基于角色(求职者/企业/管理员)+ 资源权限进行访问控制

### 2.6 文件上传规范
| 项目 | 说明 |
| ---- | ---- |
| 上传方式 | multipart/form-data |
| 简历附件 | 支持 PDF、DOC、DOCX，单个文件 ≤ 10MB |
| 图片文件 | 支持 JPG、JPEG、PNG，单个文件 ≤ 5MB |
| 营业执照 | 支持 JPG、JPEG、PNG、PDF，单个文件 ≤ 10MB |
| 存储方式 | 对接平台统一文件存储(OSS)，返回可访问URL |

---

## 3 接口模块总览
| 编号 | 模块 | 对应用户角色 | 章节 |
| ---- | ---- | ------------ | ---- |
| A | 通用接口(首页/公共) | 全部 | 第4章 |
| B | 求职者-账号与认证 | 个人求职者 | 5.1 |
| C | 求职者-简历管理 | 个人求职者 | 5.2 |
| D | 求职者-职位操作 | 个人求职者 | 5.3 |
| E | 求职者-消息中心 | 个人求职者 | 5.4 |
| F | 企业-认证与登录 | 企业用户 | 6.1 |
| G | 企业-职位管理 | 企业用户 | 6.2 |
| H | 企业-简历管理 | 企业用户 | 6.3 |
| I | 企业-人才搜索 | 企业用户 | 6.4 |
| J | 管理员-用户管理 | 平台管理员 | 7.1 |
| K | 管理员-信息审核 | 平台管理员 | 7.2 |
| L | 管理员-系统管理 | 平台管理员 | 7.3 |
| M | 消息通知 | 全部 | 第8章 |
| N | 统计分析 | 按角色 | 第9章 |
| O | 外部接口 | 系统集成 | 第10章 |

---

## 4 通用接口(首页/公共)
本章节接口用于招聘模块首页展示与通用公共能力，大部分无需登录即可访问。

### 4.2 职位搜索(首页搜索)
支持按职位名称、专业、企业名称、学历要求等关键词进行职位搜索，并可按多条件筛选。
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/home/jobs/search |
| 鉴权要求 | 无需登录(登录后可获取个性化结果) |
| Content-Type | application/json |

#### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| keyword | string | 否 | 搜索关键词(职位名称/专业/企业名称) |
| industry | string | 否 | 行业 |
| education | string | 否 | 学历要求 |
| salaryMin | int | 否 | 最低薪资 |
| salaryMax | int | 否 | 最高薪资 |
| location | string | 否 | 工作地点 |
| experience | string | 否 | 工作经验，如：应届/1-3年/3-5年 |
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认10 |

#### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 职位列表(职位简要信息) |
| total | long | 是 | 总记录数 |
| pageNum | int | 是 | 当前页码 |
| pageSize | int | 是 | 每页数量 |

### 4.3 获取字典数据
获取行业、学历、薪资区间、工作经验、职位分类等字典数据，用于下拉框展示。
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/common/dict/{dictType} |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dictType | string | 是 | 字典类型：industry/education/salary/experience/jobCategory |

#### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dictType | string | 是 | 字典类型 |
| items | array | 是 | 字典项列表，元素包含 code、name、sort |

### 4.4 发送邮箱验证码
用于注册、登录、找回密码、敏感操作的邮箱验证码发送。
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/common/email/send |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

#### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| email | string | 是 | 邮箱地址 |
| scene | string | 是 | 场景：REGISTER/LOGIN/RESET_PWD/BIND_MOBILE |
| captcha | string | 是 | 图形验证码(防刷) |
| captchaKey | string | 是 | 图形验证码Key |

#### 请求示例
```json
{
  "email": "zhangsan@example.com",
  "scene": "REGISTER",
  "captcha": "A3F9",
  "captchaKey": "cap_20260417_8a9f0b1c"
}
```

#### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| expireSeconds | int | 是 | 验证码有效期(秒)，默认300 |

**说明**：每个邮箱60秒内只能发送1次，同一场景每天最多发送10次。

### 4.5 文件上传
统一文件上传接口，返回可访问URL。
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/common/file/upload |
| 鉴权要求 | 需要登录 |
| Content-Type | application/json |

#### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| file | file | 是 | 文件(multipart/form-data) |
| bizType | string | 是 | 业务类型：RESUME/LOGO/LICENSE/AVATAR/BANNER |

#### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| fileId | string | 是 | 文件唯一ID |
| fileName | string | 是 | 原始文件名 |
| fileUrl | string | 是 | 可访问URL |
| fileSize | long | 是 | 文件大小(字节) |
| fileType | string | 是 | 文件类型(扩展名) |

---

## 5 求职者端接口
本章节接口供个人求职者(学生/社会求职者)使用，涵盖注册登录、简历管理、职位操作、消息中心四大功能域。

### 5.1 求职者账号与认证
#### 5.1.1 求职者注册
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/register |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| mobile | string | 是 | 手机号 |
| email | string | 是 | 登录邮箱 |
| emailCode | string | 是 | 邮箱验证码 |
| password | string | 是 | 密码，8-20位，需含字母+数字 |
| identity | string | 是 | 身份：STUDENT(学生) / SOCIAL(社会求职者) |
| name | string | 是 | 真实姓名 |
| agreeProtocol | boolean | 是 | 是否同意用户协议，必须为true |

##### 请求示例
```json
{
  "mobile": "13812345678",
  "email": "zhangsan@example.com",
  "emailCode": "123456",
  "password": "Abc@123456",
  "identity": "STUDENT",
  "name": "张三",
  "agreeProtocol": true
}
```

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 是 | 用户ID |
| accessToken | string | 是 | 访问令牌 |
| refreshToken | string | 是 | 刷新令牌 |
| expiresIn | int | 是 | 令牌过期时间(秒) |

#### 5.1.2 求职者密码登录
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/login/password |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| mobile | string | 是 | 手机号 |
| password | string | 是 | 密码 |
| captcha | string | 否 | 图形验证码(登录失败3次后必填) |
| captchaKey | string | 否 | 图形验证码Key |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 是 | 用户ID |
| userName | string | 是 | 用户姓名 |
| accessToken | string | 是 | 访问令牌 |
| refreshToken | string | 是 | 刷新令牌 |
| expiresIn | int | 是 | 过期秒数 |
| identity | string | 是 | 身份类型 |
| hasResume | boolean | 是 | 是否已创建简历 |

#### 5.1.3 求职者邮箱验证码登录
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/login/email |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| email | string | 是 | 登录邮箱 |
| emailCode | string | 是 | 邮箱验证码 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| accessToken | string | 是 | 访问令牌 |
| refreshToken | string | 是 | 刷新令牌 |
| expiresIn | int | 是 | 过期秒数 |
| isNewUser | boolean | 是 | 是否新用户(首次登录) |

#### 5.1.4 忘记密码 / 重置密码
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/password/reset |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| email | string | 是 | 登录邮箱 |
| emailCode | string | 是 | 邮箱验证码 |
| newPassword | string | 是 | 新密码 |

#### 5.1.5 刷新 Token
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/token/refresh |
| 鉴权要求 | 无需登录(携带refreshToken) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| refreshToken | string | 是 | 刷新令牌 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| accessToken | string | 是 | 新的访问令牌 |
| expiresIn | int | 是 | 过期秒数 |

#### 5.1.6 退出登录
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/user/logout |
| 鉴权要求 | 需要登录 |
| Content-Type | application/json |

#### 5.1.7 完善基础信息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/user/profile |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| avatar | string | 否 | 头像URL |
| gender | string | 否 | 性别：MALE/FEMALE |
| birthday | string | 否 | 生日 yyyy-MM-dd |
| email | string | 否 | 邮箱 |
| school | string | 否 | 学校(学生) |
| major | string | 否 | 专业(学生) |
| graduationYear | int | 否 | 毕业年份(学生) |
| currentCity | string | 否 | 现居城市 |

#### 5.1.8 获取当前登录用户信息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/user/profile |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 是 | 用户ID |
| mobile | string | 是 | 手机号(脱敏) |
| name | string | 是 | 姓名 |
| avatar | string | 否 | 头像URL |
| identity | string | 是 | 身份 |
| email | string | 否 | 邮箱 |
| school | string | 否 | 学校 |
| major | string | 否 | 专业 |

### 5.2 求职者简历管理
#### 5.2.1 创建简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/resume |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| basicInfo | object | 是 | 基础信息对象 |
| basicInfo.name | string | 是 | 姓名 |
| basicInfo.gender | string | 是 | 性别 |
| basicInfo.birthday | string | 是 | 出生年月 |
| basicInfo.mobile | string | 是 | 联系电话 |
| basicInfo.email | string | 是 | 邮箱 |
| basicInfo.currentCity | string | 否 | 所在城市 |
| jobIntention | object | 是 | 求职意向对象 |
| jobIntention.expectPosition | string | 是 | 期望职位 |
| jobIntention.expectIndustry | string | 否 | 期望行业 |
| jobIntention.expectSalaryMin | int | 否 | 期望薪资下限 |
| jobIntention.expectSalaryMax | int | 否 | 期望薪资上限 |
| jobIntention.expectCity | string | 否 | 期望城市 |
| educationList | array | 是 | 教育经历列表 |
| workList | array | 否 | 工作/实习经历列表 |
| skillList | array | 否 | 技能证书列表 |
| selfEvaluation | string | 否 | 自我评价 |
| privacy | string | 否 | 隐私：PUBLIC/ENTERPRISE_ONLY/HIDDEN，默认ENTERPRISE_ONLY |

##### 请求示例
```json
{
  "basicInfo": {
    "name": "张三",
    "gender": "MALE",
    "birthday": "2001-05-10",
    "mobile": "13812345678",
    "email": "zhangsan@xx.com",
    "currentCity": "成都市"
  },
  "jobIntention": {
    "expectPosition": "Java开发工程师",
    "expectIndustry": "互联网",
    "expectSalaryMin": 6000,
    "expectSalaryMax": 12000,
    "expectCity": "成都市郫都区"
  },
  "educationList": [{
    "school": "XX大学",
    "major": "计算机科学与技术",
    "degree": "本科",
    "startDate": "2019-09",
    "endDate": "2023-06"
  }],
  "workList": [{
    "company": "XX科技",
    "position": "Java实习生",
    "startDate": "2022-07",
    "endDate": "2022-12",
    "description": "负责..."
  }],
  "skillList": [
    { "name": "Java", "level": "熟练" },
    { "name": "英语四级", "level": "CET-4" }
  ],
  "selfEvaluation": "...",
  "privacy": "ENTERPRISE_ONLY"
}
```

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

#### 5.2.2 更新简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/resume/{resumeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| basicInfo | object | 否 | 基础信息 |
| jobIntention | object | 否 | 求职意向 |
| educationList | array | 否 | 教育经历 |
| workList | array | 否 | 工作经历 |
| skillList | array | 否 | 技能 |
| selfEvaluation | string | 否 | 自我评价 |
| privacy | string | 否 | 隐私设置 |

**说明**：支持部分字段更新，仅提交需要修改的字段。

#### 5.2.3 删除简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | DELETE |
| 请求路径 | /recruit/api/v1/resume/{resumeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

#### 5.2.4 查询简历详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/resume/{resumeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |
| basicInfo | object | 是 | 基础信息 |
| jobIntention | object | 是 | 求职意向 |
| educationList | array | 是 | 教育经历 |
| workList | array | 是 | 工作经历 |
| skillList | array | 否 | 技能 |
| selfEvaluation | string | 否 | 自我评价 |
| privacy | string | 是 | 隐私设置 |
| attachmentList | array | 否 | 简历附件列表 |
| updateTime | string | 是 | 最后更新时间 |

#### 5.2.5 查询我的简历列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/resume/my |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 简历列表 |
| list[].resumeId | string | 是 | 简历ID |
| list[].title | string | 是 | 简历标题 |
| list[].privacy | string | 是 | 隐私状态 |
| list[].isDefault | boolean | 是 | 是否默认投递简历 |
| list[].updateTime | string | 是 | 更新时间 |

#### 5.2.6 设置默认简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/resume/{resumeId}/default |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

#### 5.2.7 修改简历隐私设置
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/resume/{resumeId}/privacy |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| privacy | string | 是 | PUBLIC/ENTERPRISE_ONLY/HIDDEN |

#### 5.2.8 简历导出PDF
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/resume/{resumeId}/export/pdf |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| fileId | string | 是 | 生成后的文件ID |
| fileName | string | 是 | 下载文件名 |
| downloadUrl | string | 是 | 静态文件访问地址 |

**实现说明**：前端可优先调用 `/recruit/api/v1/common/file/download/{fileId}` 进行鉴权下载，也可在同域环境下使用 `downloadUrl` 触发浏览器下载。

#### 5.2.9 上传简历附件
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/resume/{resumeId}/attachment |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| fileId | string | 是 | 通过统一上传接口获得的fileId |
| fileName | string | 是 | 附件文件名 |

#### 5.2.10 删除简历附件
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | DELETE |
| 请求路径 | /recruit/api/v1/resume/{resumeId}/attachment/{attachmentId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |
| attachmentId | string | 是 | 附件ID |

### 5.3 求职者职位操作
#### 5.3.1 职位多条件搜索
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/jobs/search |
| 鉴权要求 | 可选(登录后可获得个性化排序) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| keyword | string | 否 | 关键词 |
| salaryMin | int | 否 | 最低薪资 |
| salaryMax | int | 否 | 最高薪资 |
| education | string | 否 | 学历要求 |
| experience | string | 否 | 工作经验 |
| location | string | 否 | 工作地点 |
| industry | string | 否 | 行业 |
| jobCategory | string | 否 | 职位分类 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |
| orderBy | string | 否 | publishTime/salary/matchScore |

#### 5.3.2 获取职位详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/jobs/{jobId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |
| jobName | string | 是 | 职位名称 |
| companyId | string | 是 | 企业ID |
| companyName | string | 是 | 企业名称 |
| companyLogo | string | 否 | 企业Logo |
| salaryMin | int | 是 | 薪资下限 |
| salaryMax | int | 是 | 薪资上限 |
| location | string | 是 | 工作地点 |
| education | string | 是 | 学历要求 |
| experience | string | 是 | 经验要求 |
| headCount | int | 是 | 招聘人数 |
| responsibility | string | 是 | 岗位职责 |
| requirement | string | 是 | 任职要求 |
| welfare | array | 否 | 福利标签 |
| contactName | string | 否 | 联系人 |
| contactMobile | string | 否 | 联系电话(登录后可见) |
| publishTime | string | 是 | 发布时间 |
| status | string | 是 | RECRUITING/FINISHED/OFFLINE |
| collected | boolean | 否 | 当前用户是否已收藏(登录后返回) |
| applied | boolean | 否 | 当前用户是否已投递 |

#### 5.3.3 投递简历(一键投递)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/apply |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |
| resumeId | string | 是 | 投递使用的简历ID |
| coverLetter | string | 否 | 求职信(可选) |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| applyId | string | 是 | 投递记录ID |
| applyTime | string | 是 | 投递时间 |

**说明**：同一职位不可重复投递，返回 5003 错误码。

#### 5.3.4 批量投递简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/apply/batch |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobIds | array<string> | 是 | 职位ID列表，最多20个 |
| resumeId | string | 是 | 简历ID |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| successCount | int | 是 | 成功投递数 |
| failCount | int | 是 | 失败数 |
| failList | array | 否 | 失败明细：{jobId, reason} |

#### 5.3.5 收藏职位
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/jobs/{jobId}/collect |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

#### 5.3.6 取消收藏
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | DELETE |
| 请求路径 | /recruit/api/v1/jobs/{jobId}/collect |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

#### 5.3.7 我的收藏列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/jobs/collections |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 5.3.8 投递记录列表(投递状态跟踪)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/apply/my |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 否 | 状态：PENDING(待查看)/VIEWED(已查看)/INVITED(面试邀约)/REJECTED(不合适) |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 投递记录列表 |
| list[].applyId | string | 是 | 投递ID |
| list[].jobId | string | 是 | 职位ID |
| list[].jobName | string | 是 | 职位名称 |
| list[].companyName | string | 是 | 企业名称 |
| list[].status | string | 是 | 状态 |
| list[].applyTime | string | 是 | 投递时间 |
| list[].updateTime | string | 是 | 最后状态变更时间 |
| total | long | 是 | 总数 |

### 5.4 求职者消息中心
#### 5.4.1 消息列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/messages |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| type | string | 否 | INTERVIEW(面试邀约)/REPLY(企业回复)/SYSTEM(系统通知) |
| readStatus | string | 否 | READ/UNREAD，默认全部 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 消息列表 |
| list[].messageId | string | 是 | 消息ID |
| list[].type | string | 是 | 消息类型 |
| list[].title | string | 是 | 消息标题 |
| list[].content | string | 是 | 消息内容 |
| list[].bizId | string | 否 | 关联业务ID(如职位ID/投递ID) |
| list[].readStatus | string | 是 | READ/UNREAD |
| list[].createTime | string | 是 | 创建时间 |
| unreadCount | int | 是 | 未读数量 |

#### 5.4.2 消息详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/messages/{messageId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| messageId | string | 是 | 消息ID |

#### 5.4.3 标记消息已读
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/messages/read |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| messageIds | array<string> | 否 | 消息ID列表，为空则全部已读 |

#### 5.4.4 删除消息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | DELETE |
| 请求路径 | /recruit/api/v1/messages |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| messageIds | array<string> | 是 | 消息ID列表 |

#### 5.4.5 未读消息数量统计
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/messages/unread/count |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| total | int | 是 | 总未读数 |
| byType | object | 是 | 按类型分组的未读数，如 { INTERVIEW: 3, REPLY: 1, SYSTEM: 0 } |

---

## 6 企业端接口
本章节接口供企业HR/负责人使用，涵盖企业认证、职位管理、简历管理、人才搜索。

### 6.1 企业认证与登录
#### 6.1.1 企业账号注册
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/register |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| contactMobile | string | 是 | 联系人手机号(存档字段) |
| email | string | 是 | 联系人邮箱(登录验证码接收邮箱) |
| emailCode | string | 是 | 邮箱验证码 |
| password | string | 是 | 密码 |
| contactName | string | 是 | 联系人姓名 |
| companyName | string | 是 | 企业名称 |
| agreeProtocol | boolean | 是 | 是否同意协议 |

#### 6.1.2 企业登录
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/login |
| 鉴权要求 | 无需登录 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| mobile | string | 是 | 手机号 |
| password | string | 是 | 密码 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| enterpriseId | string | 是 | 企业ID |
| accountId | string | 是 | 企业账号ID |
| accessToken | string | 是 | 访问令牌 |
| refreshToken | string | 是 | 刷新令牌 |
| expiresIn | int | 是 | 过期秒数 |
| authStatus | string | 是 | 认证状态：UNAUTH/PENDING/PASS/REJECT |

#### 6.1.3 提交企业认证
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/auth |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| companyName | string | 是 | 企业全称 |
| creditCode | string | 是 | 统一社会信用代码 |
| legalPerson | string | 是 | 法人姓名 |
| licenseFileId | string | 是 | 营业执照文件ID |
| industry | string | 是 | 所属行业 |
| scale | string | 是 | 企业规模，如 0-20/20-99/100-499/500-999/1000+ |
| address | string | 是 | 详细地址 |
| introduction | string | 否 | 企业简介 |
| logoFileId | string | 否 | 企业Logo文件ID |
| website | string | 否 | 企业官网 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| authId | string | 是 | 认证记录ID |
| authStatus | string | 是 | PENDING(审核中) |

#### 6.1.4 查询企业认证状态
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/auth/status |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| authStatus | string | 是 | UNAUTH/PENDING/PASS/REJECT |
| rejectReason | string | 否 | 驳回原因(authStatus=REJECT时) |
| submitTime | string | 否 | 提交时间 |
| auditTime | string | 否 | 审核时间 |

#### 6.1.5 查询企业信息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/info |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| enterpriseId | string | 是 | 企业ID |
| companyName | string | 是 | 企业名称 |
| logo | string | 否 | Logo URL |
| industry | string | 是 | 行业 |
| scale | string | 是 | 规模 |
| address | string | 是 | 地址 |
| introduction | string | 否 | 简介 |
| website | string | 否 | 官网 |
| authStatus | string | 是 | 认证状态 |

#### 6.1.6 编辑企业信息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/info |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| logoFileId | string | 否 | Logo文件ID |
| introduction | string | 否 | 企业简介 |
| address | string | 否 | 地址 |
| website | string | 否 | 官网 |
| welfare | array | 否 | 福利标签列表 |

**说明**：营业执照、企业名称、统一信用代码等核心信息需重新提交认证。

### 6.2 企业职位管理
#### 6.2.1 发布职位
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/jobs |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobName | string | 是 | 职位名称 |
| jobCategory | string | 是 | 职位分类 |
| responsibility | string | 是 | 岗位职责 |
| requirement | string | 是 | 任职要求 |
| salaryMin | int | 是 | 薪资下限 |
| salaryMax | int | 是 | 薪资上限 |
| salaryNegotiable | boolean | 否 | 薪资面议 |
| location | string | 是 | 工作地点 |
| headCount | int | 是 | 招聘人数 |
| education | string | 是 | 学历要求 |
| experience | string | 是 | 工作经验要求 |
| welfare | array<string> | 否 | 福利标签 |
| contactName | string | 是 | 联系人 |
| contactMobile | string | 是 | 联系电话 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |
| status | string | 是 | PENDING(待审核) |

**说明**：发布后进入平台审核，审核通过后状态变为 RECRUITING。

#### 6.2.2 编辑职位
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

##### 请求体参数(Body)
同发布职位参数，按需提交

**说明**：修改关键字段(职位名称/职责/要求/薪资)将重新触发审核。

#### 6.2.3 职位下架
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId}/offline |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

#### 6.2.4 职位刷新(置顶时间)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId}/refresh |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

**说明**：刷新后职位在首页/搜索中排名提前，每天限刷3次。

#### 6.2.5 删除职位
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | DELETE |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

**说明**：仅允许删除未投递过简历的职位，否则应执行下架操作。

#### 6.2.6 企业职位列表(含状态筛选)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/jobs |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 否 | PENDING/RECRUITING/FINISHED/OFFLINE/REJECTED |
| keyword | string | 否 | 职位名称关键词 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 职位列表 |
| list[].jobId | string | 是 | 职位ID |
| list[].jobName | string | 是 | 职位名称 |
| list[].status | string | 是 | 状态 |
| list[].viewCount | int | 是 | 浏览量 |
| list[].applyCount | int | 是 | 投递数 |
| list[].publishTime | string | 是 | 发布时间 |
| list[].refreshTime | string | 否 | 最后刷新时间 |
| list[].rejectReason | string | 否 | 驳回原因 |

#### 6.2.7 职位预览
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId}/preview |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

#### 6.2.8 获取职位分享链接
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/jobs/{jobId}/share |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| shareUrl | string | 是 | 分享链接 |
| qrCodeUrl | string | 是 | 二维码图片URL |

### 6.3 企业简历管理
#### 6.3.1 收到的简历列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/applies |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 否 | 职位ID过滤 |
| status | string | 否 | UNVIEWED/VIEWED/SUITABLE/UNSUITABLE/PENDING/INVITED |
| keyword | string | 否 | 求职者姓名 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 投递列表 |
| list[].applyId | string | 是 | 投递ID |
| list[].resumeId | string | 是 | 简历ID |
| list[].jobId | string | 是 | 职位ID |
| list[].jobName | string | 是 | 职位名称 |
| list[].candidateName | string | 是 | 求职者姓名 |
| list[].education | string | 是 | 学历 |
| list[].major | string | 否 | 专业 |
| list[].status | string | 是 | 状态 |
| list[].applyTime | string | 是 | 投递时间 |

#### 6.3.2 查看简历详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/resumes/{resumeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

**说明**：查看后投递状态自动从 UNVIEWED 变为 VIEWED，同时生成一条企业回复消息给求职者。

#### 6.3.3 简历筛选(标记状态)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/applies/{applyId}/status |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| applyId | string | 是 | 投递ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 是 | SUITABLE(合格)/UNSUITABLE(不合适)/PENDING(待沟通) |
| remark | string | 否 | 备注 |

#### 6.3.4 批量标记简历状态
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/enterprise/applies/status/batch |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| applyIds | array<string> | 是 | 投递ID列表 |
| status | string | 是 | 状态 |

#### 6.3.5 发送面试邀约
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/interviews |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| applyId | string | 是 | 投递ID |
| interviewTime | string | 是 | 面试时间 yyyy-MM-dd HH:mm:ss |
| interviewType | string | 是 | ONLINE(线上) / OFFLINE(线下) |
| interviewPlace | string | 否 | 面试地点(OFFLINE必填) |
| interviewLink | string | 否 | 视频会议链接(ONLINE必填) |
| contactName | string | 是 | 联系人 |
| contactMobile | string | 是 | 联系电话 |
| remark | string | 否 | 备注 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| interviewId | string | 是 | 面试ID |

**说明**：发送后会同步推送站内信 + 邮件(可选)至求职者。

#### 6.3.6 收藏简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/resumes/{resumeId}/favorite |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

#### 6.3.7 企业简历收藏夹
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/resumes/favorites |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 6.3.8 批量导出简历
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/resumes/export |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeIds | array<string> | 是 | 简历ID列表，最多100条 |
| format | string | 否 | PDF / EXCEL，默认PDF |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| taskId | string | 是 | 异步任务ID |
| downloadUrl | string | 否 | 下载地址(任务完成后返回) |
| status | string | 是 | PROCESSING / SUCCESS / FAIL |

**说明**：异步任务，可通过 /export/{taskId}/status 轮询状态。

#### 6.3.9 简历搜索(企业维度)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/resumes/search |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| major | string | 否 | 专业 |
| education | string | 否 | 学历 |
| skillKeywords | string | 否 | 技能关键词，多个用英文逗号分隔 |
| jobId | string | 否 | 限定职位 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

### 6.4 企业人才搜索
#### 6.4.1 搜索公开简历(人才库)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/talents/search |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| keyword | string | 否 | 关键词(姓名/学校/技能) |
| major | string | 否 | 专业 |
| education | string | 否 | 学历 |
| experience | string | 否 | 工作经验 |
| skillKeywords | string | 否 | 技能关键词 |
| expectCity | string | 否 | 期望城市 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 人才列表 |
| list[].resumeId | string | 是 | 简历ID |
| list[].maskedName | string | 是 | 脱敏姓名(如 张*) |
| list[].education | string | 是 | 学历 |
| list[].major | string | 否 | 专业 |
| list[].school | string | 否 | 学校 |
| list[].expectPosition | string | 否 | 期望职位 |
| list[].expectCity | string | 否 | 期望城市 |
| list[].skills | array | 否 | 技能标签 |

**说明**：仅返回 privacy=PUBLIC 或 ENTERPRISE_ONLY 的简历，联系方式需发起沟通后由求职者同意展示。

#### 6.4.2 查看公开简历详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/enterprise/talents/{resumeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

#### 6.4.3 向人才发起沟通/邀约
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/enterprise/talents/contact |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |
| jobId | string | 是 | 推荐的职位ID |
| message | string | 是 | 沟通内容，最多500字 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| contactId | string | 是 | 沟通记录ID |

---

## 7 平台管理员接口（续）
### 7.1 用户管理（续）
#### 7.1.1 查询求职者列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/users/candidates |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| keyword | string | 否 | 姓名/手机号 |
| identity | string | 否 | STUDENT/SOCIAL |
| status | string | 否 | ACTIVE/DISABLED |
| registerStart | string | 否 | 注册起始日期 |
| registerEnd | string | 否 | 注册结束日期 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 7.1.2 查询求职者详情
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/users/candidates/{userId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 是 | 用户ID |

#### 7.1.3 禁用 / 启用求职者账号
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/users/candidates/{userId}/status |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 是 | 用户ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 是 | ACTIVE/DISABLED |
| reason | string | 否 | 禁用原因 |

#### 7.1.4 查询企业列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/users/enterprises |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| keyword | string | 否 | 企业名称 |
| authStatus | string | 否 | UNAUTH/PENDING/PASS/REJECT |
| status | string | 否 | ACTIVE/DISABLED |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 7.1.5 企业认证审核
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/users/enterprises/{enterpriseId}/audit |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| enterpriseId | string | 是 | 企业ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| result | string | 是 | PASS/REJECT |
| reason | string | 否 | 驳回原因(result=REJECT时必填) |

#### 7.1.6 禁用 / 启用企业账号
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/users/enterprises/{enterpriseId}/status |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| enterpriseId | string | 是 | 企业ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 是 | ACTIVE/DISABLED |
| reason | string | 否 | 原因 |

#### 7.1.7 编辑企业信息(管理员)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/users/enterprises/{enterpriseId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| enterpriseId | string | 是 | 企业ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| companyName | string | 否 | 企业名称 |
| industry | string | 否 | 行业 |
| scale | string | 否 | 规模 |
| address | string | 否 | 地址 |
| introduction | string | 否 | 简介 |

---

### 7.2 信息审核
#### 7.2.1 待审核职位列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/audit/jobs |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 否 | PENDING(默认)/PASSED/REJECTED |
| companyName | string | 否 | 企业名称 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 7.2.2 审核职位
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/audit/jobs/{jobId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobId | string | 是 | 职位ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| result | string | 是 | PASS/REJECT |
| reason | string | 否 | 驳回原因 |

#### 7.2.3 简历违规检测 / 清理
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/admin/audit/resumes/{resumeId}/moderate |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| resumeId | string | 是 | 简历ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| action | string | 是 | MARK_VIOLATION(标记违规)/CLEAR(清理违规内容) |
| reason | string | 否 | 原因 |

#### 7.2.4 待审核公告 / 招聘信息列表
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/audit/notices |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| status | string | 否 | PENDING/PASSED/REJECTED |
| type | string | 否 | JOB_FAIR(招聘会)/POLICY(政策)/COOPERATION(校企合作) |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 7.2.5 审核公告 / 招聘信息
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/audit/notices/{noticeId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| noticeId | string | 是 | 公告ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| result | string | 是 | PASS/REJECT |
| reason | string | 否 | 原因 |

---

### 7.3 系统管理
#### 7.3.1 职位分类 - 查询
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/system/categories |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| parentId | string | 否 | 父级ID，为空查根节点 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 分类列表(树形/层级) |

#### 7.3.2 职位分类 - 新增
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/admin/system/categories |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| name | string | 是 | 分类名称 |
| parentId | string | 否 | 父级ID |
| sort | int | 否 | 排序 |

#### 7.3.3 职位分类 - 修改 / 删除
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT / DELETE |
| 请求路径 | /recruit/api/v1/admin/system/categories/{categoryId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| categoryId | string | 是 | 分类ID |

#### 7.3.4 轮播图管理
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST / PUT / DELETE / GET |
| 请求路径 | /recruit/api/v1/admin/system/banners[/{bannerId}] |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| title | string | 是 | 标题 |
| imageFileId | string | 是 | 图片文件ID |
| linkUrl | string | 否 | 跳转链接 |
| sort | int | 否 | 排序 |
| startTime | string | 否 | 上架时间 |
| endTime | string | 否 | 下架时间 |
| status | string | 否 | ONLINE/OFFLINE |

#### 7.3.5 公告管理
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST / PUT / DELETE / GET |
| 请求路径 | /recruit/api/v1/admin/system/notices[/{noticeId}] |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| title | string | 是 | 标题 |
| content | string | 是 | 内容(支持富文本) |
| type | string | 是 | JOB_FAIR / POLICY / COOPERATION |
| status | string | 否 | DRAFT/ONLINE/OFFLINE |
| publishTime | string | 否 | 发布时间 |

#### 7.3.6 操作日志查询
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/system/logs/operation |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | string | 否 | 用户ID |
| action | string | 否 | 操作类型 |
| startTime | string | 否 | 起始时间 |
| endTime | string | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| list | array | 是 | 日志列表 |
| list[].logId | string | 是 | 日志ID |
| list[].userId | string | 是 | 操作人ID |
| list[].userName | string | 是 | 操作人 |
| list[].action | string | 是 | 动作 |
| list[].resource | string | 是 | 资源 |
| list[].ip | string | 是 | IP地址 |
| list[].createTime | string | 是 | 时间 |

#### 7.3.7 审核日志查询
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/system/logs/audit |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| bizType | string | 否 | JOB/ENTERPRISE/RESUME/NOTICE |
| auditor | string | 否 | 审核人 |
| result | string | 否 | PASS/REJECT |
| startTime | string | 否 | 起始时间 |
| endTime | string | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

#### 7.3.8 角色与权限管理
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET / POST / PUT / DELETE |
| 请求路径 | /recruit/api/v1/admin/system/roles[/{roleId}] |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| roleName | string | 是 | 角色名称 |
| roleCode | string | 是 | 角色编码 |
| permissionIds | array<string> | 是 | 权限ID列表 |
| remark | string | 否 | 备注 |

---

## 8 消息通知接口
### 8.1 站内信推送(内部调用)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/internal/message/push |
| 鉴权要求 | 内部接口，需服务间密钥 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| receiverType | string | 是 | CANDIDATE/ENTERPRISE/ADMIN |
| receiverIds | array<string> | 是 | 接收人ID列表 |
| type | string | 是 | 消息类型:AUDIT_RESULT/ACCOUNT/APPLY/INTERVIEW/JOB_UPDATE |
| title | string | 是 | 标题 |
| content | string | 是 | 内容 |
| bizId | string | 否 | 业务关联ID |
| channels | array<string> | 否 | 通道:INSITE(站内信,默认)/SMS/EMAIL |

### 8.2 消息模板查询
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/system/message/templates |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| type | string | 否 | 消息类型 |

### 8.3 消息模板编辑
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | PUT |
| 请求路径 | /recruit/api/v1/admin/system/message/templates/{templateId} |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| templateId | string | 是 | 模板ID |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| title | string | 否 | 标题模板 |
| content | string | 否 | 内容模板，支持占位符 {变量名} |
| channels | array<string> | 否 | 默认通道 |

---

## 9 统计分析接口
### 9.1 个人统计
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/statistics/candidate |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| applyCount | int | 是 | 累计投递数 |
| interviewCount | int | 是 | 面试邀约数 |
| collectionCount | int | 是 | 收藏职位数 |
| viewCount | int | 是 | 简历被查看数 |
| last7Days | array | 是 | 近7天投递趋势 [{date, count}] |

### 9.2 企业统计
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/statistics/enterprise |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| startDate | string | 否 | 统计开始日期，默认近30天 |
| endDate | string | 否 | 统计结束日期 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| jobViewCount | long | 是 | 职位浏览总量 |
| applyCount | long | 是 | 收到简历总数 |
| interviewCount | long | 是 | 发出面试邀约数 |
| activeJobCount | int | 是 | 在招职位数 |
| jobStats | array | 是 | 各职位明细 |
| trend | array | 是 | 趋势数据 [{date, view, apply, interview}] |

### 9.3 平台总览统计
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | GET |
| 请求路径 | /recruit/api/v1/admin/statistics/overview |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 查询参数(Query)
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| startDate | string | 否 | 开始日期 |
| endDate | string | 否 | 结束日期 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| totalCandidates | long | 是 | 求职者总数 |
| totalEnterprises | long | 是 | 企业总数 |
| totalJobs | long | 是 | 职位总数 |
| totalApplies | long | 是 | 投递总量 |
| totalInterviews | long | 是 | 面试邀约总量 |
| employmentByArea | array | 是 | 区域就业数据 [{area, count}] |
| industryDist | array | 是 | 行业分布 [{industry, jobCount, applyCount}] |
| educationDist | array | 是 | 学历分布 |
| trend | array | 是 | 趋势数据 |

### 9.4 导出统计报表(Excel)
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/statistics/export |
| 鉴权要求 | 需要登录(携带Token) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| type | string | 是 | CANDIDATE/ENTERPRISE/PLATFORM |
| startDate | string | 是 | 开始日期 |
| endDate | string | 是 | 结束日期 |
| format | string | 否 | XLSX(默认)/CSV |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| taskId | string | 是 | 导出任务ID |
| status | string | 是 | SUCCESS |
| fileId | string | 是 | 导出后的文件ID |
| fileName | string | 是 | 导出文件名 |
| downloadUrl | string | 是 | 静态文件访问地址 |

**实现说明**：当前统计导出支持 `xlsx/csv` 两种格式，前端可使用 `fileId` 调用 `/recruit/api/v1/common/file/download/{fileId}` 下载。

---

## 10 系统接口与外部接口
### 10.1 系统内部接口
| 接口名称 | 对接系统 | 用途 |
| -------- | -------- | ---- |
| 统一用户接口 | 校企慧平台用户中心 | 账号同步、统一身份认证、SSO 登录 |
| 统一权限接口 | 校企慧平台权限中心 | 角色、权限数据同步，访问鉴权校验 |
| 消息中心接口 | 校企慧平台消息中心 | 站内信、邮件消息统一推送 |
| 文件存储接口 | 校企慧平台文件服务(OSS) | 简历、企业Logo、营业执照文件存储 |

#### 10.1.1 统一身份认证(SSO)
##### 10.1.1.1 Token 校验
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/internal/sso/verify |
| 鉴权要求 | 服务间鉴权(内部密钥) |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| accessToken | string | 是 | 用户访问令牌 |

##### 响应数据字段(data)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| valid | boolean | 是 | 是否有效 |
| userId | string | 否 | 用户ID |
| roles | array | 否 | 角色列表 |

### 10.2 外部接口
| 接口名称 | 用途 | 使用场景 |
| -------- | ---- | -------- |
| 邮件验证码接口 | 发送登录/注册/找回密码验证码 | 注册/登录验证码、找回密码推送 |
| 邮件接口 | 发送通知邮件 | 简历投递反馈、面试通知 |
| 数据导出接口 | 生成Excel/PDF文件 | 统计报表导出、简历批量导出 |

#### 10.2.1 邮箱验证码发送(调用说明)
调用邮件服务，平台内部封装为 /common/email/send 接口。验证码模板在后台消息模板管理中维护。

#### 10.2.2 邮件发送
##### 10.2.2.1 内部邮件推送
| 项目 | 说明 |
| ---- | ---- |
| 请求方法 | POST |
| 请求路径 | /recruit/api/v1/internal/email/send |
| 鉴权要求 | 内部接口 |
| Content-Type | application/json |

##### 请求体参数(Body)
| 字段名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| to | array<string> | 是 | 收件人邮箱 |
| subject | string | 是 | 主题 |
| content | string | 是 | 正文(HTML) |
| templateCode | string | 否 | 模板编码(优先使用) |
| params | object | 否 | 模板变量 |

---

## 11 附录
### 11.1 核心枚举值
#### 11.1.1 投递状态 ApplyStatus
| 值 | 含义 |
| ---- | ---- |
| PENDING | 待查看(求职者刚投递) |
| UNVIEWED | 企业未查看 |
| VIEWED | 已查看 |
| SUITABLE | 合格/通过筛选 |
| UNSUITABLE | 不合适 |
| INVITED | 已发出面试邀约 |
| REJECTED | 不合适/拒绝 |

#### 11.1.2 职位状态 JobStatus
| 值 | 含义 |
| ---- | ---- |
| PENDING | 待审核 |
| RECRUITING | 招聘中 |
| FINISHED | 已结束 |
| OFFLINE | 已下架 |
| REJECTED | 审核驳回 |

#### 11.1.3 企业认证状态 AuthStatus
| 值 | 含义 |
| ---- | ---- |
| UNAUTH | 未认证 |
| PENDING | 审核中 |
| PASS | 已通过 |
| REJECT | 已驳回 |

#### 11.1.4 简历隐私 ResumePrivacy
| 值 | 含义 |
| ---- | ---- |
| PUBLIC | 完全公开 |
| ENTERPRISE_ONLY | 仅企业可见(默认) |
| HIDDEN | 仅自己可见 |

#### 11.1.5 消息类型 MessageType
| 值 | 含义 |
| ---- | ---- |
| INTERVIEW | 面试邀约 |
| REPLY | 企业回复 |
| SYSTEM | 系统通知 |
| AUDIT_RESULT | 审核结果 |
| ACCOUNT | 账号状态 |
| APPLY | 简历投递反馈 |
| JOB_UPDATE | 职位更新 |

### 11.2 核心数据字典(摘要)
#### 11.2.1 职位(Job)
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| jobId | string(32) | 职位ID，主键 |
| jobName | string(100) | 职位名称 |
| enterpriseId | string(32) | 所属企业ID |
| jobCategory | string(50) | 职位分类 |
| salaryMin/salaryMax | int | 薪资区间 |
| location | string(100) | 工作地点 |
| education | string(20) | 学历要求 |
| experience | string(20) | 经验要求 |
| headCount | int | 招聘人数 |
| status | string(20) | 状态(见 11.1.2) |
| publishTime | datetime | 发布时间 |
| refreshTime | datetime | 最后刷新时间 |
| createTime | datetime | 创建时间 |
| updateTime | datetime | 更新时间 |

#### 11.2.2 简历(Resume)
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| resumeId | string(32) | 简历ID |
| userId | string(32) | 求职者ID |
| title | string(100) | 简历标题 |
| basicInfo | json | 基础信息JSON |
| jobIntention | json | 求职意向JSON |
| educationList | json | 教育经历 |
| workList | json | 工作经历 |
| skillList | json | 技能证书 |
| selfEvaluation | text | 自我评价 |
| privacy | string(20) | 隐私设置 |
| isDefault | boolean | 是否默认投递简历 |
| updateTime | datetime | 更新时间 |

#### 11.2.3 投递记录(Apply)
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| applyId | string(32) | 投递ID |
| jobId | string(32) | 职位ID |
| resumeId | string(32) | 简历ID |
| userId | string(32) | 求职者ID |
| enterpriseId | string(32) | 企业ID |
| status | string(20) | 状态(见 11.1.1) |
| coverLetter | text | 求职信 |
| applyTime | datetime | 投递时间 |
| updateTime | datetime | 最后变更时间 |

### 11.3 性能与安全要求对照
| 维度 | 要求 | 接口实现说明 |
| ---- | ---- | ---- |
| 响应时间 | 页面加载 ≤3s、查询 ≤2s、提交 ≤1s | 列表接口强制分页，关键接口加缓存 |
| 并发能力 | ≥500 人同时在线 | 接口无状态，水平扩展 + Nginx 负载均衡 |
| 稳定性 | 7×24，年故障率 ≤0.1% | 健康检查、熔断、限流、灰度发布 |
| 数据安全 | 密码加密、敏感脱敏、防SQL注入/XSS | 密码 BCrypt、参数校验、MyBatis 预编译 |
| 权限约束 | 角色分离、权限最小化 | 基于角色 + 资源粒度鉴权 |

### 11.4 变更记录
| 版本 | 日期 | 变更内容 | 作者 |
| ---- | ---- | ---- | ---- |
| V1.0 | 2026-04-17 | 初版发布，覆盖全部需求章节接口定义 | 智慧教育研教部 |

---
