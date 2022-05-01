package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.json.View;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @JsonView(View.UI.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(View.UI.class)
    @Column(unique = true, updatable = false)
    private String authority;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE)
    private Set<User> users;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    public Role(Long id, String authority, Set<User> users) {
        this.id = id;
        this.authority = authority;
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            Role o = (Role) obj;
            return o.getAuthority().equals(this.authority);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authority);
    }
}
