package com._pearls.contactms.specification;

import com._pearls.contactms.model.Contact;
import com._pearls.contactms.model.EmailContact;
import com._pearls.contactms.model.PhoneContact;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ContactSpecification {

    public static Specification<Contact> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            query.distinct(true); // avoid duplicate results from joins

            String pattern = "%" + keyword.toLowerCase() + "%";

            Join<Contact, EmailContact> emailJoin = root.join("email", JoinType.LEFT);
            Join<Contact, PhoneContact> phoneJoin = root.join("phone", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(emailJoin.get("email")), pattern),
                    cb.like(cb.lower(phoneJoin.get("phone")), pattern)
            );
        };
    }
}
