package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.exceptions.CreateUserException;
import ru.kata.spring.boot_security.demo.exceptions.DuplicateEmailException;
import ru.kata.spring.boot_security.demo.exceptions.NonExistingRolesException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional()
    @Override
    public User add(User user) throws DuplicateEmailException {
        if (user.getId() != null) {
            user.setId(null);
        }

        Set<Role> roleSet = getAvailableRoles();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (roleSet.containsAll(authorities)) {
            return userDao.add(user);
        } else {
            throw new NonExistingRolesException("Roles from request not exists");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User get(long id) throws UserNotFoundException {
        return userDao.get(id);
    }

    @Transactional
    @Override
    public void update(User user) throws CreateUserException {
        userDao.update(user);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsersList() {
        return userDao.getUsersList();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails details = userDao.loadUserByUsername(username);
        if (details == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return details;
    }

    @Transactional
    @Override
    public Set<Role> getAvailableRoles() {
        return userDao.getRolesSet();
    }
}