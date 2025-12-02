package com.proyectoweb.reports.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LeadsReportDto(
        String period,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int totalLeads,
        int activeLeads,
        int convertedLeads,
        int lostLeads,
        double conversionRate,
        double averageConversionTime,
        List<LeadBySourceDto> leadsBySource,
        List<LeadByStatusDto> leadsByStatus
) {
    public record LeadBySourceDto(
            String source,
            int count,
            int converted,
            double conversionRate
    ) {
    }

    public record LeadByStatusDto(
            String status,
            int count,
            double percentage
    ) {
    }
}
