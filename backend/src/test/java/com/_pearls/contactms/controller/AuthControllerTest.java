package com._pearls.contactms.controller;

import com._pearls.contactms.dto.authdto.ChangePasswordRequestDTO;
import com._pearls.contactms.dto.authdto.LoginRequestDTO;
import com._pearls.contactms.dto.authdto.RegisterRequestDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.exception.ConflictException;
import com._pearls.contactms.exception.UnauthorizedException;
import com._pearls.contactms.service.AuthService;
import com._pearls.contactms.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  // disables security filters
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    JwtService jwtService;  // add this

    @Autowired
    ObjectMapper objectMapper;

    // Happy Path
    @Test
    @DisplayName("POST /auth/login -> 200 OK with JWT token when credentials are valid")
    void login_validCredentials_returns200WithToken() throws Exception {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("safwan123@gmail.com", "safwan@123");
        when(authService.authenticate(any(LoginRequestDTO.class)))
                .thenReturn("mocked.jwt.token");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"));
    }

    // Edge Case: Invalid Credentials
    @Test
    @DisplayName("POST /auth/login -> 401 UNAUTHORIZED when credentials are wrong")
    void login_invalidCredentials_returns401() throws Exception {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("user@example.com", "wrongPassword");
        when(authService.authenticate(any(LoginRequestDTO.class)))
                .thenThrow(new UnauthorizedException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // Edge Case: User Not Found
    @Test
    @DisplayName("POST /auth/login -> 401 UNAUTHORIZED when user does not exist")
    void login_userNotFound_returns401() throws Exception {
        // Arrange — UserDetailsService throws UsernameNotFoundException internally,
        // which Spring Security wraps as BadCredentialsException by default.
        LoginRequestDTO request = new LoginRequestDTO("arslan@gmail.com", "password1234");
        when(authService.authenticate(any(LoginRequestDTO.class)))
                .thenThrow(new UnauthorizedException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // Edge Case: Empty Body
    @Test
    @DisplayName("POST /auth/login -> 400 BAD REQUEST when request body is missing")
    void login_emptyRequestBody_returns400() throws Exception {
        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().is4xxClientError());
    }

    // register() — Happy Path
    @Test
    @DisplayName("POST /auth/register → 200 OK when valid email")
    void register_validEmail_returns200() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("safwan@test.com", "password123");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"));
    }

    @Test
    @DisplayName("POST /auth/register → 200 OK when valid phone number")
    void register_validPhone_returns200() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("03001234567", "password123");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"));
    }

    // register() — Edge Case: Duplicate Email
    @Test
    @DisplayName("POST /auth/register → 409 CONFLICT when email already exists")
    void register_duplicateEmail_returns409() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("safwan@test.com", "password123");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new ConflictException("Email already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // register() — Edge Case: Duplicate Phone
    @Test
    @DisplayName("POST /auth/register → 409 CONFLICT when phone already exists")
    void register_duplicatePhone_returns409() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("03001234567", "password123");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new ConflictException("Phone no already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // register() — Edge Case: Invalid Identifier
    @Test
    @DisplayName("POST /auth/register → 400 BAD REQUEST when identifier is invalid")
    void register_invalidIdentifier_returns400() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("invalid_input", "password123");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new BadRequestException("Invalid Email or Phone Number"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // register() — Edge Case: Password limit
    @Test
    @DisplayName("POST /auth/register → 400 when password is less than 6 characters")
    void register_shortPassword_returns400() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("safwan@test.com", "pass");
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new BadRequestException("Password must have atleast 6 digit"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // Happy Path
    @Test
    @DisplayName("PUT /auth/password/update → 200 OK when password updated successfully")
    void updatePassword_validRequest_returns200() throws Exception {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setOldPassword("oldPass123");
        request.setNewPassword("newPass123");

        doNothing().when(authService).updatePassword(any(ChangePasswordRequestDTO.class));

        mockMvc.perform(put("/auth/password/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // Edge Case: Invalid current password
    @Test
    @DisplayName("PUT /auth/password/update → 401 when old password is wrong")
    void updatePassword_wrongOldPassword_returns401() throws Exception {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setOldPassword("wrongPass");
        request.setNewPassword("newPass123");

        doThrow(new UnauthorizedException("Invalid current password"))
                .when(authService).updatePassword(any(ChangePasswordRequestDTO.class));

        mockMvc.perform(put("/auth/password/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

}
