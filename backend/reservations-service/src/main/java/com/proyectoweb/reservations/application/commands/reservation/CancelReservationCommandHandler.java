package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import org.springframework.stereotype.Component;

@Component
public class CancelReservationCommandHandler implements Command.Handler<CancelReservationCommand, ReservationDto> {
    
    private final ReservationRepository reservationRepository;

    public CancelReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
