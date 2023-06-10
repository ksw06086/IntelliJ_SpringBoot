package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.RefundAccount;
import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "account/guest_login";
    }

    // 로그인 완료(방문 횟수 증가)
    @GetMapping("/loginComplete")
    public String loginComplete() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        userService.userVisitCntPlus(username);

        return "redirect:/main";
    }

    @GetMapping("/register")
    public String register(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        model.addAttribute("userDTO", new User());
        model.addAttribute("refundAccDTO", new RefundAccount());
        return "account/guest_register";
    }

    @PostMapping("/register_finish")
    public String registerFinish(Model model, User user, RefundAccount refundAccount) {
        System.out.println("user = " + user);
        userService.save(user, refundAccount);
        model.addAttribute("user", user);
        return "account/guest_register_finish";
    }

    @GetMapping("/register_finish")
    public String registerFinish() {
        return "account/guest_register_finish";
    }
}
