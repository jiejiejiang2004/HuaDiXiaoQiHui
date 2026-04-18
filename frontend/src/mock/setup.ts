import type { AxiosInstance, AxiosRequestConfig } from "axios";
import MockAdapter from "axios-mock-adapter";
import * as D from "./data";

function ok<T>(data: T): [number, { code: number; message: string; data: T }] {
  return [200, { code: 0, message: "ok", data }];
}

function normPath(config: AxiosRequestConfig): string {
  const raw = config.url || "";
  const path = raw.split("?")[0];
  if (path.startsWith("http")) {
    try {
      const u = new URL(path).pathname;
      return u.replace(/^\/recruit\/api\/v1\/?/, "/") || "/";
    } catch {
      return raw;
    }
  }
  if (path.startsWith("/recruit/api/v1")) {
    return path.slice("/recruit/api/v1".length) || "/";
  }
  return path.startsWith("/") ? path : `/${path}`;
}

const collectedJobIds = new Set<number>();
const favoritedResumeIds = new Set<number>([7001]);

function jobsWithCollectFlags() {
  return D.MOCK_JOBS.map((j) => ({
    ...j,
    collected: collectedJobIds.has(j.jobId),
  }));
}

function jobDetail(jobId: number) {
  const base = D.MOCK_JOBS.find((j) => j.jobId === jobId);
  if (!base) {
    return null;
  }
  return { ...base, collected: collectedJobIds.has(jobId) };
}

/** 与首页 searchJobs 传参一致：支持 config.params 或拼在 url 上的 query */
function getSearchParam(config: AxiosRequestConfig, key: string): string {
  const params = config.params as Record<string, unknown> | undefined;
  if (params && Object.prototype.hasOwnProperty.call(params, key)) {
    const v = params[key];
    if (v === undefined || v === null) {
      return "";
    }
    return String(v).trim();
  }
  const raw = config.url || "";
  const qIdx = raw.indexOf("?");
  if (qIdx === -1) {
    return "";
  }
  return new URLSearchParams(raw.slice(qIdx + 1)).get(key)?.trim() || "";
}

function mockJobSearch(config: AxiosRequestConfig): {
  list: ReturnType<typeof jobsWithCollectFlags>;
  total: number;
} {
  const keyword = getSearchParam(config, "keyword").toLowerCase();
  const education = getSearchParam(config, "education");
  const experience = getSearchParam(config, "experience");
  const location = getSearchParam(config, "location").toLowerCase();
  const pageNum = Math.max(1, Number(getSearchParam(config, "pageNum")) || 1);
  const pageSize = Math.min(
    50,
    Math.max(1, Number(getSearchParam(config, "pageSize")) || 10)
  );

  let list = jobsWithCollectFlags();
  if (keyword) {
    list = list.filter(
      (j) =>
        j.jobName.toLowerCase().includes(keyword) ||
        j.companyName.toLowerCase().includes(keyword)
    );
  }
  if (education) {
    list = list.filter((j) => j.education === education);
  }
  if (experience) {
    list = list.filter((j) => j.experience === experience);
  }
  if (location) {
    list = list.filter((j) => j.location.toLowerCase().includes(location));
  }

  const total = list.length;
  const start = (pageNum - 1) * pageSize;
  return { list: list.slice(start, start + pageSize), total };
}

export function setupMocks(api: AxiosInstance): void {
  const mock = new MockAdapter(api, { delayResponse: 120 });

  mock.onAny().reply((config) => {
    const method = (config.method || "get").toUpperCase();
    const path = normPath(config);

    const tokens = () => ({
      accessToken: "mock-access-token",
      refreshToken: "mock-refresh-token",
    });

    if (method === "POST" && path === "/common/email/send") {
      return ok({ debugCode: "123456", expireSeconds: 300 });
    }

    if (method === "POST" && path === "/common/file/upload") {
      return ok({
        fileId: `mock-file-${Date.now()}`,
        fileUrl: "https://picsum.photos/seed/upload/400/300",
      });
    }

    if (method === "GET" && /^\/common\/dict\/.+/.test(path)) {
      return ok([{ label: "示例", value: "DEMO" }]);
    }

    if (method === "POST" && path === "/user/login/password") {
      return ok({
        ...tokens(),
        userName: "张同学（Mock）",
      });
    }

    if (method === "POST" && path === "/user/login/email") {
      return ok({
        ...tokens(),
        userName: "张同学（邮箱登录 Mock）",
      });
    }

    if (method === "POST" && path === "/user/register") {
      return ok({
        ...tokens(),
        userName: "新注册用户",
      });
    }

    if (method === "POST" && path === "/enterprise/login") {
      return ok({
        ...tokens(),
        userName: "成都校企科技有限公司",
      });
    }

    if (method === "POST" && path === "/enterprise/register") {
      return ok({
        ...tokens(),
        userName: "新注册企业",
      });
    }

    if (method === "POST" && path === "/admin/login") {
      return ok({
        ...tokens(),
        userName: "平台管理员（Mock）",
      });
    }

    if (method === "POST" && path === "/user/password/reset") {
      return ok(null);
    }

    if (method === "POST" && path === "/enterprise/password/reset") {
      return ok(null);
    }

    if (method === "POST" && path === "/user/token/refresh") {
      return ok({
        accessToken: "mock-access-token-refreshed",
        refreshToken: "mock-refresh-token",
      });
    }

    if (method === "POST" && path === "/user/logout") {
      return ok(null);
    }

    if (method === "GET" && path === "/jobs/search") {
      return ok(mockJobSearch(config));
    }

    if (method === "GET" && /^\/jobs\/\d+$/.test(path)) {
      const jobId = Number(path.split("/").pop());
      const detail = jobDetail(jobId);
      return detail
        ? ok(detail)
        : [404, { code: 404, message: "职位不存在", data: null }];
    }

    if (method === "POST" && /^\/jobs\/\d+\/favorite$/.test(path)) {
      const jobId = Number(path.split("/")[2]);
      collectedJobIds.add(jobId);
      return ok(null);
    }

    if (method === "DELETE" && /^\/jobs\/\d+\/favorite$/.test(path)) {
      const jobId = Number(path.split("/")[2]);
      collectedJobIds.delete(jobId);
      return ok(null);
    }

    if (method === "GET" && path === "/jobs/favorites") {
      const list = D.MOCK_FAVORITE_JOBS;
      return ok({ list, total: list.length });
    }

    if (method === "POST" && path === "/apply") {
      return ok({ applyId: 9999 });
    }

    if (method === "POST" && path === "/apply/batch") {
      let body: { jobIds?: number[] } = {};
      try {
        body = JSON.parse(config.data as string) || {};
      } catch {
        body = {};
      }
      const n = body.jobIds?.length ?? 0;
      return ok({ successCount: n });
    }

    if (method === "GET" && path === "/apply/my") {
      return ok({ list: D.MOCK_APPLY_LIST, total: D.MOCK_APPLY_LIST.length });
    }

    if (method === "GET" && path === "/user/profile") {
      return ok(D.MOCK_PROFILE);
    }

    if (method === "PUT" && path === "/user/profile") {
      return ok(null);
    }

    if (method === "GET" && path === "/resume/my") {
      return ok({ list: [D.MOCK_RESUME_LIST_ITEM], total: 1 });
    }

    if (method === "GET" && /^\/resume\/\d+$/.test(path)) {
      return ok(D.MOCK_RESUME_DETAIL);
    }

    if (method === "POST" && path === "/resume") {
      return ok({ resumeId: 7001 });
    }

    if (
      method === "PUT" &&
      /^\/resume\/\d+$/.test(path) &&
      !path.includes("/default") &&
      !path.includes("/privacy")
    ) {
      return ok(null);
    }

    if (method === "PUT" && /^\/resume\/\d+\/default$/.test(path)) {
      return ok(null);
    }

    if (method === "PUT" && /^\/resume\/\d+\/privacy$/.test(path)) {
      return ok(null);
    }

    if (method === "DELETE" && /^\/resume\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && /^\/resume\/\d+\/export\/pdf$/.test(path)) {
      return ok({ fileId: "mock-pdf-export", fileName: "resume.pdf" });
    }

    if (method === "POST" && /^\/resume\/\d+\/attachment$/.test(path)) {
      return ok(null);
    }

    if (method === "DELETE" && /^\/resume\/\d+\/attachment\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/messages") {
      return ok(D.MOCK_MESSAGES);
    }

    if (method === "GET" && /^\/messages\/\d+$/.test(path)) {
      const id = Number(path.split("/").pop());
      const row = D.MOCK_MESSAGES.list.find((m) => m.messageId === id);
      return ok(row || D.MOCK_MESSAGES.list[0]);
    }

    if (method === "PUT" && path === "/messages/read") {
      return ok(null);
    }

    if (method === "DELETE" && path === "/messages") {
      return ok(null);
    }

    if (method === "GET" && path === "/messages/unread/count") {
      return ok({ total: 1 });
    }

    if (method === "GET" && path === "/statistics/candidate") {
      return ok(D.MOCK_CANDIDATE_STATS);
    }

    if (method === "GET" && path === "/statistics/enterprise") {
      return ok({
        jobViewCount: 1200,
        resumeReceivedCount: 86,
        interviewCount: 14,
        activeJobCount: 8,
      });
    }

    if (method === "GET" && path === "/statistics/platform/overview") {
      return ok(D.MOCK_PLATFORM_OVERVIEW);
    }

    if (method === "POST" && path === "/statistics/export") {
      return ok({ fileId: "mock-stat-export", fileName: "stats.xlsx" });
    }

    if (method === "GET" && path === "/enterprise/info") {
      return ok({ ...D.MOCK_ENTERPRISE_INFO, ...D.MOCK_ENTERPRISE_AUTH });
    }

    if (method === "GET" && path === "/enterprise/auth/status") {
      return ok(D.MOCK_ENTERPRISE_AUTH);
    }

    if (method === "PUT" && path === "/enterprise/info") {
      return ok(null);
    }

    if (method === "POST" && path === "/enterprise/auth") {
      return ok({ authStatus: "PENDING" });
    }

    if (method === "POST" && path === "/enterprise/jobs") {
      return ok({ jobId: 9100 });
    }

    if (
      method === "PUT" &&
      /^\/enterprise\/jobs\/\d+$/.test(path) &&
      !path.includes("/offline") &&
      !path.includes("/refresh")
    ) {
      return ok(null);
    }

    if (method === "PUT" && /^\/enterprise\/jobs\/\d+\/offline$/.test(path)) {
      return ok(null);
    }

    if (method === "PUT" && /^\/enterprise\/jobs\/\d+\/refresh$/.test(path)) {
      return ok(null);
    }

    if (method === "DELETE" && /^\/enterprise\/jobs\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/enterprise/jobs") {
      return ok({
        list: D.MOCK_ENTERPRISE_JOBS,
        total: D.MOCK_ENTERPRISE_JOBS.length,
      });
    }

    if (method === "GET" && /^\/enterprise\/jobs\/\d+\/preview$/.test(path)) {
      const jobId = Number(path.split("/")[3]);
      const j =
        D.MOCK_ENTERPRISE_JOBS.find((x) => x.jobId === jobId) ||
        D.MOCK_ENTERPRISE_JOBS[0];
      return ok({ ...j, jobName: j.jobName + "（预览）" });
    }

    if (method === "GET" && /^\/enterprise\/jobs\/\d+\/share$/.test(path)) {
      return ok({ shareUrl: `/jobs/share/mock-${path.split("/")[3]}` });
    }

    if (method === "GET" && path === "/enterprise/applies") {
      return ok({
        list: D.MOCK_ENTERPRISE_APPLIES,
        total: D.MOCK_ENTERPRISE_APPLIES.length,
      });
    }

    if (method === "PUT" && /^\/enterprise\/applies\/\d+\/status$/.test(path)) {
      return ok(null);
    }

    if (method === "PUT" && path === "/enterprise/applies/status/batch") {
      return ok(null);
    }

    if (method === "POST" && path === "/enterprise/interviews") {
      return ok({ interviewId: 6002 });
    }

    if (method === "GET" && path === "/enterprise/interviews") {
      return ok({ list: D.MOCK_INTERVIEWS, total: D.MOCK_INTERVIEWS.length });
    }

    if (method === "GET" && path === "/enterprise/talents/search") {
      return ok({ list: D.MOCK_TALENTS, total: D.MOCK_TALENTS.length });
    }

    if (method === "GET" && /^\/enterprise\/talents\/\d+$/.test(path)) {
      return ok(D.MOCK_TALENT_DETAIL);
    }

    if (method === "POST" && path === "/enterprise/talents/contact") {
      return ok(null);
    }

    if (
      method === "GET" &&
      /^\/enterprise\/resumes\/\d+$/.test(path) &&
      !path.includes("/export")
    ) {
      return ok(D.MOCK_ENTERPRISE_RESUME_DETAIL);
    }

    if (
      method === "POST" &&
      /^\/enterprise\/resumes\/\d+\/favorite$/.test(path)
    ) {
      const id = Number(path.split("/")[3]);
      favoritedResumeIds.add(id);
      return ok(null);
    }

    if (
      method === "DELETE" &&
      /^\/enterprise\/resumes\/\d+\/favorite$/.test(path)
    ) {
      const id = Number(path.split("/")[3]);
      favoritedResumeIds.delete(id);
      return ok(null);
    }

    if (method === "GET" && path === "/enterprise/resumes/favorites") {
      const list = D.MOCK_FAVORITE_RESUMES.map((r) => ({
        ...r,
        favorited: favoritedResumeIds.has(r.resumeId),
      }));
      return ok({ list, total: list.length });
    }

    if (method === "GET" && path === "/enterprise/resumes/search") {
      const list = D.MOCK_FAVORITE_RESUMES.map((r) => ({
        ...r,
        favorited: favoritedResumeIds.has(r.resumeId),
      }));
      return ok({ list, total: list.length });
    }

    if (method === "POST" && path === "/enterprise/resumes/export") {
      return ok({ fileId: "mock-resume-zip", fileName: "resumes.zip" });
    }

    if (
      method === "GET" &&
      /^\/enterprise\/resumes\/\d+\/export\/pdf$/.test(path)
    ) {
      return ok({ fileId: "mock-ent-resume-pdf", fileName: "candidate.pdf" });
    }

    if (method === "GET" && path === "/admin/users/candidates") {
      return ok({
        list: D.MOCK_ADMIN_CANDIDATES,
        total: D.MOCK_ADMIN_CANDIDATES.length,
      });
    }

    if (method === "GET" && /^\/admin\/users\/candidates\/\d+$/.test(path)) {
      const userId = Number(path.split("/").pop());
      return ok(D.buildCandidateDetail(userId));
    }

    if (
      method === "PUT" &&
      /^\/admin\/users\/candidates\/\d+\/status$/.test(path)
    ) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/users/enterprises") {
      return ok({
        list: D.MOCK_ADMIN_ENTERPRISES,
        total: D.MOCK_ADMIN_ENTERPRISES.length,
      });
    }

    if (
      method === "PUT" &&
      /^\/admin\/users\/enterprises\/\d+\/audit$/.test(path)
    ) {
      return ok(null);
    }

    if (
      method === "PUT" &&
      /^\/admin\/users\/enterprises\/\d+\/status$/.test(path)
    ) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/audit/jobs") {
      return ok({ list: D.MOCK_AUDIT_JOBS, total: D.MOCK_AUDIT_JOBS.length });
    }

    if (method === "PUT" && /^\/admin\/audit\/jobs\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/audit/notices") {
      return ok({
        list: D.MOCK_AUDIT_NOTICES,
        total: D.MOCK_AUDIT_NOTICES.length,
      });
    }

    if (method === "POST" && path === "/admin/system/notices") {
      return ok({ noticeId: 502 });
    }

    if (method === "PUT" && /^\/admin\/audit\/notices\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "DELETE" && /^\/admin\/system\/notices\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/system/categories") {
      return ok({ list: D.MOCK_CATEGORIES, total: D.MOCK_CATEGORIES.length });
    }

    if (method === "POST" && path === "/admin/system/categories") {
      return ok({ categoryId: 99 });
    }

    if (method === "PUT" && /^\/admin\/system\/categories\/\d+$/.test(path)) {
      return ok(null);
    }

    if (
      method === "DELETE" &&
      /^\/admin\/system\/categories\/\d+$/.test(path)
    ) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/system/banners") {
      return ok({ list: D.MOCK_BANNERS, total: D.MOCK_BANNERS.length });
    }

    if (method === "POST" && path === "/admin/system/banners") {
      return ok({ bannerId: 2 });
    }

    if (method === "DELETE" && /^\/admin\/system\/banners\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/system/permissions") {
      return ok({ list: D.MOCK_PERMISSIONS, total: D.MOCK_PERMISSIONS.length });
    }

    if (method === "GET" && path === "/admin/system/roles") {
      return ok({ list: D.MOCK_ROLES, total: D.MOCK_ROLES.length });
    }

    if (method === "POST" && path === "/admin/system/roles") {
      return ok({ roleId: 3 });
    }

    if (method === "DELETE" && /^\/admin\/system\/roles\/\d+$/.test(path)) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/system/message/templates") {
      return ok({
        list: D.MOCK_MESSAGE_TEMPLATES,
        total: D.MOCK_MESSAGE_TEMPLATES.length,
      });
    }

    if (
      method === "PUT" &&
      /^\/admin\/system\/message\/templates\/\d+$/.test(path)
    ) {
      return ok(null);
    }

    if (method === "GET" && path === "/admin/system/logs/audit") {
      return ok({ list: D.MOCK_AUDIT_LOGS, total: D.MOCK_AUDIT_LOGS.length });
    }

    if (method === "GET" && path === "/admin/system/logs/operation") {
      return ok({
        list: D.MOCK_OPERATION_LOGS,
        total: D.MOCK_OPERATION_LOGS.length,
      });
    }

    if (
      method === "PUT" &&
      /^\/admin\/audit\/resumes\/\d+\/moderate$/.test(path)
    ) {
      return ok(null);
    }

    if (process.env.NODE_ENV === "development") {
      // eslint-disable-next-line no-console -- 便于补齐未实现的 mock 路由
      console.warn("[mock] 未匹配请求:", method, path, config.url);
    }
    return ok(null);
  });
}
