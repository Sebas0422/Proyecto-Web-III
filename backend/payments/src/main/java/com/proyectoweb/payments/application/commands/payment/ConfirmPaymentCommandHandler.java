package com.proyectoweb.payments.application.commands.payment;

import an.awesome.pipelinr.Command;
import com.proyectoweb.payments.application.dto.PaymentDto;
import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.payments.infrastructure.messaging.events.PaymentCompletedEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.proyectoweb.payments.infrastructure.external.ReservationClient;

import java.time.LocalDateTime;

@Component
public class ConfirmPaymentCommandHandler implements Command.Handler<ConfirmPaymentCommand, PaymentDto> {

    private final PaymentRepository paymentRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ReservationClient reservationClient;


    public ConfirmPaymentCommandHandler(PaymentRepository paymentRepository,
                                        KafkaProducerService kafkaProducerService,
                                        ReservationClient reservationClient) {
        this.paymentRepository = paymentRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.reservationClient = reservationClient;
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

            String lotIdStr = null;

            try {
                String token = extractTokenFromContext();
                var reservation = reservationClient.getReservationById(saved.getReservationId(), token);
                if (reservation != null && reservation.lotId != null) {
                    lotIdStr = reservation.lotId;
                }
                System.out.println("lote id obtenidoooo: "+ lotIdStr);
            } catch (Exception ex) {
                System.err.println("No se pudo obtener lotId desde reservations-service: " + ex.getMessage());
            }

            // Publicar evento de pago completado a Kafka
            PaymentCompletedEvent kafkaEvent = new PaymentCompletedEvent(
                saved.getId().toString(),
                saved.getReservationId().toString(),
                lotIdStr,
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

    private String extractTokenFromContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null && context.getAuthentication().getCredentials() instanceof String) {
            return (String) context.getAuthentication().getCredentials();
        }
        throw new RuntimeException("No se pudo extraer el token JWT del contexto de seguridad");
    }
}


