package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员角色实体类
 * 用于管理系统中的角色信息
 */
@Data
public class AdminRoleEntity {
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 权限ID列表
     */
    private List<Long> permissionIds;
}