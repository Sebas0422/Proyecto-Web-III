package com.proyectoweb.reservations.infrastructure.persistence.adapters;

import com.proyectoweb.reservations.domain.aggregates.Reservation;
import com.proyectoweb.reservations.domain.repositories.ReservationRepository;
import com.proyectoweb.reservations.domain.value_objects.ReservationStatus;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.models.ReservationJpaModel;
import com.proyectoweb.reservations.infrastructure.persistence.jpa.repositories.ReservationJpaRepository;
import com.proyectoweb.reservations.infrastructure.persistence.mappers.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationMapper mapper;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaModel jpaModel = mapper.toJpa(reservation);
        ReservationJpaModel saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByTenantIdAndStatus(UUID tenantId, ReservationStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByLotId(UUID lotId) {
        return jpaRepository.findByLotId(lotId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findExpiredReservations() {
        return jpaRepository.findExpiredReservations(LocalDateTime.now()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLotIdAndStatusIn(UUID lotId, List<ReservationStatus> statuses) {
        return jpaRepository.existsByLotIdAndStatusIn(lotId, statuses);
    }
}
