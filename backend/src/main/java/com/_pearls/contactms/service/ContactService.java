package com._pearls.contactms.service;

import com._pearls.contactms.dto.PaginatedResponseDTO;
import com._pearls.contactms.modal.Contact;
import com._pearls.contactms.repo.ContactRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
