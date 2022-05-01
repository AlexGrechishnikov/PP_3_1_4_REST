package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.exceptions.CreateUserException;
import ru.kata.spring.boot_security.demo.exceptions.DuplicateEmailException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoHibernate implements UserDao {

    private final EntityManager manager;

    public UserDaoHibernate(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public User add(User user) throws DuplicateEmailException {
        checkEmailDuplicate(user);
        manager.persist(user);
        manager.flush();
        return user;
    }

    @Override
    public User get(long id) throws UserNotFoundException {
        return manager
                .createQuery("select u from User u where u.id=:id", User.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void update(User user) throws CreateUserException {
        User saved = manager.find(User.class, user.getId());
        if (!user.getEmail().equals(saved.getEmail())) {
            checkEmailDuplicate(user);
        }
        manager.merge(user);
    }

    @Override
    public void delete(long id) {
        manager.createQuery("delete from User u where u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<User> getUsersList() {
        return manager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return manager
                .createQuery("select u from User u join fetch u.roles r where u.email=?1", UserDetails.class)
                .setParameter(1,username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Role> getRolesSet() {
        return new HashSet<>(manager
                .createQuery("select r from Role r", Role.class)
                .getResultList());
    }

    private void checkEmailDuplicate(User user) throws DuplicateEmailException {
        manager.clear();
        manager
                .createQuery("select u from User u where u.email=:e", User.class)
                .setParameter("e", user.getEmail())
                .getResultStream()
                .findFirst()
                .ifPresent(x-> {
                    throw new DuplicateEmailException(x.getEmail() + " already exists");});
    }
}