package main.xiaoqihui.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员候选人视图类
 * 用于展示候选人的详细信息
 */
@Data
public class AdminCandidateView {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份类型
     */
    private String identityType;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 学校
     */
    private String school;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 当前城市
     */
    private String currentCity;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}