package main.xiaoqihui.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

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

record BannerSaveRequest(
    @NotBlank String title,
    @NotBlank String imageFileId,
    String linkUrl,
    Integer sort,
    String startTime,
    String endTime,
    String status
) {
}

record RoleSaveRequest(
    @NotBlank String roleName,
    @NotBlank String roleCode,
    @NotEmpty List<Long> permissionIds,
    String remark,
    String status
) {
}

record MessageTemplateUpdateRequest(
    String titleTemplate,
    String contentTemplate,
    List<String> channels,
    String enabled
) {
}

record ResumeModerationRequest(
    @NotBlank String action,
    String reason
) {
}
