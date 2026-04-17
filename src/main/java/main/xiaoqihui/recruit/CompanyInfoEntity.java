package main.xiaoqihui.recruit;

import java.time.LocalDateTime;

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

    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getScale() { return scale; }
    public void setScale(String scale) { this.scale = scale; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String authStatus) { this.authStatus = authStatus; }
    public String getAuthRemark() { return authRemark; }
    public void setAuthRemark(String authRemark) { this.authRemark = authRemark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
