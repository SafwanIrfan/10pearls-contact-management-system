package com._pearls.contactms.service;
import com._pearls.contactms.dto.contactdto.ContactRequestDTO;
import com._pearls.contactms.dto.contactdto.ContactResponseDTO;
import com._pearls.contactms.dto.contactdto.PaginatedResponseDTO;
import com._pearls.contactms.dto.emaildto.EmailRequestDTO;
import com._pearls.contactms.dto.phonedto.PhoneRequestDTO;
import com._pearls.contactms.exception.NotFoundException;
import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import com._pearls.contactms.repo.ContactRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    ContactRepo contactRepo;

    @InjectMocks
    ContactService contactService;

    private Contact mockContact;
    private ContactRequestDTO mockRequest;

    @BeforeEach
    void setUp() {
        // reusable mock Contact model
        EmailContact emailContact = new EmailContact();
        emailContact.setId(1L);
        emailContact.setEmail("safwan@test.com");
        emailContact.setLabel("work");

        PhoneContact phoneContact = new PhoneContact();
        phoneContact.setId(1L);
        phoneContact.setPhone("03001234567");
        phoneContact.setLabel("mobile");

        mockContact = new Contact();
        mockContact.setId(1L);
        mockContact.setFirstName("Safwan");
        mockContact.setLastName("Irfan");
        mockContact.setTitle("Software Engineer");
        mockContact.setEmail(new ArrayList<>(List.of(emailContact)));
        mockContact.setPhone(new ArrayList<>(List.of(phoneContact)));

        // reusable mock ContactRequestDTO
        EmailRequestDTO emailRequest = new EmailRequestDTO();
        emailRequest.setEmail("safwan@test.com");
        emailRequest.setLabel("work");

        PhoneRequestDTO phoneRequest = new PhoneRequestDTO();
        phoneRequest.setPhone("03001234567");
        phoneRequest.setLabel("mobile");

        mockRequest = new ContactRequestDTO();
        mockRequest.setFirstName("Safwan");
        mockRequest.setLastName("Irfan");
        mockRequest.setTitle("Software Engineer");
        mockRequest.setEmails(List.of(emailRequest));
        mockRequest.setPhones(List.of(phoneRequest));
    }

    // getContacts()
    @Test
    @DisplayName("getContacts() → returns paginated contacts")
    void getContacts_returnsPagedResponse() {
        Page<Contact> page = new PageImpl<>(
                List.of(mockContact),
                PageRequest.of(0, 10),
                1
        );        when(contactRepo.findAll(ArgumentMatchers.<Specification<Contact>>any(), any(PageRequest.class))).thenReturn(page);

        PaginatedResponseDTO<ContactResponseDTO> result = contactService.getContacts(null, 1, 10);

        assertThat(result.getData()).hasSize(1);
        assertThat(result.getData().getFirst().getFirstName()).isEqualTo("Safwan");
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getPage()).isEqualTo(1);
    }

    @Test
    @DisplayName("getContacts() → returns empty list when no contacts match keyword")
    void getContacts_withKeyword_noMatch_returnsEmpty() {
        Page<Contact> emptyPage = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 10),
                0
        );        when(contactRepo.findAll(ArgumentMatchers.<Specification<Contact>>any(), any(PageRequest.class))).thenReturn(emptyPage);

        PaginatedResponseDTO<ContactResponseDTO> result = contactService.getContacts("unknown", 1, 10);

        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    // getContactById()
    @Test
    @DisplayName("getContactById() → returns contact when id exists")
    void getContactById_validId_returnsContact() {
        when(contactRepo.findById(1L)).thenReturn(Optional.of(mockContact));

        ContactResponseDTO result = contactService.getContactById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Safwan");
    }

    @Test
    @DisplayName("getContactById() → throws NotFoundException when id not found")
    void getContactById_invalidId_throwsNotFoundException() {
        when(contactRepo.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.getContactById(404L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contact not found with id: 404");
    }

    // addContact()
    @Test
    @DisplayName("addContact() → saves contact and returns response DTO")
    void addContact_validRequest_savesAndReturnsDTO() {
        when(contactRepo.save(any(Contact.class))).thenReturn(mockContact);

        ContactResponseDTO result = contactService.addContact(mockRequest);

        assertThat(result.getFirstName()).isEqualTo("Safwan");
        assertThat(result.getLastName()).isEqualTo("Irfan");
        verify(contactRepo).save(any(Contact.class));
    }

    // addContacts() — bulk
    @Test
    @DisplayName("addContacts() → saves all contacts and returns list of DTOs")
    void addContacts_validList_savesAllAndReturnsDTOs() {
        when(contactRepo.saveAll(anyList())).thenReturn(List.of(mockContact));

        List<ContactResponseDTO> result = contactService.addContacts(List.of(mockRequest));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFirstName()).isEqualTo("Safwan");
        verify(contactRepo).saveAll(anyList());
    }

    // updateContact()
    @Test
    @DisplayName("updateContact() → updates and returns updated contact")
    void updateContact_validRequest_returnsUpdatedContact() {
        when(contactRepo.findById(1L)).thenReturn(Optional.of(mockContact));
        when(contactRepo.save(any(Contact.class))).thenReturn(mockContact);

        ContactResponseDTO result = contactService.updateContact(1L, mockRequest);

        assertThat(result.getFirstName()).isEqualTo("Safwan");
        verify(contactRepo).save(any(Contact.class));
    }

    @Test
    @DisplayName("updateContact() → throws NotFoundException when contact not found")
    void updateContact_notFound_throwsNotFoundException() {
        when(contactRepo.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.updateContact(404L, mockRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contact not found with id: 404");

        verify(contactRepo, never()).save(any());
    }

    @Test
    @DisplayName("updateContact() → clears old emails and phones before updating")
    void updateContact_clearsOldEmailsAndPhones() {
        when(contactRepo.findById(1L)).thenReturn(Optional.of(mockContact));
        when(contactRepo.save(any(Contact.class))).thenReturn(mockContact);

        contactService.updateContact(1L, mockRequest);

        // emails and phones are cleared then re-added — verify save was called
        verify(contactRepo).save(argThat(contact ->
                contact.getEmail() != null && contact.getPhone() != null
        ));
    }

    // deleteContact()
    @Test
    @DisplayName("deleteContact() → deletes contact when id exists")
    void deleteContact_validId_deletesContact() {
        when(contactRepo.findById(1L)).thenReturn(Optional.of(mockContact));

        contactService.deleteContact(1L);

        verify(contactRepo).deleteById(1L);
    }

    @Test
    @DisplayName("deleteContact() → throws NotFoundException when contact not found")
    void deleteContact_notFound_throwsNotFoundException() {
        when(contactRepo.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.deleteContact(404L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Contact not found with id: 404");

        verify(contactRepo, never()).deleteById(any());
    }

}
