package com.proyectoweb.reports.application.queries.sales;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.SalesReportDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class GetSalesReportQueryHandler implements Command.Handler<GetSalesReportQuery, SalesReportDto> {

    @Override
    public SalesReportDto handle(GetSalesReportQuery query) {
        
        List<SalesReportDto.SaleByProjectDto> salesByProject = List.of(
                new SalesReportDto.SaleByProjectDto(
                        "proj-1",
                        "Proyecto Los Jardines",
                        35,
                        new BigDecimal("525000.00"),
                        "BOB"
                ),
                new SalesReportDto.SaleByProjectDto(
                        "proj-2",
                        "Proyecto El Roble",
                        28,
                        new BigDecimal("420000.00"),
                        "BOB"
                ),
                new SalesReportDto.SaleByProjectDto(
                        "proj-3",
                        "Proyecto Vista Hermosa",
                        22,
                        new BigDecimal("305000.00"),
                        "BOB"
                )
        );

        List<SalesReportDto.SaleByMonthDto> salesByMonth = List.of(
                new SalesReportDto.SaleByMonthDto("Enero", 18, new BigDecimal("270000.00"), "BOB"),
                new SalesReportDto.SaleByMonthDto("Febrero", 22, new BigDecimal("330000.00"), "BOB"),
                new SalesReportDto.SaleByMonthDto("Marzo", 25, new BigDecimal("375000.00"), "BOB"),
                new SalesReportDto.SaleByMonthDto("Abril", 20, new BigDecimal("275000.00"), "BOB")
        );

        int totalSales = salesByProject.stream().mapToInt(SalesReportDto.SaleByProjectDto::lotsSold).sum();
        BigDecimal totalRevenue = salesByProject.stream()
                .map(SalesReportDto.SaleByProjectDto::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageSalePrice = totalSales > 0 
                ? totalRevenue.divide(new BigDecimal(totalSales), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        return new SalesReportDto(
                "Últimos 4 meses",
                query.getStartDate(),
                query.getEndDate(),
                totalSales,
                totalRevenue,
                averageSalePrice,
                "BOB",
                salesByProject,
                salesByMonth
        );
    }
}
