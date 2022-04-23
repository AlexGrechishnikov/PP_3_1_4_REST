package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @GetMapping()
    public String getUser(ModelMap model, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        model.addAttribute(principal);
        return "userPage";
    }
}