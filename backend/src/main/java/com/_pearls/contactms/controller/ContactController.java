package com._pearls.contactms.controller;

import com._pearls.contactms.dto.PaginatedResponseDTO;
import com._pearls.contactms.modal.Contact;
import com._pearls.contactms.service.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Contact", description = "API for managing Contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public ResponseEntity<PaginatedResponseDTO<Contact>> getPaginatedContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        PaginatedResponseDTO<Contact> contacts = contactService.getPaginatedContacts(page,size);
        return ResponseEntity.ok().body(contacts);
    }
}
