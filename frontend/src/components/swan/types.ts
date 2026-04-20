/** Swan 首页与职位相关组件共用的数据结构 */

export interface SwanJobSummary {
  jobId: number;
  jobName: string;
  companyName: string;
  salaryMin: number;
  salaryMax: number;
  /** 全职 / 兼职 / 实习 等，用于卡片角标 */
  jobType?: string;
  location: string;
  education: string;
  experience: string;
  welfare?: string[];
  responsibility?: string;
  requirement?: string;
  collected?: boolean;
  /** 详情页：发布时间、截止、职级、精选等（后端可选） */
  jobPostedAt?: string;
  jobExpireAt?: string;
  jobLevel?: string;
  featured?: boolean;
  /** 加分项 / 优先条件 */
  desirable?: string;
}

export interface SwanJobFilters {
  keyword: string;
  education: string;
  experience: string;
  location: string;
}
