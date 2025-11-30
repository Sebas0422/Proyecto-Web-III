package com.proyectoweb.reservations.application.queries.lead;

import an.awesome.pipelinr.Command;
import com.proyectoweb.reservations.application.dto.LeadDto;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.domain.value_objects.LeadStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetLeadsByTenantQueryHandler implements Command.Handler<GetLeadsByTenantQuery, List<LeadDto>> {
    
    private final LeadRepository leadRepository;

    public GetLeadsByTenantQueryHandler(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Override
    public List<LeadDto> handle(GetLeadsByTenantQuery query) {
        List<Lead> leads;

        if (query.status() != null) {
            LeadStatus status = LeadStatus.valueOf(query.status());
            leads = leadRepository.findByTenantIdAndStatus(query.tenantId(), status);
        } else {
            leads = leadRepository.findByTenantId(query.tenantId());
        }

        return leads.stream()
                .map(l -> new LeadDto(
                        l.getId(),
                        l.getTenantId(),
                        l.getProjectId(),
                        l.getLotId(),
                        l.getCustomerInfo().fullName(),
                        l.getCustomerInfo().email(),
                        l.getCustomerInfo().phone(),
                        l.getCustomerInfo().documentNumber(),
                        l.getStatus().name(),
                        l.getSource(),
                        l.getInterestLevel(),
                        l.getNotes(),
                        l.getAssignedTo(),
                        l.getContactedAt(),
                        l.getConvertedAt(),
                        l.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
