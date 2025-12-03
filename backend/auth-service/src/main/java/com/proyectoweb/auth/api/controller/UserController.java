package com.proyectoweb.auth.api.controller;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.api.dto.request.ChangePasswordRequest;
import com.proyectoweb.auth.api.dto.request.CreateUserRequest;
import com.proyectoweb.auth.api.dto.request.UpdateUserRequest;
import com.proyectoweb.auth.api.dto.response.UserDto;
import com.proyectoweb.auth.application.commands.user.*;
import com.proyectoweb.auth.application.queries.user.GetAllUsersQuery;
import com.proyectoweb.auth.application.queries.user.GetUserByIdQuery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final Pipeline pipeline;

    public UserController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive
    ) {
        var query = new GetAllUsersQuery(role, isActive);
        List<UserDto> users = pipeline.send(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        var query = new GetUserByIdQuery(id);
        UserDto user = pipeline.send(query);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody CreateUserRequest request) {
        var command = new CreateUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.role(),
                request.companyId()
        );
        
        UUID userId = pipeline.send(command);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario creado exitosamente",
                "userId", userId
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        var command = new UpdateUserCommand(
                id,
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.role(),
                request.companyId()
        );
        
        pipeline.send(command);
        
        return ResponseEntity.ok(Map.of("message", "Usuario actualizado exitosamente"));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable UUID id) {
        var command = new ActivateUserCommand(id);
        pipeline.send(command);
        return ResponseEntity.ok(Map.of("message", "Usuario activado exitosamente"));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable UUID id) {
        var command = new DeactivateUserCommand(id);
        pipeline.send(command);
        return ResponseEntity.ok(Map.of("message", "Usuario desactivado exitosamente"));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        var command = new ChangePasswordCommand(id, request.newPassword());
        pipeline.send(command);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente"));
    }
}
