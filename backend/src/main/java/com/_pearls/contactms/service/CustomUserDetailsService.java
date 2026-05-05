package com._pearls.contactms.service;

import com._pearls.contactms.exception.NotFoundException;
import com._pearls.contactms.model.User;
import com._pearls.contactms.repo.AuthRepo;
import com._pearls.contactms.utils.AuthHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepo authRepo;

    public CustomUserDetailsService(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws NotFoundException {

        User user;

        if (AuthHelper.isEmail(identifier)) {
            user = authRepo.findByEmail(identifier)
                    .orElseThrow(() -> new NotFoundException("User not found with email"));

        } else if (AuthHelper.isPhoneNo(identifier)) {
            user = authRepo.findByPhoneNo(identifier)
                    .orElseThrow(() -> new NotFoundException("User not found with phone number"));

        } else {
            throw new NotFoundException("Invalid email or phone number");
        }

        return new org.springframework.security.core.userdetails.User(
                identifier,                 // username (we use identifier)
                user.getPassword(),         // encoded password from DB
                new ArrayList<>()           // roles/authorities (empty for now)
        );
    }
}
