package com._pearls.contactms.dto;

import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ContactRequestDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Title is required")
    private String title;

    private List<EmailRequestDTO> emails;

    private List<PhoneRequestDTO> phones;

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

    public List<EmailRequestDTO> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailRequestDTO> emails) {
        this.emails = emails;
    }

    public List<PhoneRequestDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneRequestDTO> phones) {
        this.phones = phones;
    }
}
