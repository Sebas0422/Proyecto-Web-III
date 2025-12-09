package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import com.proyectoweb.reservations.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationCancelledEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CancelReservationCommandHandler implements Command.Handler<CancelReservationCommand, ReservationDto> {
    
    private final ReservationRepository reservationRepository;
    private final KafkaProducerService kafkaProducerService;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository,
                                          KafkaProducerService kafkaProducerService) {
        this.reservationRepository = reservationRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public ReservationDto handle(CancelReservationCommand command) {
        Reservation reservation = reservationRepository.findById(command.reservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!reservation.getTenantId().equals(command.tenantId())) {
            throw new IllegalArgumentException("Reservation does not belong to this tenant");
        }

        reservation.cancel();
        Reservation saved = reservationRepository.save(reservation);

        // Publicar evento de cancelación
        ReservationCancelledEvent event = new ReservationCancelledEvent(
            saved.getId().toString(),
            saved.getLotId().toString(),
            saved.getProjectId().toString(),
            saved.getTenantId().toString(),
            LocalDateTime.now()
        );
        kafkaProducerService.publishReservationCancelled(event);

        return new ReservationDto(
                saved.getId(),
                saved.getTenantId(),
                saved.getLotId(),
                saved.getProjectId(),
                saved.getCustomerInfo().fullName(),
                saved.getCustomerInfo().email(),
                saved.getCustomerInfo().phone(),
                saved.getCustomerInfo().documentNumber(),
                saved.getReservationAmount().amount(),
                saved.getStatus().name(),
                saved.getReservationDate(),
                saved.getExpirationDate(),
                saved.getConfirmedAt(),
                saved.getCancelledAt(),
                saved.getNotes(),
                saved.getCreatedBy(),
                saved.getCreatedAt()
        );
    }
}
