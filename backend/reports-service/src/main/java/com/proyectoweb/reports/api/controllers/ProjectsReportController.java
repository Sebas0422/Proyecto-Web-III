package com.proyectoweb.reports.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reports.api.dto.responses.ApiResponse;
import com.proyectoweb.reports.application.dto.ProjectsReportDto;
import com.proyectoweb.reports.application.queries.projects.GetProjectsReportQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports/projects")
public class ProjectsReportController {

    private final Pipeline pipeline;

    public ProjectsReportController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProjectsReportDto>> getProjectsReport(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetProjectsReportQuery query = new GetProjectsReportQuery(tenantId, startDate, endDate);
            ProjectsReportDto report = pipeline.send(query);
            
            return ResponseEntity.ok(ApiResponse.success(report));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al generar reporte de proyectos: " + e.getMessage(), "PROJECTS_REPORT_ERROR"));
        }
    }
}
