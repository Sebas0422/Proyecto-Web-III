package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import com.proyectoweb.reservations.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateLeadStatusCommandHandler implements Command.Handler<UpdateLeadStatusCommand, LeadDto> {

    private final LeadRepository leadRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public LeadDto handle(UpdateLeadStatusCommand command) {
        Lead lead = leadRepository.findById(command.getLeadId())
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        if (!lead.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No permission to update this lead");
        }

        LeadStatus newStatus = LeadStatus.valueOf(command.getStatus().toUpperCase());
        lead.updateStatus(newStatus);
        
        Lead updatedLead = leadRepository.save(lead);

        LeadStatusChangedEvent event = new LeadStatusChangedEvent(
                updatedLead.getId().toString(),
                updatedLead.getTenantId().toString(),
                updatedLead.getStatus().name(),
                updatedLead.getProjectId() != null ? updatedLead.getProjectId().toString() : null,
                LocalDateTime.now()
        );
        kafkaProducerService.publishLeadStatusChanged(event);

        return new LeadDto(
                updatedLead.getId(),
                updatedLead.getTenantId(),
                updatedLead.getProjectId(),
                updatedLead.getLotId(),
                updatedLead.getCustomerInfo().fullName(),
                updatedLead.getCustomerInfo().email(),
                updatedLead.getCustomerInfo().phone(),
                updatedLead.getCustomerInfo().documentNumber(),
                updatedLead.getStatus().name(),
                updatedLead.getSource(),
                updatedLead.getInterestLevel(),
                updatedLead.getNotes(),
                updatedLead.getAssignedTo(),
                updatedLead.getContactedAt(),
                updatedLead.getConvertedAt(),
                updatedLead.getCreatedAt()
        );
    }
}
