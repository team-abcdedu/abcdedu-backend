package com.abcdedu_backend.contact.repository;

import com.abcdedu_backend.contact.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
