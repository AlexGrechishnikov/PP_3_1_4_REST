package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.UnexpectedRollbackException;
import ru.kata.spring.boot_security.demo.exceptions.CreateUserException;
import ru.kata.spring.boot_security.demo.exceptions.DuplicateEmailException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    User add(User user) throws UnexpectedRollbackException;
    User get(long id) throws UserNotFoundException;
    void update(User user) throws CreateUserException;
    void delete(long id);
    List<User> getUsersList();
    UserDetails loadUserByUsername(String username);
    Set<Role> getRolesSet();
}
