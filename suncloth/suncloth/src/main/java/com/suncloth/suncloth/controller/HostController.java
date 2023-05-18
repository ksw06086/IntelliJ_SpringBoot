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
    public String findRoad() {
        return "/host/host_productList";
    }
}
