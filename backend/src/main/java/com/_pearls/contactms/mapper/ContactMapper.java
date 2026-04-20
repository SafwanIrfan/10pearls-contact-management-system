package com._pearls.contactms.mapper;

import com._pearls.contactms.dto.ContactRequestDTO;
import com._pearls.contactms.dto.ContactResponseDTO;
import com._pearls.contactms.model.Contact;


public class ContactMapper {

    public static ContactResponseDTO toDTO(Contact contact) {
        ContactResponseDTO contactDTO = new ContactResponseDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setTitle(contact.getTitle());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setEmail(contact.getEmail());
        contactDTO.setPhone(contact.getPhone());
        contactDTO.setCreatedAt(contact.getCreatedAt());
        return contactDTO;
    }

    public static Contact toModel(ContactRequestDTO  contactRequestDTO) {
        Contact contact = new Contact();

        contact.setFirstName(contactRequestDTO.getFirstName());
        contact.setLastName(contactRequestDTO.getLastName());
        contact.setTitle(contactRequestDTO.getTitle());
        contact.setEmail(contactRequestDTO.getEmail());
        contact.setPhone(contactRequestDTO.getPhone());

        return contact;
    }
}
