package com._pearls.contactms.controller;

import com._pearls.contactms.dto.ContactRequestDTO;
import com._pearls.contactms.dto.ContactResponseDTO;
import com._pearls.contactms.dto.PaginatedResponseDTO;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Contact", description = "API for managing Contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    @Operation(summary = "Get Contacts")
    public ResponseEntity<PaginatedResponseDTO<Contact>> getPaginatedContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        PaginatedResponseDTO<Contact> contacts = contactService.getPaginatedContacts(page,size);
        return ResponseEntity.ok().body(contacts);
    }

    @PostMapping("/contact/add")
    @Operation(summary = "Create a new Contact")
    public ResponseEntity<ContactResponseDTO> addContact(@Validated({Default.class}) @RequestBody ContactRequestDTO newContact) {

        ContactResponseDTO contact = contactService.addContact(newContact);
        return ResponseEntity.ok().body(contact);
    }

    @PutMapping("/contact/update/{id}")
    @Operation(summary = "Update a Contact")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable Long id,
                                                            @Validated({Default.class}) @RequestBody ContactRequestDTO updatedContact) {

        ContactResponseDTO contactResponseDTO = contactService.updateContact(id, updatedContact);

        return ResponseEntity.ok().body(contactResponseDTO);

    }
}
