package com.proyectoweb.reports.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reports.api.dto.responses.ApiResponse;
import com.proyectoweb.reports.application.dto.LeadsReportDto;
import com.proyectoweb.reports.application.queries.leads.GetLeadsReportQuery;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports/leads")
public class LeadsReportController {

    private final Pipeline pipeline;

    public LeadsReportController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<LeadsReportDto>> getLeadsReport(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetLeadsReportQuery query = new GetLeadsReportQuery(tenantId, startDate, endDate);
            LeadsReportDto report = pipeline.send(query);
            
            return ResponseEntity.ok(ApiResponse.success(report));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al generar reporte de leads: " + e.getMessage(), "LEADS_REPORT_ERROR"));
        }
    }
}
