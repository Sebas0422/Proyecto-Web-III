package com.proyectoweb.auth.application.event_handlers;

import an.awesome.pipelinr.Notification;
import com.proyectoweb.auth.domain.events.UserAssignedToCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAssignedToCompanyEventHandler implements Notification.Handler<UserAssignedToCompany> {

    private static final Logger logger = LoggerFactory.getLogger(UserAssignedToCompanyEventHandler.class);

    @Override
    public void handle(UserAssignedToCompany event) {
        logger.info("Usuario {} asignado a empresa {} con rol {}", 
            event.getUserId(), event.getCompanyId(), event.getRole());
        
        // TODO: Publicar a Kafka
        // kafkaProducer.send("user-events", event);
    }
}
