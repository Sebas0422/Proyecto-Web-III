package com.proyectoweb.payments.application.queries.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.domain.value_objects.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetPaymentsByTenantQueryHandler implements Command.Handler<GetPaymentsByTenantQuery, List<PaymentDto>> {

    private final PaymentRepository paymentRepository;

    public GetPaymentsByTenantQueryHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentDto> handle(GetPaymentsByTenantQuery query) {
        List<Payment> payments;

        if (query.status() != null && !query.status().isBlank()) {
            PaymentStatus status = PaymentStatus.valueOf(query.status().toUpperCase());
            payments = paymentRepository.findByTenantIdAndStatus(query.tenantId(), status);
        } else {
            payments = paymentRepository.findByTenantId(query.tenantId());
        }

        return payments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PaymentDto mapToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getTenantId(),
                payment.getReservationId(),
                payment.getCustomerInfo().fullName(),
                payment.getCustomerInfo().email(),
                payment.getCustomerInfo().phone(),
                payment.getCustomerInfo().documentNumber(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getPaymentMethod().name(),
                payment.getStatus().name(),
                payment.getTransactionReference(),
                payment.getQrCodeData(),
                payment.getPaymentDate(),
                payment.getExpirationDate(),
                payment.getConfirmedAt(),
                payment.getNotes(),
                payment.getCreatedBy(),
                payment.getCreatedAt()
        );
    }
}
