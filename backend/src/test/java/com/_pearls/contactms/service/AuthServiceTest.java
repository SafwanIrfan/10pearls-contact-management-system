package com._pearls.contactms.service;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.exception.ConflictException;
import com._pearls.contactms.exception.UnauthorizedException;
import com._pearls.contactms.model.User;
import com._pearls.contactms.repo.AuthRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    AuthRepo authRepo;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    AuthService authService;

    private LoginRequestDTO validLoginRequest;
    private RegisterRequestDTO validEmailRegisterRequest;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequestDTO("safwan123@gmail.com", "safwan@123");
    }

    // authenticate() — Happy Path
    @Test
    @DisplayName("authenticate() -> returns JWT token when credentials are valid")
    void authenticate_validCredentials_returnsToken() {
        // Arrange
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(jwtService.generateToken("safwan123@gmail.com"))
                .thenReturn("mocked.jwt.token");

        // Act
        String token = authService.authenticate(validLoginRequest);

        // Assert
        assertThat(token).isEqualTo("mocked.jwt.token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("safwan123@gmail.com");
    }

    // authenticate() — Edge Case: Invalid Credentials

    @Test
    @DisplayName("authenticate() -> throws BadCredentialsException when password is wrong")
    void authenticate_wrongPassword_throwsBadCredentialsException() {
        // Arrange — AuthenticationManager throws when credentials don't match
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UnauthorizedException("Bad credentials"));

        // Act & Assert
        assertThatThrownBy(() -> authService.authenticate(validLoginRequest))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Bad credentials");

        // jwtService should never be called if authentication fails
        verifyNoInteractions(jwtService);
    }

    // authenticate() — Edge Case: JWT Token Validation
    @Test
    @DisplayName("authenticate() -> token generated uses the correct identifier")
    void authenticate_validCredentials_tokenGeneratedWithCorrectIdentifier() {
        // Ensures jwtService receives the identifier from the request, not a hardcoded value
        Authentication mockAuth = mock(Authentication.class);
        LoginRequestDTO phoneLoginRequest = new LoginRequestDTO("03001234567", "password123");

        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(jwtService.generateToken("03001234567")).thenReturn("phone.jwt.token");

        String token = authService.authenticate(phoneLoginRequest);

        assertThat(token).isEqualTo("phone.jwt.token");
        verify(jwtService).generateToken("03001234567");
    }

    // register() — Happy Path (Phone Number)
    @Test
    @DisplayName("register() -> saves user and returns success message for valid phone number")
    void register_validPhoneNumber_savesUserAndReturnsMessage() {
        // Arrange
        RegisterRequestDTO phoneRequest = new RegisterRequestDTO("03001234789", "password123");
        when(authRepo.existsByPhoneNo("03001234789")).thenReturn(false);
        when(jwtService.generateToken("03001234789")).thenReturn("mocked.jwt.token");

        // Act
        String result = authService.register(phoneRequest);

        // Assert
        assertThat(result).isEqualTo("mocked.jwt.token");
        verify(authRepo).save(any(User.class));
    }

    // register() — Happy Path (Email)
    @Test
    @DisplayName("register() -> saves user and returns success message for valid email")
    void register_validEmail_savesUserAndReturnsMessage() {
        // Arrange
        RegisterRequestDTO emailRequest = new RegisterRequestDTO("safwan@test.com", "password123");
        when(authRepo.existsByEmail("safwan@test.com")).thenReturn(false);
        when(jwtService.generateToken("safwan@test.com")).thenReturn("mocked.jwt.token");


        // Act
        String result = authService.register(emailRequest);

        // Assert
        assertThat(result).isEqualTo("mocked.jwt.token");
        verify(authRepo).save(any(User.class));
        verify(jwtService).generateToken("safwan@test.com");
    }

    // register() — Edge Case: Duplicate Email
    @Test
    @DisplayName("register() -> throws ConflictException when email already exists")
    void register_duplicateEmail_throwsConflictException() {

        RegisterRequestDTO duplicateEmailRequest = new RegisterRequestDTO("safwan@test.com", "password123");
        when(authRepo.existsByEmail("safwan@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(duplicateEmailRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Email already exists");

        verify(authRepo, never()).save(any());
    }

    // register() — Edge Case: Duplicate Phone
    @Test
    @DisplayName("register() -> throws ConflictException when phone number already exists")
    void register_duplicatePhone_throwsConflictException() {
        RegisterRequestDTO duplicatePhoneRequest = new RegisterRequestDTO("03001234789", "password123");
        when(authRepo.existsByPhoneNo("03001234789")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(duplicatePhoneRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Phone no already exists");

        verify(authRepo, never()).save(any());
    }

    // register() — Edge Case: Invalid Identifier
    @Test
    @DisplayName("register() -> throws BadRequestException for invalid identifier (not email or phone)")
    void register_invalidIdentifier_throwsBadRequestException() {
        RegisterRequestDTO badRequest = new RegisterRequestDTO("invalid_email_or_phone", "password123");

        assertThatThrownBy(() -> authService.register(badRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid Email or Phone Number");

        verify(authRepo, never()).save(any());
    }

}
