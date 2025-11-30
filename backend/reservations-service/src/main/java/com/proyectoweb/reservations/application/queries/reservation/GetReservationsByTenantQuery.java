package com.proyectoweb.reservations.application.queries.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;

import java.util.List;
import java.util.UUID;

public record GetReservationsByTenantQuery(
        UUID tenantId,
        String status
) implements Command<List<ReservationDto>> {
}
