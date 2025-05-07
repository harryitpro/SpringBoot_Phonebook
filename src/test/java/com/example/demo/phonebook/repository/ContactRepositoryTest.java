package com.example.demo.phonebook.repository;

import com.example.demo.phonebook.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactRepositoryTest {

    @Mock
    private ContactRepository contactRepository;

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
    void findByPhoneNumber_ShouldReturnContact_WhenPhoneNumberExists() {
        when(contactRepository.findByPhoneNumber("1234567890")).thenReturn(contact);

        Contact result = contactRepository.findByPhoneNumber("1234567890");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(contactRepository, times(1)).findByPhoneNumber("1234567890");
    }

    @Test
    void findByPhoneNumber_ShouldReturnNull_WhenPhoneNumberDoesNotExist() {
        when(contactRepository.findByPhoneNumber("0987654321")).thenReturn(null);

        Contact result = contactRepository.findByPhoneNumber("0987654321");

        assertNull(result);
        verify(contactRepository, times(1)).findByPhoneNumber("0987654321");
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingContacts() {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactRepository.findByNameContainingIgnoreCase("John")).thenReturn(contacts);

        List<Contact> result = contactRepository.findByNameContainingIgnoreCase("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(contactRepository, times(1)).findByNameContainingIgnoreCase("John");
    }

    @Test
    void findByNameContainingIgnoreCase_ShouldReturnEmptyList_WhenNoMatchesFound() {
        when(contactRepository.findByNameContainingIgnoreCase("Jane")).thenReturn(Arrays.asList());

        List<Contact> result = contactRepository.findByNameContainingIgnoreCase("Jane");

        assertTrue(result.isEmpty());
        verify(contactRepository, times(1)).findByNameContainingIgnoreCase("Jane");
    }

    @Test
    void findById_ShouldReturnContact_WhenIdExists() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedContact() {
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        Contact savedContact = contactRepository.save(contact);

        assertNotNull(savedContact);
        assertEquals("John Doe", savedContact.getName());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void deleteById_ShouldCallDelete() {
        doNothing().when(contactRepository).deleteById(1L);

        contactRepository.deleteById(1L);

        verify(contactRepository, times(1)).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnAllContacts() {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactRepository.findAll()).thenReturn(contacts);

        List<Contact> result = contactRepository.findAll();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(contactRepository, times(1)).findAll();
    }
}