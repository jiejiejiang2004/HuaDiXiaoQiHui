package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员权限实体类
 * 用于管理系统中的权限信息
 */
@Data
public class AdminPermissionEntity {
    /**
     * 权限ID
     */
    private Long permissionId;
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限编码
     */
    private String permissionCode;
    
    /**
     * 菜单键
     */
    private String menuKey;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}