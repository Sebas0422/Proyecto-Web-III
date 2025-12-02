package com.proyectoweb.reports.domain.value_objects;

import java.time.LocalDateTime;

public class DateRange {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange of(LocalDateTime startDate, LocalDateTime endDate) {
        return new DateRange(startDate, endDate);
    }

    public static DateRange lastMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);
        return new DateRange(startDate, endDate);
    }

    public static DateRange currentMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = now;
        return new DateRange(startDate, endDate);
    }

    public static DateRange lastYear() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusYears(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).minusSeconds(1);
        return new DateRange(startDate, endDate);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean contains(LocalDateTime date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return startDate.equals(dateRange.startDate) && endDate.equals(dateRange.endDate);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
