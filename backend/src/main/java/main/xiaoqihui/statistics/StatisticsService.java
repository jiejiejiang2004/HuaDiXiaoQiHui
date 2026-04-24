package main.xiaoqihui.statistics;

import main.xiaoqihui.common.CommonService;
import main.xiaoqihui.common.domain.FileRecordEntity;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.util.SecurityUtils;
import main.xiaoqihui.recruit.RecruitmentMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        data.put("favoriteCount", recruitmentMapper.countCollectedJobs(userId));
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

    /**
     * 首页等匿名场景：仅返回企业数、岗位数、求职者数。
     */
    public Map<String, Object> platformPublicBrief() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("enterpriseCount", statisticsMapper.countTotalEnterprises());
        data.put("jobCount", statisticsMapper.countTotalJobs());
        data.put("candidateCount", statisticsMapper.countTotalCandidates());
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

        String format = request.format() == null || request.format().isBlank() ? "xlsx" : request.format().toLowerCase();
        ExportFile exportFile = buildExportFile(type, format);
        String fileName = "statistics-" + type.toLowerCase() + "-" + LocalDate.now() + "." + exportFile.extension();
        FileRecordEntity fileRecord = commonService.saveGeneratedFile("REPORT", fileName, exportFile.content());

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
            "fileId", fileRecord.getFileId(),
            "fileName", fileName
        );
    }

    private ExportFile buildExportFile(String type, String format) {
        Map<String, Object> data = loadExportMetrics(type);
        if ("csv".equalsIgnoreCase(format)) {
            return new ExportFile("csv", buildCsvContent(type, data));
        }
        return new ExportFile("xlsx", buildExcelContent(type, data));
    }

    private Map<String, Object> loadExportMetrics(String type) {
        if ("CANDIDATE".equals(type)) {
            return candidateStatistics();
        }
        if ("ENTERPRISE".equals(type)) {
            return enterpriseStatistics();
        }
        return platformOverviewStatistics();
    }

    private byte[] buildExcelContent(String type, Map<String, Object> data) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet(type + "_statistics");
            int rowNum = 0;
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("指标");
            header.createCell(1).setCellValue("值");

            for (Map.Entry<String, Object> entry : buildMetricMap(type, data).entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(String.valueOf(entry.getValue()));
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new BusinessException(7001, "Excel 导出失败");
        }
    }

    private byte[] buildCsvContent(String type, Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        builder.append("指标,值\n");
        for (Map.Entry<String, Object> entry : buildMetricMap(type, data).entrySet()) {
            builder.append(entry.getKey()).append(',').append(entry.getValue()).append('\n');
        }
        return builder.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private Map<String, Object> buildMetricMap(String type, Map<String, Object> data) {
        Map<String, Object> result = new LinkedHashMap<>();
        if ("CANDIDATE".equals(type)) {
            result.put("投递次数", data.get("applyCount"));
            result.put("面试次数", data.get("interviewCount"));
            result.put("被查看次数", data.get("viewedCount"));
            result.put("收藏次数", data.get("favoriteCount"));
            return result;
        }
        if ("ENTERPRISE".equals(type)) {
            result.put("职位浏览量", data.get("jobViewCount"));
            result.put("收到简历数", data.get("resumeReceivedCount"));
            result.put("面试邀约数", data.get("interviewCount"));
            result.put("招聘中职位数", data.get("activeJobCount"));
            return result;
        }
        result.put("个人用户数", data.get("candidateCount"));
        result.put("企业用户数", data.get("enterpriseCount"));
        result.put("职位总数", data.get("jobCount"));
        result.put("投递总数", data.get("applyCount"));
        result.put("面试总数", data.get("interviewCount"));
        return result;
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

    private record ExportFile(String extension, byte[] content) {
    }
}