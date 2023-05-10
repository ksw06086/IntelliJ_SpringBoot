package com.suncloth.suncloth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/boardList")
    public String noticeList(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/board/guest_boardList";
    }

}
