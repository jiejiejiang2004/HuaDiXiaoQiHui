/** Swan 首页与职位相关组件共用的数据结构 */

export interface SwanJobSummary {
  jobId: number;
  jobName: string;
  companyName: string;
  salaryMin: number;
  salaryMax: number;
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
