package com.proyectoweb.auth.application.event_handlers;

import an.awesome.pipelinr.Notification;
import com.proyectoweb.auth.domain.events.CompanyCreated;
import com.proyectoweb.auth.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.auth.infrastructure.messaging.events.CompanyCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompanyCreatedEventHandler implements Notification.Handler<CompanyCreated> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyCreatedEventHandler.class);
    private final KafkaProducerService kafkaProducerService;

    public CompanyCreatedEventHandler(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void handle(CompanyCreated event) {
        logger.info("Empresa creada: {} con RUC {}", event.getCompanyName(), event.getRuc());
        
        // Publicar evento a Kafka
        CompanyCreatedEvent kafkaEvent = new CompanyCreatedEvent(
            event.getCompanyId().getMostSignificantBits(), // Convert UUID to Long
            event.getCompanyName(),
            event.getRuc(),
            null, // email - no disponible en el evento de dominio
            null, // phoneNumber - no disponible
            java.time.LocalDateTime.now()
        );
        
        kafkaProducerService.publishCompanyCreated(kafkaEvent);
        logger.info("Evento CompanyCreated publicado a Kafka para companyId: {}", event.getCompanyId());
    }
}

