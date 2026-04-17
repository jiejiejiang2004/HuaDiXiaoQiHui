package main.xiaoqihui.statistics;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/candidate")
    public ApiResponse<?> candidateStatistics() {
        return ApiResponse.success(statisticsService.candidateStatistics());
    }

    @GetMapping("/enterprise")
    public ApiResponse<?> enterpriseStatistics() {
        return ApiResponse.success(statisticsService.enterpriseStatistics());
    }

    @GetMapping("/platform/overview")
    public ApiResponse<?> platformOverviewStatistics() {
        return ApiResponse.success(statisticsService.platformOverviewStatistics());
    }

    @PostMapping("/export")
    public ApiResponse<?> exportStatistics(@Valid @RequestBody ExportStatisticsRequest request) {
        return ApiResponse.success(statisticsService.exportStatistics(request));
    }
}
