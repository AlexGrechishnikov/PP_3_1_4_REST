package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.exceptions.CreateUserException;
import ru.kata.spring.boot_security.demo.exceptions.DuplicateEmailException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    User add(User user) throws DuplicateEmailException;
    User get(long id) throws UserNotFoundException;
    void update(User user) throws CreateUserException;
    void delete(long id);
    List<User> getUsersList();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    Set<Role> getAvailableRoles();
}
