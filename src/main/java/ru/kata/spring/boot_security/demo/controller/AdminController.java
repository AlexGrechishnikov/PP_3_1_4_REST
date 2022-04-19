package ru.kata.spring.boot_security.demo.controller;

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

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUsers(ModelMap model) {
        List<User> users = userService.getUsersList();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping(value = "/{id}")
    public String getUser(ModelMap model, @PathVariable(name = "id") long id) {
        User user = userService.get(id);
        model.addAttribute(user);
        return "admin/user";
    }

    @GetMapping(value = "/new")
    public String getNewUser(@ModelAttribute("user") User user) {
        user.setRoles(userService.getAvailableRoles());
        return "admin/new";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            user.setRoles(userService.getAvailableRoles());
            return "admin/new";
        }
        Set<Role> roles = userService.getAvailableRoles();
        roles.retainAll(user.getAuthorities());
        user.setRoles(roles);
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String getEditUser(ModelMap modelMap, @PathVariable("id") long id) {
        modelMap.addAttribute("user", userService.get(id));
        return "admin/edit";
    }

    @PatchMapping()
    public String updateUser(@ModelAttribute(name = "user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("name")
        || bindingResult.hasFieldErrors("lastName")
        || bindingResult.hasFieldErrors("age")) {
            return "admin/edit";
        }
        User saved = userService.get(user.getId());
        saved.setAge(user.getAge());
        saved.setName(user.getName());
        saved.setLastName(user.getLastName());
        userService.update(saved);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@ModelAttribute(name = "user") User user, @PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}