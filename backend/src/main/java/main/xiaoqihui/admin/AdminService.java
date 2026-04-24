package main.xiaoqihui.admin;

import jakarta.servlet.http.HttpServletRequest;
import main.xiaoqihui.admin.entity.*;
import main.xiaoqihui.common.CommonService;
import main.xiaoqihui.common.auth.RedisTokenStore;
import main.xiaoqihui.common.domain.FileRecordEntity;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.security.LoginUser;
import main.xiaoqihui.common.util.JwtUtil;
import main.xiaoqihui.recruit.entity.CompanyAuthEntity;
import main.xiaoqihui.common.util.SecurityUtils;
import main.xiaoqihui.recruit.entity.MessageEntity;
import main.xiaoqihui.recruit.RecruitmentMapper;
import main.xiaoqihui.recruit.entity.ResumeEntity;
import main.xiaoqihui.recruit.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private static final List<String> RESUME_ILLEGAL_KEYWORDS = List.of(
        "代写", "刷单", "博彩", "赌博", "色情", "诈骗", "兼职刷单", "虚假学历", "违禁"
    );

    private final AdminMapper adminMapper;
    private final RecruitmentMapper recruitmentMapper;
    private final CommonService commonService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTokenStore redisTokenStore;

    public AdminService(
        AdminMapper adminMapper,
        RecruitmentMapper recruitmentMapper,
        CommonService commonService,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        RedisTokenStore redisTokenStore
    ) {
        this.adminMapper = adminMapper;
        this.recruitmentMapper = recruitmentMapper;
        this.commonService = commonService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.redisTokenStore = redisTokenStore;
    }

    public Map<String, Object> adminLogin(AdminLoginRequest request) {
        UserEntity user = adminMapper.findUserByMobile(request.mobile());
        if (user == null || !"ADMIN".equals(user.getUserType())) {
            throw new BusinessException(3001, "管理员账号不存在");
        }
        boolean matched = passwordEncoder.matches(request.password(), user.getPassword())
            || request.password().equals(user.getPassword());
        if (!matched) {
            throw new BusinessException(3002, "密码错误");
        }
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(2003, "账号已被禁用");
        }
        LoginUser loginUser = new LoginUser(
            user.getUserId(),
            user.getMobile(),
            user.getPassword(),
            user.getUserType(),
            user.getRealName(),
            user.getStatus()
        );
        Map<String, Object> data = new LinkedHashMap<>();
        String accessToken = jwtUtil.generateAccessToken(loginUser);
        String refreshToken = jwtUtil.generateRefreshToken(loginUser);
        redisTokenStore.saveTokens(
            user.getMobile(),
            accessToken,
            jwtUtil.getAccessExpirationTime(),
            refreshToken,
            jwtUtil.getRefreshExpirationTime()
        );
        data.put("userId", user.getUserId());
        data.put("userName", user.getRealName());
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("expiresIn", jwtUtil.getAccessExpirationTime() / 1000);
        return data;
    }

    public Map<String, Object> listCandidates(String keyword, String identity, String status, int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AdminCandidateView> list = adminMapper.listCandidates(keyword, identity, status, offset, pageSize);
        long total = adminMapper.countCandidates(keyword, identity, status);
        return pageData(list, total, pageNum, pageSize);
    }

    public Map<String, Object> getCandidateDetail(Long userId) {
        ensureAdmin();
        UserEntity user = adminMapper.findCandidateById(userId);
        if (user == null) {
            throw new BusinessException(3001, "求职者不存在");
        }
        List<ResumeEntity> resumes = recruitmentMapper.findResumesByUserId(userId);
        List<Map<String, Object>> resumeList = new ArrayList<>();
        for (ResumeEntity resume : resumes) {
            List<String> violationKeywords = detectResumeViolationKeywords(resume);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("resumeId", resume.getResumeId());
            item.put("title", resume.getTitle());
            item.put("privacy", resume.getPrivacy());
            item.put("updateTime", resume.getUpdateTime());
            item.put("violationDetected", !violationKeywords.isEmpty());
            item.put("violationKeywords", violationKeywords);
            resumeList.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userId", user.getUserId());
        data.put("mobile", user.getMobile());
        data.put("name", user.getRealName());
        data.put("identity", user.getIdentityType());
        data.put("status", user.getStatus());
        data.put("school", user.getSchool());
        data.put("major", user.getMajor());
        data.put("currentCity", user.getCurrentCity());
        data.put("resumeCount", resumes.size());
        data.put("resumes", resumeList);
        return data;
    }

    @Transactional
    public void updateCandidateStatus(Long userId, AdminStatusUpdateRequest request) {
        UserEntity admin = ensureAdmin();
        UserEntity user = adminMapper.findCandidateById(userId);
        if (user == null) {
            throw new BusinessException(3001, "求职者不存在");
        }
        adminMapper.updateUserStatus(userId, request.status());
        createMessage(
            userId,
            "ACCOUNT",
            "账号状态已更新",
            "您的账号状态已被管理员调整为: " + request.status(),
            userId
        );
        writeOperationLog(admin, "CANDIDATE_STATUS_UPDATE", "candidate:" + userId, request.reason());
    }

    public Map<String, Object> listEnterprises(String keyword, String authStatus, String status, int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AdminEnterpriseView> list = adminMapper.listEnterprises(keyword, authStatus, status, offset, pageSize);
        long total = adminMapper.countEnterprises(keyword, authStatus, status);
        return pageData(list, total, pageNum, pageSize);
    }

    @Transactional
    public void auditEnterprise(Long enterpriseId, AuditRequest request) {
        UserEntity admin = ensureAdmin();
        AdminEnterpriseView enterprise = requireEnterprise(enterpriseId);
        String beforeStatus = enterprise.getAuthStatus();
        String afterStatus = "PASS".equals(request.result()) ? "PASS" : "REJECT";
        enterprise.setAuthStatus(afterStatus);
        enterprise.setAuthRemark(request.reason());
        adminMapper.updateEnterprise(enterprise);
        CompanyAuthEntity auth = adminMapper.findLatestCompanyAuth(enterpriseId);
        if (auth != null) {
            adminMapper.updateCompanyAuthAudit(auth.getAuthId(), afterStatus, request.reason(), admin.getUserId());
        }
        writeAuditLog("ENTERPRISE", enterpriseId, beforeStatus, afterStatus, request.result(), request.reason(), admin);
        createMessage(
            enterprise.getUserId(),
            "AUDIT_RESULT",
            "企业认证审核结果",
            "企业认证审核结果为: " + afterStatus + (request.reason() == null ? "" : "，原因: " + request.reason()),
            enterpriseId
        );
        writeOperationLog(admin, "ENTERPRISE_AUDIT", "enterprise:" + enterpriseId, request.reason());
    }

    @Transactional
    public void updateEnterpriseStatus(Long enterpriseId, AdminStatusUpdateRequest request) {
        UserEntity admin = ensureAdmin();
        AdminEnterpriseView enterprise = requireEnterprise(enterpriseId);
        adminMapper.updateUserStatus(enterprise.getUserId(), request.status());
        createMessage(
            enterprise.getUserId(),
            "ACCOUNT",
            "企业账号状态已更新",
            "企业账号状态已被管理员调整为: " + request.status(),
            enterpriseId
        );
        writeOperationLog(admin, "ENTERPRISE_STATUS_UPDATE", "enterprise:" + enterpriseId, request.reason());
    }

    @Transactional
    public void updateEnterpriseInfo(Long enterpriseId, EnterpriseAdminUpdateRequest request) {
        UserEntity admin = ensureAdmin();
        AdminEnterpriseView enterprise = requireEnterprise(enterpriseId);
        enterprise.setCompanyName(request.companyName());
        enterprise.setIndustry(request.industry());
        enterprise.setScale(request.scale());
        enterprise.setAddress(request.address());
        enterprise.setIntroduction(request.introduction());
        adminMapper.updateEnterprise(enterprise);
        writeOperationLog(admin, "ENTERPRISE_INFO_UPDATE", "enterprise:" + enterpriseId, request.companyName());
    }

    public Map<String, Object> listAuditJobs(String status, String companyName, int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AdminJobAuditView> list = adminMapper.listAuditJobs(status, companyName, offset, pageSize);
        long total = adminMapper.countAuditJobs(status, companyName);
        return pageData(list, total, pageNum, pageSize);
    }

    @Transactional
    public void auditJob(Long jobId, AuditRequest request) {
        UserEntity admin = ensureAdmin();
        AdminJobAuditView job = adminMapper.findAuditJobById(jobId);
        if (job == null) {
            throw new BusinessException(5001, "职位不存在");
        }
        String beforeStatus = job.getStatus();
        String afterStatus = "PASS".equals(request.result()) ? "RECRUITING" : "REJECTED";
        adminMapper.updateJobAudit(jobId, afterStatus, request.reason());
        writeAuditLog("JOB", jobId, beforeStatus, afterStatus, request.result(), request.reason(), admin);

        AdminEnterpriseView enterprise = requireEnterprise(job.getEnterpriseId());
        createMessage(
            enterprise.getUserId(),
            "AUDIT_RESULT",
            "职位审核结果通知",
            "职位《" + job.getJobName() + "》审核结果为: " + afterStatus + (request.reason() == null ? "" : "，原因: " + request.reason()),
            jobId
        );
        writeOperationLog(admin, "JOB_AUDIT", "job:" + jobId, request.reason());
    }

    public Map<String, Object> listAuditNotices(String status, String type, int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AdminNoticeEntity> list = adminMapper.listNotices(status, type, offset, pageSize);
        long total = adminMapper.countNotices(status, type);
        return pageData(list, total, pageNum, pageSize);
    }

    @Transactional
    public void auditNotice(Long noticeId, AuditRequest request) {
        UserEntity admin = ensureAdmin();
        AdminNoticeEntity notice = requireNotice(noticeId);
        String beforeStatus = notice.getStatus();
        String afterStatus = "PASS".equals(request.result()) ? "ONLINE" : "REJECTED";
        notice.setStatus(afterStatus);
        notice.setAuditRemark(request.reason());
        if ("ONLINE".equals(afterStatus)) {
            notice.setPublishTime(LocalDateTime.now());
        }
        adminMapper.updateNotice(notice);
        writeAuditLog("NOTICE", noticeId, beforeStatus, afterStatus, request.result(), request.reason(), admin);
        writeOperationLog(admin, "NOTICE_AUDIT", "notice:" + noticeId, request.reason());
    }

    public Map<String, Object> listCategories(Long parentId) {
        ensureAdmin();
        Long targetParent = parentId == null ? 0L : parentId;
        return Map.of("list", adminMapper.listCategories(targetParent));
    }

    @Transactional
    public Map<String, Object> createCategory(CategorySaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminCategoryEntity category = new AdminCategoryEntity();
        category.setParentId(request.parentId() == null ? 0L : request.parentId());
        category.setName(request.name());
        category.setCode(request.name().replace(" ", "_").toUpperCase());
        category.setSort(request.sort() == null ? 0 : request.sort());
        category.setStatus("ACTIVE");
        adminMapper.insertCategory(category);
        writeOperationLog(admin, "CATEGORY_CREATE", "category:" + category.getCategoryId(), category.getName());
        return Map.of("categoryId", category.getCategoryId());
    }

    @Transactional
    public void updateCategory(Long categoryId, CategorySaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminCategoryEntity category = new AdminCategoryEntity();
        category.setCategoryId(categoryId);
        category.setName(request.name());
        category.setSort(request.sort() == null ? 0 : request.sort());
        adminMapper.updateCategory(category);
        writeOperationLog(admin, "CATEGORY_UPDATE", "category:" + categoryId, request.name());
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        UserEntity admin = ensureAdmin();
        adminMapper.deleteCategory(categoryId);
        writeOperationLog(admin, "CATEGORY_DELETE", "category:" + categoryId, null);
    }

    public Map<String, Object> listBanners() {
        ensureAdmin();
        return Map.of("list", adminMapper.listBanners());
    }

    @Transactional
    public Map<String, Object> createBanner(BannerSaveRequest request) {
        UserEntity admin = ensureAdmin();
        FileRecordEntity image = commonService.requireFileById(request.imageFileId());
        AdminBannerEntity banner = new AdminBannerEntity();
        banner.setTitle(request.title());
        banner.setImageFileId(image.getFileId());
        banner.setImageUrl(image.getFileUrl());
        banner.setLinkUrl(request.linkUrl());
        banner.setSort(request.sort() == null ? 0 : request.sort());
        banner.setStatus(defaultIfBlank(request.status(), "ONLINE"));
        banner.setStartTime(parseDateTime(request.startTime()));
        banner.setEndTime(parseDateTime(request.endTime()));
        adminMapper.insertBanner(banner);
        writeOperationLog(admin, "BANNER_CREATE", "banner:" + banner.getBannerId(), banner.getTitle());
        return Map.of("bannerId", banner.getBannerId());
    }

    @Transactional
    public void updateBanner(Long bannerId, BannerSaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminBannerEntity banner = requireBanner(bannerId);
        FileRecordEntity image = commonService.requireFileById(request.imageFileId());
        banner.setTitle(request.title());
        banner.setImageFileId(image.getFileId());
        banner.setImageUrl(image.getFileUrl());
        banner.setLinkUrl(request.linkUrl());
        banner.setSort(request.sort() == null ? 0 : request.sort());
        banner.setStatus(defaultIfBlank(request.status(), banner.getStatus()));
        banner.setStartTime(parseDateTime(request.startTime()));
        banner.setEndTime(parseDateTime(request.endTime()));
        adminMapper.updateBanner(banner);
        writeOperationLog(admin, "BANNER_UPDATE", "banner:" + bannerId, banner.getTitle());
    }

    @Transactional
    public void deleteBanner(Long bannerId) {
        UserEntity admin = ensureAdmin();
        requireBanner(bannerId);
        adminMapper.deleteBanner(bannerId);
        writeOperationLog(admin, "BANNER_DELETE", "banner:" + bannerId, null);
    }

    public Map<String, Object> listSystemNotices(String type, String status, int pageNum, int pageSize) {
        return listAuditNotices(status, type, pageNum, pageSize);
    }

    @Transactional
    public Map<String, Object> createNotice(NoticeSaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminNoticeEntity notice = new AdminNoticeEntity();
        notice.setTitle(request.title());
        notice.setContent(request.content());
        notice.setType(request.type());
        notice.setStatus(request.status() == null || request.status().isBlank() ? "PENDING" : request.status());
        notice.setCreateBy(admin.getUserId());
        if ("ONLINE".equals(notice.getStatus())) {
            notice.setPublishTime(LocalDateTime.now());
        }
        adminMapper.insertNotice(notice);
        writeOperationLog(admin, "NOTICE_CREATE", "notice:" + notice.getNoticeId(), notice.getTitle());
        return Map.of("noticeId", notice.getNoticeId(), "status", notice.getStatus());
    }

    @Transactional
    public void updateNotice(Long noticeId, NoticeSaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminNoticeEntity notice = requireNotice(noticeId);
        notice.setTitle(request.title());
        notice.setContent(request.content());
        notice.setType(request.type());
        notice.setStatus(request.status() == null || request.status().isBlank() ? notice.getStatus() : request.status());
        if ("ONLINE".equals(notice.getStatus()) && notice.getPublishTime() == null) {
            notice.setPublishTime(LocalDateTime.now());
        }
        adminMapper.updateNotice(notice);
        writeOperationLog(admin, "NOTICE_UPDATE", "notice:" + noticeId, notice.getTitle());
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        UserEntity admin = ensureAdmin();
        adminMapper.deleteNotice(noticeId);
        writeOperationLog(admin, "NOTICE_DELETE", "notice:" + noticeId, null);
    }

    public Map<String, Object> listPermissions() {
        ensureAdmin();
        return Map.of("list", adminMapper.listPermissions());
    }

    public Map<String, Object> listRoles() {
        ensureAdmin();
        List<AdminRoleEntity> roles = adminMapper.listRoles();
        roles.forEach(role -> role.setPermissionIds(adminMapper.listRolePermissionIds(role.getRoleId())));
        return Map.of("list", roles);
    }

    @Transactional
    public Map<String, Object> createRole(RoleSaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminRoleEntity role = new AdminRoleEntity();
        role.setRoleName(request.roleName());
        role.setRoleCode(request.roleCode());
        role.setRemark(request.remark());
        role.setStatus(defaultIfBlank(request.status(), "ACTIVE"));
        adminMapper.insertRole(role);
        replaceRolePermissions(role.getRoleId(), request.permissionIds());
        writeOperationLog(admin, "ROLE_CREATE", "role:" + role.getRoleId(), role.getRoleName());
        return Map.of("roleId", role.getRoleId());
    }

    @Transactional
    public void updateRole(Long roleId, RoleSaveRequest request) {
        UserEntity admin = ensureAdmin();
        AdminRoleEntity role = requireRole(roleId);
        role.setRoleName(request.roleName());
        role.setRoleCode(request.roleCode());
        role.setRemark(request.remark());
        role.setStatus(defaultIfBlank(request.status(), role.getStatus()));
        adminMapper.updateRole(role);
        replaceRolePermissions(roleId, request.permissionIds());
        writeOperationLog(admin, "ROLE_UPDATE", "role:" + roleId, role.getRoleName());
    }

    @Transactional
    public void deleteRole(Long roleId) {
        UserEntity admin = ensureAdmin();
        requireRole(roleId);
        adminMapper.deleteRolePermissions(roleId);
        adminMapper.deleteRole(roleId);
        writeOperationLog(admin, "ROLE_DELETE", "role:" + roleId, null);
    }

    public Map<String, Object> listMessageTemplates(String type) {
        ensureAdmin();
        return Map.of("list", adminMapper.listMessageTemplates(type));
    }

    @Transactional
    public void updateMessageTemplate(Long templateId, MessageTemplateUpdateRequest request) {
        UserEntity admin = ensureAdmin();
        AdminMessageTemplateEntity template = requireMessageTemplate(templateId);
        if (request.titleTemplate() != null) {
            template.setTitleTemplate(request.titleTemplate());
        }
        if (request.contentTemplate() != null) {
            template.setContentTemplate(request.contentTemplate());
        }
        if (request.channels() != null) {
            template.setChannels(String.join(",", request.channels()));
        }
        if (request.enabled() != null) {
            template.setEnabled(request.enabled());
        }
        adminMapper.updateMessageTemplate(template);
        writeOperationLog(admin, "MESSAGE_TEMPLATE_UPDATE", "message-template:" + templateId, template.getType());
    }

    public Map<String, Object> listAuditLogs(int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AuditLogEntity> list = adminMapper.listAuditLogs(offset, pageSize);
        long total = adminMapper.countAuditLogs();
        return pageData(list, total, pageNum, pageSize);
    }

    public Map<String, Object> listOperationLogs(
        Long userId,
        String action,
        String startTime,
        String endTime,
        int pageNum,
        int pageSize
    ) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<OperationLogEntity> list = adminMapper.listOperationLogs(userId, action, startTime, endTime, offset, pageSize);
        long total = adminMapper.countOperationLogs(userId, action, startTime, endTime);
        return pageData(list, total, pageNum, pageSize);
    }

    @Transactional
    public Map<String, Object> moderateResume(Long resumeId, ResumeModerationRequest request) {
        UserEntity admin = ensureAdmin();
        ResumeEntity resume = recruitmentMapper.findResumeById(resumeId);
        if (resume == null) {
            throw new BusinessException(4001, "简历不存在");
        }
        List<String> violationKeywords = detectResumeViolationKeywords(resume);
        boolean violationDetected = !violationKeywords.isEmpty();
        String action = request.action().trim().toUpperCase();
        if ("DETECT".equals(action)) {
            return Map.of(
                "resumeId", resumeId,
                "action", action,
                "violationDetected", violationDetected,
                "keywords", violationKeywords
            );
        }
        if ("CLEAN".equals(action)) {
            if (violationDetected) {
                resume.setTitle("[已清理] " + resume.getTitle());
                resume.setSelfEvaluation("该简历存在违规内容，已由管理员清理。");
                resume.setWorkList("[]");
                resume.setSkillList("[]");
                resume.setPrivacy("PRIVATE");
                recruitmentMapper.updateResume(resume);
            }
            writeAuditLog("RESUME", resumeId, "VISIBLE", "PRIVATE", violationDetected ? "REJECT" : "PASS", request.reason(), admin);
            writeOperationLog(admin, "RESUME_MODERATE", "resume:" + resumeId, request.reason());
            createMessage(
                resume.getUserId(),
                "AUDIT_RESULT",
                "简历内容治理通知",
                violationDetected
                    ? "您的简历存在违规内容，系统已执行清理并转为私密状态。"
                    : "管理员已复核您的简历，当前未发现违规内容。",
                resumeId
            );
            return Map.of(
                "resumeId", resumeId,
                "action", action,
                "violationDetected", violationDetected,
                "keywords", violationKeywords,
                "privacy", resume.getPrivacy()
            );
        }
        throw new BusinessException(1001, "不支持的简历治理动作");
    }

    private UserEntity ensureAdmin() {
        Long userId = SecurityUtils.getUserId();
        String userType = SecurityUtils.getUserType();
        if (userId == null || !"ADMIN".equals(userType)) {
            throw new BusinessException(2002, "当前接口仅管理员可用");
        }
        UserEntity user = adminMapper.findUserByMobile(SecurityUtils.getLoginUser().getUsername());
        if (user == null) {
            throw new BusinessException(3001, "管理员不存在");
        }
        return user;
    }

    private AdminEnterpriseView requireEnterprise(Long enterpriseId) {
        AdminEnterpriseView enterprise = adminMapper.findEnterpriseById(enterpriseId);
        if (enterprise == null) {
            throw new BusinessException(6001, "企业不存在");
        }
        return enterprise;
    }

    private AdminNoticeEntity requireNotice(Long noticeId) {
        AdminNoticeEntity notice = adminMapper.findNoticeById(noticeId);
        if (notice == null) {
            throw new BusinessException(9002, "公告不存在");
        }
        return notice;
    }

    private AdminBannerEntity requireBanner(Long bannerId) {
        AdminBannerEntity banner = adminMapper.findBannerById(bannerId);
        if (banner == null) {
            throw new BusinessException(9002, "轮播图不存在");
        }
        return banner;
    }

    private AdminRoleEntity requireRole(Long roleId) {
        AdminRoleEntity role = adminMapper.findRoleById(roleId);
        if (role == null) {
            throw new BusinessException(9002, "角色不存在");
        }
        return role;
    }

    private AdminMessageTemplateEntity requireMessageTemplate(Long templateId) {
        AdminMessageTemplateEntity template = adminMapper.findMessageTemplateById(templateId);
        if (template == null) {
            throw new BusinessException(9002, "消息模板不存在");
        }
        return template;
    }

    private void writeAuditLog(
        String bizType,
        Long bizId,
        String beforeStatus,
        String afterStatus,
        String result,
        String reason,
        UserEntity admin
    ) {
        AuditLogEntity log = new AuditLogEntity();
        log.setBizType(bizType);
        log.setBizId(bizId);
        log.setBeforeStatus(beforeStatus);
        log.setAfterStatus(afterStatus);
        log.setAuditResult(result);
        log.setAuditRemark(reason);
        log.setAuditorId(admin.getUserId());
        log.setAuditorName(admin.getRealName());
        adminMapper.insertAuditLog(log);
    }

    private void writeOperationLog(UserEntity admin, String action, String resource, String detail) {
        OperationLogEntity log = new OperationLogEntity();
        log.setUserId(admin.getUserId());
        log.setUserName(admin.getRealName());
        log.setAction(action);
        log.setResource(resource);
        log.setDetail(detail);
        log.setIp(resolveRequestIp());
        adminMapper.insertOperationLog(log);
    }

    private void createMessage(Long userId, String type, String title, String content, Long bizId) {
        MessageEntity message = new MessageEntity();
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setBizId(bizId);
        message.setReadStatus("UNREAD");
        recruitmentMapper.insertMessage(message);
    }

    private void replaceRolePermissions(Long roleId, List<Long> permissionIds) {
        adminMapper.deleteRolePermissions(roleId);
        for (Long permissionId : permissionIds) {
            adminMapper.insertRolePermission(roleId, permissionId);
        }
    }

    private LocalDateTime parseDateTime(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private List<String> detectResumeViolationKeywords(ResumeEntity resume) {
        String content = String.join(" ",
            defaultIfBlank(resume.getTitle(), ""),
            defaultIfBlank(resume.getBasicInfo(), ""),
            defaultIfBlank(resume.getJobIntention(), ""),
            defaultIfBlank(resume.getEducationList(), ""),
            defaultIfBlank(resume.getWorkList(), ""),
            defaultIfBlank(resume.getSkillList(), ""),
            defaultIfBlank(resume.getSelfEvaluation(), "")
        ).toLowerCase();
        List<String> matches = new ArrayList<>();
        for (String keyword : RESUME_ILLEGAL_KEYWORDS) {
            if (content.contains(keyword.toLowerCase())) {
                matches.add(keyword);
            }
        }
        return matches;
    }

    private String resolveRequestIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        HttpServletRequest request = attributes.getRequest();
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Map<String, Object> pageData(List<?> list, long total, int pageNum, int pageSize) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPages", pageSize <= 0 ? 0 : (long) Math.ceil((double) total / pageSize));
        return data;
    }
}
