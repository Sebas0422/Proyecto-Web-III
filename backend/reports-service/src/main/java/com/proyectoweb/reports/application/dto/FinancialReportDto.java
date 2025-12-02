package com.proyectoweb.reports.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FinancialReportDto(
        String period,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netProfit,
        BigDecimal profitMargin,
        BigDecimal pendingPayments,
        BigDecimal confirmedPayments,
        String currency,
        List<IncomeBySourceDto> incomeBySource,
        List<MonthlyFinancialDto> monthlyData
) {
    public record IncomeBySourceDto(
            String source,
            BigDecimal amount,
            String currency
    ) {
    }

    public record MonthlyFinancialDto(
            String month,
            BigDecimal income,
            BigDecimal expenses,
            BigDecimal profit,
            String currency
    ) {
    }
}
