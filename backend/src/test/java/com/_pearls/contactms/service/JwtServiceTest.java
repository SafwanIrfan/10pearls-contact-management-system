package com._pearls.contactms.service;

import com._pearls.contactms.exception.JwtInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private String validBase64Secret;
    private String generatedToken;

    @BeforeEach
    void setUp() {
        validBase64Secret = Base64.getEncoder().encodeToString(
                new byte[32]
        );
        jwtService = new JwtService(validBase64Secret);
        generatedToken = jwtService.generateToken("safwan@test.com");
    }

    // Constructor
    @Test
    @DisplayName("JwtService() → throws JwtInitializationException for invalid secret")
    void constructor_invalidSecret_throwsJwtInitializationException() {
        assertThatThrownBy(() -> new JwtService("not_valid_base64"))
                .isInstanceOf(JwtInitializationException.class)
                .hasMessageContaining("Failed to initialize JWT secret key");
    }

    @Test
    @DisplayName("JwtService() → initializes successfully with valid secret")
    void constructor_validSecret_initializesSuccessfully() {
        assertThatCode(() -> new JwtService(validBase64Secret)).doesNotThrowAnyException();
    }

    // generateToken()
    @Test
    @DisplayName("generateToken() → returns non-null token")
    void generateToken_returnsNonNullToken() {
        assertThat(generatedToken).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("generateToken() → token contains correct identifier as subject")
    void generateToken_tokenContainsCorrectIdentifier() {
        String identifier = jwtService.extractIdentifier(generatedToken);
        assertThat(identifier).isEqualTo("safwan@test.com");
    }

    // extractIdentifier()
    @Test
    @DisplayName("extractIdentifier() → returns correct identifier from token")
    void extractIdentifier_returnsCorrectIdentifier() {
        assertThat(jwtService.extractIdentifier(generatedToken)).isEqualTo("safwan@test.com");
    }

    // extractExpiration()
    @Test
    @DisplayName("extractExpiration() → returns future expiration date")
    void extractExpiration_returnsFutureDate() {
        Date expiration = jwtService.extractExpiration(generatedToken);
        assertThat(expiration).isAfter(new Date());
    }

    // isTokenExpired()
    @Test
    @DisplayName("isTokenExpired() → returns false for freshly generated token")
    void isTokenExpired_freshToken_returnsFalse() {
        assertThat(jwtService.isTokenExpired(generatedToken)).isFalse();
    }

    // validateToken()
    @Test
    @DisplayName("validateToken() → returns true when token matches UserDetails")
    void validateToken_validTokenAndUser_returnsTrue() {
        UserDetails userDetails = User.withUsername("safwan@test.com")
                .password("password123")
                .authorities(Collections.emptyList())
                .build();

        assertThat(jwtService.validateToken(generatedToken, userDetails)).isTrue();
    }

    @Test
    @DisplayName("validateToken() → returns false when username does not match token")
    void validateToken_wrongUsername_returnsFalse() {
        UserDetails differentUser = User.withUsername("other@test.com")
                .password("password321")
                .authorities(Collections.emptyList())
                .build();

        assertThat(jwtService.validateToken(generatedToken, differentUser)).isFalse();
    }

    // getKey()
    @Test
    @DisplayName("getKey() → returns non-null secret key")
    void getKey_returnsNonNullKey() {
        assertThat(jwtService.getKey()).isNotNull();
    }
}