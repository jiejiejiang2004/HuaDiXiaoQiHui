package main.xiaoqihui.admin;

import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.security.LoginUser;
import main.xiaoqihui.common.util.JwtUtil;
import main.xiaoqihui.recruit.CompanyAuthEntity;
import main.xiaoqihui.common.util.SecurityUtils;
import main.xiaoqihui.recruit.MessageEntity;
import main.xiaoqihui.recruit.RecruitmentMapper;
import main.xiaoqihui.recruit.ResumeEntity;
import main.xiaoqihui.recruit.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final RecruitmentMapper recruitmentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminService(
        AdminMapper adminMapper,
        RecruitmentMapper recruitmentMapper,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.adminMapper = adminMapper;
        this.recruitmentMapper = recruitmentMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
        data.put("userId", user.getUserId());
        data.put("userName", user.getRealName());
        data.put("accessToken", jwtUtil.generateAccessToken(loginUser));
        data.put("refreshToken", jwtUtil.generateRefreshToken(loginUser));
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
        data.put("resumes", resumes);
        return data;
    }

    @Transactional
    public void updateCandidateStatus(Long userId, AdminStatusUpdateRequest request) {
        ensureAdmin();
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
    }

    @Transactional
    public void updateEnterpriseStatus(Long enterpriseId, AdminStatusUpdateRequest request) {
        ensureAdmin();
        AdminEnterpriseView enterprise = requireEnterprise(enterpriseId);
        adminMapper.updateUserStatus(enterprise.getUserId(), request.status());
        createMessage(
            enterprise.getUserId(),
            "ACCOUNT",
            "企业账号状态已更新",
            "企业账号状态已被管理员调整为: " + request.status(),
            enterpriseId
        );
    }

    @Transactional
    public void updateEnterpriseInfo(Long enterpriseId, EnterpriseAdminUpdateRequest request) {
        ensureAdmin();
        AdminEnterpriseView enterprise = requireEnterprise(enterpriseId);
        enterprise.setCompanyName(request.companyName());
        enterprise.setIndustry(request.industry());
        enterprise.setScale(request.scale());
        enterprise.setAddress(request.address());
        enterprise.setIntroduction(request.introduction());
        adminMapper.updateEnterprise(enterprise);
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
    }

    public Map<String, Object> listCategories(Long parentId) {
        ensureAdmin();
        Long targetParent = parentId == null ? 0L : parentId;
        return Map.of("list", adminMapper.listCategories(targetParent));
    }

    @Transactional
    public Map<String, Object> createCategory(CategorySaveRequest request) {
        ensureAdmin();
        AdminCategoryEntity category = new AdminCategoryEntity();
        category.setParentId(request.parentId() == null ? 0L : request.parentId());
        category.setName(request.name());
        category.setCode(request.name().replace(" ", "_").toUpperCase());
        category.setSort(request.sort() == null ? 0 : request.sort());
        category.setStatus("ACTIVE");
        adminMapper.insertCategory(category);
        return Map.of("categoryId", category.getCategoryId());
    }

    @Transactional
    public void updateCategory(Long categoryId, CategorySaveRequest request) {
        ensureAdmin();
        AdminCategoryEntity category = new AdminCategoryEntity();
        category.setCategoryId(categoryId);
        category.setName(request.name());
        category.setSort(request.sort() == null ? 0 : request.sort());
        adminMapper.updateCategory(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        ensureAdmin();
        adminMapper.deleteCategory(categoryId);
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
        return Map.of("noticeId", notice.getNoticeId(), "status", notice.getStatus());
    }

    @Transactional
    public void updateNotice(Long noticeId, NoticeSaveRequest request) {
        ensureAdmin();
        AdminNoticeEntity notice = requireNotice(noticeId);
        notice.setTitle(request.title());
        notice.setContent(request.content());
        notice.setType(request.type());
        notice.setStatus(request.status() == null || request.status().isBlank() ? notice.getStatus() : request.status());
        if ("ONLINE".equals(notice.getStatus()) && notice.getPublishTime() == null) {
            notice.setPublishTime(LocalDateTime.now());
        }
        adminMapper.updateNotice(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        ensureAdmin();
        adminMapper.deleteNotice(noticeId);
    }

    public Map<String, Object> listAuditLogs(int pageNum, int pageSize) {
        ensureAdmin();
        int offset = (pageNum - 1) * pageSize;
        List<AuditLogEntity> list = adminMapper.listAuditLogs(offset, pageSize);
        long total = adminMapper.countAuditLogs();
        return pageData(list, total, pageNum, pageSize);
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
