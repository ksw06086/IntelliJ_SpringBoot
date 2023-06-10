package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import com.suncloth.suncloth.repository.*;
import com.suncloth.suncloth.repository.querydsl.ClothRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class ClothApiController {
    @Autowired
    private final MainCategoryRepository mainCategoryRepository;
    @Autowired
    private final SubCategoryRepository subCategoryRepository;
    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final ClothRepository clothRepository;
    @Autowired
    private final ClothRepositoryImpl clothRepositoyImpl;

    ClothApiController(MainCategoryRepository mainCategoryRepository
            ,SubCategoryRepository subCategoryRepository
            , ClothRepository clothRepository
            , BrandRepository brandRepository
            , ClothRepositoryImpl clothRepositoyImpl) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.clothRepository = clothRepository;
        this.brandRepository = brandRepository;
        this.clothRepositoyImpl = clothRepositoyImpl;
    }

    // GET : Cloth 테이블 정보 가져오기
    @GetMapping("/cloths")
    Map<String, Object> all(@RequestParam(required = false) String searchType       // 검색 타입
            , @RequestParam(required = false) String searchInput            // 검색 TextInput value
            , @RequestParam(required = false) List<String> icons            // 아이콘
            , @RequestParam(required = false) Long brandId                  // 브랜드 식별자
            , @RequestParam(required = false) Long mainCategoryId           // 메인 카테고리 식별자
            , @RequestParam(required = false) Long subCategoryId            // 서브 카테고리 식별자
            , @RequestParam(required = false) String firstDay               // 시작 날짜
            , @RequestParam(required = false) String lastDay                // 끝 날짜
            , @PageableDefault(size = 3) Pageable pageable) {               // 페이지 객체

        Brand brand = brandRepository.findById(brandId).orElse(null);
        MainCategory mainCategory = mainCategoryRepository.findById(mainCategoryId).orElse(null);
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);

        Page<Cloth> clothList = clothRepositoyImpl.findSearchAll(searchType, searchInput, brand, mainCategory, subCategory, icons, firstDay, lastDay, pageable);
        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, clothList.getPageable().getPageNumber()-4);
        int endPage = Math.min(clothList.getTotalPages(), clothList.getPageable().getPageNumber()+4);

        // 각 프로퍼티 결과 출력
        System.out.println("searchType : " + searchType + ", searchInput : " + searchInput + ", icons : " + icons +
                ", brand : " + brand + ", mainCategory : " + mainCategory + ", subCategory : " + subCategory +
                ", firstDay : " + firstDay + ", lastDay : " + lastDay + ", pageable : " + pageable);

        List<Map<String, Object>> clothMap = new ArrayList<>();
        for (Cloth cloth : clothList) {
            clothMap.add(new HashMap<>());
            clothMap.get(clothMap.size()-1).put("cloth", cloth);
            clothMap.get(clothMap.size()-1).put("brand", cloth.getBrand());
            clothMap.get(clothMap.size()-1).put("subCategory", cloth.getSubCategory());
        }

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("contentList", clothMap);
        pageMap.put("pageObject", clothList);
        pageMap.put("startPage", startPage);
        pageMap.put("endPage", endPage);
        return pageMap;
    }
    // end::get-aggregate-root[]

    // POST : Cloth 테이블에 정보 삽입하기
    @PostMapping("/cloth")
    Cloth newCloth(Cloth newCloth, Long brandId, Long subCategoryId) {
        Brand brand = brandRepository.findById(brandId).orElse(null);
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);

        newCloth.setBrand(brand);
        newCloth.setSubCategory(subCategory);

        System.out.println(newCloth);
        return clothRepository.save(newCloth);
    }

    // Single item

    // GET : brandId 에 맞게 Cloth 정보 가져오기
    @GetMapping("/cloths/brand/{brandId}")
    List<Cloth> oneBrandAndMany(@PathVariable Long brandId) {
        Brand brand = brandRepository.findById(brandId).orElse(null);
        return clothRepository.findByBrand(brand);
    }

    // GET : subCode 에 맞게 Cloth 정보 가져오기
    @GetMapping("/cloths/subCategory/{subCode}")
    List<Cloth> oneSubCategoryAndMany(@PathVariable Long subCode) {
        SubCategory subCategory = subCategoryRepository.findById(subCode).orElse(null);
        return clothRepository.findBySubCategory(subCategory);
    }

    // GET : Id에 맞게 한가지 Cloth 정보만 가져오기
    @GetMapping("/cloth/{clothId}")
    Cloth one(@PathVariable Long clothId) {
        return clothRepository.findById(clothId).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Cloth 정보만 갱신
    @PutMapping("/cloth/{clothId}")
    Cloth replaceCloth(@RequestBody Cloth newCloth, @PathVariable Long clothId) {

        return clothRepository.findById(clothId)
                .map(cloth -> {
                    cloth.setClothName(newCloth.getClothName());
                    return clothRepository.save(cloth);
                })
                .orElseGet(() -> {
                    newCloth.setClothId(clothId);
                    return clothRepository.save(newCloth);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Cloth 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/cloths/{clothId}")
    void deleteCloth(@PathVariable Long clothId) {
        clothRepository.deleteById(clothId);
    }

}
