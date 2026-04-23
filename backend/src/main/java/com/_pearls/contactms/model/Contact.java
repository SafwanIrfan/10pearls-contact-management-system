package com._pearls.contactms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailContact> email;

    @OneToMany(mappedBy = "contact",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneContact> phone;

    public @NotNull LocalDateTime getCreatedAt() { return createdAt;}

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {this.createdAt = createdAt;}

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    public @NotNull String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EmailContact> getEmail() {
        return email;
    }

    public void setEmail(List<EmailContact> email) {
        this.email = email;
    }

    public List<PhoneContact> getPhone() {
        return phone;
    }

    public void setPhone(List<PhoneContact> phone) {
        this.phone = phone;
    }
}
