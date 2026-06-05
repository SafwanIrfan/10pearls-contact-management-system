package com._pearls.contactms.utils;

import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CsvHelperTest {

    // escapeCsv()
    @Test
    @DisplayName("escapeCsv() → returns value as-is when no special characters")
    void escapeCsv_noSpecialChars_returnsAsIs() {
        assertThat(CsvHelper.escapeCsv("Safwan")).isEqualTo("Safwan");
    }

    @Test
    @DisplayName("escapeCsv() → wraps in quotes when value contains comma")
    void escapeCsv_withComma_wrapsInQuotes() {
        assertThat(CsvHelper.escapeCsv("Irfan, Software Engineer")).isEqualTo("\"Irfan, Software Engineer\"");
    }

    @Test
    @DisplayName("escapeCsv() → escapes existing quotes")
    void escapeCsv_withQuotes_escapesQuotes() {
        assertThat(CsvHelper.escapeCsv("say \"hi\"")).isEqualTo("\"say \"\"hi\"\"\"");
    }

    @Test
    @DisplayName("escapeCsv() → returns empty string for null")
    void escapeCsv_null_returnsEmpty() {
        assertThat(CsvHelper.escapeCsv(null)).isEqualTo("");
    }

    // unquote()
    @Test
    @DisplayName("unquote() → removes surrounding quotes")
    void unquote_quotedValue_removesQuotes() {
        assertThat(CsvHelper.unquote("\"Safwan\"")).isEqualTo("Safwan");
    }

    // formatEmails()
    @Test
    @DisplayName("formatEmails() → returns pipe-separated email:label pairs")
    void formatEmails_withEmails_returnsFormattedString() {
        EmailContact e = new EmailContact();
        e.setEmail("safwan@example.com");
        e.setLabel("work");

        assertThat(CsvHelper.formatEmails(List.of(e))).isEqualTo("safwan@example.com:work");
    }

    @Test
    @DisplayName("formatEmails() → returns empty string for null list")
    void formatEmails_null_returnsEmpty() {
        assertThat(CsvHelper.formatEmails(null)).isEqualTo("");
    }

    @Test
    @DisplayName("formatEmails() → returns empty string for empty list")
    void formatEmails_emptyList_returnsEmpty() {
        assertThat(CsvHelper.formatEmails(List.of())).isEqualTo("");
    }

    // formatPhones()
    @Test
    @DisplayName("formatPhones() → returns pipe-separated phone:label pairs")
    void formatPhones_withPhones_returnsFormattedString() {
        PhoneContact p = new PhoneContact();
        p.setPhone("+923001234567");
        p.setLabel("mobile");

        assertThat(CsvHelper.formatPhones(List.of(p))).isEqualTo("+923001234567:mobile");
    }

    @Test
    @DisplayName("formatPhones() → returns empty string for null list")
    void formatPhones_null_returnsEmpty() {
        assertThat(CsvHelper.formatPhones(null)).isEqualTo("");
    }

    // validateCsvFile()
    @Test
    @DisplayName("validateCsvFile() → throws RuntimeException for empty file")
    void validateCsvFile_emptyFile_throwsRuntimeException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> CsvHelper.validateCsvFile(file))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("File is empty");
    }

    @Test
    @DisplayName("validateCsvFile() → throws RuntimeException for non-CSV file")
    void validateCsvFile_nonCsvFile_throwsRuntimeException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.txt", "text/plain", "content".getBytes());

        assertThatThrownBy(() -> CsvHelper.validateCsvFile(file))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only CSV files are supported");
    }

    @Test
    @DisplayName("validateCsvFile() → passes for valid CSV file")
    void validateCsvFile_validCsvFile_SafwanNotThrow() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv", "content".getBytes());

        assertThatCode(() -> CsvHelper.validateCsvFile(file)).doesNotThrowAnyException();
    }

    // parseCSVLine()
    @Test
    @DisplayName("parseCSVLine() → parses contact with emails and phones")
    void parseCSVLine_fullLine_parsesCorrectly() {
        String line = "Safwan,Irfan,Software Engineer,safwan@example.com:work,+923001234567:mobile";

        Contact contact = CsvHelper.parseCSVLine(line);

        assertThat(contact.getFirstName()).isEqualTo("Safwan");
        assertThat(contact.getLastName()).isEqualTo("Irfan");
        assertThat(contact.getTitle()).isEqualTo("Software Engineer");
        assertThat(contact.getEmail()).hasSize(1);
        assertThat(contact.getEmail().getFirst().getEmail()).isEqualTo("safwan@example.com");
        assertThat(contact.getPhone()).hasSize(1);
        assertThat(contact.getPhone().getFirst().getPhone()).isEqualTo("+923001234567");
    }

    @Test
    @DisplayName("parseCSVLine() → parses contact without emails or phones")
    void parseCSVLine_noEmailsOrPhones_parsesCorrectly() {
        String line = "Safwan,Irfan,Software Engineer,,";

        Contact contact = CsvHelper.parseCSVLine(line);

        assertThat(contact.getFirstName()).isEqualTo("Safwan");
        assertThat(contact.getEmail()).isNull();
        assertThat(contact.getPhone()).isNull();
    }

    // parseEmails() & parsePhones()
    @Test
    @DisplayName("parseEmails() → parses multiple emails separated by pipe")
    void parseEmails_multipleEmails_parsesAll() {
        Contact contact = new Contact();
        List<EmailContact> emails = CsvHelper.parseEmails(
                "safwan@example.com:work|jane@example.com:personal", contact);

        assertThat(emails).hasSize(2);
        assertThat(emails.get(0).getEmail()).isEqualTo("safwan@example.com");
        assertThat(emails.get(1).getLabel()).isEqualTo("personal");
    }

    @Test
    @DisplayName("parsePhones() → parses multiple phones separated by pipe")
    void parsePhones_multiplePhones_parsesAll() {
        Contact contact = new Contact();
        List<PhoneContact> phones = CsvHelper.parsePhones(
                "+923001234567:mobile|03009876543:home", contact);

        assertThat(phones).hasSize(2);
        assertThat(phones.get(0).getPhone()).isEqualTo("+923001234567");
        assertThat(phones.get(1).getLabel()).isEqualTo("home");
    }

    @Test
    @DisplayName("parseEmails() → skips entries without colon separator")
    void parseEmails_invalidEntry_skipped() {
        Contact contact = new Contact();
        List<EmailContact> emails = CsvHelper.parseEmails("invalidemail", contact);

        assertThat(emails).isEmpty();
    }
}