package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InterviewEntity {
    private Long interviewId;
    private Long applyId;
    private Long enterpriseId;
    private Long userId;
    private Long resumeId;
    private Long jobId;
    private LocalDateTime interviewTime;
    private String interviewType;
    private String interviewPlace;
    private String interviewLink;
    private String contactName;
    private String contactMobile;
    private String remark;
    private String status;
    private String candidateName;
    private String jobName;
    private String resumeTitle;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
