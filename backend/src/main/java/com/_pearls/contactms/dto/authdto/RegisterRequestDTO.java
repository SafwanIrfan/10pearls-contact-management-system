package com._pearls.contactms.dto.authdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank(message = "Email or Phone no is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have atleast 6 digit")
    private String password;

    public @NotBlank(message = "Email or Phone no is required") String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank(message = "Email or Phone no is required") String identifier) {
        this.identifier = identifier;
    }

    public @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must have atleast 6 digit") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must have atleast 6 digit") String password) {
        this.password = password;
    }
}
