package com.proyectoweb.payments.domain.repositories;

import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.value_objects.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    Optional<Payment> findByReservationId(UUID reservationId);
    List<Payment> findByTenantId(UUID tenantId);
    List<Payment> findByTenantIdAndStatus(UUID tenantId, PaymentStatus status);
    List<Payment> findExpiredPayments();
}
