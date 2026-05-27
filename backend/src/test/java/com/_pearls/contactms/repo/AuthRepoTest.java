package com._pearls.contactms.repo;

import com._pearls.contactms.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class AuthRepoTest {

    @Autowired
    AuthRepo authRepo;

    @BeforeEach
    void setUp() {
        authRepo.deleteAll(); // clean slate before each test

        User userWithEmail = new User();
        userWithEmail.setEmail("safwan_exists@example.com");
        userWithEmail.setPassword("password123");
        authRepo.save(userWithEmail);

        User userWithPhone = new User();
        userWithPhone.setPhoneNo("03001234567");
        userWithPhone.setPassword("password123");
        authRepo.save(userWithPhone);
    }

    // existsByEmail()
    @Test
    @DisplayName("existsByEmail() -> returns true when email exists in DB")
    void existsByEmail_emailExists_returnsTrue() {
        assertThat(authRepo.existsByEmail("safwan_exists@example.com")).isTrue();
    }

    @Test
    @DisplayName("existsByEmail() -> returns false when email does not exist in DB")
    void existsByEmail_emailNotFound_returnsFalse() {
        assertThat(authRepo.existsByEmail("safwan_not_exists@example.com")).isFalse();
    }

    // existsByPhoneNo()
    @Test
    @DisplayName("existsByPhoneNo() -> returns true when phone number exists in DB")
    void existsByPhoneNo_phoneExists_returnsTrue() {
        assertThat(authRepo.existsByPhoneNo("03001234567")).isTrue();
    }

    @Test
    @DisplayName("existsByPhoneNo() -> returns false when phone number does not exist in DB")
    void existsByPhoneNo_phoneNotFound_returnsFalse() {
        assertThat(authRepo.existsByPhoneNo("00000000000")).isFalse();
    }

    // findByEmail()
    @Test
    @DisplayName("findByEmail() -> returns user when email exists")
    void findByEmail_emailExists_returnsUser() {
        Optional<User> result = authRepo.findByEmail("safwan_exists@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("safwan_exists@example.com");
    }

    @Test
    @DisplayName("findByEmail() -> returns empty Optional when email not found")
    void findByEmail_emailNotFound_returnsEmpty() {
        Optional<User> result = authRepo.findByEmail("safwan_not_found@example.com");

        assertThat(result).isEmpty();
    }

    // findByPhoneNo()
    @Test
    @DisplayName("findByPhoneNo() -> returns user when phone number exists")
    void findByPhoneNo_phoneExists_returnsUser() {
        Optional<User> result = authRepo.findByPhoneNo("03001234567");

        assertThat(result).isPresent();
        assertThat(result.get().getPhoneNo()).isEqualTo("03001234567");
    }

    @Test
    @DisplayName("findByPhoneNo() -> returns empty Optional when phone not found")
    void findByPhoneNo_phoneNotFound_returnsEmpty() {
        Optional<User> result = authRepo.findByPhoneNo("00000000000");

        assertThat(result).isEmpty();
    }
}