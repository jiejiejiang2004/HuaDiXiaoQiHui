package main.xiaoqihui.recruit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.security.LoginUser;
import main.xiaoqihui.common.util.JwtUtil;
import main.xiaoqihui.common.util.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecruitmentService {

    private final RecruitmentMapper recruitmentMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public RecruitmentService(
        RecruitmentMapper recruitmentMapper,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        ObjectMapper objectMapper
    ) {
        this.recruitmentMapper = recruitmentMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Map<String, Object> registerCandidate(CandidateRegisterRequest request) {
        if (Boolean.FALSE.equals(request.agreeProtocol())) {
            throw new BusinessException(1001, "请先同意用户协议");
        }
        ensureMobileNotExists(request.mobile());

        UserEntity user = new UserEntity();
        user.setMobile(request.mobile());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRealName(request.name());
        user.setIdentityType(request.identity());
        user.setUserType("CANDIDATE");
        user.setStatus("ACTIVE");
        recruitmentMapper.insertUser(user);
        return buildTokenResponse(user);
    }

    public Map<String, Object> candidateLogin(LoginRequest request) {
        authenticate(request.mobile(), request.password());
        UserEntity user = getUserByMobile(request.mobile());
        if (!"CANDIDATE".equals(user.getUserType())) {
            throw new BusinessException(2002, "当前账号不是个人求职者账号");
        }
        Map<String, Object> data = buildTokenResponse(user);
        data.put("userName", user.getRealName());
        data.put("identity", user.getIdentityType());
        data.put("hasResume", !recruitmentMapper.findResumesByUserId(user.getUserId()).isEmpty());
        return data;
    }

    @Transactional
    public Map<String, Object> registerEnterprise(EnterpriseRegisterRequest request) {
        if (Boolean.FALSE.equals(request.agreeProtocol())) {
            throw new BusinessException(1001, "请先同意用户协议");
        }
        ensureMobileNotExists(request.contactMobile());

        UserEntity user = new UserEntity();
        user.setMobile(request.contactMobile());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRealName(request.contactName());
        user.setIdentityType("ENTERPRISE");
        user.setUserType("ENTERPRISE");
        user.setStatus("ACTIVE");
        recruitmentMapper.insertUser(user);

        CompanyInfoEntity companyInfo = new CompanyInfoEntity();
        companyInfo.setUserId(user.getUserId());
        companyInfo.setCompanyName(request.companyName());
        companyInfo.setAuthStatus("PENDING");
        recruitmentMapper.insertCompanyInfo(companyInfo);

        Map<String, Object> data = buildTokenResponse(user);
        data.put("enterpriseId", companyInfo.getEnterpriseId());
        return data;
    }

    public Map<String, Object> enterpriseLogin(LoginRequest request) {
        authenticate(request.mobile(), request.password());
        UserEntity user = getUserByMobile(request.mobile());
        if (!"ENTERPRISE".equals(user.getUserType())) {
            throw new BusinessException(2002, "当前账号不是企业账号");
        }
        CompanyInfoEntity companyInfo = requireCompanyByUserId(user.getUserId());
        Map<String, Object> data = buildTokenResponse(user);
        data.put("enterpriseId", companyInfo.getEnterpriseId());
        data.put("accountId", user.getUserId());
        data.put("authStatus", companyInfo.getAuthStatus());
        return data;
    }

    public Map<String, Object> getCurrentProfile() {
        UserEntity user = requireCurrentUser();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userId", user.getUserId());
        data.put("mobile", maskMobile(user.getMobile()));
        data.put("name", user.getRealName());
        data.put("avatar", user.getAvatar());
        data.put("identity", user.getIdentityType());
        data.put("email", user.getEmail());
        data.put("school", user.getSchool());
        data.put("major", user.getMajor());
        data.put("graduationYear", user.getGraduationYear());
        data.put("currentCity", user.getCurrentCity());
        return data;
    }

    @Transactional
    public void updateCurrentProfile(ProfileUpdateRequest request) {
        UserEntity current = requireCurrentUser();
        current.setAvatar(request.avatar());
        current.setEmail(request.email());
        current.setSchool(request.school());
        current.setMajor(request.major());
        current.setGraduationYear(request.graduationYear());
        current.setCurrentCity(request.currentCity());
        recruitmentMapper.updateUserProfile(current);
    }

    @Transactional
    public Map<String, Object> createResume(ResumeSaveRequest request) {
        Long userId = requireCandidateUserId();
        ResumeEntity resume = toResumeEntity(request, userId);
        resume.setIsDefault(recruitmentMapper.findResumesByUserId(userId).isEmpty());
        recruitmentMapper.insertResume(resume);
        if (Boolean.TRUE.equals(resume.getIsDefault())) {
            recruitmentMapper.clearDefaultResume(userId);
            recruitmentMapper.setDefaultResume(userId, resume.getResumeId());
        }
        return Map.of("resumeId", resume.getResumeId());
    }

    @Transactional
    public void updateResume(Long resumeId, ResumeSaveRequest request) {
        Long userId = requireCandidateUserId();
        ResumeEntity current = requireOwnResume(resumeId, userId);
        ResumeEntity resume = toResumeEntity(request, userId);
        resume.setResumeId(current.getResumeId());
        recruitmentMapper.updateResume(resume);
    }

    public Map<String, Object> getResumeDetail(Long resumeId) {
        Long userId = requireCandidateUserId();
        ResumeEntity resume = requireOwnResume(resumeId, userId);
        return buildResumeDetail(resume);
    }

    public Map<String, Object> getEnterpriseResumeDetail(Long resumeId) {
        Long enterpriseId = requireEnterprise().getEnterpriseId();
        ResumeEntity resume = recruitmentMapper.findResumeById(resumeId);
        if (resume == null || recruitmentMapper.countEnterpriseResumeAccess(enterpriseId, resumeId) == 0) {
            throw new BusinessException(4001, "简历不存在");
        }
        return buildResumeDetail(resume);
    }

    public List<Map<String, Object>> listMyResumes() {
        Long userId = requireCandidateUserId();
        List<ResumeEntity> resumes = recruitmentMapper.findResumesByUserId(userId);
        List<Map<String, Object>> data = new ArrayList<>();
        for (ResumeEntity resume : resumes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("resumeId", resume.getResumeId());
            item.put("title", resume.getTitle());
            item.put("privacy", resume.getPrivacy());
            item.put("isDefault", resume.getIsDefault());
            item.put("updateTime", resume.getUpdateTime());
            data.add(item);
        }
        return data;
    }

    @Transactional
    public Map<String, Object> applyJob(ApplyRequest request) {
        Long userId = requireCandidateUserId();
        JobEntity job = requireRecruitingJob(request.jobId());
        ResumeEntity resume = requireOwnResume(request.resumeId(), userId);
        if (recruitmentMapper.countUserApplication(job.getJobId(), userId) > 0) {
            throw new BusinessException(5003, "该职位已投递");
        }

        ApplicationEntity application = new ApplicationEntity();
        application.setJobId(job.getJobId());
        application.setResumeId(resume.getResumeId());
        application.setUserId(userId);
        application.setEnterpriseId(job.getEnterpriseId());
        application.setCoverLetter(request.coverLetter());
        application.setStatus("PENDING");
        recruitmentMapper.insertApplication(application);

        CompanyInfoEntity companyInfo = recruitmentMapper.findCompanyById(job.getEnterpriseId());
        createMessage(
            companyInfo.getUserId(),
            "APPLY",
            "收到新的简历投递",
            resume.getTitle() + " 已投递到职位 " + job.getJobName(),
            application.getApplyId()
        );

        return Map.of(
            "applyId", application.getApplyId(),
            "applyTime", LocalDateTime.now()
        );
    }

    public Map<String, Object> searchJobs(String keyword, String education, String experience, String location, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<JobEntity> jobs = recruitmentMapper.searchJobs(keyword, education, experience, location, offset, pageSize);
        long total = recruitmentMapper.countJobs(keyword, education, experience, location);
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobEntity job : jobs) {
            list.add(buildJobSummary(job));
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getJobDetail(Long jobId) {
        JobEntity job = recruitmentMapper.findJobById(jobId);
        if (job == null) {
            throw new BusinessException(5001, "职位不存在");
        }
        Map<String, Object> data = buildJobSummary(job);
        data.put("companyId", job.getEnterpriseId());
        data.put("responsibility", job.getResponsibility());
        data.put("requirement", job.getRequirementText());
        data.put("contactName", job.getContactName());
        data.put("contactMobile", job.getContactMobile());
        data.put("publishTime", job.getPublishTime());
        data.put("status", job.getStatus());
        data.put("welfare", readJsonList(job.getWelfare()));
        Long userId = SecurityUtils.getUserId();
        data.put("applied", userId != null && recruitmentMapper.countUserApplication(jobId, userId) > 0);
        data.put("collected", false);
        return data;
    }

    public Map<String, Object> listMyApplications(String status, int pageNum, int pageSize) {
        Long userId = requireCandidateUserId();
        int offset = (pageNum - 1) * pageSize;
        List<ApplicationEntity> applications = recruitmentMapper.listUserApplications(userId, status, offset, pageSize);
        long total = recruitmentMapper.countUserApplications(userId, status);
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApplicationEntity application : applications) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("applyId", application.getApplyId());
            item.put("jobId", application.getJobId());
            item.put("jobName", application.getJobName());
            item.put("companyName", application.getCompanyName());
            item.put("status", application.getStatus());
            item.put("applyTime", application.getApplyTime());
            item.put("updateTime", application.getUpdateTime());
            list.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getEnterpriseInfo() {
        CompanyInfoEntity companyInfo = requireEnterprise();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("enterpriseId", companyInfo.getEnterpriseId());
        data.put("companyName", companyInfo.getCompanyName());
        data.put("logo", companyInfo.getLogo());
        data.put("industry", companyInfo.getIndustry());
        data.put("scale", companyInfo.getScale());
        data.put("address", companyInfo.getAddress());
        data.put("introduction", companyInfo.getIntroduction());
        data.put("website", companyInfo.getWebsite());
        data.put("authStatus", companyInfo.getAuthStatus());
        data.put("rejectReason", companyInfo.getAuthRemark());
        return data;
    }

    @Transactional
    public void updateEnterpriseInfo(EnterpriseInfoUpdateRequest request) {
        CompanyInfoEntity companyInfo = requireEnterprise();
        companyInfo.setIndustry(request.industry());
        companyInfo.setScale(request.scale());
        companyInfo.setAddress(request.address());
        companyInfo.setIntroduction(request.introduction());
        companyInfo.setWebsite(request.website());
        companyInfo.setLogo(request.logo());
        recruitmentMapper.updateCompanyInfo(companyInfo);
    }

    @Transactional
    public Map<String, Object> createEnterpriseJob(JobPublishRequest request) {
        CompanyInfoEntity enterprise = requireEnterprise();
        ensureEnterpriseApproved(enterprise);
        JobEntity job = toJobEntity(request, enterprise.getEnterpriseId());
        job.setStatus("PENDING");
        job.setPublishTime(LocalDateTime.now());
        job.setRefreshTime(LocalDateTime.now());
        recruitmentMapper.insertJob(job);
        return Map.of("jobId", job.getJobId(), "status", job.getStatus());
    }

    @Transactional
    public void updateEnterpriseJob(Long jobId, JobPublishRequest request) {
        CompanyInfoEntity enterprise = requireEnterprise();
        ensureEnterpriseApproved(enterprise);
        JobEntity current = recruitmentMapper.findJobById(jobId);
        if (current == null || !enterprise.getEnterpriseId().equals(current.getEnterpriseId())) {
            throw new BusinessException(5001, "职位不存在");
        }
        JobEntity job = toJobEntity(request, enterprise.getEnterpriseId());
        job.setJobId(jobId);
        job.setStatus("PENDING");
        recruitmentMapper.updateJob(job);
    }

    public Map<String, Object> listEnterpriseJobs(String status, String keyword, int pageNum, int pageSize) {
        CompanyInfoEntity enterprise = requireEnterprise();
        int offset = (pageNum - 1) * pageSize;
        List<JobEntity> jobs = recruitmentMapper.listEnterpriseJobs(enterprise.getEnterpriseId(), status, keyword, offset, pageSize);
        long total = recruitmentMapper.countEnterpriseJobs(enterprise.getEnterpriseId(), status, keyword);
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobEntity job : jobs) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("jobId", job.getJobId());
            item.put("jobName", job.getJobName());
            item.put("status", job.getStatus());
            item.put("publishTime", job.getPublishTime());
            item.put("refreshTime", job.getRefreshTime());
            item.put("viewCount", 0);
            item.put("applyCount", recruitmentMapper.countApplicationsByJob(job.getJobId()));
            list.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> listEnterpriseApplications(String status, Long jobId, int pageNum, int pageSize) {
        CompanyInfoEntity enterprise = requireEnterprise();
        int offset = (pageNum - 1) * pageSize;
        List<ApplicationEntity> applications = recruitmentMapper.listEnterpriseApplications(enterprise.getEnterpriseId(), status, jobId, offset, pageSize);
        long total = recruitmentMapper.countEnterpriseApplications(enterprise.getEnterpriseId(), status, jobId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApplicationEntity application : applications) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("applyId", application.getApplyId());
            item.put("resumeId", application.getResumeId());
            item.put("jobId", application.getJobId());
            item.put("jobName", application.getJobName());
            item.put("candidateName", application.getCandidateName());
            item.put("status", application.getStatus());
            item.put("applyTime", application.getApplyTime());
            list.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return data;
    }

    @Transactional
    public void updateEnterpriseApplicationStatus(Long applyId, ApplicationStatusRequest request) {
        CompanyInfoEntity enterprise = requireEnterprise();
        ApplicationEntity application = recruitmentMapper.findApplicationById(applyId);
        if (application == null || !enterprise.getEnterpriseId().equals(application.getEnterpriseId())) {
            throw new BusinessException(5001, "投递记录不存在");
        }
        recruitmentMapper.updateApplicationStatus(applyId, request.status());
        String messageType = "INTERVIEW".equals(request.status()) ? "INTERVIEW" : "REPLY";
        String title = "INTERVIEW".equals(request.status()) ? "收到面试邀约" : "简历状态已更新";
        String content = "您的投递状态已更新为: " + request.status() + (request.remark() == null ? "" : "，备注: " + request.remark());
        createMessage(application.getUserId(), messageType, title, content, applyId);
    }

    public Map<String, Object> listMessages(String type, String readStatus, int pageNum, int pageSize) {
        Long userId = requireLoginUserId();
        int offset = (pageNum - 1) * pageSize;
        List<MessageEntity> messages = recruitmentMapper.listMessages(userId, type, readStatus, offset, pageSize);
        long total = recruitmentMapper.countMessages(userId, type, readStatus);
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageEntity message : messages) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("messageId", message.getMessageId());
            item.put("type", message.getType());
            item.put("title", message.getTitle());
            item.put("content", message.getContent());
            item.put("bizId", message.getBizId());
            item.put("readStatus", message.getReadStatus());
            item.put("createTime", message.getCreateTime());
            list.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("unreadCount", recruitmentMapper.countUnreadMessages(userId));
        return data;
    }

    @Transactional
    public void markMessagesRead(ReadMessageRequest request) {
        Long userId = requireLoginUserId();
        recruitmentMapper.markMessagesRead(userId, request == null ? null : request.messageIds());
    }

    private void authenticate(String mobile, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mobile, password));
    }

    private void ensureMobileNotExists(String mobile) {
        if (recruitmentMapper.findUserByMobile(mobile) != null) {
            throw new BusinessException(3003, "手机号已注册");
        }
    }

    private UserEntity getUserByMobile(String mobile) {
        UserEntity user = recruitmentMapper.findUserByMobile(mobile);
        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }
        return user;
    }

    private UserEntity requireCurrentUser() {
        return requireUserById(requireLoginUserId());
    }

    private UserEntity requireUserById(Long userId) {
        UserEntity user = recruitmentMapper.findUserById(userId);
        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }
        return user;
    }

    private Long requireLoginUserId() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BusinessException(2001, "用户未登录或Token失效");
        }
        return userId;
    }

    private Long requireCandidateUserId() {
        String userType = SecurityUtils.getUserType();
        if (!"CANDIDATE".equals(userType)) {
            throw new BusinessException(2002, "当前接口仅个人求职者可用");
        }
        return requireLoginUserId();
    }

    private CompanyInfoEntity requireEnterprise() {
        String userType = SecurityUtils.getUserType();
        if (!"ENTERPRISE".equals(userType)) {
            throw new BusinessException(2002, "当前接口仅企业用户可用");
        }
        return requireCompanyByUserId(requireLoginUserId());
    }

    private CompanyInfoEntity requireCompanyByUserId(Long userId) {
        CompanyInfoEntity companyInfo = recruitmentMapper.findCompanyByUserId(userId);
        if (companyInfo == null) {
            throw new BusinessException(6001, "企业信息不存在");
        }
        return companyInfo;
    }

    private void ensureEnterpriseApproved(CompanyInfoEntity companyInfo) {
        if ("PASS".equals(companyInfo.getAuthStatus())) {
            return;
        }
        if ("REJECT".equals(companyInfo.getAuthStatus())) {
            throw new BusinessException(6003, "企业认证已被驳回");
        }
        throw new BusinessException(6002, "企业认证待审核");
    }

    private ResumeEntity requireOwnResume(Long resumeId, Long userId) {
        ResumeEntity resume = recruitmentMapper.findResumeById(resumeId);
        if (resume == null || !userId.equals(resume.getUserId())) {
            throw new BusinessException(4001, "简历不存在");
        }
        return resume;
    }

    private JobEntity requireRecruitingJob(Long jobId) {
        JobEntity job = recruitmentMapper.findJobById(jobId);
        if (job == null) {
            throw new BusinessException(5001, "职位不存在");
        }
        if (!"RECRUITING".equals(job.getStatus())) {
            throw new BusinessException(5002, "职位已下架");
        }
        return job;
    }

    private Map<String, Object> buildTokenResponse(UserEntity user) {
        LoginUser loginUser = new LoginUser(
            user.getUserId(),
            user.getMobile(),
            user.getPassword(),
            user.getUserType(),
            user.getRealName(),
            user.getStatus()
        );
        return new LinkedHashMap<>(Map.of(
            "userId", user.getUserId(),
            "accessToken", jwtUtil.generateAccessToken(loginUser),
            "refreshToken", jwtUtil.generateRefreshToken(loginUser),
            "expiresIn", jwtUtil.getAccessExpirationTime() / 1000
        ));
    }

    private ResumeEntity toResumeEntity(ResumeSaveRequest request, Long userId) {
        ResumeEntity resume = new ResumeEntity();
        resume.setUserId(userId);
        resume.setTitle((request.title() == null || request.title().isBlank()) ? "默认简历" : request.title());
        resume.setBasicInfo(writeJson(request.basicInfo()));
        resume.setJobIntention(writeJson(request.jobIntention()));
        resume.setEducationList(writeJson(request.educationList()));
        resume.setWorkList(writeJson(request.workList() == null ? List.of() : request.workList()));
        resume.setSkillList(writeJson(request.skillList() == null ? List.of() : request.skillList()));
        resume.setSelfEvaluation(request.selfEvaluation());
        resume.setPrivacy(request.privacy() == null || request.privacy().isBlank() ? "ENTERPRISE_ONLY" : request.privacy());
        return resume;
    }

    private JobEntity toJobEntity(JobPublishRequest request, Long enterpriseId) {
        JobEntity job = new JobEntity();
        job.setEnterpriseId(enterpriseId);
        job.setJobName(request.jobName());
        job.setJobCategory(request.jobCategory());
        job.setResponsibility(request.responsibility());
        job.setRequirementText(request.requirement());
        job.setSalaryMin(request.salaryMin());
        job.setSalaryMax(request.salaryMax());
        job.setLocation(request.location());
        job.setHeadCount(request.headCount());
        job.setEducation(request.education());
        job.setExperience(request.experience());
        job.setWelfare(writeJson(request.welfare() == null ? List.of() : request.welfare()));
        job.setContactName(request.contactName());
        job.setContactMobile(request.contactMobile());
        return job;
    }

    private Map<String, Object> buildResumeDetail(ResumeEntity resume) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("resumeId", resume.getResumeId());
        data.put("title", resume.getTitle());
        data.put("basicInfo", readJsonObject(resume.getBasicInfo()));
        data.put("jobIntention", readJsonObject(resume.getJobIntention()));
        data.put("educationList", readJsonList(resume.getEducationList()));
        data.put("workList", readJsonList(resume.getWorkList()));
        data.put("skillList", readJsonList(resume.getSkillList()));
        data.put("selfEvaluation", resume.getSelfEvaluation());
        data.put("privacy", resume.getPrivacy());
        data.put("isDefault", resume.getIsDefault());
        data.put("updateTime", resume.getUpdateTime());
        return data;
    }

    private Map<String, Object> buildJobSummary(JobEntity job) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("jobId", job.getJobId());
        item.put("jobName", job.getJobName());
        item.put("companyId", job.getEnterpriseId());
        item.put("companyName", job.getCompanyName());
        item.put("companyLogo", job.getCompanyLogo());
        item.put("salaryMin", job.getSalaryMin());
        item.put("salaryMax", job.getSalaryMax());
        item.put("location", job.getLocation());
        item.put("education", job.getEducation());
        item.put("experience", job.getExperience());
        item.put("headCount", job.getHeadCount());
        item.put("publishTime", job.getPublishTime());
        item.put("status", job.getStatus());
        item.put("welfare", readJsonList(job.getWelfare()));
        return item;
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

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(1001, "JSON数据格式不正确");
        }
    }

    private Map<String, Object> readJsonObject(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            return Map.of();
        }
    }

    private List<Object> readJsonList(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            return List.of();
        }
    }

    private String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 7) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }
}
