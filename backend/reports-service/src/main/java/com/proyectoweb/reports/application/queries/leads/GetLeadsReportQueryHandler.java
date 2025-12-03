package com.proyectoweb.reports.application.queries.leads;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.LeadsReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetLeadsReportQueryHandler implements Command.Handler<GetLeadsReportQuery, LeadsReportDto> {

    @Override
    public LeadsReportDto handle(GetLeadsReportQuery query) {
        
        List<LeadsReportDto.LeadBySourceDto> leadsBySource = List.of(
                new LeadsReportDto.LeadBySourceDto("Facebook", 35, 15, 42.86),
                new LeadsReportDto.LeadBySourceDto("Instagram", 28, 10, 35.71),
                new LeadsReportDto.LeadBySourceDto("Referidos", 18, 12, 66.67),
                new LeadsReportDto.LeadBySourceDto("Web Directa", 22, 8, 36.36)
        );

        List<LeadsReportDto.LeadByStatusDto> leadsByStatus = List.of(
                new LeadsReportDto.LeadByStatusDto("NUEVO", 25, 28.09),
                new LeadsReportDto.LeadByStatusDto("CONTACTADO", 30, 33.71),
                new LeadsReportDto.LeadByStatusDto("CALIFICADO", 12, 13.48),
                new LeadsReportDto.LeadByStatusDto("CONVERTIDO", 32, 35.96),
                new LeadsReportDto.LeadByStatusDto("PERDIDO", 15, 16.85)
        );

        int totalLeads = leadsBySource.stream().mapToInt(LeadsReportDto.LeadBySourceDto::count).sum();
        int convertedLeads = leadsBySource.stream().mapToInt(LeadsReportDto.LeadBySourceDto::converted).sum();
        double conversionRate = totalLeads > 0 ? ((double) convertedLeads / totalLeads) * 100 : 0.0;

        return new LeadsReportDto(
                "Últimos 4 meses",
                query.getStartDate(),
                query.getEndDate(),
                totalLeads,
                45,
                convertedLeads,
                15,
                conversionRate,
                12.5,
                leadsBySource,
                leadsByStatus
        );
    }
}
