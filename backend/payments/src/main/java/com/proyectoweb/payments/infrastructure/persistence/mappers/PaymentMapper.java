package com.proyectoweb.payments.infrastructure.persistence.mappers;

import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.value_objects.CustomerInfo;
import com.proyectoweb.payments.domain.value_objects.Money;
import com.proyectoweb.payments.infrastructure.persistence.jpa.models.PaymentJpaModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentJpaModel toJpa(Payment domain) {
        PaymentJpaModel jpa = new PaymentJpaModel();
        jpa.setId(domain.getId());
        jpa.setTenantId(domain.getTenantId());
        jpa.setReservationId(domain.getReservationId());
        
        // Map CustomerInfo
        CustomerInfo customer = domain.getCustomerInfo();
        jpa.setCustomerName(customer.fullName());
        jpa.setCustomerEmail(customer.email());
        jpa.setCustomerPhone(customer.phone());
        jpa.setCustomerDocument(customer.documentNumber());
        
        // Map Money
        jpa.setAmount(domain.getAmount().amount());
        jpa.setCurrency(domain.getAmount().currency());
        
        jpa.setPaymentMethod(domain.getPaymentMethod());
        jpa.setStatus(domain.getStatus());
        jpa.setTransactionReference(domain.getTransactionReference());
        jpa.setQrCodeData(domain.getQrCodeData());
        jpa.setPaymentDate(domain.getPaymentDate());
        jpa.setExpirationDate(domain.getExpirationDate());
        jpa.setConfirmedAt(domain.getConfirmedAt());
        jpa.setNotes(domain.getNotes());
        jpa.setCreatedBy(domain.getCreatedBy());
        jpa.setCreatedAt(domain.getCreatedAt());
        jpa.setUpdatedAt(domain.getUpdatedAt());
        
        return jpa;
    }

    public Payment toDomain(PaymentJpaModel jpa) {
        CustomerInfo customerInfo = new CustomerInfo(
            jpa.getCustomerName(),
            jpa.getCustomerEmail(),
            jpa.getCustomerPhone(),
            jpa.getCustomerDocument()
        );
        
        Money money = new Money(jpa.getAmount(), jpa.getCurrency());
        
        return Payment.reconstitute(
            jpa.getId(),
            jpa.getTenantId(),
            jpa.getReservationId(),
            customerInfo,
            money,
            jpa.getPaymentMethod(),
            jpa.getStatus(),
            jpa.getTransactionReference(),
            jpa.getQrCodeData(),
            jpa.getPaymentDate(),
            jpa.getExpirationDate(),
            jpa.getConfirmedAt(),
            jpa.getNotes(),
            jpa.getCreatedBy(),
            jpa.getCreatedAt(),
            jpa.getUpdatedAt()
        );
    }
}
