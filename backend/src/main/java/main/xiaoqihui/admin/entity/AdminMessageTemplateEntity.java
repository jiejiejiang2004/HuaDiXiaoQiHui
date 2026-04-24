package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员消息模板实体类
 * 用于管理系统中的消息模板信息
 */
@Data
public class AdminMessageTemplateEntity {
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 模板类型
     */
    private String type;
    
    /**
     * 标题模板
     */
    private String titleTemplate;
    
    /**
     * 内容模板
     */
    private String contentTemplate;
    
    /**
     * 发送渠道
     */
    private String channels;
    
    /**
     * 是否启用
     */
    private String enabled;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}