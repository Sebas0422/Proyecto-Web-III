package com.proyectoweb.reservations.infrastructure.messaging;

import com.proyectoweb.reservations.infrastructure.messaging.events.PaymentCompletedEvent;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final ReservationRepository reservationRepository;
    private final KafkaProducerService kafkaProducerService;

    public KafkaConsumerService(ReservationRepository reservationRepository,
                                KafkaProducerService kafkaProducerService) {
        this.reservationRepository = reservationRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "payment-events", groupId = "reservations-service-group")
    public void consumePaymentEvents(PaymentCompletedEvent event) {
        logger.info("Recibido PaymentCompletedEvent para reservationId: {}", event.getReservationId());

        try {
            // Buscar la reservación por ID
            UUID reservationId = new UUID(event.getReservationId(), 0L);
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + reservationId));

            // Confirmar la reservación
            reservation.confirm();
            reservationRepository.save(reservation);

            logger.info("Reservación {} confirmada automáticamente por pago {}", 
                       reservationId, event.getPaymentId());

            // Publicar evento de reservación confirmada
            com.proyectoweb.reservations.infrastructure.messaging.events.ReservationConfirmedEvent confirmedEvent = 
                new com.proyectoweb.reservations.infrastructure.messaging.events.ReservationConfirmedEvent(
                    Math.abs(reservation.getId().hashCode() * 1L),
                    null, // leadId
                    Math.abs(reservation.getLotId().hashCode() * 1L),
                    Math.abs(reservation.getProjectId().hashCode() * 1L),
                    Math.abs(reservation.getTenantId().hashCode() * 1L),
                    event.getPaymentId(),
                    java.time.LocalDateTime.now()
                );
            
            kafkaProducerService.publishReservationConfirmed(confirmedEvent);
            
        } catch (Exception e) {
            logger.error("Error procesando PaymentCompletedEvent: {}", e.getMessage(), e);
        }
    }
}
