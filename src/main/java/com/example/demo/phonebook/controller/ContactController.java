package com.example.demo.phonebook.controller;

import com.example.demo.phonebook.model.Contact;
import com.example.demo.phonebook.service.ContactService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/phonebook")
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) {
        Contact savedContact = contactService.saveContact(contact);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<Contact> getContactByPhoneNumber(@PathVariable String phoneNumber) {
        Contact contact = contactService.getContactByPhoneNumber(phoneNumber);
        if (contact != null) {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Contact>> searchByName(@PathVariable String name) {
        List<Contact> contacts = contactService.getContactsByName(name);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @Valid @RequestBody Contact contact) {
        Contact updatedContact = contactService.updateContact(id, contact);
        if (updatedContact != null) {
            return new ResponseEntity<>(updatedContact, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
