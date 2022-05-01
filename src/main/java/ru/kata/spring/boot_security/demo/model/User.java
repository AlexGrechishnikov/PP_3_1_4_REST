package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.json.View;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property  = "id",
        scope     = Long.class)
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @JsonView(View.UI.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(View.UI.class)
    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 20, message = "Допустимая длинна от 2 до 20 символов")
    private String name;

    @JsonView(View.UI.class)
    @NotEmpty(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 20, message = "Допустимая длинна от 2 до 20 символов")
    private String lastName;

    @JsonView(View.UI.class)
    @Positive(message = "Поле не должно быть пустым")
    @Max(value = 127, message = "Не может быть больше 127")
    private byte age;

    @JsonView(View.UI.class)
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @JsonView(View.REST.class)
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 2, message = "Минимальная длинна 2 символа")
    private String password;

    @Column(unique = true)
    @JsonView(View.UI.class)
    @NotEmpty(message = "Поле не должно быть пустым")
    @Email( regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message = "Некорректный e-mail")
    private String email;

    public User() {
    }

    public User(String name, String lastName, byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public User(String name, String lastName, byte age, Set<Role> roles, String password, String email) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.roles = roles;
        this.password = password;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder("User{")
                .append("id='").append(id).append("', ")
                .append("name='").append(name).append("', ")
                .append("lastName='").append(lastName).append("', ")
                .append("age='").append(age).append("', ")
                .append("roles='").append(roles).append("', ")
                .append("email='").append(email).append("', ")
                .append("password='").append(password).append("'}")
                .toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void updateFrom(User updateUser) {
        if (updateUser.name != null) {
            this.name = updateUser.name;
        }
        if (updateUser.lastName != null) {
            this.lastName = updateUser.lastName;
        }
        if (updateUser.age > 0 && updateUser.age <= 127) {
            this.age = updateUser.age;
        }
        if (updateUser.roles != null && !updateUser.roles.isEmpty()) {
            this.roles = updateUser.roles;
        }
        if (updateUser.email != null) {
            this.email = updateUser.email;
        }
    }
}