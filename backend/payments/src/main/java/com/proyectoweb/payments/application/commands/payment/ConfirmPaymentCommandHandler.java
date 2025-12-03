package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.payments.infrastructure.messaging.events.PaymentCompletedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConfirmPaymentCommandHandler implements Command.Handler<ConfirmPaymentCommand, PaymentDto> {

    private final PaymentRepository paymentRepository;
    private final KafkaProducerService kafkaProducerService;

    public ConfirmPaymentCommandHandler(PaymentRepository paymentRepository,
                                        KafkaProducerService kafkaProducerService) {
        this.paymentRepository = paymentRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public PaymentDto handle(ConfirmPaymentCommand command) {
        try {
            Payment payment = paymentRepository.findById(command.paymentId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + command.paymentId()));

            if (!payment.getTenantId().equals(command.tenantId())) {
                throw new IllegalArgumentException("Payment does not belong to tenant");
            }

            payment.confirm(command.transactionReference());

            Payment saved = paymentRepository.save(payment);

            // Publicar evento de pago completado a Kafka
            PaymentCompletedEvent kafkaEvent = new PaymentCompletedEvent(
                saved.getId().toString(),
                saved.getReservationId().toString(),
                null, // lotId - agregar si está disponible
                saved.getTenantId().toString(),
                saved.getAmount().amount(),
                saved.getPaymentMethod().name(),
                saved.getPaymentDate() != null ? saved.getPaymentDate().toLocalDate() : java.time.LocalDate.now(),
                saved.getStatus().name(),
                LocalDateTime.now()
            );
            
            try {
                kafkaProducerService.publishPaymentCompleted(kafkaEvent);
            } catch (Exception e) {
                System.err.println("Error publishing to Kafka: " + e.getMessage());
                e.printStackTrace();
                // No lanzar la excepción, el pago ya está confirmado
            }

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
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error confirming payment: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error confirming payment: " + e.getMessage(), e);
        }
    }
}

