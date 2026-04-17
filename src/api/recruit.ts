import http from "./http";

export function candidateRegister(payload: Record<string, unknown>) {
  return http.post("/user/register", payload);
}

export function candidateLogin(payload: Record<string, unknown>) {
  return http.post("/user/login/password", payload);
}

export function enterpriseRegister(payload: Record<string, unknown>) {
  return http.post("/enterprise/register", payload);
}

export function enterpriseLogin(payload: Record<string, unknown>) {
  return http.post("/enterprise/login", payload);
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
