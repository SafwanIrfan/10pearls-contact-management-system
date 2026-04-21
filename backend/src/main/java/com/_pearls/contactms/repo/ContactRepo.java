package com._pearls.contactms.repo;

import com._pearls.contactms.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactRepo extends JpaRepository<Contact, Long> {
}
