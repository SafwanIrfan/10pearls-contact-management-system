package com._pearls.contactms.service;

import com._pearls.contactms.exception.CsvProcessingException;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import com._pearls.contactms.repo.ContactRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactExportImportServiceTest {

    @Mock
    ContactRepo contactRepo;

    @InjectMocks
    ContactExportImportService exportImportService;

    private Contact mockContact;

    @BeforeEach
    void setUp() {
        EmailContact email = new EmailContact();
        email.setEmail("safwan@test.com");
        email.setLabel("work");

        PhoneContact phone = new PhoneContact();
        phone.setPhone("+923001234567");
        phone.setLabel("mobile");

        mockContact = new Contact();
        mockContact.setId(1L);
        mockContact.setFirstName("Safwan");
        mockContact.setLastName("Irfan");
        mockContact.setTitle("Software Engineer");
        mockContact.setEmail(new ArrayList<>(List.of(email)));
        mockContact.setPhone(new ArrayList<>(List.of(phone)));
    }

    // exportContactsToCSV()
    @Test
    @DisplayName("exportContactsToCSV() → returns CSV bytes with header and contact data")
    void exportContactsToCSV_returnsCorrectCsvBytes() {
        when(contactRepo.findAll()).thenReturn(List.of(mockContact));

        byte[] result = exportImportService.exportContactsToCSV();
        String csv = new String(result, StandardCharsets.UTF_8);

        assertThat(csv).startsWith("firstName,lastName,title,emails,phones");
        assertThat(csv).contains("Safwan");
        assertThat(csv).contains("Irfan");
        assertThat(csv).contains("safwan@test.com:work");
        assertThat(csv).contains("+923001234567:mobile");
    }

    @Test
    @DisplayName("exportContactsToCSV() → returns only header when no contacts exist")
    void exportContactsToCSV_noContacts_returnsHeaderOnly() {
        when(contactRepo.findAll()).thenReturn(List.of());

        byte[] result = exportImportService.exportContactsToCSV();
        String csv = new String(result, StandardCharsets.UTF_8);

        assertThat(csv).isEqualTo("firstName,lastName,title,emails,phones\n");
    }

    @Test
    @DisplayName("exportContactsToCSV() → handles contact with no emails or phones")
    void exportContactsToCSV_contactWithNoEmailsOrPhones_returnsCsvWithEmptyFields() {
        mockContact.setEmail(new ArrayList<>());
        mockContact.setPhone(new ArrayList<>());
        when(contactRepo.findAll()).thenReturn(List.of(mockContact));

        byte[] result = exportImportService.exportContactsToCSV();
        String csv = new String(result, StandardCharsets.UTF_8);

        assertThat(csv).contains("Safwan,Irfan,Software Engineer,,");
    }

    
    // importContactsFromCSV()
    @Test
    @DisplayName("importContactsFromCSV() → saves contacts from valid CSV")
    void importContactsFromCSV_validCsv_savesContacts() {
        String csvContent = """
                firstName,lastName,title,emails,phones
                Safwan,Irfan,Software Engineer,safwan@test.com:work,+923001234567:mobile
                """;
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        exportImportService.importContactsFromCSV(file);

        verify(contactRepo, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("importContactsFromCSV() → skips blank lines in CSV")
    void importContactsFromCSV_withBlankLines_skipsThemAndSavesValidOnes() {
        String csvContent = """
                firstName,lastName,title,emails,phones
                Safwan,Irfan,Software Engineer,,
                
                Arsalan,Irfan,Backend Engineer,,
                """;
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        exportImportService.importContactsFromCSV(file);

        verify(contactRepo, times(2)).save(any(Contact.class));
    }

    @Test
    @DisplayName("importContactsFromCSV() → skips header row only")
    void importContactsFromCSV_headerOnly_savesNothing() {
        String csvContent = "firstName,lastName,title,emails,phones\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        exportImportService.importContactsFromCSV(file);

        verify(contactRepo, never()).save(any());
    }

    @Test
    @DisplayName("importContactsFromCSV() → throws RuntimeException for empty file")
    void importContactsFromCSV_emptyFile_throwsRuntimeException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv", new byte[0]
        );

        assertThatThrownBy(() -> exportImportService.importContactsFromCSV(file))
                .isInstanceOf(CsvProcessingException.class)
                .hasMessageContaining("File is empty");
    }

    @Test
    @DisplayName("importContactsFromCSV() → throws RuntimeException for non-CSV file")
    void importContactsFromCSV_nonCsvFile_throwsRuntimeException() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.txt", "text/plain",
                "some content".getBytes()
        );

        assertThatThrownBy(() -> exportImportService.importContactsFromCSV(file))
                .isInstanceOf(CsvProcessingException.class)
                .hasMessageContaining("Only CSV files are supported");
    }
}
