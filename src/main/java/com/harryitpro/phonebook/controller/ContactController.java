package com.harryitpro.phonebook.controller;

import com.harryitpro.phonebook.model.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    // In-memory storage
    private final List<Contact> contacts = new ArrayList<>();

    public ContactController() {
        // Pre-populate with sample data
        contacts.add(new Contact(1L, "Alice", "123-456-7890"));
        contacts.add(new Contact(2L, "Bob", "098-765-4321"));
    }

    // GET all contacts
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contacts);
    }

    // GET a contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contacts.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        return contact.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST a new contact
    @PostMapping
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
        contact.setId((long) (contacts.size() + 1)); // Simple ID generation
        contacts.add(contact);
        return new ResponseEntity<>(contact, HttpStatus.CREATED);
    }

    // PUT to update a contact
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Optional<Contact> existingContact = contacts.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setName(updatedContact.getName());
            contact.setPhoneNumber(updatedContact.getPhoneNumber());
            return ResponseEntity.ok(contact);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE a contact by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        boolean removed = contacts.removeIf(c -> c.getId().equals(id));
        return removed ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
