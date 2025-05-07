package com.example.demo.phonebook.service;

import com.example.demo.phonebook.model.Contact;
import com.example.demo.phonebook.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setPhoneNumber("1234567890");
        contact.setEmail("john.doe@example.com");
    }

    @Test
    void saveContact_ShouldReturnSavedContact() {
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        Contact savedContact = contactService.saveContact(contact);

        assertNotNull(savedContact);
        assertEquals("John Doe", savedContact.getName());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void getAllContacts_ShouldReturnContactList() {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactRepository.findAll()).thenReturn(contacts);

        List<Contact> result = contactService.getAllContacts();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void getContactById_ShouldReturnContact_WhenContactExists() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.getContactById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void getContactById_ShouldReturnEmpty_WhenContactDoesNotExist() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Contact> result = contactService.getContactById(1L);

        assertFalse(result.isPresent());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void getContactByPhoneNumber_ShouldReturnContact_WhenContactExists() {
        when(contactRepository.findByPhoneNumber("1234567890")).thenReturn(contact);

        Contact result = contactService.getContactByPhoneNumber("1234567890");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(contactRepository, times(1)).findByPhoneNumber("1234567890");
    }

    @Test
    void getContactsByName_ShouldReturnMatchingContacts() {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactRepository.findByNameContainingIgnoreCase("John")).thenReturn(contacts);

        List<Contact> result = contactService.getContactsByName("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(contactRepository, times(1)).findByNameContainingIgnoreCase("John");
    }

    @Test
    void updateContact_ShouldUpdateAndReturnContact_WhenContactExists() {
        Contact updatedContact = new Contact();
        updatedContact.setName("Jane Doe");
        updatedContact.setPhoneNumber("0987654321");
        updatedContact.setEmail("jane.doe@example.com");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        Contact result = contactService.updateContact(1L, updatedContact);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("0987654321", result.getPhoneNumber());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void updateContact_ShouldReturnNull_WhenContactDoesNotExist() {
        Contact updatedContact = new Contact();
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        Contact result = contactService.updateContact(1L, updatedContact);

        assertNull(result);
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void deleteContact_ShouldCallDeleteById() {
        doNothing().when(contactRepository).deleteById(1L);

        contactService.deleteContact(1L);

        verify(contactRepository, times(1)).deleteById(1L);
    }
}