package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.domain.value_objects.ReservationAmount;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateReservationCommandHandler implements Command.Handler<CreateReservationCommand, ReservationDto> {
    
    private final ReservationRepository reservationRepository;

    public CreateReservationCommandHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationDto handle(CreateReservationCommand command) {
        CustomerInfo customerInfo = new CustomerInfo(
                command.customerName(),
                command.customerEmail(),
                command.customerPhone(),
                command.customerDocument()
        );

        Reservation reservation = Reservation.create(
                UUID.randomUUID(),
                command.tenantId(),
                command.lotId(),
                command.projectId(),
                customerInfo,
                new ReservationAmount(command.reservationAmount()),
                command.expirationDays(),
                command.createdBy(),
                command.notes()
        );

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
