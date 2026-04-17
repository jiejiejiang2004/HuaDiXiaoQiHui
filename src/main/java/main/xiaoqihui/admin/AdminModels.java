package main.xiaoqihui.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}

@Data
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
}

@Data
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
}

@Data
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
}

@Data
class AdminCategoryEntity {
    private Long categoryId;
    private Long parentId;
    private String name;
    private String code;
    private Integer sort;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

@Data
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
}
