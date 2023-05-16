package com.suncloth.suncloth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/popup")
public class PopupController {
    @GetMapping("/payDetail")
    public String payDetail(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/popup/guest_payDetail";
    }

    @GetMapping("/refundBankChange")
    public String refundBankChange(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/popup/guest_refundBankChange";
    }

    @GetMapping("/payListRecive")
    public String payListRecive(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/popup/guest_payListRecive";
    }

}
