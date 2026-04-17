import http from "./http";

export function sendSmsCode(payload: Record<string, unknown>) {
  return http.post("/common/sms/send", payload);
}

export function uploadCommonFile(file: File, bizType: string) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("bizType", bizType);
  return http.post("/common/file/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export function getDict(dictType: string) {
  return http.get(`/common/dict/${dictType}`);
}

export function candidateRegister(payload: Record<string, unknown>) {
  return http.post("/user/register", payload);
}

export function candidateLogin(payload: Record<string, unknown>) {
  return http.post("/user/login/password", payload);
}

export function candidateSmsLogin(payload: Record<string, unknown>) {
  return http.post("/user/login/sms", payload);
}

export function resetPassword(payload: Record<string, unknown>) {
  return http.post("/user/password/reset", payload);
}

export function refreshToken(payload: Record<string, unknown>) {
  return http.post("/user/token/refresh", payload);
}

export function logoutRequest() {
  return http.post("/user/logout");
}

export function enterpriseRegister(payload: Record<string, unknown>) {
  return http.post("/enterprise/register", payload);
}

export function adminLogin(payload: Record<string, unknown>) {
  return http.post("/admin/login", payload);
}

export function enterpriseLogin(payload: Record<string, unknown>) {
  return http.post("/enterprise/login", payload);
}

export function resetEnterprisePassword(payload: Record<string, unknown>) {
  return http.post("/enterprise/password/reset", payload);
}

export function getProfile() {
  return http.get("/user/profile");
}

export function updateProfile(payload: Record<string, unknown>) {
  return http.put("/user/profile", payload);
}

export function searchJobs(params: Record<string, unknown>) {
  return http.get("/jobs/search", { params });
}

export function getJobDetail(jobId: number) {
  return http.get(`/jobs/${jobId}`);
}

export function listResumes() {
  return http.get("/resume/my");
}

export function createResume(payload: Record<string, unknown>) {
  return http.post("/resume", payload);
}

export function updateResume(
  resumeId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/resume/${resumeId}`, payload);
}

export function getResumeDetail(resumeId: number) {
  return http.get(`/resume/${resumeId}`);
}

export function applyJob(payload: Record<string, unknown>) {
  return http.post("/apply", payload);
}

export function listMyApplies(params: Record<string, unknown>) {
  return http.get("/apply/my", { params });
}

export function listMessages(params: Record<string, unknown>) {
  return http.get("/messages", { params });
}

export function markMessagesRead(payload?: Record<string, unknown>) {
  return http.put("/messages/read", payload || {});
}

export function getEnterpriseInfo() {
  return http.get("/enterprise/info");
}

export function submitEnterpriseAuth(payload: Record<string, unknown>) {
  return http.post("/enterprise/auth", payload);
}

export function getEnterpriseAuthStatus() {
  return http.get("/enterprise/auth/status");
}

export function getCandidateStatistics() {
  return http.get("/statistics/candidate");
}

export function getEnterpriseStatistics() {
  return http.get("/statistics/enterprise");
}

export function getPlatformOverviewStatistics() {
  return http.get("/statistics/platform/overview");
}

export function exportStatistics(payload: Record<string, unknown>) {
  return http.post("/statistics/export", payload);
}

export function updateEnterpriseInfo(payload: Record<string, unknown>) {
  return http.put("/enterprise/info", payload);
}

export function createEnterpriseJob(payload: Record<string, unknown>) {
  return http.post("/enterprise/jobs", payload);
}

export function updateEnterpriseJob(
  jobId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/enterprise/jobs/${jobId}`, payload);
}

export function listEnterpriseJobs(params: Record<string, unknown>) {
  return http.get("/enterprise/jobs", { params });
}

export function listEnterpriseApplies(params: Record<string, unknown>) {
  return http.get("/enterprise/applies", { params });
}

export function updateEnterpriseApplyStatus(
  applyId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/enterprise/applies/${applyId}/status`, payload);
}

export function getEnterpriseResumeDetail(resumeId: number) {
  return http.get(`/enterprise/resumes/${resumeId}`);
}

export function listCandidates(params: Record<string, unknown>) {
  return http.get("/admin/users/candidates", { params });
}

export function getCandidateDetail(userId: number) {
  return http.get(`/admin/users/candidates/${userId}`);
}

export function updateCandidateStatus(
  userId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/users/candidates/${userId}/status`, payload);
}

export function listEnterprisesAdmin(params: Record<string, unknown>) {
  return http.get("/admin/users/enterprises", { params });
}

export function auditEnterprise(
  enterpriseId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/users/enterprises/${enterpriseId}/audit`, payload);
}

export function updateEnterpriseStatusAdmin(
  enterpriseId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/users/enterprises/${enterpriseId}/status`, payload);
}

export function listAuditJobs(params: Record<string, unknown>) {
  return http.get("/admin/audit/jobs", { params });
}

export function auditJob(jobId: number, payload: Record<string, unknown>) {
  return http.put(`/admin/audit/jobs/${jobId}`, payload);
}

export function listAuditNotices(params: Record<string, unknown>) {
  return http.get("/admin/audit/notices", { params });
}

export function auditNotice(
  noticeId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/audit/notices/${noticeId}`, payload);
}

export function listCategoriesAdmin(params?: Record<string, unknown>) {
  return http.get("/admin/system/categories", { params });
}

export function createCategoryAdmin(payload: Record<string, unknown>) {
  return http.post("/admin/system/categories", payload);
}

export function updateCategoryAdmin(
  categoryId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/system/categories/${categoryId}`, payload);
}

export function deleteCategoryAdmin(categoryId: number) {
  return http.delete(`/admin/system/categories/${categoryId}`);
}

export function listSystemNotices(params: Record<string, unknown>) {
  return http.get("/admin/system/notices", { params });
}

export function createSystemNotice(payload: Record<string, unknown>) {
  return http.post("/admin/system/notices", payload);
}

export function deleteSystemNotice(noticeId: number) {
  return http.delete(`/admin/system/notices/${noticeId}`);
}

export function listAuditLogs(params: Record<string, unknown>) {
  return http.get("/admin/system/logs/audit", { params });
}
