package com.proyectoweb.proyectos.infrastructure.messaging;

import com.proyectoweb.proyectos.application.commands.lote.CancelarReservaLoteCommand;
import com.proyectoweb.proyectos.application.commands.lote.MarcarLoteComoVendidoCommand;
import com.proyectoweb.proyectos.application.commands.lote.ReservarLoteCommand;
import com.proyectoweb.proyectos.infrastructure.messaging.events.PaymentCompletedEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ReservationCancelledEvent;
import com.proyectoweb.proyectos.infrastructure.messaging.events.ReservationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import an.awesome.pipelinr.Pipeline;

import java.util.UUID;

@Service
public class KafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    private final Pipeline pipeline;

    public KafkaConsumerService(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @KafkaListener(
        topics = "reservation-created-events",
        groupId = "proyectos-lotes-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleReservationCreated(ReservationCreatedEvent event) {
        try {
            logger.info("Recibido evento de reserva creada: reservationId={}, lotUuid={}", 
                       event.getReservationId(), event.getLotUuid());
            
            if (event.getLotUuid() != null && !event.getLotUuid().isBlank()) {
                UUID lotId = UUID.fromString(event.getLotUuid());
                
                ReservarLoteCommand command = new ReservarLoteCommand(lotId);
                pipeline.send(command);
                
                logger.info("Lote {} marcado como RESERVADO exitosamente", lotId);
            } else {
                logger.warn("El evento de reserva no contiene lotUuid válido");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error al parsear UUID del lote: {}", event.getLotUuid(), e);
        } catch (Exception e) {
            logger.error("Error al procesar evento de reserva creada", e);
        }
    }

    @KafkaListener(
        topics = "reservation-cancelled-events",
        groupId = "proyectos-lotes-service-group",
        containerFactory = "kafkaListenerContainerFactoryCancelled"
    )
    public void handleReservationCancelled(ReservationCancelledEvent event) {
        try {
            logger.info("Recibido evento de reserva cancelada: reservationId={}, lotUuid={}", 
                       event.getReservationId(), event.getLotUuid());
            
            if (event.getLotUuid() != null && !event.getLotUuid().isBlank()) {
                UUID lotId = UUID.fromString(event.getLotUuid());
                
                CancelarReservaLoteCommand command = new CancelarReservaLoteCommand(lotId);
                pipeline.send(command);
                
                logger.info("Lote {} marcado como DISPONIBLE exitosamente", lotId);
            } else {
                logger.warn("El evento de cancelación no contiene lotUuid válido");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error al parsear UUID del lote: {}", event.getLotUuid(), e);
        } catch (Exception e) {
            logger.error("Error al procesar evento de reserva cancelada", e);
        }
    }

    @KafkaListener(
        topics = "payment-completed-events",
        groupId = "proyectos-lotes-service-group",
        containerFactory = "kafkaListenerContainerFactoryPayment"
    )
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        try {
            logger.info("Recibido evento de pago completado: paymentId={}, lotId={}", 
                       event.getPaymentId(), event.getLotId());
            
            if (event.getLotId() != null && !event.getLotId().isBlank()) {
                UUID lotId = UUID.fromString(event.getLotId());
                
                MarcarLoteComoVendidoCommand command = new MarcarLoteComoVendidoCommand(lotId);
                pipeline.send(command);
                
                logger.info("Lote {} marcado como VENDIDO exitosamente", lotId);
            } else {
                logger.warn("El evento de pago no contiene lotId válido");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error al parsear UUID del lote: {}", event.getLotId(), e);
        } catch (Exception e) {
            logger.error("Error al procesar evento de pago completado", e);
        }
    }
}
