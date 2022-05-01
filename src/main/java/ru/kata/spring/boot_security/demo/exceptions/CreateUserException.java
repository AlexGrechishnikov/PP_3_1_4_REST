package ru.kata.spring.boot_security.demo.exceptions;

import javax.persistence.RollbackException;

public class CreateUserException extends RollbackException {
    public CreateUserException(String message) {
        super(message);
    }
}
