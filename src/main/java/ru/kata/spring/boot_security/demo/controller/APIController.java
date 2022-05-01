package ru.kata.spring.boot_security.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exceptions.CreateUserException;
import ru.kata.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.kata.spring.boot_security.demo.json.View;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class APIController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public APIController(UserService userService, PasswordEncoder encoder, ObjectMapper objectMapper) {
        this.userService = userService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @JsonView(View.UI.class)
    @GetMapping()
    public List<User> getUsersList() {
        return userService.getUsersList();
    }

    @JsonView(View.UI.class)
    @GetMapping("/me")
    public User getMe(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @JsonView(View.UI.class)
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable("id") Long id) throws JsonProcessingException {
        User user;
        try {
            user = userService.get(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writerWithView(View.UI.class).writeValueAsString(user));
    }

    @PostMapping()
    public ResponseEntity<String> postNewUser(@RequestBody User newUser) {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        User user;
        try {
            user = userService.add(newUser);
        } catch (CreateUserException | ConstraintViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("http://localhost:8080/api/users/" + user.getId()))
                .build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> wrongPost() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping()
    public ResponseEntity<String> wrongDelete() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @RequestBody User updateUser) {
        User saved;
        try {
            saved = userService.get(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
        saved.updateFrom(updateUser);
        String updatedPassword = updateUser.getPassword();
        if (updatedPassword != null && !updatedPassword.isBlank()) {
            saved.setPassword(encoder.encode(updatedPassword));
        }
        try {
            userService.update(saved);
        } catch (CreateUserException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping()
    public ResponseEntity<String> WrongPut() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
