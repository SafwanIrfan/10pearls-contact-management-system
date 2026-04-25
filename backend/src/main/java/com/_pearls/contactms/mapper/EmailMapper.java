package com._pearls.contactms.mapper;

import com._pearls.contactms.dto.emaildto.EmailRequestDTO;
import com._pearls.contactms.dto.emaildto.EmailResponseDTO;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;

import java.util.List;
import java.util.stream.Collectors;

public class EmailMapper {

    public static List<EmailContact> mapEmailsToModel(
            List<EmailRequestDTO> emailRequestDTOS, Contact contact) {

        return emailRequestDTOS
                .stream()
                .map(dto -> {
                    EmailContact emailContact = new EmailContact();
                    emailContact.setEmail(dto.getEmail());
                    emailContact.setLabel(dto.getLabel());
                    emailContact.setContact(contact);
                    return emailContact;
                }).collect(Collectors.toList());
        //.collect(Collectors.toList()) is mutable
    }

    public static List<EmailResponseDTO> mapEmailsToDTO(List<EmailContact> emailContacts) {

        return emailContacts
                .stream()
                .map(email -> {
                    EmailResponseDTO emailResponseDTO = new EmailResponseDTO();
                    emailResponseDTO.setId(email.getId());
                    emailResponseDTO.setEmail(email.getEmail());
                    emailResponseDTO.setLabel(email.getLabel());
                    return emailResponseDTO;
                }).toList();
    }
}
