package com.harryitpro.phonebook.repository;

import com.harryitpro.phonebook.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByPhoneNumber(String phoneNumber);

    List<Contact> findByNameContainingIgnoreCase(String name);
}
