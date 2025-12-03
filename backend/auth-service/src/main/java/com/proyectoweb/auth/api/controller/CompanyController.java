package com.proyectoweb.auth.api.controller;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.api.dto.UpdateCompanyRequest;
import com.proyectoweb.auth.api.dto.request.AssignUserToCompanyRequest;
import com.proyectoweb.auth.api.dto.request.CreateCompanyRequest;
import com.proyectoweb.auth.api.dto.response.MessageResponse;
import com.proyectoweb.auth.application.commands.company.DeactivateCompanyCommand;
import com.proyectoweb.auth.application.commands.company.UpdateCompanyCommand;
import com.proyectoweb.auth.application.queries.company.GetCompanyByIdQuery;
import com.proyectoweb.auth.application.use_cases.company.commands.AssignUserToCompanyCommand;
import com.proyectoweb.auth.application.use_cases.company.commands.CreateCompanyCommand;
import com.proyectoweb.auth.application.use_cases.company.queries.CompanyDto;
import com.proyectoweb.auth.application.use_cases.company.queries.GetAllCompaniesQuery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/companies")
public class CompanyController {

    private final Pipeline pipeline;

    public CompanyController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        CreateCompanyCommand command = new CreateCompanyCommand(
                request.name(),
                request.ruc(),
                request.address(),
                request.phoneNumber(),
                request.email()
        );

        UUID companyId = command.execute(pipeline);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Empresa creada con ID: " + companyId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CompanyDto>> getAllCompanies(
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        GetAllCompaniesQuery query = new GetAllCompaniesQuery(onlyActive);
        List<CompanyDto> companies = query.execute(pipeline);
        return ResponseEntity.ok(companies);
    }

    @PostMapping("/assign-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> assignUserToCompany(
            @Valid @RequestBody AssignUserToCompanyRequest request) {
        AssignUserToCompanyCommand command = new AssignUserToCompanyCommand(
                request.userId(),
                request.companyId(),
                request.role()
        );

        command.execute(pipeline);

        return ResponseEntity.ok(new MessageResponse("Usuario asignado a empresa exitosamente"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable UUID id) {
        GetCompanyByIdQuery query = new GetCompanyByIdQuery(id);
        CompanyDto company = query.execute(pipeline);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CompanyDto> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequest request) {
        UpdateCompanyCommand command = new UpdateCompanyCommand(
                id,
                request.name(),
                request.address(),
                request.phoneNumber(),
                request.email(),
                request.logoUrl()
        );

        CompanyDto updatedCompany = command.execute(pipeline);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> deactivateCompany(@PathVariable UUID id) {
        DeactivateCompanyCommand command = new DeactivateCompanyCommand(id);
        command.execute(pipeline);
        return ResponseEntity.ok(new MessageResponse("Empresa desactivada exitosamente"));
    }
}
