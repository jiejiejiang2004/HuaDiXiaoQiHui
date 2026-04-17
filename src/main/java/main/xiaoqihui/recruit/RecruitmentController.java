package main.xiaoqihui.recruit;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit/api/v1")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @PostMapping("/user/register")
    public ApiResponse<?> registerCandidate(@Valid @RequestBody CandidateRegisterRequest request) {
        return ApiResponse.success(recruitmentService.registerCandidate(request));
    }

    @PostMapping("/user/login/password")
    public ApiResponse<?> loginCandidate(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(recruitmentService.candidateLogin(request));
    }

    @PostMapping("/user/login/sms")
    public ApiResponse<?> loginCandidateBySms(@Valid @RequestBody SmsLoginRequest request) {
        return ApiResponse.success(recruitmentService.candidateSmsLogin(request));
    }

    @PostMapping("/user/password/reset")
    public ApiResponse<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        recruitmentService.resetPassword(request);
        return ApiResponse.success();
    }

    @PostMapping("/user/token/refresh")
    public ApiResponse<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(recruitmentService.refreshAccessToken(request));
    }

    @PostMapping("/user/logout")
    public ApiResponse<?> logout() {
        recruitmentService.logout();
        return ApiResponse.success();
    }

    @PostMapping("/enterprise/register")
    public ApiResponse<?> registerEnterprise(@Valid @RequestBody EnterpriseRegisterRequest request) {
        return ApiResponse.success(recruitmentService.registerEnterprise(request));
    }

    @PostMapping("/enterprise/login")
    public ApiResponse<?> loginEnterprise(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(recruitmentService.enterpriseLogin(request));
    }

    @PostMapping("/enterprise/password/reset")
    public ApiResponse<?> resetEnterprisePassword(@Valid @RequestBody ResetPasswordRequest request) {
        recruitmentService.resetPassword(request);
        return ApiResponse.success();
    }

    @GetMapping("/user/profile")
    public ApiResponse<?> getProfile() {
        return ApiResponse.success(recruitmentService.getCurrentProfile());
    }

    @PutMapping("/user/profile")
    public ApiResponse<?> updateProfile(@RequestBody ProfileUpdateRequest request) {
        recruitmentService.updateCurrentProfile(request);
        return ApiResponse.success();
    }

    @GetMapping("/home/jobs/search")
    public ApiResponse<?> homeSearchJobs(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String education,
        @RequestParam(required = false) String experience,
        @RequestParam(required = false) String location,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.searchJobs(keyword, education, experience, location, pageNum, pageSize));
    }

    @GetMapping("/jobs/search")
    public ApiResponse<?> searchJobs(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String education,
        @RequestParam(required = false) String experience,
        @RequestParam(required = false) String location,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.searchJobs(keyword, education, experience, location, pageNum, pageSize));
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<?> getJobDetail(@PathVariable Long jobId) {
        return ApiResponse.success(recruitmentService.getJobDetail(jobId));
    }

    @PostMapping("/resume")
    public ApiResponse<?> createResume(@Valid @RequestBody ResumeSaveRequest request) {
        return ApiResponse.success(recruitmentService.createResume(request));
    }

    @PutMapping("/resume/{resumeId}")
    public ApiResponse<?> updateResume(@PathVariable Long resumeId, @Valid @RequestBody ResumeSaveRequest request) {
        recruitmentService.updateResume(resumeId, request);
        return ApiResponse.success();
    }

    @GetMapping("/resume/{resumeId}")
    public ApiResponse<?> getResumeDetail(@PathVariable Long resumeId) {
        return ApiResponse.success(recruitmentService.getResumeDetail(resumeId));
    }

    @GetMapping("/resume/my")
    public ApiResponse<?> listMyResumes() {
        return ApiResponse.success(java.util.Map.of("list", recruitmentService.listMyResumes()));
    }

    @PostMapping("/apply")
    public ApiResponse<?> applyJob(@Valid @RequestBody ApplyRequest request) {
        return ApiResponse.success(recruitmentService.applyJob(request));
    }

    @GetMapping("/apply/my")
    public ApiResponse<?> listMyApplications(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.listMyApplications(status, pageNum, pageSize));
    }

    @GetMapping("/messages")
    public ApiResponse<?> listMessages(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String readStatus,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.listMessages(type, readStatus, pageNum, pageSize));
    }

    @PutMapping("/messages/read")
    public ApiResponse<?> markMessagesRead(@RequestBody(required = false) ReadMessageRequest request) {
        recruitmentService.markMessagesRead(request);
        return ApiResponse.success();
    }

    @GetMapping("/enterprise/info")
    public ApiResponse<?> getEnterpriseInfo() {
        return ApiResponse.success(recruitmentService.getEnterpriseInfo());
    }

    @PostMapping("/enterprise/auth")
    public ApiResponse<?> submitEnterpriseAuth(@Valid @RequestBody EnterpriseAuthSubmitRequest request) {
        return ApiResponse.success(recruitmentService.submitEnterpriseAuth(request));
    }

    @GetMapping("/enterprise/auth/status")
    public ApiResponse<?> getEnterpriseAuthStatus() {
        return ApiResponse.success(recruitmentService.getEnterpriseAuthStatus());
    }

    @PutMapping("/enterprise/info")
    public ApiResponse<?> updateEnterpriseInfo(@RequestBody EnterpriseInfoUpdateRequest request) {
        recruitmentService.updateEnterpriseInfo(request);
        return ApiResponse.success();
    }

    @PostMapping("/enterprise/jobs")
    public ApiResponse<?> createEnterpriseJob(@Valid @RequestBody JobPublishRequest request) {
        return ApiResponse.success(recruitmentService.createEnterpriseJob(request));
    }

    @PutMapping("/enterprise/jobs/{jobId}")
    public ApiResponse<?> updateEnterpriseJob(@PathVariable Long jobId, @Valid @RequestBody JobPublishRequest request) {
        recruitmentService.updateEnterpriseJob(jobId, request);
        return ApiResponse.success();
    }

    @GetMapping("/enterprise/jobs")
    public ApiResponse<?> listEnterpriseJobs(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.listEnterpriseJobs(status, keyword, pageNum, pageSize));
    }

    @GetMapping("/enterprise/applies")
    public ApiResponse<?> listEnterpriseApplications(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long jobId,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(recruitmentService.listEnterpriseApplications(status, jobId, pageNum, pageSize));
    }

    @PutMapping("/enterprise/applies/{applyId}/status")
    public ApiResponse<?> updateEnterpriseApplicationStatus(
        @PathVariable Long applyId,
        @Valid @RequestBody ApplicationStatusRequest request
    ) {
        recruitmentService.updateEnterpriseApplicationStatus(applyId, request);
        return ApiResponse.success();
    }

    @GetMapping("/enterprise/resumes/{resumeId}")
    public ApiResponse<?> getEnterpriseResumeDetail(@PathVariable Long resumeId) {
        return ApiResponse.success(recruitmentService.getEnterpriseResumeDetail(resumeId));
    }
}
