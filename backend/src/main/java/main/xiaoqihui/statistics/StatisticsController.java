package main.xiaoqihui.statistics;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit/api/v1")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/home/platform-brief")
    public ApiResponse<?> platformPublicBrief() {
        return ApiResponse.success(statisticsService.platformPublicBrief());
    }

    @GetMapping("/statistics/candidate")
    public ApiResponse<?> candidateStatistics() {
        return ApiResponse.success(statisticsService.candidateStatistics());
    }

    @GetMapping("/statistics/enterprise")
    public ApiResponse<?> enterpriseStatistics() {
        return ApiResponse.success(statisticsService.enterpriseStatistics());
    }

    @GetMapping("/statistics/platform/overview")
    public ApiResponse<?> platformOverviewStatistics() {
        return ApiResponse.success(statisticsService.platformOverviewStatistics());
    }

    @GetMapping("/admin/statistics/overview")
    public ApiResponse<?> adminOverviewStatistics() {
        return ApiResponse.success(statisticsService.platformOverviewStatistics());
    }

    @PostMapping("/statistics/export")
    public ApiResponse<?> exportStatistics(@Valid @RequestBody ExportStatisticsRequest request) {
        return ApiResponse.success(statisticsService.exportStatistics(request));
    }
}
