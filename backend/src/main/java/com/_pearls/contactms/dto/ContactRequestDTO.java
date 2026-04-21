package com._pearls.contactms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ContactRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Title is required")
    private String title;

    @Email(message = "Email should be valid")
    private String email;

    @Column(unique = true)
    @NotBlank(message = "Phone no is required")
    private String phone;

    public @NotBlank(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last Name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last Name is required") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    public @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Phone no is required") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "Phone no is required") String phone) {
        this.phone = phone;
    }
}
