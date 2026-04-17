package main.xiaoqihui.common.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileRecordEntity {
    private String fileId;
    private String bizType;
    private String originalName;
    private String fileUrl;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private Long uploaderId;
    private LocalDateTime createTime;
}
