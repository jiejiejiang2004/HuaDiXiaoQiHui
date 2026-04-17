package main.xiaoqihui.admin;

import java.time.LocalDateTime;

class AdminCandidateView {
    private Long userId;
    private String mobile;
    private String realName;
    private String identityType;
    private String status;
    private String school;
    private String major;
    private String currentCity;
    private LocalDateTime createTime;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getIdentityType() { return identityType; }
    public void setIdentityType(String identityType) { this.identityType = identityType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

class AdminEnterpriseView {
    private Long enterpriseId;
    private Long userId;
    private String companyName;
    private String industry;
    private String scale;
    private String address;
    private String introduction;
    private String authStatus;
    private String authRemark;
    private String status;
    private String contactMobile;
    private LocalDateTime createTime;

    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getScale() { return scale; }
    public void setScale(String scale) { this.scale = scale; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String authStatus) { this.authStatus = authStatus; }
    public String getAuthRemark() { return authRemark; }
    public void setAuthRemark(String authRemark) { this.authRemark = authRemark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

class AdminJobAuditView {
    private Long jobId;
    private Long enterpriseId;
    private String companyName;
    private String jobName;
    private String jobCategory;
    private String location;
    private Integer salaryMin;
    private Integer salaryMax;
    private String status;
    private String auditRemark;
    private LocalDateTime createTime;

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getJobCategory() { return jobCategory; }
    public void setJobCategory(String jobCategory) { this.jobCategory = jobCategory; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Integer salaryMin) { this.salaryMin = salaryMin; }
    public Integer getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Integer salaryMax) { this.salaryMax = salaryMax; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

class AdminNoticeEntity {
    private Long noticeId;
    private String title;
    private String content;
    private String type;
    private String status;
    private String auditRemark;
    private LocalDateTime publishTime;
    private Long createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getNoticeId() { return noticeId; }
    public void setNoticeId(Long noticeId) { this.noticeId = noticeId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public LocalDateTime getPublishTime() { return publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

class AdminCategoryEntity {
    private Long categoryId;
    private Long parentId;
    private String name;
    private String code;
    private Integer sort;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

class AuditLogEntity {
    private Long auditId;
    private String bizType;
    private Long bizId;
    private String beforeStatus;
    private String afterStatus;
    private String auditResult;
    private String auditRemark;
    private Long auditorId;
    private String auditorName;
    private LocalDateTime createTime;

    public Long getAuditId() { return auditId; }
    public void setAuditId(Long auditId) { this.auditId = auditId; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public String getBeforeStatus() { return beforeStatus; }
    public void setBeforeStatus(String beforeStatus) { this.beforeStatus = beforeStatus; }
    public String getAfterStatus() { return afterStatus; }
    public void setAfterStatus(String afterStatus) { this.afterStatus = afterStatus; }
    public String getAuditResult() { return auditResult; }
    public void setAuditResult(String auditResult) { this.auditResult = auditResult; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public Long getAuditorId() { return auditorId; }
    public void setAuditorId(Long auditorId) { this.auditorId = auditorId; }
    public String getAuditorName() { return auditorName; }
    public void setAuditorName(String auditorName) { this.auditorName = auditorName; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

class AdminBannerEntity {
    private Long bannerId;
    private String title;
    private String imageFileId;
    private String imageUrl;
    private String linkUrl;
    private Integer sort;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getBannerId() { return bannerId; }
    public void setBannerId(Long bannerId) { this.bannerId = bannerId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getImageFileId() { return imageFileId; }
    public void setImageFileId(String imageFileId) { this.imageFileId = imageFileId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

class AdminRoleEntity {
    private Long roleId;
    private String roleName;
    private String roleCode;
    private String remark;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private java.util.List<Long> permissionIds;

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public java.util.List<Long> getPermissionIds() { return permissionIds; }
    public void setPermissionIds(java.util.List<Long> permissionIds) { this.permissionIds = permissionIds; }
}

class AdminPermissionEntity {
    private Long permissionId;
    private String permissionName;
    private String permissionCode;
    private String menuKey;
    private String description;
    private LocalDateTime createTime;

    public Long getPermissionId() { return permissionId; }
    public void setPermissionId(Long permissionId) { this.permissionId = permissionId; }
    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
    public String getPermissionCode() { return permissionCode; }
    public void setPermissionCode(String permissionCode) { this.permissionCode = permissionCode; }
    public String getMenuKey() { return menuKey; }
    public void setMenuKey(String menuKey) { this.menuKey = menuKey; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

class AdminMessageTemplateEntity {
    private Long templateId;
    private String type;
    private String titleTemplate;
    private String contentTemplate;
    private String channels;
    private String enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitleTemplate() { return titleTemplate; }
    public void setTitleTemplate(String titleTemplate) { this.titleTemplate = titleTemplate; }
    public String getContentTemplate() { return contentTemplate; }
    public void setContentTemplate(String contentTemplate) { this.contentTemplate = contentTemplate; }
    public String getChannels() { return channels; }
    public void setChannels(String channels) { this.channels = channels; }
    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
