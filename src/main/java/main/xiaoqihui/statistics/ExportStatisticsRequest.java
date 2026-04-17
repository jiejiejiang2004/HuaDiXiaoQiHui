package main.xiaoqihui.statistics;

import jakarta.validation.constraints.NotBlank;

public record ExportStatisticsRequest(
    @NotBlank String type,
    @NotBlank String startDate,
    @NotBlank String endDate,
    String format
) {
}
