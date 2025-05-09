package com.example.demo.phonebook.repository;

import com.example.demo.phonebook.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact findByPhoneNumber(String phoneNumber);

    List<Contact> findByNameContainingIgnoreCase(String name);
}
