package com._pearls.contactms.repo;

import com._pearls.contactms.modal.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepo extends JpaRepository<Contact, UUID> {
}
