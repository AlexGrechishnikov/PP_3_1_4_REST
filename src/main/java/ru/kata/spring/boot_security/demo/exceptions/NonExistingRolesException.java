package ru.kata.spring.boot_security.demo.exceptions;

public class NonExistingRolesException extends CreateUserException {
    public NonExistingRolesException(String message) {
        super(message);
    }
}
