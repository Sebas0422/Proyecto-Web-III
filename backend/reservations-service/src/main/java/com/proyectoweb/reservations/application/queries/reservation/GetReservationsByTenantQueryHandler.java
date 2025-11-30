package com.proyectoweb.reservations.application.queries.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import com.proyectoweb.reservations.domain.value_objects.ReservationStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetReservationsByTenantQueryHandler implements Command.Handler<GetReservationsByTenantQuery, List<ReservationDto>> {
    
    private final ReservationRepository reservationRepository;

    public GetReservationsByTenantQueryHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<ReservationDto> handle(GetReservationsByTenantQuery query) {
        List<Reservation> reservations;

        if (query.status() != null) {
            ReservationStatus status = ReservationStatus.valueOf(query.status());
            reservations = reservationRepository.findByTenantIdAndStatus(query.tenantId(), status);
        } else {
            reservations = reservationRepository.findByTenantId(query.tenantId());
        }

        return reservations.stream()
                .map(r -> new ReservationDto(
                        r.getId(),
                        r.getTenantId(),
                        r.getLotId(),
                        r.getProjectId(),
                        r.getCustomerInfo().fullName(),
                        r.getCustomerInfo().email(),
                        r.getCustomerInfo().phone(),
                        r.getCustomerInfo().documentNumber(),
                        r.getReservationAmount().amount(),
                        r.getStatus().name(),
                        r.getReservationDate(),
                        r.getExpirationDate(),
                        r.getConfirmedAt(),
                        r.getCancelledAt(),
                        r.getNotes(),
                        r.getCreatedBy(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
