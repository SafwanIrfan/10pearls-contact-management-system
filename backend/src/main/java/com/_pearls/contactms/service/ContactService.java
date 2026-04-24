package com._pearls.contactms.service;

import com._pearls.contactms.dto.ContactRequestDTO;
import com._pearls.contactms.dto.ContactResponseDTO;
import com._pearls.contactms.dto.PaginatedResponseDTO;
import com._pearls.contactms.exception.ContactNotFoundException;
import com._pearls.contactms.mapper.ContactMapper;
import com._pearls.contactms.mapper.EmailMapper;
import com._pearls.contactms.mapper.PhoneMapper;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import com._pearls.contactms.repo.ContactRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepo contactRepo;

    public ContactService(ContactRepo contactRepo) {
        this.contactRepo = contactRepo;
    }

    public PaginatedResponseDTO<ContactResponseDTO> getPaginatedContacts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Contact> contactPage = contactRepo.findAll(pageable);

        List<ContactResponseDTO> dtoList = contactPage.getContent()
                .stream()
                .map(ContactMapper::toDTO)
                .toList();

        PaginatedResponseDTO<ContactResponseDTO> response = new PaginatedResponseDTO<>();

        response.setData(dtoList);
        response.setPage(contactPage.getNumber());
        response.setSize(contactPage.getSize());
        response.setTotalElements(contactPage.getTotalElements());
        response.setTotalPages(contactPage.getTotalPages());

        return response;

    }

    public ContactResponseDTO addContact(ContactRequestDTO contactRequestDTO) {

        Contact contact = ContactMapper.toModel(contactRequestDTO);
        contact.setCreatedAt(LocalDateTime.now());

        //map emails to Model
        List<EmailContact> emails = EmailMapper.mapEmailsToModel(contactRequestDTO.getEmails(), contact);
        contact.setEmail(emails);

        //map phones to Model
        List<PhoneContact> phones = PhoneMapper.mapPhonesToModel(contactRequestDTO.getPhones(), contact);
        contact.setPhone(phones);

        contactRepo.save(contact);

        return ContactMapper.toDTO(contact);
    }

    public ContactResponseDTO updateContact(Long id, ContactRequestDTO updatedContact) {

        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));

        contact.setTitle(updatedContact.getTitle());
        contact.setLastName(updatedContact.getLastName());
        contact.setFirstName(updatedContact.getFirstName());

        //first clear emails
        contact.getEmail().clear();
        //map emails to Modal
        List<EmailContact> emails = EmailMapper.mapEmailsToModel(updatedContact.getEmails(), contact);
        contact.getEmail().addAll(emails);


        //first clear phones
        contact.getPhone().clear();
        //map phones to Modal
        List<PhoneContact> phones = PhoneMapper.mapPhonesToModel(updatedContact.getPhones(), contact);
        contact.getPhone().addAll(phones);

        Contact contactUpdated = contactRepo.save(contact);
        return ContactMapper.toDTO(contactUpdated);
    }

    public void deleteContact(Long id) {

        contactRepo.findById(id).
                orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + id));

        contactRepo.deleteById(id);
    }
}
