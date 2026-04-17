package main.xiaoqihui.recruit;

import java.time.LocalDateTime;

public class JobEntity {
    private Long jobId;
    private Long enterpriseId;
    private String jobName;
    private String jobCategory;
    private String responsibility;
    private String requirementText;
    private Integer salaryMin;
    private Integer salaryMax;
    private String location;
    private Integer headCount;
    private String education;
    private String experience;
    private String welfare;
    private String contactName;
    private String contactMobile;
    private String status;
    private LocalDateTime publishTime;
    private LocalDateTime refreshTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String companyName;
    private String companyLogo;
    private String companyIndustry;

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getJobCategory() { return jobCategory; }
    public void setJobCategory(String jobCategory) { this.jobCategory = jobCategory; }
    public String getResponsibility() { return responsibility; }
    public void setResponsibility(String responsibility) { this.responsibility = responsibility; }
    public String getRequirementText() { return requirementText; }
    public void setRequirementText(String requirementText) { this.requirementText = requirementText; }
    public Integer getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Integer salaryMin) { this.salaryMin = salaryMin; }
    public Integer getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Integer salaryMax) { this.salaryMax = salaryMax; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getHeadCount() { return headCount; }
    public void setHeadCount(Integer headCount) { this.headCount = headCount; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getWelfare() { return welfare; }
    public void setWelfare(String welfare) { this.welfare = welfare; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPublishTime() { return publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
    public LocalDateTime getRefreshTime() { return refreshTime; }
    public void setRefreshTime(LocalDateTime refreshTime) { this.refreshTime = refreshTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyLogo() { return companyLogo; }
    public void setCompanyLogo(String companyLogo) { this.companyLogo = companyLogo; }
    public String getCompanyIndustry() { return companyIndustry; }
    public void setCompanyIndustry(String companyIndustry) { this.companyIndustry = companyIndustry; }
}
