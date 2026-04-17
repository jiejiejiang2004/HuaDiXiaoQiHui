package main.xiaoqihui.recruit;

import lombok.Data;

import java.time.LocalDateTime;

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
