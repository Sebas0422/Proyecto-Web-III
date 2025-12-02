package com.proyectoweb.reports.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reports.api.dto.responses.ApiResponse;
import com.proyectoweb.reports.application.dto.DashboardMetricsDto;
import com.proyectoweb.reports.application.queries.dashboard.GetDashboardMetricsQuery;
import com.proyectoweb.reports.application.services.ExcelExportService;
import com.proyectoweb.reports.application.services.PdfExportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports/dashboard")
public class DashboardController {

    private final Pipeline pipeline;
    private final PdfExportService pdfExportService;
    private final ExcelExportService excelExportService;

    public DashboardController(Pipeline pipeline, PdfExportService pdfExportService, ExcelExportService excelExportService) {
        this.pipeline = pipeline;
        this.pdfExportService = pdfExportService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<DashboardMetricsDto>> getDashboardMetrics(HttpServletRequest request) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetDashboardMetricsQuery query = new GetDashboardMetricsQuery(tenantId);
            DashboardMetricsDto metrics = pipeline.send(query);
            
            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener métricas del dashboard: " + e.getMessage(), "DASHBOARD_ERROR"));
        }
    }

    @GetMapping("/metrics/export/pdf")
    public ResponseEntity<byte[]> exportDashboardToPdf(HttpServletRequest request) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetDashboardMetricsQuery query = new GetDashboardMetricsQuery(tenantId);
            DashboardMetricsDto metrics = pipeline.send(query);
            
            byte[] pdfBytes = pdfExportService.exportDashboardToPdf(metrics);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "dashboard-metrics.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/metrics/export/excel")
    public ResponseEntity<byte[]> exportDashboardToExcel(HttpServletRequest request) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetDashboardMetricsQuery query = new GetDashboardMetricsQuery(tenantId);
            DashboardMetricsDto metrics = pipeline.send(query);
            byte[] excelBytes = excelExportService.exportDashboardToExcel(metrics);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "dashboard-metrics.xlsx");
            
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
