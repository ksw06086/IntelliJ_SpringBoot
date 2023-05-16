package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String register(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "account/guest_register";
    }

    @PostMapping("/register_finish")
    public String registerFinish(Model model, User user) {
        userService.save(user);
        System.out.println("user = " + user);
        model.addAttribute("user", user);
        return "account/guest_register_finish";
    }

    @GetMapping("/register_finish")
    public String registerFinish() {
        return "account/guest_register_finish";
    }
}
