package main.xiaoqihui.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员横幅实体类
 * 用于管理系统中的横幅信息
 */
@Data
public class AdminBannerEntity {
    /**
     * 横幅ID
     */
    private Long bannerId;
    
    /**
     * 横幅标题
     */
    private String title;
    
    /**
     * 图片文件ID
     */
    private String imageFileId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 链接URL
     */
    private String linkUrl;
    
    /**
     * 排序值
     */
    private Integer sort;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}