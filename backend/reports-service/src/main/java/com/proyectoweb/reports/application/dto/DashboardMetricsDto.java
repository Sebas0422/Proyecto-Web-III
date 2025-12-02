package com.proyectoweb.reports.application.dto;

import java.math.BigDecimal;

public record DashboardMetricsDto(
        int totalProjects,
        int activeProjects,
        int totalLots,
        int availableLots,
        int soldLots,
        int reservedLots,
        int totalLeads,
        int activeLeads,
        int convertedLeads,
        double conversionRate,
        int totalReservations,
        int confirmedReservations,
        int pendingReservations,
        BigDecimal totalRevenue,
        BigDecimal pendingPayments,
        BigDecimal confirmedPayments,
        String currency
) {
}
