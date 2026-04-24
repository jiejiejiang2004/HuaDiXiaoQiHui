package main.xiaoqihui.recruit.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyInfoEntity {
    private Long enterpriseId;
    private Long userId;
    private String companyName;
    private String industry;
    private String scale;
    private String address;
    private String introduction;
    private String website;
    private String logo;
    private String authStatus;
    private String authRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
