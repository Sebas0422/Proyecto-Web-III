package com.proyectoweb.auth.application.event_handlers;

import an.awesome.pipelinr.Notification;
import com.proyectoweb.auth.domain.events.CompanyCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompanyCreatedEventHandler implements Notification.Handler<CompanyCreated> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyCreatedEventHandler.class);

    @Override
    public void handle(CompanyCreated event) {
        logger.info("Empresa creada: {} con RUC {}", event.getCompanyName(), event.getRuc());
        
        // TODO: Publicar a Kafka topic "company-events"
        // kafkaProducer.send("company-events", event);
    }
}
