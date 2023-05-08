package com.suncloth.suncloth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/guest")
public class GuestController {
    @GetMapping("/category")
    public String categoryPage(Model model
            , @RequestParam(required = false) String category) {
        if("outer".equals(category)){
            System.out.println("model = " + model + ", category = " + category);
        } else {
            System.out.println("model = " + model + ", category = " + category);
        }
        model.addAttribute("category", category);
        return "/guest/guest_category";
    }

    @GetMapping("/cartlist")
    public String cartlist(Model model) {
        return "/guest/guest_cartlist";
    }

}
