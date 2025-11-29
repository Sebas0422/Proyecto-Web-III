package com.proyectoweb.payments.infrastructure.persistence.jpa.models;

import com.proyectoweb.payments.domain.value_objects.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "receipts")
@Getter
@Setter
public class ReceiptJpaModel {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID tenantId;

    @Column(name = "payment_id", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID paymentId;

    @Column(name = "receipt_number", nullable = false, unique = true, length = 50)
    private String receiptNumber;

    // Customer Info flattened
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_document")
    private String customerDocument;

    // Money flattened
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_reference", nullable = false)
    private String transactionReference;

    @Column(name = "pdf_path")
    private String pdfPath;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "issued_by", nullable = false, columnDefinition = "BINARY(16)")
    private UUID issuedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
