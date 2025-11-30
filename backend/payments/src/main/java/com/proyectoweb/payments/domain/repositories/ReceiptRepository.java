package com.proyectoweb.payments.domain.repositories;

import com.proyectoweb.payments.domain.aggregates.Receipt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptRepository {
    Receipt save(Receipt receipt);
    Optional<Receipt> findById(UUID id);
    Optional<Receipt> findByPaymentId(UUID paymentId);
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    List<Receipt> findByTenantId(UUID tenantId);
}
