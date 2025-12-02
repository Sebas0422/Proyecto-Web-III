package com.proyectoweb.reports.application.queries.financial;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.FinancialReportDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class GetFinancialReportQueryHandler implements Command.Handler<GetFinancialReportQuery, FinancialReportDto> {

    @Override
    public FinancialReportDto handle(GetFinancialReportQuery query) {
        // TODO: Implementar lógica real consultando base de datos
        
        List<FinancialReportDto.IncomeBySourceDto> incomeBySource = List.of(
                new FinancialReportDto.IncomeBySourceDto("Venta de Lotes", new BigDecimal("1100000.00"), "BOB"),
                new FinancialReportDto.IncomeBySourceDto("Reservas", new BigDecimal("150000.00"), "BOB"),
                new FinancialReportDto.IncomeBySourceDto("Otros", new BigDecimal("25000.00"), "BOB")
        );

        List<FinancialReportDto.MonthlyFinancialDto> monthlyData = List.of(
                new FinancialReportDto.MonthlyFinancialDto("Enero", new BigDecimal("300000.00"), new BigDecimal("50000.00"), new BigDecimal("250000.00"), "BOB"),
                new FinancialReportDto.MonthlyFinancialDto("Febrero", new BigDecimal("350000.00"), new BigDecimal("60000.00"), new BigDecimal("290000.00"), "BOB"),
                new FinancialReportDto.MonthlyFinancialDto("Marzo", new BigDecimal("400000.00"), new BigDecimal("70000.00"), new BigDecimal("330000.00"), "BOB"),
                new FinancialReportDto.MonthlyFinancialDto("Abril", new BigDecimal("225000.00"), new BigDecimal("45000.00"), new BigDecimal("180000.00"), "BOB")
        );

        BigDecimal totalIncome = incomeBySource.stream()
                .map(FinancialReportDto.IncomeBySourceDto::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalExpenses = new BigDecimal("225000.00");
        BigDecimal netProfit = totalIncome.subtract(totalExpenses);
        BigDecimal profitMargin = totalIncome.compareTo(BigDecimal.ZERO) > 0
                ? netProfit.divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;

        return new FinancialReportDto(
                "Últimos 4 meses",
                query.getStartDate(),
                query.getEndDate(),
                totalIncome,
                totalExpenses,
                netProfit,
                profitMargin,
                new BigDecimal("150000.00"),
                new BigDecimal("1125000.00"),
                "BOB",
                incomeBySource,
                monthlyData
        );
    }
}
