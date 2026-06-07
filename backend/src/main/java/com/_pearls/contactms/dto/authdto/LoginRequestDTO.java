package com._pearls.contactms.dto.authdto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    public LoginRequestDTO(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    @NotBlank(message = "Email or Phone no is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    public @NotBlank(message = "Email or Phone no is required") String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank(message = "Email or Phone no is required") String identifier) {
        this.identifier = identifier;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }
}
