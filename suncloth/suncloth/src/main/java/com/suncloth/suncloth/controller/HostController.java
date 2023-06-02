package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.repository.BrandRepository;
import com.suncloth.suncloth.repository.ClothRepository;
import com.suncloth.suncloth.repository.FileRepository;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    @Autowired
    BrandRepository brandRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    ClothRepository clothRepository;
    @Autowired
    FileRepository fileRepository;

    /* 상품 */
    // 상품 리스트
    @GetMapping("/productList")
    public String productList(Model model) {
        List<Cloth> cloths = clothRepository.findAll();
        model.addAttribute("clothList", cloths);
        return "/host/product/host_productList";
    }
    // 상품 등록
    @GetMapping("/productInput")
    public String productInput(Model model) {
        List<Brand> brands = brandRepository.findAll();
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();

        model.addAttribute("brandList", brands);
        model.addAttribute("mainCategoryList", mainCategories);

        return "/host/product/host_productInput";
    }
    // 상품 수정
    @GetMapping("/productView")
    public String productView(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/host/product/host_productView";
    }

    /* 브랜드 */
    // 브랜드 목록
    @GetMapping("/brandList")
    public String brandList(Model model) {
        List<Brand> brandList = brandRepository.findAll();
        model.addAttribute("brandList", brandList);
        return "/host/product/host_brandList";
    }
    // 브랜드 등록/수정
    @GetMapping("/brandInput")
    public String brandInput(Model model
            , @RequestParam(required = false) String name
            , @RequestParam(required = false) Long brandId) {
        Brand brand;
        if(brandId == null) { brand = new Brand(); }
        else { brand = brandRepository.findById(brandId).orElse(null); }
        model.addAttribute("brand", brand);
        model.addAttribute("name", name);
        return "/host/product/host_brandInput";
    }

    /* 주문 */
    // 주문 목록
    @GetMapping("/orderList")
    public String orderList() {
        return "/host/order/host_orderList";
    }

    /* 회원 */
    // 회원 목록
    @GetMapping("/memberList")
    public String memberList() {
        return "/host/member/host_memberList";
    }
    // 회원 수정
    @GetMapping("/memberView")
    public String memberView() {
        return "/host/member/host_memberView";
    }

    /* 운영 */
    // 모든 게시판 리스트
    @GetMapping("/boardAllList")
    public String boardAllList() {
        return "/host/board/host_boardAllList";
    }
    // 게시판 리스트
    @GetMapping("/boardList")
    public String boardList(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/host/board/host_boardList";
    }
    // 게시판 등록
    @GetMapping("/boardInput")
    public String boardInput(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/host/board/host_boardInput";
    }
    // 게시판 수정/삭제
    @GetMapping("/boardView")
    public String boardView(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/host/board/host_boardView";
    }

    /* 통계 */
    // 방문자 횟수 분석
    @GetMapping("/clickTotal")
    public String clickTotal() {
        return "/host/total/host_clickTotal";
    }
    // 신규회원 분석
    @GetMapping("/newMemberTotal")
    public String newMemberTotal() {
        return "/host/total/host_newMemberTotal";
    }
    // 회원 적립금 분석
    @GetMapping("/memberPlusPay")
    public String memberPlusPay() {
        return "/host/total/host_memberPlusPay";
    }
    // 카테고리 분석
    @GetMapping("/category")
    public String category() {
        return "/host/total/host_category";
    }
    // 판매순위 분석
    @GetMapping("/saleRank")
    public String saleRank() {
        return "/host/total/host_saleRank";
    }
    // 주문 통계
    @GetMapping("/orderTotal")
    public String orderTotal() {
        return "/host/total/host_orderTotal";
    }
    // 매출 통계
    @GetMapping("/saleTotal")
    public String saleTotal() {
        return "/host/total/host_saleTotal";
    }
}
