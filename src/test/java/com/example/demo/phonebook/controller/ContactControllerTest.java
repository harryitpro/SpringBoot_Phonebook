package com.example.demo.phonebook.controller;

import com.example.demo.phonebook.model.Contact;
import com.example.demo.phonebook.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createContact_shouldReturnCreatedContact() throws Exception {
        Contact contact = new Contact(1L,"Alice", "123-456-7890", "alice@email.com");

        when(contactService.saveContact(any(Contact.class))).thenReturn(contact);

        mockMvc.perform(post("/api/phonebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getAllContacts_shouldReturnList() throws Exception {
        Contact contact1 = new Contact(1L, "Alice", "123-456-7890","alice@email.com");
        Contact contact2 = new Contact(2L, "Bob", "987-654-3210","bob@email.com");

        when(contactService.getAllContacts()).thenReturn(Arrays.asList(contact1, contact2));

        mockMvc.perform(get("/api/phonebook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getContactById_shouldReturnContact_whenFound() throws Exception {
        Contact contact = new Contact(1L, "Alice", "123-456-7890","alice@email.com");

        when(contactService.getContactById(1L)).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/api/phonebook/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getContactById_shouldReturnNotFound_whenNotFound() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/phonebook/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getContactByPhoneNumber_shouldReturnContact_whenFound() throws Exception {
        Contact contact = new Contact(1L, "Alice", "123-456-7890","alice@email.com");

        when(contactService.getContactByPhoneNumber("123-456-7890")).thenReturn(contact);

        mockMvc.perform(get("/api/phonebook/phone/123-456-7890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getContactByPhoneNumber_shouldReturnNotFound_whenNotFound() throws Exception {
        when(contactService.getContactByPhoneNumber("999")).thenReturn(null);

        mockMvc.perform(get("/api/phonebook/phone/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchByName_shouldReturnMatchingContacts() throws Exception {
        Contact contact = new Contact(1L, "Alice", "123-456-7890","alice@email.com");

        when(contactService.getContactsByName("Alice")).thenReturn(Arrays.asList(contact));

        mockMvc.perform(get("/api/phonebook/search/name/Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateContact_shouldReturnUpdatedContact_whenFound() throws Exception {
        Contact contact = new Contact(1L, "Alice", "123-456-7890","alice@email.com");

        when(contactService.updateContact(Mockito.eq(1L), any(Contact.class))).thenReturn(contact);

        mockMvc.perform(put("/api/phonebook/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void updateContact_shouldReturnNotFound_whenNotFound() throws Exception {
        Contact contact = new Contact(1L, "Alice", "123-456-7890","alice@email.com");

        when(contactService.updateContact(Mockito.eq(1L), any(Contact.class))).thenReturn(null);

        mockMvc.perform(put("/api/phonebook/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteContact_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/phonebook/1"))
                .andExpect(status().isNoContent());
    }
}
