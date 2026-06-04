package com._pearls.contactms.utils;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvHelper {

    private CsvHelper() {}

    public static final String CSV_HEADER = "firstName,lastName,title,emails,phones\n";

    public static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static String unquote(String value) {
        if (value == null) return "";
        return value.trim().replaceAll("^\"|\"$", "");
    }

    public static String cleanField(String[] fields, int index) {
        if (fields.length <= index) return "";
        return unquote(fields[index]).trim();
    }

    public static String[] splitCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    public static void validateCsvFile(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("File is empty.");
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".csv")) {
            throw new RuntimeException("Only CSV files are supported.");
        }
    }

    public static String formatEmails(List<EmailContact> emails) {
        if (emails == null || emails.isEmpty()) return "";
        return emails.stream()
                .map(e -> e.getEmail() + ":" + e.getLabel())
                .collect(Collectors.joining("|"));
    }

    public static String formatPhones(List<PhoneContact> phones) {
        if (phones == null || phones.isEmpty()) return "";
        return phones.stream()
                .map(p -> p.getPhone() + ":" + p.getLabel())
                .collect(Collectors.joining("|"));
    }

    public static Contact parseCSVLine(String line) {
        String[] fields = splitCsvLine(line);

        Contact contact = new Contact();
        contact.setFirstName(cleanField(fields, 0));
        contact.setLastName(cleanField(fields,  1));
        contact.setTitle(cleanField(fields,     2));

        if (fields.length > 3 && !fields[3].isBlank()) {
            contact.setEmail(parseEmails(unquote(fields[3]), contact));
        }

        if (fields.length > 4 && !fields[4].isBlank()) {
            contact.setPhone(parsePhones(unquote(fields[4]), contact));
        }

        return contact;
    }

    public static List<EmailContact> parseEmails(String raw, Contact contact) {
        return Arrays.stream(raw.split("\\|"))
                .filter(e -> e.contains(":"))
                .map(e -> {
                    String[] parts = e.split(":", 2);
                    EmailContact email = new EmailContact();
                    email.setEmail(parts[0].trim());
                    email.setLabel(parts[1].trim());
                    email.setContact(contact);
                    return email;
                })
                .collect(Collectors.toList());
    }

    public static List<PhoneContact> parsePhones(String raw, Contact contact) {
        return Arrays.stream(raw.split("\\|"))
                .filter(p -> p.contains(":"))
                .map(p -> {
                    String[] parts = p.split(":", 2);
                    PhoneContact phone = new PhoneContact();
                    phone.setPhone(parts[0].trim());
                    phone.setLabel(parts[1].trim());
                    phone.setContact(contact);
                    return phone;
                })
                .collect(Collectors.toList());
    }
}