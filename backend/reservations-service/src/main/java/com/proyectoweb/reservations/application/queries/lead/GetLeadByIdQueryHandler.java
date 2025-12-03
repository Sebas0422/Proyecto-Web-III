package com.proyectoweb.reservations.application.queries.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetLeadByIdQueryHandler implements Command.Handler<GetLeadByIdQuery, LeadDto> {

    private final LeadRepository leadRepository;

    @Override
    public LeadDto handle(GetLeadByIdQuery query) {
        Lead lead = leadRepository.findById(query.getLeadId())
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        if (!lead.getTenantId().equals(query.getTenantId())) {
            throw new RuntimeException("No permission to access this lead");
        }

        return new LeadDto(
                lead.getId(),
                lead.getTenantId(),
                lead.getProjectId(),
                lead.getLotId(),
                lead.getCustomerInfo().fullName(),
                lead.getCustomerInfo().email(),
                lead.getCustomerInfo().phone(),
                lead.getCustomerInfo().documentNumber(),
                lead.getStatus().name(),
                lead.getSource(),
                lead.getInterestLevel(),
                lead.getNotes(),
                lead.getAssignedTo(),
                lead.getContactedAt(),
                lead.getConvertedAt(),
                lead.getCreatedAt()
        );
    }
}
