package main.xiaoqihui.recruit;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

record CandidateRegisterRequest(
    @NotBlank String mobile,
    @NotBlank String smsCode,
    @NotBlank String password,
    @NotBlank String identity,
    @NotBlank String name,
    @NotNull Boolean agreeProtocol
) {
}

record LoginRequest(
    @NotBlank String mobile,
    @NotBlank String password
) {
}

record EnterpriseRegisterRequest(
    @NotBlank String contactMobile,
    @NotBlank String smsCode,
    @NotBlank String password,
    @NotBlank String contactName,
    @NotBlank String companyName,
    @NotNull Boolean agreeProtocol
) {
}

record SmsLoginRequest(
    @NotBlank String mobile,
    @NotBlank String smsCode
) {
}

record ResetPasswordRequest(
    @NotBlank String mobile,
    @NotBlank String smsCode,
    @NotBlank String newPassword
) {
}

record RefreshTokenRequest(
    @NotBlank String refreshToken
) {
}

record ProfileUpdateRequest(
    String avatar,
    String email,
    String school,
    String major,
    Integer graduationYear,
    String currentCity
) {
}

record ResumeSaveRequest(
    String title,
    @NotNull Map<String, Object> basicInfo,
    @NotNull Map<String, Object> jobIntention,
    @NotEmpty List<Map<String, Object>> educationList,
    List<Map<String, Object>> workList,
    List<Map<String, Object>> skillList,
    String selfEvaluation,
    String privacy
) {
}

record JobPublishRequest(
    @NotBlank String jobName,
    @NotBlank String jobCategory,
    @NotBlank String responsibility,
    @NotBlank String requirement,
    @NotNull Integer salaryMin,
    @NotNull Integer salaryMax,
    @NotBlank String location,
    @NotNull Integer headCount,
    @NotBlank String education,
    @NotBlank String experience,
    List<String> welfare,
    @NotBlank String contactName,
    @NotBlank String contactMobile
) {
}

record ApplyRequest(
    @NotNull Long jobId,
    @NotNull Long resumeId,
    String coverLetter
) {
}

record ApplicationStatusRequest(
    @NotBlank String status,
    String remark
) {
}

record ReadMessageRequest(
    List<Long> messageIds
) {
}

record EnterpriseInfoUpdateRequest(
    String industry,
    String scale,
    String address,
    String introduction,
    String website,
    String logo,
    List<String> welfare
) {
}

record EnterpriseAuthSubmitRequest(
    @NotBlank String companyName,
    @NotBlank String creditCode,
    @NotBlank String legalPerson,
    @NotBlank String licenseFileId,
    @NotBlank String industry,
    @NotBlank String scale,
    @NotBlank String address,
    String introduction,
    String logoFileId,
    String website
) {
}

record PageQuery(
    @Min(1) Integer pageNum,
    @Min(1) @Max(100) Integer pageSize
) {
    int safePageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    int safePageSize() {
        return pageSize == null ? 10 : pageSize;
    }
}
