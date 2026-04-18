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
}

export interface SwanJobFilters {
  keyword: string;
  education: string;
  experience: string;
  location: string;
}
