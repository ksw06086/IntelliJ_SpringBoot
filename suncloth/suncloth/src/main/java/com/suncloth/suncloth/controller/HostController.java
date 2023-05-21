package com.suncloth.suncloth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/host")
public class HostController {
    @GetMapping("/productList")
    public String productList() {
        return "/host/host_productList";
    }

    @GetMapping("/productInput")
    public String productInput() {
        return "/host/host_productInput";
    }

    @GetMapping("/productView")
    public String productView(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/host/host_productView";
    }
}
