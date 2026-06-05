package com._pearls.contactms.service;

import com._pearls.contactms.model.Contact;
import com._pearls.contactms.repo.ContactRepo;
import com._pearls.contactms.utils.CsvHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ContactExportImportService {

    private final ContactRepo contactRepo;
    private static final Logger log = LoggerFactory.getLogger(ContactExportImportService.class);

    public ContactExportImportService(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    public byte[] exportContactsToCSV() {
        List<Contact> contacts = contactRepo.findAll();
        StringBuilder csv = new StringBuilder(CsvHelper.CSV_HEADER);

        for (Contact contact : contacts) {
            csv.append(CsvHelper.escapeCsv(contact.getFirstName())).append(",")
                    .append(CsvHelper.escapeCsv(contact.getLastName())).append(",")
                    .append(CsvHelper.escapeCsv(contact.getTitle())).append(",")
                    .append(CsvHelper.escapeCsv(CsvHelper.formatEmails(contact.getEmail()))).append(",")
                    .append(CsvHelper.escapeCsv(CsvHelper.formatPhones(contact.getPhone()))).append("\n");
        }

        log.info("Exported {} contacts to CSV", contacts.size());
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public void importContactsFromCSV(MultipartFile file) {
        CsvHelper.validateCsvFile(file);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;
            int importedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                } else if (!line.isBlank()) {
                    contactRepo.save(CsvHelper.parseCSVLine(line));
                    importedCount++;
                }
            }

            log.info("Imported {} contacts from CSV", importedCount);

        } catch (IOException e) {
            log.error("Failed to import contacts from CSV", e);
            throw new RuntimeException("Failed to read CSV file.");
        }
    }
}