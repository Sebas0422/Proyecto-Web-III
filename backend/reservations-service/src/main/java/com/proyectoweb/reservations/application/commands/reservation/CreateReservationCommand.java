package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReservationCommand(
        UUID tenantId,
        UUID lotId,
        UUID projectId,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerDocument,
        BigDecimal reservationAmount,
        int expirationDays,
        UUID createdBy,
        String notes
) implements Command<ReservationDto> {
}
