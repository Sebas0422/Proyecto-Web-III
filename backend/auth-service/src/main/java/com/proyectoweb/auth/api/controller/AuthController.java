package com.proyectoweb.auth.api.controller;

import an.awesome.pipelinr.Pipeline;
import com.proyectoweb.auth.api.dto.request.LoginRequest;
import com.proyectoweb.auth.api.dto.request.RegisterUserRequest;
import com.proyectoweb.auth.api.dto.response.LoginResponseDto;
import com.proyectoweb.auth.api.dto.response.UserResponse;
import com.proyectoweb.auth.application.use_cases.auth.commands.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Pipeline pipeline;

    public AuthController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.role()
        );

        RegisterUserResponse response = command.execute(pipeline);

        UserResponse userResponse = new UserResponse(
                response.getUserId(),
                response.getEmail(),
                response.getFullName(),
                response.getRole(),
                response.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(
                request.email(),
                request.password()
        );

        LoginResponse response = command.execute(pipeline);

        LoginResponseDto dto = new LoginResponseDto(
                response.getToken(),
                response.getUserId(),
                response.getEmail(),
                response.getFullName(),
                response.getRole(),
                response.getTenantId()
        );

        return ResponseEntity.ok(dto);
    }
}
