package main.xiaoqihui.recruit;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyAuthEntity {
    private Long authId;
    private Long companyId;
    private String creditCode;
    private String legalPerson;
    private String licenseFileId;
    private String licenseImage;
    private String logoFileId;
    private String logoUrl;
    private LocalDateTime applyTime;
    private LocalDateTime auditTime;
    private String auditStatus;
    private String auditRemark;
    private Long auditorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
