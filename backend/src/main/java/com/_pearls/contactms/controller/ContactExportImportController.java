package com._pearls.contactms.controller;

import com._pearls.contactms.service.ContactExportImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/contacts")
@Tag(name = "Contact Export/Import")
public class ContactExportImportController {

    private final ContactExportImportService exportImportService;

    public ContactExportImportController(ContactExportImportService exportImportService) {
        this.exportImportService = exportImportService;
    }

    @GetMapping("/export")
    @Operation(summary = "Export all contacts as CSV")
    public ResponseEntity<byte[]> exportContacts() {
        byte[] csvBytes = exportImportService.exportContactsToCSV();
        String filename = "contacts_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @PostMapping("/import")
    @Operation(summary = "Import contacts from CSV")
    public ResponseEntity<Void> importContacts(@RequestParam("file") MultipartFile   file) {
        exportImportService.importContactsFromCSV(file);
        return ResponseEntity.ok().build();
    }
}