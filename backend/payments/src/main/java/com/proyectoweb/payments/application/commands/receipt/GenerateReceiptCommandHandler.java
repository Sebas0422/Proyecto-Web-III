package com.proyectoweb.payments.application.commands.receipt;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.ReceiptDto;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.aggregates.Receipt;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.domain.repositories.ReceiptRepository;
import com.proyectoweb.payments.domain.value_objects.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class GenerateReceiptCommandHandler implements Command.Handler<GenerateReceiptCommand, ReceiptDto> {

    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;

    public GenerateReceiptCommandHandler(PaymentRepository paymentRepository, ReceiptRepository receiptRepository) {
        this.paymentRepository = paymentRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
    public ReceiptDto handle(GenerateReceiptCommand command) {
        // Verify payment exists and is completed
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + command.paymentId()));

        if (!payment.getTenantId().equals(command.tenantId())) {
            throw new IllegalArgumentException("Payment does not belong to tenant");
        }

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot generate receipt for non-completed payment");
        }

        // Check if receipt already exists
        if (receiptRepository.findByPaymentId(command.paymentId()).isPresent()) {
            throw new IllegalStateException("Receipt already exists for payment: " + command.paymentId());
        }

        // Generate receipt number
        String receiptNumber = generateReceiptNumber(command.tenantId());

        // Create receipt aggregate
        Receipt receipt = Receipt.generate(
                command.tenantId(),
                command.paymentId(),
                receiptNumber,
                payment.getCustomerInfo(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionReference(),
                command.issuedBy(),
                "Receipt for payment: " + payment.getId()
        );

        // Save receipt
        Receipt saved = receiptRepository.save(receipt);

        // Return DTO
        return mapToDto(saved);
    }

    private String generateReceiptNumber(UUID tenantId) {
        // Format: RCP-{TENANT_PREFIX}-{YYYYMMDD}-{SEQUENCE}
        String tenantPrefix = tenantId.toString().substring(0, 8).toUpperCase();
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequence = String.format("%04d", (int) (Math.random() * 10000));
        
        return String.format("RCP-%s-%s-%s", tenantPrefix, datePart, sequence);
    }

    private ReceiptDto mapToDto(Receipt receipt) {
        return new ReceiptDto(
                receipt.getId(),
                receipt.getTenantId(),
                receipt.getPaymentId(),
                receipt.getReceiptNumber(),
                receipt.getCustomerInfo().fullName(),
                receipt.getCustomerInfo().email(),
                receipt.getCustomerInfo().phone(),
                receipt.getCustomerInfo().documentNumber(),
                receipt.getAmount().amount(),
                receipt.getAmount().currency(),
                receipt.getPaymentMethod().name(),
                receipt.getTransactionReference(),
                receipt.getPdfPath(),
                receipt.getIssuedAt(),
                receipt.getNotes(),
                receipt.getIssuedBy(),
                receipt.getCreatedAt()
        );
    }
}
