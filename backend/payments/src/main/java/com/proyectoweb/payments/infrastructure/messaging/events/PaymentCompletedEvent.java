package com.proyectoweb.payments.infrastructure.messaging.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentCompletedEvent {
    private String paymentId;
    private String reservationId;
    private String lotId;
    private String tenantId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDate paymentDate;
    private String status;
    private LocalDateTime completedAt;

    public PaymentCompletedEvent() {
    }

    public PaymentCompletedEvent(String paymentId, String reservationId, String lotId, String tenantId, 
                                 BigDecimal amount, String paymentMethod, LocalDate paymentDate, 
                                 String status, LocalDateTime completedAt) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.lotId = lotId;
        this.tenantId = tenantId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
        this.completedAt = completedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
