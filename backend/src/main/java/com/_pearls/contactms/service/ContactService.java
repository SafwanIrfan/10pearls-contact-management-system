package com._pearls.contactms.service;

import com._pearls.contactms.dto.ContactRequestDTO;
import com._pearls.contactms.dto.ContactResponseDTO;
import com._pearls.contactms.dto.PaginatedResponseDTO;
import com._pearls.contactms.exception.ContactNotFoundException;
import com._pearls.contactms.exception.EmailAlreadyExistsException;
import com._pearls.contactms.exception.PhoneNoAlreadyExistsException;
import com._pearls.contactms.mapper.ContactMapper;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.repo.ContactRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContactService {

    private final ContactRepo contactRepo;

    public ContactService(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    public PaginatedResponseDTO<Contact> getPaginatedContacts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contact> contactPage = contactRepo.findAll(pageable);

        PaginatedResponseDTO<Contact> response = new PaginatedResponseDTO<>();

        response.setData(contactPage.getContent());
        response.setPage(contactPage.getNumber());
        response.setSize(contactPage.getSize());
        response.setTotalElements(contactPage.getTotalElements());
        response.setTotalPages(contactPage.getTotalPages());

        return response;

    }

    public ContactResponseDTO addContact(ContactRequestDTO contactRequestDTO) {

        Contact contact = ContactMapper.toModel(contactRequestDTO);
        contact.setCreatedAt(LocalDateTime.now());
        contactRepo.save(contact);

        return ContactMapper.toDTO(contact);
    }

    public ContactResponseDTO updateContact(Long id, ContactRequestDTO updatedContact) {

        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));

        if(contactRepo.existsByEmailAndIdNot(updatedContact.getEmail(), id)) {

            throw new EmailAlreadyExistsException(
                    "A contact with this email" //throw new stops execution
                            + " already exists: " + updatedContact.getEmail()
            );
        }

        if(contactRepo.existsByPhoneAndIdNot(updatedContact.getPhone(), id)) {

            throw new PhoneNoAlreadyExistsException(
                    "A contact with this phone number" //throw new stops execution
                            + " already exists: " + updatedContact.getPhone()
            );
        }

        contact.setTitle(updatedContact.getTitle());
        contact.setLastName(updatedContact.getLastName());
        contact.setFirstName(updatedContact.getFirstName());
        contact.setEmail(updatedContact.getEmail());
        contact.setPhone(updatedContact.getPhone());

        Contact contactUpdated = contactRepo.save(contact);
        return ContactMapper.toDTO(contactUpdated);

    }
}
