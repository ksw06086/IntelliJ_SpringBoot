package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.*;
import com.suncloth.suncloth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    ClothRepository clothRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    CartRepository cartRepository;

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

        int clothOneBlockCnt = 4;                                               // 한 줄에 들어갈 상품의 개수
        int clothLastBlockCnt = (clothList.size()+3)%clothOneBlockCnt;          // 마지막 줄에 들어갈 상품의 개수(1=0,2=1,...이 나오게 하기 위해 +3)
        int clothBlockLineCnt = (clothList.size()+3)/clothOneBlockCnt;          // 줄 수(1=1,2=1,..,4=1이 나오게 하기 위해 +3)
        model.addAttribute("clothOneBlockCnt", clothOneBlockCnt-1);
        model.addAttribute("clothLastBlockCnt", clothLastBlockCnt);
        model.addAttribute("clothBlockLineCnt", clothBlockLineCnt-1);

        model.addAttribute("mainCategory", mainCategory);
        model.addAttribute("subCategoryList", subCategoryList);
        model.addAttribute("clothList", clothList);
        model.addAttribute("category", category);
        return "/guest/guest_category";
    }

    @GetMapping("/cartList")
    public String cartList(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);
        List<Cart> cartList = cartRepository.findByCartUser(user);

        model.addAttribute("cartList", cartList);
        model.addAttribute("user", user);
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
    public String productDetail(Model model
            , @RequestParam(required = false) Long clothId) {
        Cloth cloth = clothRepository.findById(clothId).orElse(null);
        List<File> mainFile = fileRepository.findByClothAndFileType(cloth, "main");
        List<File> subFile = fileRepository.findByClothAndFileType(cloth, "sub");
        List<Color> colorList = stockRepository.findColorByStockCloth(cloth);
        for (Color color : colorList) {
            System.out.println("color : " + color.getColorName());
        }

        model.addAttribute("cloth", cloth);
        model.addAttribute("mainFile", mainFile.get(0));
        model.addAttribute("subFileList", subFile);
        model.addAttribute("colorList", colorList);
        return "/guest/guest_productDetail";
    }

    @GetMapping("/orderForm")
    public String orderForm(Model model
            , @RequestParam(required = false) List<Long> stockIds
            , @RequestParam(required = false) List<Long> counts) {
        // stockIds, counts 확인 출력
        System.out.println("stockIds : " + stockIds);
        System.out.println("counts : " + counts);

        // Map 생성하여 stock과 count를 넣어줌 -> 리스트에 추가해줌
        List<Map<String, Object>> orderMapList = new ArrayList<>();

        // 1개 이상의 stock이 넘겨져 왔을 경우 반복해줌
        for (int i = 0; i < stockIds.size(); i++) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("stock", stockRepository.findById(stockIds.get(i)).orElse(null));
            orderMap.put("count", counts.get(i));
            System.out.println("stock : " + orderMap.get("stock") + ", count : " + orderMap.get("count"));
            orderMapList.add(orderMap);
        }

        // 잘 가져왔는지 0개인지 확인
        System.out.println("orderMapList : " + orderMapList.size());

        // 시큐리티 이용해 로그인 유저의 username 가져와 user 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);

        model.addAttribute("orderMapList", orderMapList);
        model.addAttribute("user", user);

        return "/guest/guest_orderForm";
    }

    @GetMapping("/findRoad")
    public String findRoad() {
        return "/guest/guest_findRoad";
    }
}
