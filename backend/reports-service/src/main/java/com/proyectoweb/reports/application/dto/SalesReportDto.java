package com.proyectoweb.reports.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SalesReportDto(
        String period,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int totalSales,
        BigDecimal totalRevenue,
        BigDecimal averageSalePrice,
        String currency,
        List<SaleByProjectDto> salesByProject,
        List<SaleByMonthDto> salesByMonth
) {
    public record SaleByProjectDto(
            String projectId,
            String projectName,
            int lotsSold,
            BigDecimal revenue,
            String currency
    ) {
    }

    public record SaleByMonthDto(
            String month,
            int lotsSold,
            BigDecimal revenue,
            String currency
    ) {
    }
}
