package ru.kata.spring.boot_security.demo.exceptions;

public class DuplicateEmailException extends CreateUserException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
