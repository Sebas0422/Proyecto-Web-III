package com.proyectoweb.reservations.application.commands.reservation;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.ReservationDto;
import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.domain.value_objects.ReservationAmount;
import com.proyectoweb.reservations.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.reservations.infrastructure.messaging.events.ReservationCreatedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreateReservationCommandHandler implements Command.Handler<CreateReservationCommand, ReservationDto> {
    
    private final ReservationRepository reservationRepository;
    private final KafkaProducerService kafkaProducerService;

    public CreateReservationCommandHandler(ReservationRepository reservationRepository,
                                           KafkaProducerService kafkaProducerService) {
        this.reservationRepository = reservationRepository;
        this.kafkaProducerService = kafkaProducerService;
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

        // Publicar evento a Kafka
        ReservationCreatedEvent kafkaEvent = new ReservationCreatedEvent(
            Math.abs(saved.getId().hashCode() * 1L),
            null, // leadId si aplica
            Math.abs(saved.getLotId().hashCode() * 1L),
            Math.abs(saved.getProjectId().hashCode() * 1L),
            Math.abs(saved.getTenantId().hashCode() * 1L),
            saved.getReservationAmount().amount(),
            saved.getReservationDate().toLocalDate(),
            saved.getExpirationDate().toLocalDate(),
            saved.getStatus().name(),
            LocalDateTime.now()
        );
        kafkaProducerService.publishReservationCreated(kafkaEvent);

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

