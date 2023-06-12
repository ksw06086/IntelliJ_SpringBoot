package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import com.suncloth.suncloth.repository.ClothRepository;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import com.suncloth.suncloth.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    ClothRepository clothRepository;


    @GetMapping("/category")
    public String categoryPage(Model model
            , @RequestParam(required = false) String category
            , @RequestParam(required = false) Long subCategoryId) {
        MainCategory mainCategory = mainCategoryRepository.findByMainName(category);
        List<SubCategory> subCategoryList = subCategoryRepository.findByMainCategory(mainCategory);
        String subCategoryName = subCategoryList.get(0).getSubName();
        List<Cloth> clothList = clothRepository.findBySubCategory(subCategoryList.get(0));
        if(subCategoryId != null){
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
            subCategoryName = subCategory.getSubName();
            clothList = clothRepository.findBySubCategory(subCategory);
        }

        model.addAttribute("mainCategory", mainCategory);
        model.addAttribute("subCategoryList", subCategoryList);
        model.addAttribute("clothList", clothList);
        model.addAttribute("category", category);
        model.addAttribute("subCategoryName", subCategoryName);
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
