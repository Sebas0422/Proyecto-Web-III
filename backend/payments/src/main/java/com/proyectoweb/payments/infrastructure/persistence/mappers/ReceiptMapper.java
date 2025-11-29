package com.proyectoweb.payments.infrastructure.persistence.mappers;

import com.proyectoweb.payments.domain.aggregates.Receipt;
import com.proyectoweb.payments.domain.value_objects.CustomerInfo;
import com.proyectoweb.payments.domain.value_objects.Money;
import com.proyectoweb.payments.infrastructure.persistence.jpa.models.ReceiptJpaModel;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {

    public ReceiptJpaModel toJpa(Receipt domain) {
        ReceiptJpaModel jpa = new ReceiptJpaModel();
        jpa.setId(domain.getId());
        jpa.setTenantId(domain.getTenantId());
        jpa.setPaymentId(domain.getPaymentId());
        jpa.setReceiptNumber(domain.getReceiptNumber());
        
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
        jpa.setTransactionReference(domain.getTransactionReference());
        jpa.setPdfPath(domain.getPdfPath());
        jpa.setIssuedAt(domain.getIssuedAt());
        jpa.setNotes(domain.getNotes());
        jpa.setIssuedBy(domain.getIssuedBy());
        jpa.setCreatedAt(domain.getCreatedAt());
        
        return jpa;
    }

    public Receipt toDomain(ReceiptJpaModel jpa) {
        CustomerInfo customerInfo = new CustomerInfo(
            jpa.getCustomerName(),
            jpa.getCustomerEmail(),
            jpa.getCustomerPhone(),
            jpa.getCustomerDocument()
        );
        
        Money money = new Money(jpa.getAmount(), jpa.getCurrency());
        
        return Receipt.reconstitute(
            jpa.getId(),
            jpa.getTenantId(),
            jpa.getPaymentId(),
            jpa.getReceiptNumber(),
            customerInfo,
            money,
            jpa.getPaymentMethod(),
            jpa.getTransactionReference(),
            jpa.getPdfPath(),
            jpa.getIssuedAt(),
            jpa.getNotes(),
            jpa.getIssuedBy(),
            jpa.getCreatedAt()
        );
    }
}
