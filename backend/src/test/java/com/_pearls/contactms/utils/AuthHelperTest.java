package com._pearls.contactms.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class AuthHelperTest {

    @Test
    @DisplayName("isEmail() -> returns true for valid email")
    void isEmail_validEmail_returnsTrue() {
        assertThat(AuthHelper.isEmail("user@example.com")).isTrue();
    }

    @Test
    @DisplayName("isEmail() -> returns false for phone number")
    void isEmail_phoneNumber_returnsFalse() {
        assertThat(AuthHelper.isEmail("03001234567")).isFalse();
    }

    @Test
    @DisplayName("isPhoneNo() -> returns true for valid phone")
    void isPhoneNo_validPhone_returnsTrue() {
        assertThat(AuthHelper.isPhoneNo("03001234567")).isTrue();
    }

    @Test
    @DisplayName("isPhoneNo() -> returns false for email")
    void isPhoneNo_email_returnsFalse() {
        assertThat(AuthHelper.isPhoneNo("user@example.com")).isFalse();
    }

    @Test
    @DisplayName("isPhoneNo() -> returns false for invalid format")
    void isPhoneNo_invalidFormat_returnsFalse() {
        assertThat(AuthHelper.isPhoneNo("+923301234567")).isFalse();
    }
}
