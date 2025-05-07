package com.example.demo.phonebook.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleException_ShouldReturnInternalServerError_WithExceptionMessage() {
        // Arrange
        Exception exception = new Exception("Test error message");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Test error message", response.getBody());
    }

    @Test
    void handleException_ShouldHandleNullMessageException() {
        // Arrange
        Exception exception = new Exception(); // No message provided

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: null", response.getBody());
    }
}