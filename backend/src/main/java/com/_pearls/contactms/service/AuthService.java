package com._pearls.contactms.service;

import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.exception.ConflictException;
import com._pearls.contactms.model.User;
import com._pearls.contactms.repo.AuthRepo;
import com._pearls.contactms.utils.AuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthRepo authRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepo authRepo, PasswordEncoder passwordEncoder) {
        this.authRepo = authRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public String register(RegisterRequestDTO registerRequestDTO) {

        String identifier = registerRequestDTO.getIdentifier();

        User user =  new User();

        if(AuthHelper.isEmail(identifier)) {
            if(authRepo.existsByEmail(identifier)) {
                throw new ConflictException("Email already exists");
            }

            user.setEmail(identifier);
        } else if (AuthHelper.isPhoneNo(identifier)) {
            if(authRepo.existsByPhoneNo(identifier)) {
                throw new ConflictException("Phone no already exists");
            }

            user.setPhoneNo(identifier);
        } else {
            throw new BadRequestException("Invalid Email or Phone Number");
        }

        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        authRepo.save(user);

        return "Registered Successfully : " + registerRequestDTO.getIdentifier();
    }
}
