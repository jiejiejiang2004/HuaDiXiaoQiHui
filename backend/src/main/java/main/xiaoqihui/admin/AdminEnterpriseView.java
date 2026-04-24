package main.xiaoqihui.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员企业视图类
 * 用于展示企业的详细信息
 */
@Data
public class AdminEnterpriseView {
    /**
     * 企业ID
     */
    private Long enterpriseId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 行业
     */
    private String industry;
    
    /**
     * 规模
     */
    private String scale;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 认证状态
     */
    private String authStatus;
    
    /**
     * 认证备注
     */
    private String authRemark;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 联系电话
     */
    private String contactMobile;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}