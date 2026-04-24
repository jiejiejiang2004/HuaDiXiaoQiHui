package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员通知实体类
 * 用于管理系统中的通知信息
 */
@Data
public class AdminNoticeEntity {
    /**
     * 通知ID
     */
    private Long noticeId;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 通知类型
     */
    private String type;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 审核备注
     */
    private String auditRemark;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 创建人ID
     */
    private Long createBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}