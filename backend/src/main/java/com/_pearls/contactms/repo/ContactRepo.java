package com._pearls.contactms.repo;

import com._pearls.contactms.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepo extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {

}
