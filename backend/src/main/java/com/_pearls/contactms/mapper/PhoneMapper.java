package com._pearls.contactms.mapper;

import com._pearls.contactms.dto.PhoneRequestDTO;
import com._pearls.contactms.dto.PhoneResponseDTO;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.PhoneContact;

import java.util.List;

public class PhoneMapper {

    public static List<PhoneContact> mapPhonesToModel(
            List<PhoneRequestDTO> phoneRequestDTOS, Contact contact) {

        return phoneRequestDTOS
                .stream()
                .map(dto -> {
                    PhoneContact phoneContact = new PhoneContact();
                    phoneContact.setPhone(dto.getPhone());
                    phoneContact.setLabel(dto.getLabel());
                    phoneContact.setContact(contact);
                    return phoneContact;
                }).toList();
    }

    public static List<PhoneResponseDTO> mapPhonesToDTO(List<PhoneContact> phoneContacts) {

        return phoneContacts
                .stream()
                .map(phone  -> {
                    PhoneResponseDTO phoneResponseDTO = new PhoneResponseDTO();
                    phoneResponseDTO.setId(phone.getId());
                    phoneResponseDTO.setPhone(phone.getPhone());
                    phoneResponseDTO.setLabel(phone.getLabel());
                    return phoneResponseDTO;
                }).toList();
    }
}
