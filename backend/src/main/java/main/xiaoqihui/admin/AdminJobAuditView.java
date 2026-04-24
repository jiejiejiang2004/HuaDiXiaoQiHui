package main.xiaoqihui.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员职位审核视图类
 * 用于展示职位审核的详细信息
 */
@Data
public class AdminJobAuditView {
    /**
     * 职位ID
     */
    private Long jobId;
    
    /**
     * 企业ID
     */
    private Long enterpriseId;
    
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 职位名称
     */
    private String jobName;
    
    /**
     * 职位分类
     */
    private String jobCategory;
    
    /**
     * 工作地点
     */
    private String location;
    
    /**
     * 最低薪资
     */
    private Integer salaryMin;
    
    /**
     * 最高薪资
     */
    private Integer salaryMax;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 审核备注
     */
    private String auditRemark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}