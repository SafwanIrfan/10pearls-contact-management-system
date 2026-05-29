package com._pearls.contactms.controller;

import com._pearls.contactms.dto.contactdto.ContactRequestDTO;
import com._pearls.contactms.dto.contactdto.ContactResponseDTO;
import com._pearls.contactms.dto.contactdto.PaginatedResponseDTO;
import com._pearls.contactms.dto.emaildto.EmailRequestDTO;
import com._pearls.contactms.dto.phonedto.PhoneRequestDTO;
import com._pearls.contactms.exception.NotFoundException;
import com._pearls.contactms.service.ContactService;
import com._pearls.contactms.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ContactService contactService;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserDetailsService userDetailsService;

    private ContactResponseDTO mockResponse;
    private ContactRequestDTO mockRequest;

    @BeforeEach
    void setUp() {
        // reusable mock response
        mockResponse = new ContactResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setFirstName("Safwan");
        mockResponse.setLastName("Irfan");
        mockResponse.setTitle("Software Engineer");
        mockResponse.setEmails(List.of());
        mockResponse.setPhones(List.of());
        mockResponse.setCreatedAt(LocalDateTime.now());

        // reusable mock request
        EmailRequestDTO email = new EmailRequestDTO();
        email.setEmail("safwan@test.com");
        email.setLabel("work");

        PhoneRequestDTO phone = new PhoneRequestDTO();
        phone.setPhone("03001234567");
        phone.setLabel("mobile");

        mockRequest = new ContactRequestDTO();
        mockRequest.setFirstName("Safwan");
        mockRequest.setLastName("Irfan");
        mockRequest.setTitle("Software Engineer");
        mockRequest.setEmails(List.of(email));
        mockRequest.setPhones(List.of(phone));
    }

    // GET /contacts
    @Test
    @DisplayName("GET /contacts → 200 OK with paginated contacts")
    void getContacts_returns200WithPaginatedResponse() throws Exception {
        PaginatedResponseDTO<ContactResponseDTO> paginated = new PaginatedResponseDTO<>();
        paginated.setData(List.of(mockResponse));
        paginated.setPage(1);
        paginated.setSize(10);
        paginated.setTotalElements(1);
        paginated.setTotalPages(1);

        when(contactService.getContacts(null, 1, 10)).thenReturn(paginated);

        mockMvc.perform(get("/contacts")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].firstName").value("Safwan"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.page").value(1));
    }

    @Test
    @DisplayName("GET /contacts → 200 OK with keyword search")
    void getContacts_withKeyword_returns200() throws Exception {
        PaginatedResponseDTO<ContactResponseDTO> paginated = new PaginatedResponseDTO<>();
        paginated.setData(List.of(mockResponse));
        paginated.setPage(1);
        paginated.setSize(10);
        paginated.setTotalElements(1);
        paginated.setTotalPages(1);

        when(contactService.getContacts("Irfan", 1, 10)).thenReturn(paginated);

        mockMvc.perform(get("/contacts")
                        .param("search", "Irfan")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].lastName").value("Irfan"));
    }

    @Test
    @DisplayName("GET /contacts → 200 OK with empty list when no contacts found")
    void getContacts_noResults_returnsEmptyList() throws Exception {
        PaginatedResponseDTO<ContactResponseDTO> paginated = new PaginatedResponseDTO<>();
        paginated.setData(List.of());
        paginated.setPage(1);
        paginated.setSize(10);
        paginated.setTotalElements(0);
        paginated.setTotalPages(0);

        when(contactService.getContacts(null, 1, 10)).thenReturn(paginated);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // GET /contacts/contact/{id}
    @Test
    @DisplayName("GET /contacts/contact/{id} → 200 OK when contact exists")
    void getContactById_validId_returns200() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/contacts/contact/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Safwan"));
    }

    @Test
    @DisplayName("GET /contacts/contact/{id} → 404 when contact not found")
    void getContactById_notFound_returns404() throws Exception {
        when(contactService.getContactById(404L))
                .thenThrow(new NotFoundException("Contact not found with id: 404"));

        mockMvc.perform(get("/contacts/contact/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /contacts/contact/{id} → 400 when id is invalid (zero or negative)")
    void getContactById_invalidId_returns400() throws Exception {
        mockMvc.perform(get("/contacts/contact/0"))
                .andExpect(status().isBadRequest());
    }

    // POST /contacts/contact/add
    @Test
    @DisplayName("POST /contacts/contact/add → 200 OK when contact is added")
    void addContact_validRequest_returns200() throws Exception {
        when(contactService.addContact(any(ContactRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/contacts/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Safwan"))
                .andExpect(jsonPath("$.lastName").value("Irfan"));
    }

    @Test
    @DisplayName("POST /contacts/contact/add → 400 when lastName is blank")
    void addContact_blankLastName_returns400() throws Exception {
        mockRequest.setLastName("");

        mockMvc.perform(post("/contacts/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /contacts/contact/add → 400 when firstName is blank")
    void addContact_blankFirstName_returns400() throws Exception {
        mockRequest.setFirstName("");

        mockMvc.perform(post("/contacts/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /contacts/contact/add → 400 when title is blank")
    void addContact_blankTitle_returns400() throws Exception {
        mockRequest.setTitle("");

        mockMvc.perform(post("/contacts/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isBadRequest());
    }

    // POST /contacts/contacts/add (multiple contacts)
    @Test
    @DisplayName("POST /contacts/contacts/add → 200 OK when multiple contacts added")
    void addContacts_validRequest_returns200() throws Exception {
        when(contactService.addContacts(anyList())).thenReturn(List.of(mockResponse));

        mockMvc.perform(post("/contacts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(mockRequest))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Safwan"));
    }

    // PUT /contacts/contact/update/{id}
    @Test
    @DisplayName("PUT /contacts/contact/update/{id} → 200 OK when contact updated")
    void updateContact_validRequest_returns200() throws Exception {
        when(contactService.updateContact(eq(1L), any(ContactRequestDTO.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/contacts/contact/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Safwan"));
    }

    @Test
    @DisplayName("PUT /contacts/contact/update/{id} → 404 when contact not found")
    void updateContact_notFound_returns404() throws Exception {
        when(contactService.updateContact(eq(404L), any(ContactRequestDTO.class)))
                .thenThrow(new NotFoundException("Contact not found with id: 404"));

        mockMvc.perform(put("/contacts/contact/update/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isNotFound());
    }

    // DELETE /contacts/contact/delete/{id}
    @Test
    @DisplayName("DELETE /contacts/contact/delete/{id} → 204 NO CONTENT when deleted")
    void deleteContact_validId_returns204() throws Exception {
        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(delete("/contacts/contact/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /contacts/contact/delete/{id} → 404 when contact not found")
    void deleteContact_notFound_returns404() throws Exception {
        doThrow(new NotFoundException("Contact not found with id: 404"))
                .when(contactService).deleteContact(404L);

        mockMvc.perform(delete("/contacts/contact/delete/404"))
                .andExpect(status().isNotFound());
    }

}
