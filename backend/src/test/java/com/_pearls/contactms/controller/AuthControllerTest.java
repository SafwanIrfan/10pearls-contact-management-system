package com._pearls.contactms.controller;

import com._pearls.contactms.dto.authdto.LoginRequestDTO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    // ─────────────────────────────────────────────
    // Happy Path
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/login → 200 OK with JWT token when credentials are valid")
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
}
