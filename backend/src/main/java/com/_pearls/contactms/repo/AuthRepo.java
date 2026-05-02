package com._pearls.contactms.repo;

import com._pearls.contactms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepo extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNo(String phoneNo);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNo(String phoneNo);
}
