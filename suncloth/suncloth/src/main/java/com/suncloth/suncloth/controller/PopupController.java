package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.repository.ClothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/popup")
public class PopupController {

    @Autowired
    ClothRepository clothRepository;

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

    @GetMapping("/withItemsSelect")
    public String withItemsSelect(Model model
            , @RequestParam(required = false) String name
            , @PageableDefault(size = 5) Pageable pageable) {
        Page<Cloth> cloths = clothRepository.findAll(pageable);

        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, cloths.getPageable().getPageNumber()-1);
        int endPage = Math.min(cloths.getTotalPages(), cloths.getPageable().getPageNumber()+4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("clothList", cloths);

        model.addAttribute("name", name);
        return "/popup/withItemsSelect";
    }

}
