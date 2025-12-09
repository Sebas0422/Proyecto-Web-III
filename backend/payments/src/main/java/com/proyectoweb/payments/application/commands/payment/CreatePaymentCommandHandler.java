package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.application.services.QRCodeService;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.domain.value_objects.CustomerInfo;
import com.proyectoweb.payments.domain.value_objects.Money;
import org.springframework.stereotype.Component;

@Component
public class CreatePaymentCommandHandler implements Command.Handler<CreatePaymentCommand, PaymentDto> {

    private final PaymentRepository paymentRepository;
    private final QRCodeService qrCodeService;

    public CreatePaymentCommandHandler(PaymentRepository paymentRepository, QRCodeService qrCodeService) {
        this.paymentRepository = paymentRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public PaymentDto handle(CreatePaymentCommand command) {
        CustomerInfo customerInfo = new CustomerInfo(
                command.customerName(),
                command.customerEmail(),
                command.customerPhone(),
                command.customerDocument()
        );

        Money amount = new Money(command.amount(), command.currency());

        Payment payment = Payment.create(
                command.tenantId(),
                command.reservationId(),
                customerInfo,
                amount,
                command.paymentMethod(),
                command.expirationHours(),
                command.createdBy(),
                command.notes()
        );

        String qrData = generateQRData(payment);
        payment.assignQRCode(qrData);

        Payment saved = paymentRepository.save(payment);

        return mapToDto(saved);
    }

    private String generateQRData(Payment payment) {
        return String.format("PAYMENT|%s|%.2f|%s|%s",
                payment.getId(),
                payment.getAmount().amount(),
                payment.getAmount().currency(),
                payment.getCustomerInfo().fullName()
        );
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
