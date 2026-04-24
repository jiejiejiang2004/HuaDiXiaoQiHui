package main.xiaoqihui.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 管理员状态更新请求
 */
record AdminStatusUpdateRequest( 
    /**
     * 状态
     */
    @NotBlank String status, 
    /**
     * 原因
     */
    String reason 
) { 
} 

/**
 * 审核请求
 */
record AuditRequest( 
    /**
     * 审核结果
     */
    @NotBlank String result, 
    /**
     * 审核原因
     */
    String reason 
) { 
} 

/**
 * 通知保存请求
 */
record NoticeSaveRequest( 
    /**
     * 标题
     */
    @NotBlank String title, 
    /**
     * 内容
     */
    @NotBlank String content, 
    /**
     * 类型
     */
    @NotBlank String type, 
    /**
     * 状态
     */
    String status, 
    /**
     * 发布时间
     */
    String publishTime 
) { 
} 

/**
 * 分类保存请求
 */
record CategorySaveRequest( 
    /**
     * 分类名称
     */
    @NotBlank String name, 
    /**
     * 父分类ID
     */
    Long parentId, 
    /**
     * 排序值
     */
    Integer sort 
) { 
} 

/**
 * 企业信息更新请求
 */
record EnterpriseAdminUpdateRequest( 
    /**
     * 公司名称
     */
    String companyName, 
    /**
     * 行业
     */
    String industry, 
    /**
     * 规模
     */
    String scale, 
    /**
     * 地址
     */
    String address, 
    /**
     * 简介
     */
    String introduction 
) { 
} 

/**
 * 管理员登录请求
 */
record AdminLoginRequest( 
    /**
     * 手机号码
     */
    @NotBlank String mobile, 
    /**
     * 密码
     */
    @NotBlank String password 
) { 
} 

/**
 * 通知状态更新请求
 */
record NoticeStatusUpdateRequest( 
    /**
     * 通知ID
     */
    @NotNull Long noticeId, 
    /**
     * 状态
     */
    @NotBlank String status 
) { 
} 

/**
 * 横幅保存请求
 */
record BannerSaveRequest( 
    /**
     * 标题
     */
    @NotBlank String title, 
    /**
     * 图片文件ID
     */
    @NotBlank String imageFileId, 
    /**
     * 链接URL
     */
    String linkUrl, 
    /**
     * 排序值
     */
    Integer sort, 
    /**
     * 开始时间
     */
    String startTime, 
    /**
     * 结束时间
     */
    String endTime, 
    /**
     * 状态
     */
    String status 
) { 
} 

/**
 * 角色保存请求
 */
record RoleSaveRequest( 
    /**
     * 角色名称
     */
    @NotBlank String roleName, 
    /**
     * 角色编码
     */
    @NotBlank String roleCode, 
    /**
     * 权限ID列表
     */
    @NotEmpty List<Long> permissionIds, 
    /**
     * 备注
     */
    String remark, 
    /**
     * 状态
     */
    String status 
) { 
} 

/**
 * 消息模板更新请求
 */
record MessageTemplateUpdateRequest( 
    /**
     * 标题模板
     */
    String titleTemplate, 
    /**
     * 内容模板
     */
    String contentTemplate, 
    /**
     * 发送渠道列表
     */
    List<String> channels, 
    /**
     * 是否启用
     */
    String enabled 
) { 
} 

/**
 * 简历审核请求
 */
record ResumeModerationRequest( 
    /**
     * 操作类型
     */
    @NotBlank String action, 
    /**
     * 原因
     */
    String reason 
) { 
}