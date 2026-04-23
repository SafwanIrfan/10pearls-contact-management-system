package com._pearls.contactms.dto;

import jakarta.validation.constraints.NotBlank;

public class PhoneRequestDTO {

    @NotBlank
    private String phone;

    @NotBlank
    private String label;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
