import http, { downloadBinaryFile } from "./http";

export function sendEmailCode(payload: Record<string, unknown>) {
  return http.post("/common/email/send", payload);
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

export function candidateEmailLogin(payload: Record<string, unknown>) {
  return http.post("/user/login/email", payload);
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

export function deleteResume(resumeId: number) {
  return http.delete(`/resume/${resumeId}`);
}

export function setDefaultResume(resumeId: number) {
  return http.put(`/resume/${resumeId}/default`);
}

export function updateResumePrivacy(
  resumeId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/resume/${resumeId}/privacy`, payload);
}

export function exportResumePdf(resumeId: number) {
  return http.get(`/resume/${resumeId}/export/pdf`);
}

export function addResumeAttachment(
  resumeId: number,
  payload: Record<string, unknown>
) {
  return http.post(`/resume/${resumeId}/attachment`, payload);
}

export function deleteResumeAttachment(resumeId: number, attachmentId: number) {
  return http.delete(`/resume/${resumeId}/attachment/${attachmentId}`);
}

export function applyJob(payload: Record<string, unknown>) {
  return http.post("/apply", payload);
}

export function batchApplyJobs(payload: Record<string, unknown>) {
  return http.post("/apply/batch", payload);
}

export function collectJob(jobId: number) {
  return http.post(`/jobs/${jobId}/favorite`);
}

export function uncollectJob(jobId: number) {
  return http.delete(`/jobs/${jobId}/favorite`);
}

export function listCollectedJobs(params: Record<string, unknown>) {
  return http.get("/jobs/favorites", { params });
}

export function listMyApplies(params: Record<string, unknown>) {
  return http.get("/apply/my", { params });
}

export function listMessages(params: Record<string, unknown>) {
  return http.get("/messages", { params });
}

export function getMessageDetail(messageId: number) {
  return http.get(`/messages/${messageId}`);
}

export function markMessagesRead(payload?: Record<string, unknown>) {
  return http.put("/messages/read", payload || {});
}

export function deleteMessages(payload: Record<string, unknown>) {
  return http.delete("/messages", { data: payload });
}

export function getUnreadMessageCount() {
  return http.get("/messages/unread/count");
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

export function downloadGeneratedFile(fileId: string, fileName?: string) {
  return downloadBinaryFile(
    `/recruit/api/v1/common/file/download/${fileId}`,
    fileName
  );
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

export function offlineEnterpriseJob(jobId: number) {
  return http.put(`/enterprise/jobs/${jobId}/offline`);
}

export function refreshEnterpriseJob(jobId: number) {
  return http.put(`/enterprise/jobs/${jobId}/refresh`);
}

export function deleteEnterpriseJob(jobId: number) {
  return http.delete(`/enterprise/jobs/${jobId}`);
}

export function listEnterpriseJobs(params: Record<string, unknown>) {
  return http.get("/enterprise/jobs", { params });
}

export function previewEnterpriseJob(jobId: number) {
  return http.get(`/enterprise/jobs/${jobId}/preview`);
}

export function shareEnterpriseJob(jobId: number) {
  return http.get(`/enterprise/jobs/${jobId}/share`);
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

export function batchUpdateEnterpriseApplyStatus(
  payload: Record<string, unknown>
) {
  return http.put("/enterprise/applies/status/batch", payload);
}

export function createEnterpriseInterview(payload: Record<string, unknown>) {
  return http.post("/enterprise/interviews", payload);
}

export function listEnterpriseInterviews(params: Record<string, unknown>) {
  return http.get("/enterprise/interviews", { params });
}

export function searchEnterpriseTalents(params: Record<string, unknown>) {
  return http.get("/enterprise/talents/search", { params });
}

export function getEnterpriseTalentDetail(resumeId: number) {
  return http.get(`/enterprise/talents/${resumeId}`);
}

export function contactEnterpriseTalent(payload: Record<string, unknown>) {
  return http.post("/enterprise/talents/contact", payload);
}

export function getEnterpriseResumeDetail(resumeId: number) {
  return http.get(`/enterprise/resumes/${resumeId}`);
}

export function exportEnterpriseResumePdf(resumeId: number) {
  return http.get(`/enterprise/resumes/${resumeId}/export/pdf`);
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

export function listBannersAdmin() {
  return http.get("/admin/system/banners");
}

export function createBannerAdmin(payload: Record<string, unknown>) {
  return http.post("/admin/system/banners", payload);
}

export function deleteBannerAdmin(bannerId: number) {
  return http.delete(`/admin/system/banners/${bannerId}`);
}

export function listPermissionsAdmin() {
  return http.get("/admin/system/permissions");
}

export function listRolesAdmin() {
  return http.get("/admin/system/roles");
}

export function createRoleAdmin(payload: Record<string, unknown>) {
  return http.post("/admin/system/roles", payload);
}

export function deleteRoleAdmin(roleId: number) {
  return http.delete(`/admin/system/roles/${roleId}`);
}

export function listMessageTemplatesAdmin(params?: Record<string, unknown>) {
  return http.get("/admin/system/message/templates", { params });
}

export function updateMessageTemplateAdmin(
  templateId: number,
  payload: Record<string, unknown>
) {
  return http.put(`/admin/system/message/templates/${templateId}`, payload);
}

export function deleteSystemNotice(noticeId: number) {
  return http.delete(`/admin/system/notices/${noticeId}`);
}

export function listAuditLogs(params: Record<string, unknown>) {
  return http.get("/admin/system/logs/audit", { params });
}
