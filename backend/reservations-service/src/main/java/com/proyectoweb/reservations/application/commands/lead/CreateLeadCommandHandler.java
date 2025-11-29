package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateLeadCommandHandler implements Command.Handler<CreateLeadCommand, LeadDto> {
    
    private final LeadRepository leadRepository;

    public CreateLeadCommandHandler(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
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
