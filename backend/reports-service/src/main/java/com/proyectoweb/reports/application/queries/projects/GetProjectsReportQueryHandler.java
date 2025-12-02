package com.proyectoweb.reports.application.queries.projects;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reports.application.dto.ProjectsReportDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class GetProjectsReportQueryHandler implements Command.Handler<GetProjectsReportQuery, ProjectsReportDto> {

    @Override
    public ProjectsReportDto handle(GetProjectsReportQuery query) {
        // TODO: Implementar lógica real consultando base de datos
        
        List<ProjectsReportDto.ProjectDetailsDto> projectDetails = List.of(
                new ProjectsReportDto.ProjectDetailsDto(
                        "proj-1",
                        "Proyecto Los Jardines",
                        120,
                        45,
                        60,
                        15,
                        50.0,
                        new BigDecimal("900000.00"),
                        "BOB",
                        "ACTIVO"
                ),
                new ProjectsReportDto.ProjectDetailsDto(
                        "proj-2",
                        "Proyecto El Roble",
                        80,
                        32,
                        40,
                        8,
                        50.0,
                        new BigDecimal("600000.00"),
                        "BOB",
                        "ACTIVO"
                ),
                new ProjectsReportDto.ProjectDetailsDto(
                        "proj-3",
                        "Proyecto Vista Hermosa",
                        56,
                        28,
                        25,
                        3,
                        44.64,
                        new BigDecimal("375000.00"),
                        "BOB",
                        "ACTIVO"
                ),
                new ProjectsReportDto.ProjectDetailsDto(
                        "proj-4",
                        "Proyecto Las Palmas",
                        40,
                        0,
                        40,
                        0,
                        100.0,
                        new BigDecimal("520000.00"),
                        "BOB",
                        "COMPLETADO"
                )
        );

        return new ProjectsReportDto(
                "Todos los proyectos",
                query.getStartDate(),
                query.getEndDate(),
                projectDetails.size(),
                (int) projectDetails.stream().filter(p -> "ACTIVO".equals(p.status())).count(),
                (int) projectDetails.stream().filter(p -> "COMPLETADO".equals(p.status())).count(),
                projectDetails
        );
    }
}
