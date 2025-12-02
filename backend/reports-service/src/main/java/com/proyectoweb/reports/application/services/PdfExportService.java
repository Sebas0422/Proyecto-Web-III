package com.proyectoweb.reports.application.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyectoweb.reports.application.dto.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfExportService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] exportDashboardToPdf(DashboardMetricsDto metrics) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Título
        Paragraph title = new Paragraph("Dashboard - Métricas Generales", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Tabla de métricas
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        
        addMetricRow(table, "Total Proyectos", String.valueOf(metrics.totalProjects()));
        addMetricRow(table, "Proyectos Activos", String.valueOf(metrics.activeProjects()));
        addMetricRow(table, "Total Lotes", String.valueOf(metrics.totalLots()));
        addMetricRow(table, "Lotes Disponibles", String.valueOf(metrics.availableLots()));
        addMetricRow(table, "Lotes Vendidos", String.valueOf(metrics.soldLots()));
        addMetricRow(table, "Lotes Reservados", String.valueOf(metrics.reservedLots()));
        addMetricRow(table, "Total Leads", String.valueOf(metrics.totalLeads()));
        addMetricRow(table, "Tasa de Conversión", String.format("%.2f%%", metrics.conversionRate()));
        addMetricRow(table, "Ingresos Totales", String.format("%s %s", metrics.totalRevenue(), metrics.currency()));
        addMetricRow(table, "Pagos Confirmados", String.format("%s %s", metrics.confirmedPayments(), metrics.currency()));
        
        document.add(table);
        document.close();
        
        return baos.toByteArray();
    }

    public byte[] exportSalesReportToPdf(SalesReportDto report) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Título
        Paragraph title = new Paragraph("Reporte de Ventas", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
        
        // Periodo
        Paragraph period = new Paragraph(
                String.format("Periodo: %s - %s",
                        report.startDate().format(DATE_FORMATTER),
                        report.endDate().format(DATE_FORMATTER)),
                NORMAL_FONT
        );
        period.setAlignment(Element.ALIGN_CENTER);
        period.setSpacingAfter(20);
        document.add(period);
        
        // Resumen
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        addMetricRow(summaryTable, "Total Ventas", String.valueOf(report.totalSales()));
        addMetricRow(summaryTable, "Ingresos Totales", String.format("%s %s", report.totalRevenue(), report.currency()));
        addMetricRow(summaryTable, "Precio Promedio", String.format("%s %s", report.averageSalePrice(), report.currency()));
        document.add(summaryTable);
        
        // Ventas por proyecto
        Paragraph projectsTitle = new Paragraph("Ventas por Proyecto", HEADER_FONT);
        projectsTitle.setSpacingBefore(20);
        projectsTitle.setSpacingAfter(10);
        document.add(projectsTitle);
        
        PdfPTable projectsTable = new PdfPTable(3);
        projectsTable.setWidthPercentage(100);
        
        PdfPCell cell1 = new PdfPCell(new Phrase("Proyecto", HEADER_FONT));
        PdfPCell cell2 = new PdfPCell(new Phrase("Lotes Vendidos", HEADER_FONT));
        PdfPCell cell3 = new PdfPCell(new Phrase("Ingresos", HEADER_FONT));
        projectsTable.addCell(cell1);
        projectsTable.addCell(cell2);
        projectsTable.addCell(cell3);
        
        for (SalesReportDto.SaleByProjectDto sale : report.salesByProject()) {
            projectsTable.addCell(new Phrase(sale.projectName(), NORMAL_FONT));
            projectsTable.addCell(new Phrase(String.valueOf(sale.lotsSold()), NORMAL_FONT));
            projectsTable.addCell(new Phrase(String.format("%s %s", sale.revenue(), sale.currency()), NORMAL_FONT));
        }
        
        document.add(projectsTable);
        document.close();
        
        return baos.toByteArray();
    }

    public byte[] exportFinancialReportToPdf(FinancialReportDto report) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        Paragraph title = new Paragraph("Reporte Financiero", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        addMetricRow(table, "Ingresos Totales", String.format("%s %s", report.totalIncome(), report.currency()));
        addMetricRow(table, "Gastos Totales", String.format("%s %s", report.totalExpenses(), report.currency()));
        addMetricRow(table, "Utilidad Neta", String.format("%s %s", report.netProfit(), report.currency()));
        addMetricRow(table, "Margen de Utilidad", String.format("%.2f%%", report.profitMargin()));
        addMetricRow(table, "Pagos Pendientes", String.format("%s %s", report.pendingPayments(), report.currency()));
        addMetricRow(table, "Pagos Confirmados", String.format("%s %s", report.confirmedPayments(), report.currency()));
        
        document.add(table);
        document.close();
        
        return baos.toByteArray();
    }

    private void addMetricRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
