package com.proyectoweb.payments.domain.aggregates;

import com.proyectoweb.payments.domain.value_objects.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private UUID id;
    private UUID tenantId;
    private UUID reservationId;
    private CustomerInfo customerInfo;
    private Money amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionReference;
    private String qrCodeData;
    private LocalDateTime paymentDate;
    private LocalDateTime expirationDate;
    private LocalDateTime confirmedAt;
    private String notes;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Payment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Payment create(
            UUID tenantId,
            UUID reservationId,
            CustomerInfo customerInfo,
            Money amount,
            PaymentMethod paymentMethod,
            int expirationHours,
            UUID createdBy,
            String notes) {
        
        Payment payment = new Payment();
        payment.id = UUID.randomUUID();
        payment.tenantId = tenantId;
        payment.reservationId = reservationId;
        payment.customerInfo = customerInfo;
        payment.amount = amount;
        payment.paymentMethod = paymentMethod;
        payment.status = PaymentStatus.PENDING;
        payment.paymentDate = LocalDateTime.now();
        payment.expirationDate = LocalDateTime.now().plusHours(expirationHours);
        payment.notes = notes;
        payment.createdBy = createdBy;
        
        return payment;
    }

    public static Payment reconstitute(
            UUID id,
            UUID tenantId,
            UUID reservationId,
            CustomerInfo customerInfo,
            Money amount,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            String transactionReference,
            String qrCodeData,
            LocalDateTime paymentDate,
            LocalDateTime expirationDate,
            LocalDateTime confirmedAt,
            String notes,
            UUID createdBy,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        
        Payment payment = new Payment();
        payment.id = id;
        payment.tenantId = tenantId;
        payment.reservationId = reservationId;
        payment.customerInfo = customerInfo;
        payment.amount = amount;
        payment.paymentMethod = paymentMethod;
        payment.status = status;
        payment.transactionReference = transactionReference;
        payment.qrCodeData = qrCodeData;
        payment.paymentDate = paymentDate;
        payment.expirationDate = expirationDate;
        payment.confirmedAt = confirmedAt;
        payment.notes = notes;
        payment.createdBy = createdBy;
        payment.createdAt = createdAt;
        payment.updatedAt = updatedAt;
        
        return payment;
    }

    public void assignQRCode(String qrCodeData) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot assign QR code to non-pending payment");
        }
        this.qrCodeData = qrCodeData;
        this.status = PaymentStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm(String transactionReference) {
        if (this.status != PaymentStatus.PROCESSING && this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot confirm payment in status: " + this.status);
        }
        if (LocalDateTime.now().isAfter(this.expirationDate)) {
            throw new IllegalStateException("Payment has expired");
        }
        this.status = PaymentStatus.COMPLETED;
        this.transactionReference = transactionReference;
        this.confirmedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void fail(String reason) {
        this.status = PaymentStatus.FAILED;
        this.notes = (this.notes != null ? this.notes + " | " : "") + "Failed: " + reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed payment");
        }
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Can only refund completed payments");
        }
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationDate) && 
               (this.status == PaymentStatus.PENDING || this.status == PaymentStatus.PROCESSING);
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getReservationId() { return reservationId; }
    public CustomerInfo getCustomerInfo() { return customerInfo; }
    public Money getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public String getTransactionReference() { return transactionReference; }
    public String getQrCodeData() { return qrCodeData; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public String getNotes() { return notes; }
    public UUID getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
