package main.xiaoqihui.statistics;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class DailyCount {
    private String day;
    private Long count;
}

@Data
class EnterpriseTrendRow {
    private String day;
    private Long applyCount;
    private Long interviewCount;
}

@Data
class JobStatRow {
    private Long jobId;
    private String jobName;
    private String status;
    private Integer viewCount;
    private Long applyCount;
    private Long interviewCount;
}

@Data
class IndustryDistRow {
    private String industry;
    private Long jobCount;
    private Long applyCount;
}

@Data
class EducationDistRow {
    private String education;
    private Long count;
}

@Data
class AreaDistRow {
    private String area;
    private Long count;
}

@Data
class ExportTaskEntity {
    private String taskId;
    private String taskType;
    private Long userId;
    private String fileId;
    private String downloadUrl;
    private String status;
    private LocalDateTime createTime;
}
