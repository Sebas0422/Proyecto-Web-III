package com.proyectoweb.payments.domain.aggregates;

import com.proyectoweb.payments.domain.value_objects.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receipt {
    private UUID id;
    private UUID tenantId;
    private UUID paymentId;
    private String receiptNumber;
    private CustomerInfo customerInfo;
    private Money amount;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private String pdfPath;
    private LocalDateTime issuedAt;
    private String notes;
    private UUID issuedBy;
    private LocalDateTime createdAt;

    // Constructor privado
    private Receipt() {
        this.createdAt = LocalDateTime.now();
    }

    // Factory Method
    public static Receipt generate(
            UUID tenantId,
            UUID paymentId,
            String receiptNumber,
            CustomerInfo customerInfo,
            Money amount,
            PaymentMethod paymentMethod,
            String transactionReference,
            UUID issuedBy,
            String notes) {
        
        Receipt receipt = new Receipt();
        receipt.id = UUID.randomUUID();
        receipt.tenantId = tenantId;
        receipt.paymentId = paymentId;
        receipt.receiptNumber = receiptNumber;
        receipt.customerInfo = customerInfo;
        receipt.amount = amount;
        receipt.paymentMethod = paymentMethod;
        receipt.transactionReference = transactionReference;
        receipt.issuedAt = LocalDateTime.now();
        receipt.notes = notes;
        receipt.issuedBy = issuedBy;
        
        return receipt;
    }

    // Reconstitute from persistence
    public static Receipt reconstitute(
            UUID id,
            UUID tenantId,
            UUID paymentId,
            String receiptNumber,
            CustomerInfo customerInfo,
            Money amount,
            PaymentMethod paymentMethod,
            String transactionReference,
            String pdfPath,
            LocalDateTime issuedAt,
            String notes,
            UUID issuedBy,
            LocalDateTime createdAt) {
        
        Receipt receipt = new Receipt();
        receipt.id = id;
        receipt.tenantId = tenantId;
        receipt.paymentId = paymentId;
        receipt.receiptNumber = receiptNumber;
        receipt.customerInfo = customerInfo;
        receipt.amount = amount;
        receipt.paymentMethod = paymentMethod;
        receipt.transactionReference = transactionReference;
        receipt.pdfPath = pdfPath;
        receipt.issuedAt = issuedAt;
        receipt.notes = notes;
        receipt.issuedBy = issuedBy;
        receipt.createdAt = createdAt;
        
        return receipt;
    }

    // Business methods
    public void attachPdf(String pdfPath) {
        if (pdfPath == null || pdfPath.isBlank()) {
            throw new IllegalArgumentException("PDF path cannot be empty");
        }
        this.pdfPath = pdfPath;
    }

    // Manual getters (domain purity - no Lombok)
    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getPaymentId() { return paymentId; }
    public String getReceiptNumber() { return receiptNumber; }
    public CustomerInfo getCustomerInfo() { return customerInfo; }
    public Money getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getTransactionReference() { return transactionReference; }
    public String getPdfPath() { return pdfPath; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public String getNotes() { return notes; }
    public UUID getIssuedBy() { return issuedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
