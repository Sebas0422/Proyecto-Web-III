package com.proyectoweb.reports.domain.value_objects;

public class ReportStatus {
    private final String value;

    private ReportStatus(String value) {
        this.value = value;
    }

    public static ReportStatus PENDING = new ReportStatus("PENDING");
    public static ReportStatus PROCESSING = new ReportStatus("PROCESSING");
    public static ReportStatus COMPLETED = new ReportStatus("COMPLETED");
    public static ReportStatus FAILED = new ReportStatus("FAILED");

    public static ReportStatus fromString(String value) {
        return switch (value.toUpperCase()) {
            case "PENDING" -> PENDING;
            case "PROCESSING" -> PROCESSING;
            case "COMPLETED" -> COMPLETED;
            case "FAILED" -> FAILED;
            default -> throw new IllegalArgumentException("Invalid report status: " + value);
        };
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportStatus that = (ReportStatus) o;
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
