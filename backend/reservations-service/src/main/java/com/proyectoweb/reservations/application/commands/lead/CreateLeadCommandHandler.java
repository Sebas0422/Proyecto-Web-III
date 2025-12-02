package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadCreatedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CreateLeadCommandHandler implements Command.Handler<CreateLeadCommand, LeadDto> {
    
    private final LeadRepository leadRepository;
    private final KafkaProducerService kafkaProducerService;

    public CreateLeadCommandHandler(LeadRepository leadRepository,
                                    KafkaProducerService kafkaProducerService) {
        this.leadRepository = leadRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public LeadDto handle(CreateLeadCommand command) {
        CustomerInfo customerInfo = new CustomerInfo(
                command.customerName(),
                command.customerEmail(),
                command.customerPhone(),
                command.customerDocument()
        );

        Lead lead = Lead.create(
                UUID.randomUUID(),
                command.tenantId(),
                customerInfo,
                command.source(),
                command.interestLevel(),
                command.notes(),
                command.projectId(),
                command.lotId()
        );

        Lead saved = leadRepository.save(lead);

        // Publicar evento a Kafka
        LeadCreatedEvent kafkaEvent = new LeadCreatedEvent(
            Math.abs(saved.getId().hashCode() * 1L),
            Math.abs(saved.getTenantId().hashCode() * 1L),
            saved.getCustomerInfo().fullName().split(" ")[0], // firstName
            saved.getCustomerInfo().fullName().split(" ").length > 1 ? 
                saved.getCustomerInfo().fullName().split(" ")[1] : "", // lastName
            saved.getCustomerInfo().email(),
            saved.getCustomerInfo().phone(),
            saved.getStatus().name(),
            saved.getSource(),
            saved.getCreatedAt()
        );
        kafkaProducerService.publishLeadCreated(kafkaEvent);

        return new LeadDto(
                saved.getId(),
                saved.getTenantId(),
                saved.getProjectId(),
                saved.getLotId(),
                saved.getCustomerInfo().fullName(),
                saved.getCustomerInfo().email(),
                saved.getCustomerInfo().phone(),
                saved.getCustomerInfo().documentNumber(),
                saved.getStatus().name(),
                saved.getSource(),
                saved.getInterestLevel(),
                saved.getNotes(),
                saved.getAssignedTo(),
                saved.getContactedAt(),
                saved.getConvertedAt(),
                saved.getCreatedAt()
        );
    }
}

