package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;

import java.util.UUID;

public record CancelReservationCommand(
        UUID reservationId,
        UUID tenantId
) implements Command<ReservationDto> {
}
