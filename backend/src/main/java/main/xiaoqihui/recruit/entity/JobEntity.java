package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
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
    private String auditRemark;
    private String companyName;
    private String companyLogo;
    private String companyIndustry;
}
