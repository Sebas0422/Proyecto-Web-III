package com.proyectoweb.payments.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.payments.domain.value_objects.PaymentStatus;
import com.proyectoweb.payments.infrastructure.persistence.jpa.models.PaymentJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentJpaModel, UUID> {
    
    Optional<PaymentJpaModel> findByReservationId(UUID reservationId);
    
    List<PaymentJpaModel> findByTenantId(UUID tenantId);
    
    List<PaymentJpaModel> findByTenantIdAndStatus(UUID tenantId, PaymentStatus status);
    
    @Query("SELECT p FROM PaymentJpaModel p WHERE p.expirationDate < :now AND p.status IN ('PENDING', 'PROCESSING')")
    List<PaymentJpaModel> findExpiredPayments(@Param("now") LocalDateTime now);
}
