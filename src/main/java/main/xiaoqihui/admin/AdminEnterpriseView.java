package main.xiaoqihui.admin;

import java.time.LocalDateTime;

public class AdminEnterpriseView {
    private Long enterpriseId;
    private Long userId;
    private String companyName;
    private String industry;
    private String scale;
    private String address;
    private String introduction;
    private String authStatus;
    private String authRemark;
    private String status;
    private String contactMobile;
    private LocalDateTime createTime;

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
    public String getAuthStatus() { return authStatus; }
    public void setAuthStatus(String authStatus) { this.authStatus = authStatus; }
    public String getAuthRemark() { return authRemark; }
    public void setAuthRemark(String authRemark) { this.authRemark = authRemark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
