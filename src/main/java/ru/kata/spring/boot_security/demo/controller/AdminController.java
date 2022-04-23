package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public AdminController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping()
    public String getUsers(ModelMap model, Authentication authentication) {
        List<User> users = userService.getUsersList();
        User principal = (User) authentication.getPrincipal();
        model.addAttribute("user", principal);
        model.addAttribute("users", users);
        User e = new User();
        e.setRoles(userService.getAvailableRoles());
        model.addAttribute("e", e);
        return "adminPage";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("e") @Valid User user, BindingResult bindingResult) {
        Set<Role> roles = userService.getAvailableRoles();
        roles.retainAll(user.getAuthorities());
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.add(user);
        return "redirect:/admin";
    }

    @PatchMapping()
    public String updateUser(@ModelAttribute(name = "e") @Valid User e, BindingResult bindingResult) {
        User saved = userService.get(e.getId());
        saved.setAge(e.getAge());
        saved.setName(e.getName());
        saved.setLastName(e.getLastName());
        saved.setEmail(e.getEmail());
        if (!e.getPassword().isEmpty()) {
            saved.setPassword(encoder.encode(e.getPassword()));
        }
        Set<Role> roles = userService.getAvailableRoles();
        roles.retainAll(e.getAuthorities());
        saved.setRoles(roles);
        userService.update(saved);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}