package com.proyectoweb.payments.application.queries.receipt;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.ReceiptDto;
import com.proyectoweb.payments.domain.aggregates.Receipt;
import com.proyectoweb.payments.domain.repositories.ReceiptRepository;
import org.springframework.stereotype.Component;

@Component
public class GetReceiptByIdQueryHandler implements Command.Handler<GetReceiptByIdQuery, ReceiptDto> {

    private final ReceiptRepository receiptRepository;

    public GetReceiptByIdQueryHandler(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public ReceiptDto handle(GetReceiptByIdQuery query) {
        Receipt receipt = receiptRepository.findById(query.receiptId())
                .orElseThrow(() -> new IllegalArgumentException("Receipt not found: " + query.receiptId()));

        if (!receipt.getTenantId().equals(query.tenantId())) {
            throw new IllegalArgumentException("Receipt does not belong to tenant");
        }

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
