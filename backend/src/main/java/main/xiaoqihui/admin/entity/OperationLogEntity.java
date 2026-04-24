package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 用于记录系统中的操作日志
 */
@Data
public class OperationLogEntity {
    /**
     * 日志ID
     */
    private Long logId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 操作类型
     */
    private String action;
    
    /**
     * 操作资源
     */
    private String resource;
    
    /**
     * 操作详情
     */
    private String detail;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}