package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class ResumeAttachmentEntity {
    private Long attachmentId;
    private Long resumeId;
    private String fileId;
    private String fileName;
    private String fileUrl;
    private Long uploaderId;
    private LocalDateTime createTime;
}
