package com.example.demo.phonebook.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature methodSignature;

    @InjectMocks
    private LoggingAspect loggingAspect;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture logs
        System.setOut(new PrintStream(outContent));
        // Mock the method signature
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
    }

    @AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void logBefore_ShouldLogMethodEntry_WithValidArguments() {
        // Arrange
        Object[] args = new Object[]{"arg1", 123};
        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        loggingAspect.logBefore(joinPoint);

        // Assert
        String expectedLog = "Entering testMethod with args: [arg1, 123]\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logBefore_ShouldLogMethodEntry_WithNullArguments() {
        // Arrange
        Object[] args = new Object[]{null, null};
        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        loggingAspect.logBefore(joinPoint);

        // Assert
        String expectedLog = "Entering testMethod with args: [null, null]\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logAfterReturning_ShouldLogMethodExit_WithNonNullResult() {
        // Arrange
        Object result = "successResult";

        // Act
        loggingAspect.logAfterReturning(joinPoint, result);

        // Assert
        String expectedLog = "Exiting testMethod with result: successResult\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logAfterReturning_ShouldLogMethodExit_WithNullResult() {
        // Arrange
        Object result = null;

        // Act
        loggingAspect.logAfterReturning(joinPoint, result);

        // Assert
        String expectedLog = "Exiting testMethod with result: null\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logAfterThrowing_ShouldLogExceptionMessage_WithRuntimeException() {
        // Arrange
        Throwable exception = new RuntimeException("Test runtime exception");

        // Act
        loggingAspect.logAfterThrowing(joinPoint, exception);

        // Assert
        String expectedLog = "testMethod threw exception: Test runtime exception\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logAfterThrowing_ShouldLogExceptionMessage_WithNullMessage() {
        // Arrange
        Throwable exception = new RuntimeException();

        // Act
        loggingAspect.logAfterThrowing(joinPoint, exception);

        // Assert
        String expectedLog = "testMethod threw exception: null\n";
        assertEquals(expectedLog, outContent.toString());
    }

    @Test
    void logAround_ShouldLogExecutionTimeAndReturnResult_WhenMethodSucceeds() throws Throwable {
        // Arrange
        Object result = "methodResult";
        when(proceedingJoinPoint.proceed()).thenReturn(result);

        // Act
        Object actualResult = loggingAspect.logAround(proceedingJoinPoint);

        // Assert
        assertEquals(result, actualResult);
        String logOutput = outContent.toString();
        assertTrue(logOutput.contains("testMethod executed in"));
        assertTrue(logOutput.contains("ns\n"));
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void logAround_ShouldLogFailureTimeAndThrowException_WhenMethodThrowsRuntimeException() throws Throwable {
        // Arrange
        Throwable exception = new RuntimeException("Test runtime exception");
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        // Act & Assert
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            loggingAspect.logAround(proceedingJoinPoint);
        });
        assertEquals(exception, thrown);
        String logOutput = outContent.toString();
        assertTrue(logOutput.contains("testMethod failed in"));
        assertTrue(logOutput.contains("ns\n"));
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void logAround_ShouldLogFailureTimeAndThrowException_WhenMethodThrowsCheckedException() throws Throwable {
        // Arrange
        Throwable exception = new Exception("Test checked exception");
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        // Act & Assert
        Throwable thrown = assertThrows(Exception.class, () -> {
            loggingAspect.logAround(proceedingJoinPoint);
        });
        assertEquals(exception, thrown);
        String logOutput = outContent.toString();
        assertTrue(logOutput.contains("testMethod failed in"));
        assertTrue(logOutput.contains("ns\n"));
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}