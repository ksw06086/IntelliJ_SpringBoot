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

    @GetMapping("/cartList")
    public String cartList() {
        return "/guest/guest_cartList";
    }

    @GetMapping("/myPage")
    public String myPage() {
        return "/guest/guest_myPage";
    }

    @GetMapping("/mileage")
    public String mileage() {
        return "/guest/guest_mileage";
    }

    @GetMapping("/orderList")
    public String orderList() {
        return "/guest/guest_orderList";
    }

    @GetMapping("/productDetail")
    public String productDetail() {
        return "/guest/guest_productDetail";
    }

    @GetMapping("/orderForm")
    public String orderForm() {
        return "/guest/guest_orderForm";
    }

    @GetMapping("/findRoad")
    public String findRoad() {
        return "/guest/guest_findRoad";
    }
}
