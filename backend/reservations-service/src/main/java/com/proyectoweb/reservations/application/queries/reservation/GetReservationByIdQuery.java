package com.proyectoweb.reservations.application.queries.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;

import java.util.UUID;

public class GetReservationByIdQuery implements Command<ReservationDto> {
    private final UUID reservationId;
    private final UUID tenantId;

    public GetReservationByIdQuery(UUID reservationId, UUID tenantId) {
        this.reservationId = reservationId;
        this.tenantId = tenantId;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
