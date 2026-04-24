package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResumeEntity {
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
