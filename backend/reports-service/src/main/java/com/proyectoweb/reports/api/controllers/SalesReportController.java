package com.proyectoweb.reports.api.controllers;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.reports.api.dto.responses.ApiResponse;
import com.proyectoweb.reports.application.dto.SalesReportDto;
import com.proyectoweb.reports.application.queries.sales.GetSalesReportQuery;
import com.proyectoweb.reports.application.services.ExcelExportService;
import com.proyectoweb.reports.application.services.PdfExportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports/sales")
public class SalesReportController {

    private final Pipeline pipeline;
    private final PdfExportService pdfExportService;
    private final ExcelExportService excelExportService;

    public SalesReportController(Pipeline pipeline, PdfExportService pdfExportService, ExcelExportService excelExportService) {
        this.pipeline = pipeline;
        this.pdfExportService = pdfExportService;
        this.excelExportService = excelExportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SalesReportDto>> getSalesReport(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetSalesReportQuery query = new GetSalesReportQuery(tenantId, startDate, endDate);
            SalesReportDto report = pipeline.send(query);
            
            return ResponseEntity.ok(ApiResponse.success(report));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al generar reporte de ventas: " + e.getMessage(), "SALES_REPORT_ERROR"));
        }
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportSalesReportToPdf(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetSalesReportQuery query = new GetSalesReportQuery(tenantId, startDate, endDate);
            SalesReportDto report = pipeline.send(query);
            
            byte[] pdfBytes = pdfExportService.exportSalesReportToPdf(report);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "sales-report.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportSalesReportToExcel(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            String tenantId = (String) request.getAttribute("tenantId");
            
            GetSalesReportQuery query = new GetSalesReportQuery(tenantId, startDate, endDate);
            SalesReportDto report = pipeline.send(query);
            
            byte[] excelBytes = excelExportService.exportSalesReportToExcel(report);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "sales-report.xlsx");
            
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
