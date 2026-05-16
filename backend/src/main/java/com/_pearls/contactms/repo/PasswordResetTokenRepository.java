package com._pearls.contactms.repo;

import com._pearls.contactms.model.PasswordResetToken;
import com._pearls.contactms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    // Clean up old tokens for a user before issuing a new one
    void deleteAllByUser(User user);
}
