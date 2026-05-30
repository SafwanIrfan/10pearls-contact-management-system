package com._pearls.contactms.service;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterResponseDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.exception.ConflictException;
import com._pearls.contactms.exception.NotFoundException;
import com._pearls.contactms.exception.UnauthorizedException;
import com._pearls.contactms.model.User;
import com._pearls.contactms.repo.AuthRepo;
import com._pearls.contactms.utils.AuthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthRepo authRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    public AuthService(
            AuthRepo authRepo,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            BCryptPasswordEncoder encoder
    ) {
        this.authRepo = authRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    public String register(RegisterRequestDTO registerRequestDTO) {

        String identifier = registerRequestDTO.getIdentifier();

        User user =  new User();

        if(AuthHelper.isEmail(identifier)) {
            if(authRepo.existsByEmail(identifier)) {
                log.warn("Registration attempt with existing email: {}", identifier);
                throw new ConflictException("Email already exists");
            }
            user.setEmail(identifier);
        } else if (AuthHelper.isPhoneNo(identifier)) {
            if(authRepo.existsByPhoneNo(identifier)) {
                log.warn("Registration attempt with existing phone: {}", identifier);
                throw new ConflictException("Phone no already exists");
            }
            user.setPhoneNo(identifier);
        } else {
            log.warn("Registration attempt with invalid identifier: {}", identifier);
            throw new BadRequestException("Invalid Email or Phone Number");
        }

        user.setPassword(encoder.encode(registerRequestDTO.getPassword()));
        authRepo.save(user);

        log.info("User {} has been registered", registerRequestDTO.getIdentifier());
        return jwtService.generateToken(registerRequestDTO.getIdentifier());
    }

    public String authenticate(LoginRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getIdentifier(), loginRequestDTO.getPassword()
                    ));
        } catch (BadCredentialsException e) {
            log.warn("Failed authentication attempt for: {}", loginRequestDTO.getIdentifier());
            throw new UnauthorizedException("Invalid Credentials");
        }
        log.info("User {} has been authenticated", loginRequestDTO.getIdentifier());
        return jwtService.generateToken(loginRequestDTO.getIdentifier());
    }

    private User getAuthenticatedUser() {
        String identifier = Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getName();
        return authRepo.findByEmailOrPhoneNo(identifier)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Boolean verifyPassword(String oldPassword) {
        User user = getAuthenticatedUser();
        return encoder.matches(oldPassword, user.getPassword());
    }

    public void updatePassword(String newPassword) {
        User user = getAuthenticatedUser();
        user.setPassword(encoder.encode(newPassword));
        log.info("User password has been updated with this email: {}", user.getEmail() != null ? user.getEmail() : user.getPhoneNo());
        authRepo.save(user);
    }
}
