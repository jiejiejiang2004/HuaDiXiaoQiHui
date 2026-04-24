package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApplicationEntity {
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
