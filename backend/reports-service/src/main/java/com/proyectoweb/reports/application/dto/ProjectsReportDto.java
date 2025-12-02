package com.proyectoweb.reports.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectsReportDto(
        String period,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int totalProjects,
        int activeProjects,
        int completedProjects,
        List<ProjectDetailsDto> projectDetails
) {
    public record ProjectDetailsDto(
            String projectId,
            String projectName,
            int totalLots,
            int availableLots,
            int soldLots,
            int reservedLots,
            double salesPercentage,
            BigDecimal totalRevenue,
            String currency,
            String status
    ) {
    }
}
