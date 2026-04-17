package main.xiaoqihui.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record AdminStatusUpdateRequest(
    @NotBlank String status,
    String reason
) {
}

record AuditRequest(
    @NotBlank String result,
    String reason
) {
}

record NoticeSaveRequest(
    @NotBlank String title,
    @NotBlank String content,
    @NotBlank String type,
    String status,
    String publishTime
) {
}

record CategorySaveRequest(
    @NotBlank String name,
    Long parentId,
    Integer sort
) {
}

record EnterpriseAdminUpdateRequest(
    String companyName,
    String industry,
    String scale,
    String address,
    String introduction
) {
}

record AdminLoginRequest(
    @NotBlank String mobile,
    @NotBlank String password
) {
}

record NoticeStatusUpdateRequest(
    @NotNull Long noticeId,
    @NotBlank String status
) {
}
