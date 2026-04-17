package main.xiaoqihui.statistics;

import main.xiaoqihui.common.CommonService;
import main.xiaoqihui.common.domain.FileRecordEntity;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.util.SecurityUtils;
import main.xiaoqihui.recruit.RecruitmentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class StatisticsService {

    private final StatisticsMapper statisticsMapper;
    private final RecruitmentMapper recruitmentMapper;
    private final CommonService commonService;

    public StatisticsService(
        StatisticsMapper statisticsMapper,
        RecruitmentMapper recruitmentMapper,
        CommonService commonService
    ) {
        this.statisticsMapper = statisticsMapper;
        this.recruitmentMapper = recruitmentMapper;
        this.commonService = commonService;
    }

    public Map<String, Object> candidateStatistics() {
        Long userId = requireRole("CANDIDATE");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("applyCount", statisticsMapper.countCandidateApplies(userId));
        data.put("interviewCount", statisticsMapper.countCandidateInterviews(userId));
        data.put("viewedCount", statisticsMapper.countCandidateViews(userId));
        data.put("favoriteCount", 0);
        data.put("applyTrend", statisticsMapper.candidateApplyTrend(userId, LocalDate.now().minusDays(29)));
        return data;
    }

    public Map<String, Object> enterpriseStatistics() {
        Long userId = requireRole("ENTERPRISE");
        Long enterpriseId = recruitmentMapper.findCompanyIdByUserId(userId);
        if (enterpriseId == null) {
            throw new BusinessException(6001, "企业信息不存在");
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("jobViewCount", statisticsMapper.sumEnterpriseJobViews(enterpriseId));
        data.put("resumeReceivedCount", statisticsMapper.countEnterpriseApplies(enterpriseId));
        data.put("interviewCount", statisticsMapper.countEnterpriseInterviews(enterpriseId));
        data.put("activeJobCount", statisticsMapper.countEnterpriseActiveJobs(enterpriseId));
        data.put("jobStats", statisticsMapper.listEnterpriseJobStats(enterpriseId));
        data.put(
            "trend",
            statisticsMapper.enterpriseTrend(
                enterpriseId,
                LocalDate.now().minusDays(29),
                LocalDate.now().plusDays(1)
            )
        );
        return data;
    }

    public Map<String, Object> platformOverviewStatistics() {
        requireRole("ADMIN");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateCount", statisticsMapper.countTotalCandidates());
        data.put("enterpriseCount", statisticsMapper.countTotalEnterprises());
        data.put("jobCount", statisticsMapper.countTotalJobs());
        data.put("applyCount", statisticsMapper.countTotalApplies());
        data.put("interviewCount", statisticsMapper.countTotalInterviews());
        data.put("employmentByArea", statisticsMapper.employmentByArea());
        data.put("industryDistribution", statisticsMapper.industryDistribution());
        data.put("educationDistribution", statisticsMapper.educationDistribution());
        data.put(
            "trend",
            statisticsMapper.platformTrend(LocalDate.now().minusMonths(5).withDayOfMonth(1), LocalDate.now().plusDays(1))
        );
        return data;
    }

    public Map<String, Object> exportStatistics(ExportStatisticsRequest request) {
        Long userId = requireLogin();
        String userType = SecurityUtils.getUserType();
        String type = request.type().toUpperCase();

        if ("CANDIDATE".equals(type) && !"CANDIDATE".equals(userType)) {
            throw new BusinessException(2002, "当前用户无权导出个人统计");
        }
        if ("ENTERPRISE".equals(type) && !"ENTERPRISE".equals(userType)) {
            throw new BusinessException(2002, "当前用户无权导出企业统计");
        }
        if ("PLATFORM".equals(type) && !"ADMIN".equals(userType)) {
            throw new BusinessException(2002, "当前用户无权导出平台统计");
        }

        String content = buildExportContent(type);
        String fileName = "statistics-" + type.toLowerCase() + "-" + LocalDate.now() + ".csv";
        FileRecordEntity fileRecord = commonService.saveGeneratedFile("REPORT", fileName, content);

        ExportTaskEntity task = new ExportTaskEntity();
        task.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        task.setTaskType(type);
        task.setUserId(userId);
        task.setFileId(fileRecord.getFileId());
        task.setDownloadUrl(fileRecord.getFileUrl());
        task.setStatus("SUCCESS");
        statisticsMapper.insertExportTask(task);

        return Map.of(
            "taskId", task.getTaskId(),
            "status", task.getStatus(),
            "downloadUrl", fileRecord.getFileUrl(),
            "fileId", fileRecord.getFileId()
        );
    }

    private String buildExportContent(String type) {
        StringBuilder builder = new StringBuilder();
        if ("CANDIDATE".equals(type)) {
            Map<String, Object> data = candidateStatistics();
            builder.append("指标,值\n")
                .append("投递次数,").append(data.get("applyCount")).append('\n')
                .append("面试次数,").append(data.get("interviewCount")).append('\n')
                .append("被查看次数,").append(data.get("viewedCount")).append('\n')
                .append("收藏次数,").append(data.get("favoriteCount")).append('\n');
            return builder.toString();
        }
        if ("ENTERPRISE".equals(type)) {
            Map<String, Object> data = enterpriseStatistics();
            builder.append("指标,值\n")
                .append("职位浏览量,").append(data.get("jobViewCount")).append('\n')
                .append("收到简历数,").append(data.get("resumeReceivedCount")).append('\n')
                .append("面试邀约数,").append(data.get("interviewCount")).append('\n')
                .append("招聘中职位数,").append(data.get("activeJobCount")).append('\n');
            return builder.toString();
        }
        Map<String, Object> data = platformOverviewStatistics();
        builder.append("指标,值\n")
            .append("个人用户数,").append(data.get("candidateCount")).append('\n')
            .append("企业用户数,").append(data.get("enterpriseCount")).append('\n')
            .append("职位总数,").append(data.get("jobCount")).append('\n')
            .append("投递总数,").append(data.get("applyCount")).append('\n')
            .append("面试总数,").append(data.get("interviewCount")).append('\n');
        return builder.toString();
    }

    private Long requireRole(String role) {
        Long userId = requireLogin();
        if (!role.equals(SecurityUtils.getUserType())) {
            throw new BusinessException(2002, "当前接口无访问权限");
        }
        return userId;
    }

    private Long requireLogin() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BusinessException(2001, "用户未登录或Token失效");
        }
        return userId;
    }
}
