package main.xiaoqihui.statistics;

import java.time.LocalDateTime;

class DailyCount {
    private String day;
    private Long count;
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}

class EnterpriseTrendRow {
    private String day;
    private Long applyCount;
    private Long interviewCount;
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public Long getApplyCount() { return applyCount; }
    public void setApplyCount(Long applyCount) { this.applyCount = applyCount; }
    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }
}

class JobStatRow {
    private Long jobId;
    private String jobName;
    private String status;
    private Integer viewCount;
    private Long applyCount;
    private Long interviewCount;
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Long getApplyCount() { return applyCount; }
    public void setApplyCount(Long applyCount) { this.applyCount = applyCount; }
    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }
}

class IndustryDistRow {
    private String industry;
    private Long jobCount;
    private Long applyCount;
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public Long getJobCount() { return jobCount; }
    public void setJobCount(Long jobCount) { this.jobCount = jobCount; }
    public Long getApplyCount() { return applyCount; }
    public void setApplyCount(Long applyCount) { this.applyCount = applyCount; }
}

class EducationDistRow {
    private String education;
    private Long count;
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}

class AreaDistRow {
    private String area;
    private Long count;
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}

class ExportTaskEntity {
    private String taskId;
    private String taskType;
    private Long userId;
    private String fileId;
    private String downloadUrl;
    private String status;
    private LocalDateTime createTime;
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
