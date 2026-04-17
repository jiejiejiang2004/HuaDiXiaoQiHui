package main.xiaoqihui.recruit;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class CompanyInfoEntity {
    private Long enterpriseId;
    private Long userId;
    private String companyName;
    private String industry;
    private String scale;
    private String address;
    private String introduction;
    private String website;
    private String logo;
    private String authStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

@Data
class ResumeEntity {
    private Long resumeId;
    private Long userId;
    private String title;
    private String basicInfo;
    private String jobIntention;
    private String educationList;
    private String workList;
    private String skillList;
    private String selfEvaluation;
    private String privacy;
    private Boolean isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

@Data
class JobEntity {
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
}

@Data
class ApplicationEntity {
    private Long applyId;
    private Long jobId;
    private Long resumeId;
    private Long userId;
    private Long enterpriseId;
    private String coverLetter;
    private String status;
    private LocalDateTime applyTime;
    private LocalDateTime updateTime;

    private String jobName;
    private String companyName;
    private String candidateName;
}

@Data
class MessageEntity {
    private Long messageId;
    private Long userId;
    private String type;
    private String title;
    private String content;
    private Long bizId;
    private String readStatus;
    private LocalDateTime createTime;
    private LocalDateTime readTime;
}
