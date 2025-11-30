package com.proyectoweb.reservations.infrastructure.persistence.jpa.repositories;

import com.proyectoweb.reservations.domain.value_objects.ReservationStatus;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.ReservationJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaModel, UUID> {
    List<ReservationJpaModel> findByTenantId(UUID tenantId);
    List<ReservationJpaModel> findByTenantIdAndStatus(UUID tenantId, ReservationStatus status);
    List<ReservationJpaModel> findByLotId(UUID lotId);
    
    @Query("SELECT r FROM ReservationJpaModel r WHERE r.status = 'PENDING' AND r.expirationDate < :now")
    List<ReservationJpaModel> findExpiredReservations(LocalDateTime now);
    
    boolean existsByLotIdAndStatusIn(UUID lotId, List<ReservationStatus> statuses);
}
