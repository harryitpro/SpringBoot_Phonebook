package com.harryitpro.phonebook.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut for all methods in UserService
    @Pointcut("execution(* com.harryitpro.phonebook.service.ContactService.*(..))")
    public void contactServiceMethods() {}

    // Before advice
    @Before("contactServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("Before " + methodName + " with args: " + java.util.Arrays.toString(args));
    }

    // After advice
    @After("contactServiceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("After " + methodName);
    }

    // AfterReturning advice
    @AfterReturning(pointcut = "contactServiceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("After " + methodName + " returned: " + result);
    }

    // AfterThrowing advice
    @AfterThrowing(pointcut = "contactServiceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("After " + methodName + " threw: " + exception.getMessage());
    }

    // Around advice (for timing)
    @Around("contactServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed(); // Execute the method
            long end = System.nanoTime();
            System.out.println(methodName + " took " + (end - start) + " ns");
            return result;
        } catch (Throwable t) {
            System.out.println(methodName + " failed in " + (System.nanoTime() - start) + " ns");
            throw t;
        }
    }
}