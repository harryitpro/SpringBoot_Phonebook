package com.harryitpro.phonebook.model;

/**
 * POJO
 * Represents a phonebook contact:
 */
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone number must be in format XXX-XXX-XXXX")
    private String phoneNumber;

    private String email;
}
