package com.proyectoweb.payments.infrastructure.persistence.adapters;

import com.proyectoweb.payments.domain.aggregates.Receipt;
import com.proyectoweb.payments.domain.repositories.ReceiptRepository;
import com.proyectoweb.payments.infrastructure.persistence.jpa.models.ReceiptJpaModel;
import com.proyectoweb.payments.infrastructure.persistence.jpa.repositories.ReceiptJpaRepository;
import com.proyectoweb.payments.infrastructure.persistence.mappers.ReceiptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReceiptRepositoryAdapter implements ReceiptRepository {

    private final ReceiptJpaRepository jpaRepository;
    private final ReceiptMapper mapper;

    @Override
    public Receipt save(Receipt receipt) {
        ReceiptJpaModel jpaModel = mapper.toJpa(receipt);
        ReceiptJpaModel saved = jpaRepository.save(jpaModel);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Receipt> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Receipt> findByPaymentId(UUID paymentId) {
        return jpaRepository.findByPaymentId(paymentId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Receipt> findByReceiptNumber(String receiptNumber) {
        return jpaRepository.findByReceiptNumber(receiptNumber)
                .map(mapper::toDomain);
    }

    @Override
    public List<Receipt> findByTenantId(UUID tenantId) {
        return jpaRepository.findByTenantId(tenantId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
