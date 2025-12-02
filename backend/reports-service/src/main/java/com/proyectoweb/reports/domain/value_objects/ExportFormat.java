package com.proyectoweb.reports.domain.value_objects;

public class ExportFormat {
    private final String value;

    private ExportFormat(String value) {
        this.value = value;
    }

    public static ExportFormat PDF = new ExportFormat("PDF");
    public static ExportFormat EXCEL = new ExportFormat("EXCEL");
    public static ExportFormat JSON = new ExportFormat("JSON");

    public static ExportFormat fromString(String value) {
        return switch (value.toUpperCase()) {
            case "PDF" -> PDF;
            case "EXCEL" -> EXCEL;
            case "JSON" -> JSON;
            default -> throw new IllegalArgumentException("Invalid export format: " + value);
        };
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExportFormat that = (ExportFormat) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
