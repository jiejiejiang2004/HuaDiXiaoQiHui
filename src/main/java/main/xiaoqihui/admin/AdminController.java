package main.xiaoqihui.admin;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin/login")
    public ApiResponse<?> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(adminService.adminLogin(request));
    }

    @GetMapping("/admin/users/candidates")
    public ApiResponse<?> listCandidates(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String identity,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listCandidates(keyword, identity, status, pageNum, pageSize));
    }

    @GetMapping("/admin/users/candidates/{userId}")
    public ApiResponse<?> getCandidateDetail(@PathVariable Long userId) {
        return ApiResponse.success(adminService.getCandidateDetail(userId));
    }

    @PutMapping("/admin/users/candidates/{userId}/status")
    public ApiResponse<?> updateCandidateStatus(@PathVariable Long userId, @Valid @RequestBody AdminStatusUpdateRequest request) {
        adminService.updateCandidateStatus(userId, request);
        return ApiResponse.success();
    }

    @GetMapping("/admin/users/enterprises")
    public ApiResponse<?> listEnterprises(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String authStatus,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listEnterprises(keyword, authStatus, status, pageNum, pageSize));
    }

    @PutMapping("/admin/users/enterprises/{enterpriseId}/audit")
    public ApiResponse<?> auditEnterprise(@PathVariable Long enterpriseId, @Valid @RequestBody AuditRequest request) {
        adminService.auditEnterprise(enterpriseId, request);
        return ApiResponse.success();
    }

    @PutMapping("/admin/users/enterprises/{enterpriseId}/status")
    public ApiResponse<?> updateEnterpriseStatus(@PathVariable Long enterpriseId, @Valid @RequestBody AdminStatusUpdateRequest request) {
        adminService.updateEnterpriseStatus(enterpriseId, request);
        return ApiResponse.success();
    }

    @PutMapping("/admin/users/enterprises/{enterpriseId}")
    public ApiResponse<?> updateEnterpriseInfo(@PathVariable Long enterpriseId, @RequestBody EnterpriseAdminUpdateRequest request) {
        adminService.updateEnterpriseInfo(enterpriseId, request);
        return ApiResponse.success();
    }

    @GetMapping("/admin/audit/jobs")
    public ApiResponse<?> listAuditJobs(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String companyName,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditJobs(status, companyName, pageNum, pageSize));
    }

    @PutMapping("/admin/audit/jobs/{jobId}")
    public ApiResponse<?> auditJob(@PathVariable Long jobId, @Valid @RequestBody AuditRequest request) {
        adminService.auditJob(jobId, request);
        return ApiResponse.success();
    }

    @GetMapping("/admin/audit/notices")
    public ApiResponse<?> listAuditNotices(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String type,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditNotices(status, type, pageNum, pageSize));
    }

    @PutMapping("/admin/audit/notices/{noticeId}")
    public ApiResponse<?> auditNotice(@PathVariable Long noticeId, @Valid @RequestBody AuditRequest request) {
        adminService.auditNotice(noticeId, request);
        return ApiResponse.success();
    }

    @GetMapping("/admin/system/categories")
    public ApiResponse<?> listCategories(@RequestParam(required = false) Long parentId) {
        return ApiResponse.success(adminService.listCategories(parentId));
    }

    @PostMapping("/admin/system/categories")
    public ApiResponse<?> createCategory(@Valid @RequestBody CategorySaveRequest request) {
        return ApiResponse.success(adminService.createCategory(request));
    }

    @PutMapping("/admin/system/categories/{categoryId}")
    public ApiResponse<?> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategorySaveRequest request) {
        adminService.updateCategory(categoryId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/admin/system/categories/{categoryId}")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        adminService.deleteCategory(categoryId);
        return ApiResponse.success();
    }

    @GetMapping("/admin/system/notices")
    public ApiResponse<?> listSystemNotices(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listSystemNotices(type, status, pageNum, pageSize));
    }

    @PostMapping("/admin/system/notices")
    public ApiResponse<?> createNotice(@Valid @RequestBody NoticeSaveRequest request) {
        return ApiResponse.success(adminService.createNotice(request));
    }

    @PutMapping("/admin/system/notices/{noticeId}")
    public ApiResponse<?> updateNotice(@PathVariable Long noticeId, @Valid @RequestBody NoticeSaveRequest request) {
        adminService.updateNotice(noticeId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/admin/system/notices/{noticeId}")
    public ApiResponse<?> deleteNotice(@PathVariable Long noticeId) {
        adminService.deleteNotice(noticeId);
        return ApiResponse.success();
    }

    @GetMapping("/admin/system/logs/audit")
    public ApiResponse<?> listAuditLogs(
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditLogs(pageNum, pageSize));
    }
}
