package com.proyectoweb.reports.application.services;

import com.proyectoweb.reports.application.dto.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] exportDashboardToExcel(DashboardMetricsDto metrics) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dashboard Metrics");
        
        // Estilos
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        // Título
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Dashboard - Métricas Generales");
        titleCell.setCellStyle(headerStyle);
        
        rowNum++; // Espacio
        
        // Datos
        createMetricRow(sheet, rowNum++, "Total Proyectos", metrics.totalProjects(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Proyectos Activos", metrics.activeProjects(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Total Lotes", metrics.totalLots(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Lotes Disponibles", metrics.availableLots(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Lotes Vendidos", metrics.soldLots(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Lotes Reservados", metrics.reservedLots(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Total Leads", metrics.totalLeads(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Leads Activos", metrics.activeLeads(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Leads Convertidos", metrics.convertedLeads(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Tasa de Conversión (%)", metrics.conversionRate(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Ingresos Totales", metrics.totalRevenue() + " " + metrics.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Pagos Pendientes", metrics.pendingPayments() + " " + metrics.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Pagos Confirmados", metrics.confirmedPayments() + " " + metrics.currency(), headerStyle, dataStyle);
        
        // Auto-size columnas
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        return baos.toByteArray();
    }

    public byte[] exportSalesReportToExcel(SalesReportDto report) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        // Hoja de resumen
        Sheet summarySheet = workbook.createSheet("Resumen");
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        Row titleRow = summarySheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue("Reporte de Ventas");
        
        rowNum++;
        createMetricRow(summarySheet, rowNum++, "Periodo", report.period(), headerStyle, dataStyle);
        createMetricRow(summarySheet, rowNum++, "Total Ventas", report.totalSales(), headerStyle, dataStyle);
        createMetricRow(summarySheet, rowNum++, "Ingresos Totales", report.totalRevenue() + " " + report.currency(), headerStyle, dataStyle);
        createMetricRow(summarySheet, rowNum++, "Precio Promedio", report.averageSalePrice() + " " + report.currency(), headerStyle, dataStyle);
        
        // Hoja de ventas por proyecto
        Sheet projectsSheet = workbook.createSheet("Ventas por Proyecto");
        rowNum = 0;
        
        Row headerRow = projectsSheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Proyecto");
        headerRow.createCell(1).setCellValue("Lotes Vendidos");
        headerRow.createCell(2).setCellValue("Ingresos");
        
        for (SalesReportDto.SaleByProjectDto sale : report.salesByProject()) {
            Row dataRow = projectsSheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(sale.projectName());
            dataRow.createCell(1).setCellValue(sale.lotsSold());
            dataRow.createCell(2).setCellValue(sale.revenue() + " " + sale.currency());
        }
        
        projectsSheet.autoSizeColumn(0);
        projectsSheet.autoSizeColumn(1);
        projectsSheet.autoSizeColumn(2);
        
        // Hoja de ventas por mes
        Sheet monthlySheet = workbook.createSheet("Ventas por Mes");
        rowNum = 0;
        
        headerRow = monthlySheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mes");
        headerRow.createCell(1).setCellValue("Lotes Vendidos");
        headerRow.createCell(2).setCellValue("Ingresos");
        
        for (SalesReportDto.SaleByMonthDto sale : report.salesByMonth()) {
            Row dataRow = monthlySheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(sale.month());
            dataRow.createCell(1).setCellValue(sale.lotsSold());
            dataRow.createCell(2).setCellValue(sale.revenue() + " " + sale.currency());
        }
        
        monthlySheet.autoSizeColumn(0);
        monthlySheet.autoSizeColumn(1);
        monthlySheet.autoSizeColumn(2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        return baos.toByteArray();
    }

    public byte[] exportFinancialReportToExcel(FinancialReportDto report) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte Financiero");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue("Reporte Financiero");
        
        rowNum++;
        createMetricRow(sheet, rowNum++, "Ingresos Totales", report.totalIncome() + " " + report.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Gastos Totales", report.totalExpenses() + " " + report.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Utilidad Neta", report.netProfit() + " " + report.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Margen de Utilidad (%)", report.profitMargin(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Pagos Pendientes", report.pendingPayments() + " " + report.currency(), headerStyle, dataStyle);
        createMetricRow(sheet, rowNum++, "Pagos Confirmados", report.confirmedPayments() + " " + report.currency(), headerStyle, dataStyle);
        
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        return baos.toByteArray();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        return style;
    }

    private void createMetricRow(Sheet sheet, int rowNum, String label, Object value, CellStyle headerStyle, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(headerStyle);
        
        Cell valueCell = row.createCell(1);
        if (value instanceof Number) {
            valueCell.setCellValue(((Number) value).doubleValue());
        } else {
            valueCell.setCellValue(String.valueOf(value));
        }
        valueCell.setCellStyle(dataStyle);
    }
}
