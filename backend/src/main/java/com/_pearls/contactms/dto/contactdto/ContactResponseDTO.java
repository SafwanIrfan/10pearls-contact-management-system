package com._pearls.contactms.dto.contactdto;

import com._pearls.contactms.dto.emaildto.EmailResponseDTO;
import com._pearls.contactms.dto.phonedto.PhoneResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ContactResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private List<EmailResponseDTO> emails;
    private List<PhoneResponseDTO> phones;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<EmailResponseDTO> getEmails() {
        return emails;
    }
    public void setEmails(List<EmailResponseDTO> emails) {
        this.emails = emails;
    }

    public List<PhoneResponseDTO> getPhones() {
        return phones;
    }
    public void setPhones(List<PhoneResponseDTO> phones) {
        this.phones = phones;
    }
}
