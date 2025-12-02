package com.proyectoweb.reports.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoweb.reports.domain.aggregates.MetricsSnapshot;
import com.proyectoweb.reports.domain.repositories.MetricsSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final MetricsSnapshotRepository metricsRepository;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(MetricsSnapshotRepository metricsRepository, ObjectMapper objectMapper) {
        this.metricsRepository = metricsRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "company-events", groupId = "reports-service-group")
    public void consumeCompanyEvents(String event) {
        logger.info("Recibido evento de company: {}", event);
        // Las métricas de company no están en el dashboard actual, solo logging
    }

    @KafkaListener(topics = "project-events", groupId = "reports-service-group")
    public void consumeProjectEvents(String event) {
        logger.info("Recibido evento de project: {}", event);
        try {
            Map<String, Object> eventData = objectMapper.readValue(event, Map.class);
            
            String tenantIdStr = (String) eventData.get("tenantId");
            UUID tenantId = UUID.fromString(tenantIdStr);
            String status = (String) eventData.getOrDefault("status", "ACTIVO");
            
            MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
            metrics.incrementProjectCount("ACTIVO".equalsIgnoreCase(status));
            metricsRepository.save(metrics);
            
            logger.info("✅ Métricas actualizadas para tenant {}: totalProjects={}, activeProjects={}", 
                       tenantId, metrics.getTotalProjects(), metrics.getActiveProjects());
        } catch (Exception e) {
            logger.error("Error procesando evento de project: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "lot-events", groupId = "reports-service-group")
    public void consumeLotEvents(String event) {
        logger.info("Recibido evento de lot: {}", event);
        try {
            Map<String, Object> eventData = objectMapper.readValue(event, Map.class);
            
            String tenantIdStr = (String) eventData.get("tenantId");
            UUID tenantId = UUID.fromString(tenantIdStr);
            String estado = (String) eventData.getOrDefault("estado", "DISPONIBLE");
            
            MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
            metrics.incrementLotCount(estado);
            metricsRepository.save(metrics);
            
            logger.info("✅ Métricas de lotes actualizadas para tenant {}: totalLots={}, disponibles={}, vendidos={}, reservados={}", 
                       tenantId, metrics.getTotalLots(), metrics.getAvailableLots(), metrics.getSoldLots(), metrics.getReservedLots());
        } catch (Exception e) {
            logger.error("Error procesando evento de lot: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "reservation-events", groupId = "reports-service-group")
    public void consumeReservationEvents(String event) {
        logger.info("Recibido evento de reservation: {}", event);
        try {
            Map<String, Object> eventData = objectMapper.readValue(event, Map.class);
            
            String tenantIdStr = (String) eventData.get("tenantId");
            UUID tenantId = UUID.fromString(tenantIdStr);
            String status = (String) eventData.getOrDefault("status", "PENDIENTE");
            
            MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
            metrics.incrementReservation("CONFIRMADA".equalsIgnoreCase(status));
            metricsRepository.save(metrics);
            
            logger.info("✅ Métricas de reservaciones actualizadas para tenant {}: total={}, confirmadas={}, pendientes={}", 
                       tenantId, metrics.getTotalReservations(), metrics.getConfirmedReservations(), metrics.getPendingReservations());
        } catch (Exception e) {
            logger.error("Error procesando evento de reservation: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "lead-events", groupId = "reports-service-group")
    public void consumeLeadEvents(String event) {
        logger.info("Recibido evento de lead: {}", event);
        try {
            Map<String, Object> eventData = objectMapper.readValue(event, Map.class);
            
            String tenantIdStr = (String) eventData.get("tenantId");
            UUID tenantId = UUID.fromString(tenantIdStr);
            String status = (String) eventData.getOrDefault("status", "NUEVO");
            
            MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
            
            if ("CONVERTIDO".equalsIgnoreCase(status)) {
                metrics.markLeadAsConverted();
            } else {
                metrics.incrementLeadCount(!"PERDIDO".equalsIgnoreCase(status));
            }
            
            metricsRepository.save(metrics);
            
            logger.info("✅ Métricas de leads actualizadas para tenant {}: total={}, activos={}, convertidos={}", 
                       tenantId, metrics.getTotalLeads(), metrics.getActiveLeads(), metrics.getConvertedLeads());
        } catch (Exception e) {
            logger.error("Error procesando evento de lead: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "payment-events", groupId = "reports-service-group")
    public void consumePaymentEvents(String event) {
        logger.info("Recibido evento de payment: {}", event);
        try {
            Map<String, Object> eventData = objectMapper.readValue(event, Map.class);
            
            String tenantIdStr = (String) eventData.get("tenantId");
            UUID tenantId = UUID.fromString(tenantIdStr);
            
            Number amountNumber = (Number) eventData.get("amount");
            BigDecimal amount = new BigDecimal(amountNumber.toString());
            
            String status = (String) eventData.getOrDefault("status", "PENDIENTE");
            
            MetricsSnapshot metrics = metricsRepository.getOrCreateByTenantId(tenantId);
            
            if ("CONFIRMADO".equalsIgnoreCase(status)) {
                metrics.addPayment(amount, true);
            } else {
                metrics.addPayment(amount, false);
            }
            
            metricsRepository.save(metrics);
            
            logger.info("✅ Métricas financieras actualizadas para tenant {}: totalRevenue={}, confirmed={}, pending={}", 
                       tenantId, metrics.getTotalRevenue(), metrics.getConfirmedPayments(), metrics.getPendingPayments());
        } catch (Exception e) {
            logger.error("Error procesando evento de payment: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "receipt-events", groupId = "reports-service-group")
    public void consumeReceiptEvents(String event) {
        logger.info("Recibido evento de receipt: {}", event);
        // Los recibos no están en el dashboard actual, solo logging
    }
}
