package com.proyectoweb.reservations.application.commands.lead;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.proyectoweb.reservations.domain.aggregates.Lead;
import com.proyectoweb.reservations.domain.repositories.LeadRepository;
import com.proyectoweb.reservations.infrastructure.messaging.KafkaProducerService;
import com.proyectoweb.reservations.infrastructure.messaging.events.LeadDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DeleteLeadCommandHandler implements Command.Handler<DeleteLeadCommand, Voidy> {

    private final LeadRepository leadRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public Voidy handle(DeleteLeadCommand command) {
        Lead lead = leadRepository.findById(command.getLeadId())
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        if (!lead.getTenantId().equals(command.getTenantId())) {
            throw new RuntimeException("No permission to delete this lead");
        }

        leadRepository.delete(lead);

        LeadDeletedEvent event = new LeadDeletedEvent(
                lead.getId().toString(),
                lead.getTenantId().toString(),
                lead.getCustomerInfo().fullName(),
                LocalDateTime.now()
        );
        kafkaProducerService.publishLeadDeleted(event);

        return new Voidy();
    }
}
