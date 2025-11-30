package com.proyectoweb.payments.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.payments.infrastructure.persistence.jpa.models.ReceiptJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptJpaRepository extends JpaRepository<ReceiptJpaModel, UUID> {
    
    Optional<ReceiptJpaModel> findByPaymentId(UUID paymentId);
    
    Optional<ReceiptJpaModel> findByReceiptNumber(String receiptNumber);
    
    List<ReceiptJpaModel> findByTenantId(UUID tenantId);
}
