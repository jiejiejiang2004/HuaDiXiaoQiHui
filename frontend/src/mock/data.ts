/** 纯静态示例数据，供本地 Mock 使用（与真实后端字段尽量对齐） */

export const MOCK_JOBS = [
  {
    jobId: 901,
    jobName: "Java 后端开发工程师",
    companyName: "成都校企科技有限公司",
    salaryMin: 12000,
    salaryMax: 20000,
    location: "成都·高新区",
    education: "本科",
    experience: "1-3年",
    welfare: ["五险一金", "双休", "带薪年假"],
    responsibility:
      "参与招聘平台核心模块设计与开发；与前端联调接口；编写单元测试。",
    requirement: "熟悉 Java 17、Spring Boot 3、MyBatis；了解 Vue 3 更佳。",
  },
  {
    jobId: 902,
    jobName: "前端开发工程师（Vue3）",
    companyName: "华地数字科技",
    salaryMin: 10000,
    salaryMax: 18000,
    location: "成都·郫都区",
    education: "本科",
    experience: "应届",
    welfare: ["弹性工时", "餐补"],
    responsibility: "负责企业端与求职者端页面开发与交互优化。",
    requirement: "熟练使用 Vue 3、TypeScript、Element Plus。",
  },
  {
    jobId: 903,
    jobName: "产品经理（校招方向）",
    companyName: "校企慧平台运营中心",
    salaryMin: 9000,
    salaryMax: 15000,
    location: "成都",
    education: "硕士",
    experience: "1-3年",
    welfare: ["年度体检", "补充医疗"],
    responsibility: "梳理招聘业务流程，输出 PRD，推动迭代落地。",
    requirement: "有 B 端或教育类产品经验优先。",
  },
  {
    jobId: 904,
    jobName: "测试工程师",
    companyName: "成都校企科技有限公司",
    salaryMin: 8000,
    salaryMax: 13000,
    location: "成都·高新区",
    education: "本科",
    experience: "1-3年",
    welfare: ["双休", "五险一金"],
    responsibility: "参与 Web 与接口测试，编写测试用例与缺陷跟踪。",
    requirement: "熟悉接口测试、Postman，了解自动化测试基础。",
  },
  {
    jobId: 905,
    jobName: "UI/UX 设计师",
    companyName: "像素工场设计",
    salaryMin: 8000,
    salaryMax: 14000,
    location: "成都·武侯区",
    education: "本科",
    experience: "1-3年",
    welfare: ["双休", "远程每周 1 天"],
    responsibility: "招聘与后台管理端界面与组件规范设计。",
    requirement: "熟练使用 Figma，有 B 端后台设计案例。",
  },
  {
    jobId: 906,
    jobName: "测试工程师（自动化）",
    companyName: "质效科技",
    salaryMin: 9000,
    salaryMax: 15000,
    location: "成都·双流区",
    education: "大专",
    experience: "1-3年",
    welfare: ["五险一金", "项目奖金"],
    responsibility: "搭建接口与 UI 自动化测试流水线。",
    requirement: "熟悉 Pytest / Playwright 或同类工具。",
  },
  {
    jobId: 907,
    jobName: "数据分析师",
    companyName: "慧数咨询",
    salaryMin: 11000,
    salaryMax: 18000,
    location: "成都·锦江区",
    education: "硕士",
    experience: "应届",
    welfare: ["带薪培训", "餐补"],
    responsibility: "招聘漏斗、转化与画像报表建设。",
    requirement: "SQL 熟练，了解可视化看板（Tableau / 飞书）。",
  },
  {
    jobId: 908,
    jobName: "运维工程师（DevOps）",
    companyName: "基石云服",
    salaryMin: 13000,
    salaryMax: 20000,
    location: "成都·高新区",
    education: "本科",
    experience: "3-5年",
    welfare: ["夜班补贴", "年度旅游"],
    responsibility: "CI/CD、监控告警与容器集群运维。",
    requirement: "熟悉 Linux、Docker、Jenkins 或 GitLab CI。",
  },
  {
    jobId: 909,
    jobName: "Android 开发工程师",
    companyName: "移动互联成都研发中心",
    salaryMin: 12000,
    salaryMax: 19000,
    location: "成都·成华区",
    education: "本科",
    experience: "1-3年",
    welfare: ["五险一金", "健身房"],
    responsibility: "校企端 App 功能迭代与性能优化。",
    requirement: "Kotlin 熟练，有上架应用经验。",
  },
  {
    jobId: 910,
    jobName: "人力资源专员（招聘方向）",
    companyName: "蓉城智造集团",
    salaryMin: 6000,
    salaryMax: 9000,
    location: "成都·龙泉驿区",
    education: "大专",
    experience: "应届",
    welfare: ["包午餐", "交通补贴"],
    responsibility: "一线岗位寻访、面试安排与入职跟进。",
    requirement: "沟通能力强，能接受多任务并行。",
  },
  {
    jobId: 911,
    jobName: "算法工程师（推荐）",
    companyName: "智聘 AI Lab",
    salaryMin: 20000,
    salaryMax: 35000,
    location: "成都·高新区",
    education: "硕士",
    experience: "3-5年",
    welfare: ["高额公积金", "论文专利奖励"],
    responsibility: "职位与简历匹配模型训练与线上 A/B。",
    requirement: "熟悉 Python、深度学习框架与召回排序链路。",
  },
  {
    jobId: 912,
    jobName: "新媒体运营",
    companyName: "校企品牌传播部",
    salaryMin: 7000,
    salaryMax: 11000,
    location: "成都·青羊区",
    education: "本科",
    experience: "应届",
    welfare: ["弹性打卡", "节日礼盒"],
    responsibility: "双微一抖内容策划与校招活动传播。",
    requirement: "有短视频脚本与基础剪辑能力。",
  },
  {
    jobId: 913,
    jobName: "嵌入式软件工程师",
    companyName: "川维智能硬件",
    salaryMin: 11500,
    salaryMax: 17000,
    location: "绵阳·科技城新区",
    education: "本科",
    experience: "1-3年",
    welfare: ["人才公寓", "项目津贴"],
    responsibility: "工业采集设备固件开发与联调。",
    requirement: "C/C++、RTOS，有串口/总线调试经验。",
  },
  {
    jobId: 914,
    jobName: "大客户销售（教育行业）",
    companyName: "华教解决方案",
    salaryMin: 8000,
    salaryMax: 15000,
    location: "重庆·渝北区",
    education: "本科",
    experience: "1-3年",
    welfare: ["提成上不封顶", "差旅补贴"],
    responsibility: "高校与园区招聘系统商机拓展与签约。",
    requirement: "有 To B 销售或教育信息化经验优先。",
  },
  {
    jobId: 915,
    jobName: "网络安全工程师",
    companyName: "盾安信息",
    salaryMin: 15000,
    salaryMax: 22000,
    location: "西安·高新区",
    education: "本科",
    experience: "3-5年",
    welfare: ["五险一金", "证书补贴"],
    responsibility: "渗透测试、漏洞修复与安全基线审计。",
    requirement: "熟悉 Web 安全、等保流程。",
  },
  {
    jobId: 916,
    jobName: "会计（成本方向）",
    companyName: "西部供应链",
    salaryMin: 6500,
    salaryMax: 9500,
    location: "成都·新都区",
    education: "大专",
    experience: "1-3年",
    welfare: ["双休", "年终奖"],
    responsibility: "制造业成本核算与报表支持。",
    requirement: "初级会计职称，Excel 熟练。",
  },
  {
    jobId: 917,
    jobName: "Python 后端开发",
    companyName: "爬虫与数据服务部",
    salaryMin: 11000,
    salaryMax: 18000,
    location: "成都·高新区",
    education: "本科",
    experience: "1-3年",
    welfare: ["技术分享日", "书籍报销"],
    responsibility: "数据采集清洗 API 与任务调度。",
    requirement: "FastAPI / Django 任一，熟悉 Celery。",
  },
  {
    jobId: 918,
    jobName: "客服主管",
    companyName: "校企慧客服中心",
    salaryMin: 7500,
    salaryMax: 10000,
    location: "成都·温江区",
    education: "大专",
    experience: "3-5年",
    welfare: ["五险", "带薪年假"],
    responsibility: "一线客服排班、质检与升级工单处理。",
    requirement: "有呼叫中心或在线客服团队管理经验。",
  },
  {
    jobId: 919,
    jobName: "机械设计工程师",
    companyName: "精工自动化",
    salaryMin: 9000,
    salaryMax: 13000,
    location: "德阳·经开区",
    education: "本科",
    experience: "应届",
    welfare: ["住宿补贴", "餐补"],
    responsibility: "产线工装夹具三维建模与出图。",
    requirement: "SolidWorks 熟练，懂公差与加工工艺。",
  },
  {
    jobId: 920,
    jobName: "法务专员",
    companyName: "合规与风控部",
    salaryMin: 10000,
    salaryMax: 14000,
    location: "成都·高新区",
    education: "硕士",
    experience: "1-3年",
    welfare: ["补充商业险", "年度体检"],
    responsibility: "劳动合同、外包协议与数据合规审查。",
    requirement: "通过法考，有互联网企业法务实习/工作经验。",
  },
  {
    jobId: 921,
    jobName: "实习生（全栈方向）",
    companyName: "校企慧创新实验室",
    salaryMin: 3000,
    salaryMax: 5000,
    location: "成都·郫都区",
    education: "本科",
    experience: "应届",
    welfare: ["导师带教", "转正机会"],
    responsibility: "参与内部工具与演示项目开发。",
    requirement: "每周至少到岗 4 天，有 Vue 或 Java 基础。",
  },
  {
    jobId: 922,
    jobName: "Go 微服务开发",
    companyName: "云链数据科技",
    salaryMin: 14000,
    salaryMax: 24000,
    location: "成都·天府新区",
    education: "本科",
    experience: "3-5年",
    welfare: ["五险一金", "股票期权"],
    responsibility: "负责订单与支付域微服务设计与实现。",
    requirement: "熟悉 Go、gRPC、Kubernetes 部署与排障。",
  },
];

export const MOCK_RESUME_LIST_ITEM = {
  resumeId: 7001,
  title: "校招-Java 开发",
  isDefault: true,
};

export const MOCK_RESUME_DETAIL = {
  resumeId: 7001,
  title: "校招-Java 开发",
  basicInfo: {
    name: "张同学",
    gender: "MALE",
    birthday: "2002-06-01",
    mobile: "138****5678",
    email: "candidate@example.com",
    currentCity: "成都",
  },
  jobIntention: {
    expectPosition: "Java 开发工程师",
    expectIndustry: "互联网",
    expectSalaryMin: 8000,
    expectSalaryMax: 15000,
    expectCity: "成都",
  },
  educationList: [
    {
      school: "四川大学",
      major: "软件工程",
      degree: "本科",
      startDate: "2020-09",
      endDate: "2024-06",
    },
  ],
  workList: [],
  skillList: ["Java", "Spring Boot", "MySQL", "Vue3"],
  selfEvaluation: "熟悉全栈协作，参与过校企招聘类项目前端与接口联调。",
  privacy: "ENTERPRISE_ONLY",
  attachmentList: [
    {
      attachmentId: 501,
      fileId: "mock-att-501",
      fileName: "个人简历.pdf",
    },
  ],
};

export const MOCK_PROFILE = {
  name: "张同学",
  mobile: "138****5678",
  email: "candidate@example.com",
  school: "四川大学",
  major: "软件工程",
  currentCity: "成都",
};

export const MOCK_APPLY_LIST = [
  {
    applyId: 3001,
    jobName: "Java 后端开发工程师",
    companyName: "成都校企科技有限公司",
    status: "SUBMITTED",
    applyTime: "2026-04-10 10:20:00",
  },
  {
    applyId: 3002,
    jobName: "前端开发工程师（Vue3）",
    companyName: "华地数字科技",
    status: "INVITED",
    applyTime: "2026-04-12 15:08:00",
  },
];

export const MOCK_MESSAGES = {
  list: [
    {
      messageId: 8001,
      title: "投递已送达",
      content: "您投递的「Java 后端开发工程师」已被企业查看。",
      createTime: "2026-04-11 09:00:00",
      readStatus: "READ",
    },
    {
      messageId: 8002,
      title: "面试邀约",
      content: "企业向您发起了面试邀约，请尽快在个人中心查看详情。",
      createTime: "2026-04-15 11:30:00",
      readStatus: "UNREAD",
    },
  ],
  unreadCount: 1,
};

export const MOCK_FAVORITE_JOBS = [
  {
    jobId: 903,
    jobName: "产品经理（校招方向）",
    companyName: "校企慧平台运营中心",
    location: "成都",
    salaryMin: 9000,
    salaryMax: 15000,
  },
];

export const MOCK_CANDIDATE_STATS = {
  applyCount: 12,
  interviewCount: 2,
  viewedCount: 28,
  favoriteCount: 5,
  applyTrend: [
    { day: "2026-04-14", count: 2 },
    { day: "2026-04-15", count: 1 },
    { day: "2026-04-16", count: 3 },
  ],
};

export const MOCK_ENTERPRISE_INFO = {
  companyName: "成都校企科技有限公司",
  industry: "互联网",
  scale: "20-99",
  address: "成都市郫都区合作路 88 号",
  introduction: "聚焦校企人才服务与数字化招聘。",
  website: "https://example.com",
  creditCode: "91510100MA6TEST001",
  legalPerson: "李四",
  licenseFileId: "mock-license",
  logoFileId: "mock-logo",
  licenseImage: "",
  logo: "",
  logoFileUrl: "",
};

export const MOCK_ENTERPRISE_AUTH = {
  authStatus: "PASS",
  rejectReason: "",
};

export const MOCK_ENTERPRISE_JOBS = [
  {
    jobId: 901,
    jobName: "Java 后端开发工程师",
    status: "RECRUITING",
    applyCount: 6,
    publishTime: "2026-04-01 10:00:00",
  },
  {
    jobId: 904,
    jobName: "测试工程师",
    status: "PENDING",
    applyCount: 0,
    publishTime: "2026-04-17 14:30:00",
  },
];

export const MOCK_ENTERPRISE_APPLIES = [
  {
    applyId: 4001,
    resumeId: 7001,
    candidateName: "张同学",
    jobName: "Java 后端开发工程师",
    status: "SUBMITTED",
  },
  {
    applyId: 4002,
    resumeId: 7102,
    candidateName: "李同学",
    jobName: "Java 后端开发工程师",
    status: "INVITED",
  },
];

export const MOCK_INTERVIEWS = [
  {
    interviewId: 6001,
    candidateName: "李同学",
    jobName: "Java 后端开发工程师",
    interviewTime: "2026-04-20 14:00:00",
    interviewType: "OFFLINE",
    status: "PENDING",
  },
];

export const MOCK_TALENTS = [
  {
    resumeId: 7102,
    candidateName: "李同学",
    expectPosition: "前端工程师",
    expectCity: "成都",
    education: "本科",
    school: "电子科技大学",
    major: "计算机科学与技术",
  },
];

export const MOCK_FAVORITE_RESUMES = [
  {
    resumeId: 7001,
    title: "校招-Java 开发",
    candidateName: "张同学",
    education: "本科",
    school: "四川大学",
    major: "软件工程",
    expectPosition: "Java 开发工程师",
    expectCity: "成都",
    favorited: true,
    applied: true,
  },
];

export const MOCK_ADMIN_CANDIDATES = [
  {
    userId: 101,
    realName: "张同学",
    mobile: "13812345678",
    identityType: "STUDENT",
    status: "ACTIVE",
  },
  {
    userId: 102,
    realName: "李同学",
    mobile: "13987654321",
    identityType: "STUDENT",
    status: "ACTIVE",
  },
];

export const MOCK_ADMIN_ENTERPRISES = [
  {
    enterpriseId: 201,
    companyName: "成都校企科技有限公司",
    industry: "互联网",
    authStatus: "PASS",
    status: "ACTIVE",
  },
  {
    enterpriseId: 202,
    companyName: "华地数字科技",
    industry: "软件",
    authStatus: "PENDING",
    status: "ACTIVE",
  },
];

export const MOCK_AUDIT_JOBS = [
  {
    jobId: 904,
    jobName: "测试工程师",
    companyName: "成都校企科技有限公司",
    status: "PENDING",
    auditRemark: "",
  },
];

export const MOCK_AUDIT_NOTICES = [
  {
    noticeId: 501,
    title: "春季双选会通知",
    type: "JOB_FAIR",
    status: "PENDING",
    content: "将于 5 月举办线下双选会，欢迎企业报名。",
    createTime: "2026-04-16 10:00:00",
  },
];

export const MOCK_CATEGORIES = [
  { categoryId: 1, parentId: 0, name: "互联网技术", code: "IT", sort: 1 },
  {
    categoryId: 2,
    parentId: 0,
    name: "智能制造",
    code: "MANUFACTURE",
    sort: 2,
  },
];

export const MOCK_BANNERS = [
  {
    bannerId: 1,
    title: "首页 Banner 示例",
    imageUrl: "https://picsum.photos/seed/xqh1/800/200",
    sort: 1,
    status: "ONLINE",
  },
];

export const MOCK_PERMISSIONS = [
  {
    permissionId: 1,
    permissionName: "用户管理",
    permissionCode: "user:manage",
  },
  { permissionId: 2, permissionName: "职位审核", permissionCode: "job:audit" },
];

export const MOCK_ROLES = [
  {
    roleId: 1,
    roleName: "超级管理员",
    roleCode: "SUPER_ADMIN",
    permissionIds: [1, 2],
  },
];

export const MOCK_MESSAGE_TEMPLATES = [
  {
    templateId: 1,
    type: "LOGIN",
    titleTemplate: "【校企慧】登录验证码 ${code}",
    contentTemplate: "您的验证码为 ${code}，${expireMinutes} 分钟内有效。",
    channels: "INSITE,EMAIL",
    channelList: ["INSITE", "EMAIL"],
    enabled: "ACTIVE",
  },
];

export const MOCK_AUDIT_LOGS = [
  {
    auditId: 9001,
    bizType: "JOB",
    bizId: 904,
    auditResult: "PASS",
    auditorName: "平台管理员",
    auditRemark: "职位描述合规",
    createTime: "2026-04-17 09:00:00",
  },
];

export const MOCK_OPERATION_LOGS = [
  {
    logId: 9101,
    userId: 1,
    userName: "平台管理员",
    action: "LOGIN",
    resource: "admin",
    detail: "管理员登录",
    ip: "127.0.0.1",
    createTime: "2026-04-18 08:30:00",
  },
];

export const MOCK_PLATFORM_OVERVIEW = {
  candidateCount: 320,
  enterpriseCount: 48,
  jobCount: 156,
  applyCount: 890,
  interviewCount: 120,
  employmentByArea: [{ area: "成都", count: 200 }],
  industryDistribution: [{ industry: "互联网", count: 80 }],
};

export function buildCandidateDetail(userId: number) {
  return {
    userId,
    name: userId === 101 ? "张同学" : "李同学",
    mobile: userId === 101 ? "13812345678" : "13987654321",
    school: "四川大学",
    major: "软件工程",
    status: "ACTIVE",
    resumeCount: 1,
    resumes: [
      {
        resumeId: userId === 101 ? 7001 : 7102,
        title: userId === 101 ? "校招-Java 开发" : "校招-前端",
        privacy: "PUBLIC",
        violationDetected: false,
        violationKeywords: [] as string[],
      },
    ],
  };
}

export const MOCK_TALENT_DETAIL = {
  resumeId: 7102,
  candidateName: "李同学",
  basicInfo: { mobile: "139****4321", email: "li@example.com" },
  jobIntention: { expectPosition: "前端工程师", expectCity: "成都" },
  educationList: [{ school: "电子科技大学", major: "计算机", degree: "本科" }],
};

export const MOCK_ENTERPRISE_RESUME_DETAIL = {
  resumeId: 7001,
  favorited: true,
  basicInfo: MOCK_RESUME_DETAIL.basicInfo,
  jobIntention: MOCK_RESUME_DETAIL.jobIntention,
  educationList: MOCK_RESUME_DETAIL.educationList,
  skillList: MOCK_RESUME_DETAIL.skillList,
  selfEvaluation: MOCK_RESUME_DETAIL.selfEvaluation,
};
