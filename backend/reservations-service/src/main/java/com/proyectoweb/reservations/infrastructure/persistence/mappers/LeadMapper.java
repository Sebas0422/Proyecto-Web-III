package com.proyectoweb.reservations.infrastructure.persistence.mappers;

import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.LeadJpaModel;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

    public LeadJpaModel toJpa(Lead domain) {
        LeadJpaModel jpa = new LeadJpaModel();
        jpa.setId(domain.getId());
        jpa.setTenantId(domain.getTenantId());
        jpa.setProjectId(domain.getProjectId());
        jpa.setLotId(domain.getLotId());
        
        CustomerInfo customer = domain.getCustomerInfo();
        jpa.setCustomerName(customer.fullName());
        jpa.setCustomerEmail(customer.email());
        jpa.setCustomerPhone(customer.phone());
        jpa.setCustomerDocument(customer.documentNumber());
        
        jpa.setStatus(domain.getStatus());
        jpa.setSource(domain.getSource());
        jpa.setInterestLevel(domain.getInterestLevel());
        jpa.setNotes(domain.getNotes());
        jpa.setAssignedTo(domain.getAssignedTo());
        jpa.setContactedAt(domain.getContactedAt());
        jpa.setConvertedAt(domain.getConvertedAt());
        
        return jpa;
    }

    public Lead toDomain(LeadJpaModel jpa) {
        CustomerInfo customerInfo = new CustomerInfo(
            jpa.getCustomerName(),
            jpa.getCustomerEmail(),
            jpa.getCustomerPhone(),
            jpa.getCustomerDocument()
        );
        
        return Lead.reconstitute(
            jpa.getId(),
            jpa.getTenantId(),
            jpa.getProjectId(),
            jpa.getLotId(),
            customerInfo,
            jpa.getStatus(),
            jpa.getSource(),
            jpa.getInterestLevel(),
            jpa.getNotes(),
            jpa.getAssignedTo(),
            jpa.getContactedAt(),
            jpa.getConvertedAt()
        );
    }
}
