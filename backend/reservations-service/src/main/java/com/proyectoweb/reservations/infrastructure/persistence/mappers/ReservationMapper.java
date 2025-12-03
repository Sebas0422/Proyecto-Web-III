package com.proyectoweb.reservations.infrastructure.persistence.mappers;

import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.value_objects.CustomerInfo;
import com.proyectoweb.reservations.domain.value_objects.ReservationAmount;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.ReservationJpaModel;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationJpaModel toJpa(Reservation domain) {
        ReservationJpaModel jpa = new ReservationJpaModel();
        jpa.setId(domain.getId());
        jpa.setTenantId(domain.getTenantId());
        jpa.setLotId(domain.getLotId());
        jpa.setProjectId(domain.getProjectId());
        
        CustomerInfo customer = domain.getCustomerInfo();
        jpa.setCustomerName(customer.fullName());
        jpa.setCustomerEmail(customer.email());
        jpa.setCustomerPhone(customer.phone());
        jpa.setCustomerDocument(customer.documentNumber());
        
        jpa.setReservationAmount(domain.getReservationAmount().amount());
        
        jpa.setStatus(domain.getStatus());
        jpa.setReservationDate(domain.getReservationDate());
        jpa.setExpirationDate(domain.getExpirationDate());
        jpa.setConfirmedAt(domain.getConfirmedAt());
        jpa.setCancelledAt(domain.getCancelledAt());
        jpa.setNotes(domain.getNotes());
        jpa.setCreatedBy(domain.getCreatedBy());
        
        return jpa;
    }

    public Reservation toDomain(ReservationJpaModel jpa) {
        CustomerInfo customerInfo = new CustomerInfo(
            jpa.getCustomerName(),
            jpa.getCustomerEmail(),
            jpa.getCustomerPhone(),
            jpa.getCustomerDocument()
        );
        
        ReservationAmount amount = new ReservationAmount(jpa.getReservationAmount());
        
        return Reservation.reconstitute(
            jpa.getId(),
            jpa.getTenantId(),
            jpa.getLotId(),
            jpa.getProjectId(),
            customerInfo,
            amount,
            jpa.getStatus(),
            jpa.getReservationDate(),
            jpa.getExpirationDate(),
            jpa.getConfirmedAt(),
            jpa.getCancelledAt(),
            jpa.getNotes(),
            jpa.getCreatedBy()
        );
    }
}
