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

/**
 * 管理员控制器
 * 处理管理员相关的所有API请求
 */
@RestController
@RequestMapping("/recruit/api/v1")
public class AdminController {

    private final AdminService adminService;

    /**
     * 构造函数
     * @param adminService 管理员服务
     */
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 管理员登录
     * @param request 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/admin/login")
    public ApiResponse<?> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(adminService.adminLogin(request));
    }

    /**
     * 列出候选人列表
     * @param keyword 关键词
     * @param identity 身份类型
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 候选人列表
     */
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

    /**
     * 获取候选人详情
     * @param userId 用户ID
     * @return 候选人详情
     */
    @GetMapping("/admin/users/candidates/{userId}")
    public ApiResponse<?> getCandidateDetail(@PathVariable Long userId) {
        return ApiResponse.success(adminService.getCandidateDetail(userId));
    }

    /**
     * 更新候选人状态
     * @param userId 用户ID
     * @param request 状态更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/users/candidates/{userId}/status")
    public ApiResponse<?> updateCandidateStatus(@PathVariable Long userId, @Valid @RequestBody AdminStatusUpdateRequest request) {
        adminService.updateCandidateStatus(userId, request);
        return ApiResponse.success();
    }

    /**
     * 列出企业列表
     * @param keyword 关键词
     * @param authStatus 认证状态
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 企业列表
     */
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

    /**
     * 审核企业
     * @param enterpriseId 企业ID
     * @param request 审核请求
     * @return 操作结果
     */
    @PutMapping("/admin/users/enterprises/{enterpriseId}/audit")
    public ApiResponse<?> auditEnterprise(@PathVariable Long enterpriseId, @Valid @RequestBody AuditRequest request) {
        adminService.auditEnterprise(enterpriseId, request);
        return ApiResponse.success();
    }

    /**
     * 更新企业状态
     * @param enterpriseId 企业ID
     * @param request 状态更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/users/enterprises/{enterpriseId}/status")
    public ApiResponse<?> updateEnterpriseStatus(@PathVariable Long enterpriseId, @Valid @RequestBody AdminStatusUpdateRequest request) {
        adminService.updateEnterpriseStatus(enterpriseId, request);
        return ApiResponse.success();
    }

    /**
     * 更新企业信息
     * @param enterpriseId 企业ID
     * @param request 企业信息更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/users/enterprises/{enterpriseId}")
    public ApiResponse<?> updateEnterpriseInfo(@PathVariable Long enterpriseId, @RequestBody EnterpriseAdminUpdateRequest request) {
        adminService.updateEnterpriseInfo(enterpriseId, request);
        return ApiResponse.success();
    }

    /**
     * 列出待审核的职位列表
     * @param status 状态
     * @param companyName 公司名称
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 职位列表
     */
    @GetMapping("/admin/audit/jobs")
    public ApiResponse<?> listAuditJobs(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String companyName,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditJobs(status, companyName, pageNum, pageSize));
    }

    /**
     * 审核职位
     * @param jobId 职位ID
     * @param request 审核请求
     * @return 操作结果
     */
    @PutMapping("/admin/audit/jobs/{jobId}")
    public ApiResponse<?> auditJob(@PathVariable Long jobId, @Valid @RequestBody AuditRequest request) {
        adminService.auditJob(jobId, request);
        return ApiResponse.success();
    }

    /**
     * 列出待审核的通知列表
     * @param status 状态
     * @param type 类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    @GetMapping("/admin/audit/notices")
    public ApiResponse<?> listAuditNotices(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String type,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditNotices(status, type, pageNum, pageSize));
    }

    /**
     * 审核通知
     * @param noticeId 通知ID
     * @param request 审核请求
     * @return 操作结果
     */
    @PutMapping("/admin/audit/notices/{noticeId}")
    public ApiResponse<?> auditNotice(@PathVariable Long noticeId, @Valid @RequestBody AuditRequest request) {
        adminService.auditNotice(noticeId, request);
        return ApiResponse.success();
    }

    /**
     * 审核简历
     * @param resumeId 简历ID
     * @param request 简历审核请求
     * @return 操作结果
     */
    @PutMapping("/admin/audit/resumes/{resumeId}/moderate")
    public ApiResponse<?> moderateResume(@PathVariable Long resumeId, @Valid @RequestBody ResumeModerationRequest request) {
        return ApiResponse.success(adminService.moderateResume(resumeId, request));
    }

    /**
     * 列出分类列表
     * @param parentId 父分类ID
     * @return 分类列表
     */
    @GetMapping("/admin/system/categories")
    public ApiResponse<?> listCategories(@RequestParam(required = false) Long parentId) {
        return ApiResponse.success(adminService.listCategories(parentId));
    }

    /**
     * 创建分类
     * @param request 分类创建请求
     * @return 创建结果
     */
    @PostMapping("/admin/system/categories")
    public ApiResponse<?> createCategory(@Valid @RequestBody CategorySaveRequest request) {
        return ApiResponse.success(adminService.createCategory(request));
    }

    /**
     * 更新分类
     * @param categoryId 分类ID
     * @param request 分类更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/system/categories/{categoryId}")
    public ApiResponse<?> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategorySaveRequest request) {
        adminService.updateCategory(categoryId, request);
        return ApiResponse.success();
    }

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/admin/system/categories/{categoryId}")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        adminService.deleteCategory(categoryId);
        return ApiResponse.success();
    }

    /**
     * 列出横幅列表
     * @return 横幅列表
     */
    @GetMapping("/admin/system/banners")
    public ApiResponse<?> listBanners() {
        return ApiResponse.success(adminService.listBanners());
    }

    /**
     * 创建横幅
     * @param request 横幅创建请求
     * @return 创建结果
     */
    @PostMapping("/admin/system/banners")
    public ApiResponse<?> createBanner(@Valid @RequestBody BannerSaveRequest request) {
        return ApiResponse.success(adminService.createBanner(request));
    }

    /**
     * 更新横幅
     * @param bannerId 横幅ID
     * @param request 横幅更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/system/banners/{bannerId}")
    public ApiResponse<?> updateBanner(@PathVariable Long bannerId, @Valid @RequestBody BannerSaveRequest request) {
        adminService.updateBanner(bannerId, request);
        return ApiResponse.success();
    }

    /**
     * 删除横幅
     * @param bannerId 横幅ID
     * @return 操作结果
     */
    @DeleteMapping("/admin/system/banners/{bannerId}")
    public ApiResponse<?> deleteBanner(@PathVariable Long bannerId) {
        adminService.deleteBanner(bannerId);
        return ApiResponse.success();
    }

    /**
     * 列出系统通知列表
     * @param type 类型
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    @GetMapping("/admin/system/notices")
    public ApiResponse<?> listSystemNotices(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listSystemNotices(type, status, pageNum, pageSize));
    }

    /**
     * 创建通知
     * @param request 通知创建请求
     * @return 创建结果
     */
    @PostMapping("/admin/system/notices")
    public ApiResponse<?> createNotice(@Valid @RequestBody NoticeSaveRequest request) {
        return ApiResponse.success(adminService.createNotice(request));
    }

    /**
     * 更新通知
     * @param noticeId 通知ID
     * @param request 通知更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/system/notices/{noticeId}")
    public ApiResponse<?> updateNotice(@PathVariable Long noticeId, @Valid @RequestBody NoticeSaveRequest request) {
        adminService.updateNotice(noticeId, request);
        return ApiResponse.success();
    }

    /**
     * 删除通知
     * @param noticeId 通知ID
     * @return 操作结果
     */
    @DeleteMapping("/admin/system/notices/{noticeId}")
    public ApiResponse<?> deleteNotice(@PathVariable Long noticeId) {
        adminService.deleteNotice(noticeId);
        return ApiResponse.success();
    }

    /**
     * 列出权限列表
     * @return 权限列表
     */
    @GetMapping("/admin/system/permissions")
    public ApiResponse<?> listPermissions() {
        return ApiResponse.success(adminService.listPermissions());
    }

    /**
     * 列出角色列表
     * @return 角色列表
     */
    @GetMapping("/admin/system/roles")
    public ApiResponse<?> listRoles() {
        return ApiResponse.success(adminService.listRoles());
    }

    /**
     * 创建角色
     * @param request 角色创建请求
     * @return 创建结果
     */
    @PostMapping("/admin/system/roles")
    public ApiResponse<?> createRole(@Valid @RequestBody RoleSaveRequest request) {
        return ApiResponse.success(adminService.createRole(request));
    }

    /**
     * 更新角色
     * @param roleId 角色ID
     * @param request 角色更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/system/roles/{roleId}")
    public ApiResponse<?> updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleSaveRequest request) {
        adminService.updateRole(roleId, request);
        return ApiResponse.success();
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/admin/system/roles/{roleId}")
    public ApiResponse<?> deleteRole(@PathVariable Long roleId) {
        adminService.deleteRole(roleId);
        return ApiResponse.success();
    }

    /**
     * 列出消息模板列表
     * @param type 类型
     * @return 消息模板列表
     */
    @GetMapping("/admin/system/message/templates")
    public ApiResponse<?> listMessageTemplates(@RequestParam(required = false) String type) {
        return ApiResponse.success(adminService.listMessageTemplates(type));
    }

    /**
     * 更新消息模板
     * @param templateId 模板ID
     * @param request 模板更新请求
     * @return 操作结果
     */
    @PutMapping("/admin/system/message/templates/{templateId}")
    public ApiResponse<?> updateMessageTemplate(
        @PathVariable Long templateId,
        @RequestBody MessageTemplateUpdateRequest request
    ) {
        adminService.updateMessageTemplate(templateId, request);
        return ApiResponse.success();
    }

    /**
     * 列出审核日志
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 审核日志列表
     */
    @GetMapping("/admin/system/logs/audit")
    public ApiResponse<?> listAuditLogs(
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listAuditLogs(pageNum, pageSize));
    }

    /**
     * 列出操作日志
     * @param userId 用户ID
     * @param action 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 操作日志列表
     */
    @GetMapping("/admin/system/logs/operation")
    public ApiResponse<?> listOperationLogs(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String action,
        @RequestParam(required = false) String startTime,
        @RequestParam(required = false) String endTime,
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        return ApiResponse.success(adminService.listOperationLogs(userId, action, startTime, endTime, pageNum, pageSize));
    }
}