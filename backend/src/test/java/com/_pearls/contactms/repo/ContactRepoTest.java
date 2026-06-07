package com._pearls.contactms.repo;

import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import com._pearls.contactms.specification.ContactSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @DisplayName("search() → returns correct results for various keywords")
    @MethodSource("searchKeywordProvider")
    void search_variousKeywords_returnsExpectedResults(String keyword, int expectedSize) {
        Page<Contact> result = contactRepo.findAll(
                ContactSpecification.search(keyword), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(expectedSize);
    }

    static Stream<Arguments> searchKeywordProvider() {
        return Stream.of(
                Arguments.of("safwan@test.com", 1),  // matches email
                Arguments.of("03001234567",     1),  // matches phone
                Arguments.of("unknown",         0),  // no match
                Arguments.of(null,              1),  // null keyword -> all
                Arguments.of("  ",             1)   // blank keyword -> all
        );
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
