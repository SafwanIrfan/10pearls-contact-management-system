package com._pearls.contactms.controller;

import com._pearls.contactms.exception.CsvProcessingException;
import com._pearls.contactms.service.ContactExportImportService;
import com._pearls.contactms.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContactExportImportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactExportImportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsService userDetailsService;

    @MockitoBean
    ContactExportImportService exportImportService;

    // GET /contacts/export
    @Test
    @DisplayName("GET /contacts/export → 200 OK with CSV bytes and correct headers")
    void exportContacts_returns200WithCsvBytes() throws Exception {
        byte[] csvBytes = "firstName,lastName,title,emails,phones\nSafwan,Irfan,Software Engineer,,\n".getBytes();
        when(exportImportService.exportContactsToCSV()).thenReturn(csvBytes);

        mockMvc.perform(get("/contacts/export"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("attachment; filename=")))
                .andExpect(content().bytes(csvBytes));
    }

    @Test
    @DisplayName("GET /contacts/export → 200 OK with empty CSV when no contacts exist")
    void exportContacts_noContacts_returnsHeaderOnly() throws Exception {
        byte[] csvBytes = "firstName,lastName,title,emails,phones\n".getBytes();
        when(exportImportService.exportContactsToCSV()).thenReturn(csvBytes);

        mockMvc.perform(get("/contacts/export"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(csvBytes));
    }

    // POST /contacts/import
    @Test
    @DisplayName("POST /contacts/import → 200 OK when valid CSV file uploaded")
    void importContacts_validCsvFile_returns200() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv",
                "firstName,lastName,title,emails,phones\nSafwan,Irfan,Software Engineer,,\n".getBytes()
        );
        doNothing().when(exportImportService).importContactsFromCSV(any());

        mockMvc.perform(multipart("/contacts/import").file(file))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /contacts/import → 422 when service throws CsvProcessingException")
    void importContacts_serviceThrows_returns422() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "contacts.csv", "text/csv",
                "bad content".getBytes()
        );
        doThrow(new CsvProcessingException("Failed to read CSV file."))
                .when(exportImportService).importContactsFromCSV(any());

        mockMvc.perform(multipart("/contacts/import").file(file))
                .andExpect(status().is(422));
    }
}