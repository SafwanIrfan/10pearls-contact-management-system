package com._pearls.contactms.controller;

import com._pearls.contactms.dto.contactdto.ContactRequestDTO;
import com._pearls.contactms.dto.contactdto.ContactResponseDTO;
import com._pearls.contactms.dto.contactdto.PaginatedResponseDTO;
import com._pearls.contactms.exception.BadRequestException;
import com._pearls.contactms.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController()
@RequestMapping("/contacts")
@Tag(name = "Contact", description = "API for managing Contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    @Operation(summary = "Get Contacts")
    public ResponseEntity<PaginatedResponseDTO<ContactResponseDTO>> getPaginatedContacts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
            ) {
        PaginatedResponseDTO<ContactResponseDTO> contacts = contactService.getContacts(search, page,size);
        return ResponseEntity.ok().body(contacts);
    }

    @GetMapping("/contact/{id}")
    @Operation(summary = "Get Contact")
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid Contact ID");
        }
        ContactResponseDTO contact = contactService.getContactById(id);
        return ResponseEntity.ok().body(contact);
    }

    @PostMapping("/contact/add")
    @Operation(summary = "Create a new Contact")
    public ResponseEntity<ContactResponseDTO> addContact(@Validated({Default.class}) @RequestBody ContactRequestDTO newContact) {
        ContactResponseDTO contact = contactService.addContact(newContact);
        return ResponseEntity.ok().body(contact);
    }

    @PostMapping("/add")
    @Operation(summary = "Create new Contacts")
    public ResponseEntity<List<ContactResponseDTO>> addContact(@Validated({Default.class}) @RequestBody List<ContactRequestDTO> newContacts) {
        List<ContactResponseDTO> contacts = contactService.addContacts(newContacts);
        return ResponseEntity.ok().body(contacts);
    }

    @PutMapping("/contact/update/{id}")
    @Operation(summary = "Update a Contact")
    public ResponseEntity<ContactResponseDTO> updateContact(@PathVariable Long id,
                                                            @Validated({Default.class}) @RequestBody ContactRequestDTO updatedContact) {

        ContactResponseDTO contactResponseDTO = contactService.updateContact(id, updatedContact);
        return ResponseEntity.ok().body(contactResponseDTO);
    }

    @DeleteMapping("/contact/delete/{id}")
    @Operation(summary = "Deletes a contact")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        //returns 204 response which tells no content
        return ResponseEntity.noContent().build();
    }
}

