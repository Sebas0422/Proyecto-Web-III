package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;

import java.util.UUID;

public record ConfirmReservationCommand(
        UUID reservationId,
        UUID tenantId
) implements Command<ReservationDto> {
}
