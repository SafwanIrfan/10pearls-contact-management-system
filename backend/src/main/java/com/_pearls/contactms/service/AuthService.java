package com._pearls.contactms.service;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.exception.ConflictException;
import com._pearls.contactms.model.User;
import com._pearls.contactms.repo.AuthRepo;
import com._pearls.contactms.utils.AuthHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepo authRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthRepo authRepo, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authRepo = authRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


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

        user.setPassword(encoder.encode(registerRequestDTO.getPassword()));
        authRepo.save(user);

        return "Registered Successfully : " + registerRequestDTO.getIdentifier();
    }

    public String authenticate(LoginRequestDTO loginRequestDTO) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getIdentifier(), loginRequestDTO.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(loginRequestDTO.getIdentifier());

        return "Invalid Credentials";
    }
}
