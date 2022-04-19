package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void add(User user);
    User get(long id);
    void update(User user);
    void delete(long id);
    List<User> getUsersList();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    Set<Role> getAvailableRoles();
}
