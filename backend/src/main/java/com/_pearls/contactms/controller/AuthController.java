package com._pearls.contactms.controller;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.LoginResponseDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Auth", description = "API for managing Authentication")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {

        String response = authService.register(registerRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        String token = authService.authenticate(loginRequestDTO);

        if(token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

}
