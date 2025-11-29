package com.proyectoweb.reservations.domain.repositories;

import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.value_objects.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    List<Reservation> findByTenantId(UUID tenantId);
    List<Reservation> findByTenantIdAndStatus(UUID tenantId, ReservationStatus status);
    List<Reservation> findByLotId(UUID lotId);
    List<Reservation> findExpiredReservations();
    boolean existsByLotIdAndStatusIn(UUID lotId, List<ReservationStatus> statuses);
}
