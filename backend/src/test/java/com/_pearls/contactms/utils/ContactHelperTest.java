package com._pearls.contactms.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class ContactHelperTest {

    //isEmail()
    @Test
    @DisplayName("isEmail() → returns true for valid email")
    void isEmail_validEmail_returnsTrue() {
        assertThat(ContactHelper.isEmail("safwan@test.com")).isTrue();
    }

    @Test
    @DisplayName("isEmail() → returns false for email without dot in domain")
    void isEmail_noDotInDomain_returnsFalse() {
        assertThat(ContactHelper.isEmail("safwan@testcom")).isFalse();
    }

    @Test
    @DisplayName("isEmail() → returns false for email with spaces")
    void isEmail_withSpaces_returnsFalse() {
        assertThat(ContactHelper.isEmail("safwan @test.com")).isFalse();
    }

    @Test
    @DisplayName("isEmail() → returns false for phone number")
    void isEmail_phoneNumber_returnsFalse() {
        assertThat(ContactHelper.isEmail("03001234567")).isFalse();
    }

    //isPhoneNo()
    @Test
    @DisplayName("isPhoneNo() → returns true for local number")
    void isPhoneNo_localNumber_returnsTrue() {
        assertThat(ContactHelper.isPhoneNo("03001234567")).isTrue();
    }

    @Test
    @DisplayName("isPhoneNo() → returns true for international number with +")
    void isPhoneNo_internationalNumber_returnsTrue() {
        assertThat(ContactHelper.isPhoneNo("+923001234567")).isTrue();
    }

    @Test
    @DisplayName("isPhoneNo() → returns false for number less than 7 digits")
    void isPhoneNo_tooShort_returnsFalse() {
        assertThat(ContactHelper.isPhoneNo("12345")).isFalse();
    }

    @Test
    @DisplayName("isPhoneNo() → returns false for email")
    void isPhoneNo_email_returnsFalse() {
        assertThat(ContactHelper.isPhoneNo("safwan@test.com")).isFalse();
    }

    //sanitize
    @Test
    @DisplayName("sanitize() → replaces newline, carriage return and tab with underscore")
    void sanitize_maliciousInput_replacesSpecialChars() {
        assertThat(ContactHelper.sanitize("hello\nworld")).isEqualTo("hello_world");
        assertThat(ContactHelper.sanitize("hello\r")).isEqualTo("hello_");
        assertThat(ContactHelper.sanitize("hello\tworld")).isEqualTo("hello_world");
    }

    @Test
    @DisplayName("sanitize() → returns empty string for null")
    void sanitize_null_returnsEmpty() {
        assertThat(ContactHelper.sanitize(null)).isEmpty();
    }
}
