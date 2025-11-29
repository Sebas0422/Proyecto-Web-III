package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
public class ConfirmPaymentCommandHandler implements Command.Handler<ConfirmPaymentCommand, PaymentDto> {

    private final PaymentRepository paymentRepository;

    public ConfirmPaymentCommandHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDto handle(ConfirmPaymentCommand command) {
        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + command.paymentId()));

        if (!payment.getTenantId().equals(command.tenantId())) {
            throw new IllegalArgumentException("Payment does not belong to tenant");
        }

        payment.confirm(command.transactionReference());

        Payment saved = paymentRepository.save(payment);

        return new PaymentDto(
                saved.getId(),
                saved.getTenantId(),
                saved.getReservationId(),
                saved.getCustomerInfo().fullName(),
                saved.getCustomerInfo().email(),
                saved.getCustomerInfo().phone(),
                saved.getCustomerInfo().documentNumber(),
                saved.getAmount().amount(),
                saved.getAmount().currency(),
                saved.getPaymentMethod().name(),
                saved.getStatus().name(),
                saved.getTransactionReference(),
                saved.getQrCodeData(),
                saved.getPaymentDate(),
                saved.getExpirationDate(),
                saved.getConfirmedAt(),
                saved.getNotes(),
                saved.getCreatedBy(),
                saved.getCreatedAt()
        );
    }
}
