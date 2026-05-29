package com._pearls.contactms.repo;

import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import com._pearls.contactms.specification.ContactSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContactRepoTest {

    @Autowired
    ContactRepo contactRepo;

    @BeforeEach
    void setUp() {
        contactRepo.deleteAll();

        Contact contact = new Contact();
        contact.setFirstName("Safwan");
        contact.setLastName("Irfan");
        contact.setTitle("Software Engineer");
        contact.setCreatedAt(LocalDateTime.now());

        EmailContact email = new EmailContact();
        email.setEmail("safwan@test.com");
        email.setLabel("work");
        email.setContact(contact);

        PhoneContact phone = new PhoneContact();
        phone.setPhone("03001234567");
        phone.setLabel("mobile");
        phone.setContact(contact);

        contact.setEmail(List.of(email));
        contact.setPhone(List.of(phone));

        contactRepo.save(contact);
    }

    // findById()
    @Test
    @DisplayName("findById() → returns contact when id exists")
    void findById_exists_returnsContact() {
        Contact saved = contactRepo.findAll().getFirst();
        Optional<Contact> result = contactRepo.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Safwan");
    }

    @Test
    @DisplayName("findById() → returns empty when id not found")
    void findById_notFound_returnsEmpty() {
        Optional<Contact> result = contactRepo.findById(405L);
        assertThat(result).isEmpty();
    }

    // ContactSpecification search
    @Test
    @DisplayName("search() → returns contact when keyword matches lastName")
    void search_matchesFirstName_returnsContact() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search("Irfan"), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getLastName()).isEqualTo("Irfan");
    }

    @Test
    @DisplayName("search() → returns contact when keyword matches email")
    void search_matchesEmail_returnsContact() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search("safwan@test.com"), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("search() → returns contact when keyword matches phone")
    void search_matchesPhone_returnsContact() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search("03001234567"), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("search() → returns empty when keyword does not match anything")
    void search_noMatch_returnsEmpty() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search("unknown"), PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("search() → returns all contacts when keyword is null")
    void search_nullKeyword_returnsAll() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search(null), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("search() → returns all contacts when keyword is blank")
    void search_blankKeyword_returnsAll() {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search("  "), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    // deleteById()
    @Test
    @DisplayName("deleteById() → contact is removed from DB")
    void deleteById_removesContact() {
        Contact saved = contactRepo.findAll().getFirst();
        contactRepo.deleteById(saved.getId());

        assertThat(contactRepo.findById(saved.getId())).isEmpty();
    }
}
