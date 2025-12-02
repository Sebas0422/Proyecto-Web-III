package com.proyectoweb.payments.infrastructure.messaging.events;

import java.time.LocalDateTime;

public class ReceiptGeneratedEvent {
    private Long receiptId;
    private Long paymentId;
    private Long tenantId;
    private String receiptNumber;
    private String qrCodePath;
    private LocalDateTime generatedAt;

    public ReceiptGeneratedEvent() {
    }

    public ReceiptGeneratedEvent(Long receiptId, Long paymentId, Long tenantId, String receiptNumber, 
                                 String qrCodePath, LocalDateTime generatedAt) {
        this.receiptId = receiptId;
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.receiptNumber = receiptNumber;
        this.qrCodePath = qrCodePath;
        this.generatedAt = generatedAt;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
