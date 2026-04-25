package com._pearls.contactms.mapper;

import com._pearls.contactms.dto.contactdto.ContactRequestDTO;
import com._pearls.contactms.dto.contactdto.ContactResponseDTO;
import com._pearls.contactms.dto.emaildto.EmailResponseDTO;
import com._pearls.contactms.dto.phonedto.PhoneResponseDTO;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;

import java.util.List;


public class ContactMapper {

    public static ContactResponseDTO toDTO(Contact contact) {
        ContactResponseDTO contactDTO = new ContactResponseDTO();
        contactDTO.setId(contact.getId());
        contactDTO.setTitle(contact.getTitle());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());

        List<EmailResponseDTO> emailResponseDTOS = EmailMapper.mapEmailsToDTO(contact.getEmail());

        List<PhoneResponseDTO> phoneResponseDTOS = PhoneMapper.mapPhonesToDTO(contact.getPhone());

        contactDTO.setEmails(emailResponseDTOS);
        contactDTO.setPhones(phoneResponseDTOS);
        contactDTO.setCreatedAt(contact.getCreatedAt());
        return contactDTO;
    }

    public static Contact toModel(ContactRequestDTO contactRequestDTO) {
        Contact contact = new Contact();

        contact.setFirstName(contactRequestDTO.getFirstName());
        contact.setLastName(contactRequestDTO.getLastName());
        contact.setTitle(contactRequestDTO.getTitle());

        List<EmailContact> emails = EmailMapper.mapEmailsToModel(contactRequestDTO.getEmails(), contact);
        contact.setEmail(emails);

        List<PhoneContact> phones = PhoneMapper.mapPhonesToModel(contactRequestDTO.getPhones(), contact);
        contact.setPhone(phones);

        return contact;
    }
}
