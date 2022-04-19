package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    void add(User user);
    User get(long id);
    void update(User user);
    void delete(long id);
    List<User> getUsersList();
    UserDetails loadUserByUsername(String username);
    Set<Role> getRolesSet();
}
