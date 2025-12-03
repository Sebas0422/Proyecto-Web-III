package com.proyectoweb.reservations.application.queries.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetReservationByIdQueryHandler implements Command.Handler<GetReservationByIdQuery, ReservationDto> {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDto handle(GetReservationByIdQuery query) {
        Reservation reservation = reservationRepository.findById(query.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getTenantId().equals(query.getTenantId())) {
            throw new RuntimeException("No permission to access this reservation");
        }

        return new ReservationDto(
                reservation.getId(),
                reservation.getTenantId(),
                reservation.getLotId(),
                reservation.getProjectId(),
                reservation.getCustomerInfo().fullName(),
                reservation.getCustomerInfo().email(),
                reservation.getCustomerInfo().phone(),
                reservation.getCustomerInfo().documentNumber(),
                reservation.getReservationAmount().amount(),
                reservation.getStatus().name(),
                reservation.getReservationDate(),
                reservation.getExpirationDate(),
                reservation.getConfirmedAt(),
                reservation.getCancelledAt(),
                reservation.getNotes(),
                reservation.getCreatedBy(),
                reservation.getCreatedAt()
        );
    }
}
