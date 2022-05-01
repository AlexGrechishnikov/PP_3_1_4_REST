package ru.kata.spring.boot_security.demo.exceptions;

public class UserNotFoundException extends CreateUserException {
    public UserNotFoundException(Long userId) {
        super("could not find user '" + userId + "'.");
    }
}
