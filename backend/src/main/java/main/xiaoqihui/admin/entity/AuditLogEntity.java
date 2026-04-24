package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审核日志实体类
 * 用于记录系统中的审核操作日志
 */
@Data
public class AuditLogEntity {
    /**
     * 审核ID
     */
    private Long auditId;
    
    /**
     * 业务类型
     */
    private String bizType;
    
    /**
     * 业务ID
     */
    private Long bizId;
    
    /**
     * 审核前状态
     */
    private String beforeStatus;
    
    /**
     * 审核后状态
     */
    private String afterStatus;
    
    /**
     * 审核结果
     */
    private String auditResult;
    
    /**
     * 审核备注
     */
    private String auditRemark;
    
    /**
     * 审核人ID
     */
    private Long auditorId;
    
    /**
     * 审核人名称
     */
    private String auditorName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}