package com.proyectoweb.reports.domain.value_objects;

public class ReportType {
    private final String value;

    private ReportType(String value) {
        this.value = value;
    }

    public static ReportType DASHBOARD = new ReportType("DASHBOARD");
    public static ReportType SALES = new ReportType("SALES");
    public static ReportType FINANCIAL = new ReportType("FINANCIAL");
    public static ReportType LEADS = new ReportType("LEADS");
    public static ReportType PROJECTS = new ReportType("PROJECTS");
    public static ReportType CUSTOM = new ReportType("CUSTOM");

    public static ReportType fromString(String value) {
        return switch (value.toUpperCase()) {
            case "DASHBOARD" -> DASHBOARD;
            case "SALES" -> SALES;
            case "FINANCIAL" -> FINANCIAL;
            case "LEADS" -> LEADS;
            case "PROJECTS" -> PROJECTS;
            case "CUSTOM" -> CUSTOM;
            default -> throw new IllegalArgumentException("Invalid report type: " + value);
        };
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportType that = (ReportType) o;
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
