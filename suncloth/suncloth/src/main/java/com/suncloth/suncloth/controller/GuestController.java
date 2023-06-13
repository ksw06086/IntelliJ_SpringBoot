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

import java.util.ArrayList;
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
            , @RequestParam(required = false) Long subCategoryId
            , @RequestParam(required = false) String searchText) {
        MainCategory mainCategory = mainCategoryRepository.findByMainName(category);
        List<SubCategory> subCategoryList = subCategoryRepository.findByMainCategory(mainCategory);
        List<Cloth> clothList = new ArrayList<>();

        if(category.equals("best")){            // best 카테고리일 경우
            clothList = clothRepository.findByIcon("best");
        } else if(category.equals("search")){   // 검색 했을 경우
            clothList = clothRepository.findByClothNameContains(searchText);
            model.addAttribute("searchText", searchText);
        } else {                                // 그 외 카테고리일 경우
            if (subCategoryId == null) { subCategoryId = subCategoryList.get(0).getSubCode(); }
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
            String subCategoryName = subCategory.getSubName();
            clothList = clothRepository.findBySubCategory(subCategory);
            model.addAttribute("subCategoryName", subCategoryName);
        }

        model.addAttribute("mainCategory", mainCategory);
        model.addAttribute("subCategoryList", subCategoryList);
        model.addAttribute("clothList", clothList);
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
