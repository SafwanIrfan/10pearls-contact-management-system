package com._pearls.contactms.repo;

import com._pearls.contactms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepo extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNo(String phoneNo);
}
