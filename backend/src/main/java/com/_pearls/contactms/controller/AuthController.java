package com._pearls.contactms.controller;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.LoginResponseDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterResponseDTO;
import com._pearls.contactms.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Tag(name = "Auth", description = "API for managing Authentication")
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        String token = authService.register(registerRequestDTO);
        return ResponseEntity.ok(new RegisterResponseDTO(token));
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.authenticate(loginRequestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Verify Password")
    @PostMapping("/password/verify")
    public ResponseEntity<Boolean> verifyPassword(
            @RequestBody String oldPassword
    ) {
        Boolean isPasswordMatched = authService.verifyPassword(oldPassword);
        return ResponseEntity.ok(isPasswordMatched);
    }

    @Operation(summary = "Update Password")
    @PostMapping("/password/update")
    public ResponseEntity<Void> updatePassword(
            @RequestBody String newPassword
    ) {
        authService.updatePassword(newPassword);
        return ResponseEntity.ok().build();
    }
}
