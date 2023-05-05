package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "account/guest_login";
    }

    @GetMapping("/register")
    public String register() {
        return "account/guest_register";
    }

    @PostMapping("/register")
    public String register(User user) {
        userService.save(user);
        System.out.println("user = " + user);
        return "redirect:/main";
    }
}
