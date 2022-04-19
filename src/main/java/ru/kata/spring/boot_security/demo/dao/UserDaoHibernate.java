package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
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
    public void add(User user) {
        manager.merge(user);
    }

    @Override
    public User get(long id) {
        return manager
                .createQuery("select u from User u where u.id=:id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void update(User user) {
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
}
