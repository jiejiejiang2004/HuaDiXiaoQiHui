package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageEntity {
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
