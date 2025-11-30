package com.proyectoweb.payments.infrastructure.persistence.adapters;

import com.proyectoweb.payments.domain.aggregates.Payment;
import com.proyectoweb.payments.domain.repositories.PaymentRepository;
import com.proyectoweb.payments.domain.value_objects.PaymentStatus;
import com.proyectoweb.payments.infrastructure.persistence.jpa.models.PaymentJpaModel;
import com.proyectoweb.payments.infrastructure.persistence.jpa.repositories.PaymentJpaRepository;
import com.proyectoweb.payments.infrastructure.persistence.mappers.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaModel jpaModel = mapper.toJpa(payment);
        PaymentJpaModel saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByReservationId(UUID reservationId) {
        return jpaRepository.findByReservationId(reservationId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Payment> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByTenantIdAndStatus(UUID tenantId, PaymentStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findExpiredPayments() {
        return jpaRepository.findExpiredPayments(LocalDateTime.now()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
