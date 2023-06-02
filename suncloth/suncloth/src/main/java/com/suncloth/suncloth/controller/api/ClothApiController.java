package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import com.suncloth.suncloth.repository.BrandRepository;
import com.suncloth.suncloth.repository.ClothRepository;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import com.suncloth.suncloth.repository.SubCategoryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ClothApiController {
    @Autowired
    private final SubCategoryRepository subCategoryRepository;
    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final ClothRepository clothRepository;

    ClothApiController(SubCategoryRepository subCategoryRepository
            , ClothRepository clothRepository
            , BrandRepository brandRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.clothRepository = clothRepository;
        this.brandRepository = brandRepository;
    }

    // GET : Cloth 테이블 정보 가져오기
    @GetMapping("/cloths")
    List<Cloth> all(@RequestParam(required = false, defaultValue = "") String clothName) {
        if(StringUtils.isEmpty(clothName)){
            return clothRepository.findAll();
        } else {
            return clothRepository.findByClothName(clothName);
        }
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

    // PUT : Brand, SubCategory 에 맞게 한가지 Cloth 정보 삽입하기
    @PutMapping("/cloth/{brandId}/{subCode}")
    Cloth insertCloth(@RequestBody Cloth newCloth
            , @PathVariable Long brandId
            , @PathVariable Long subCode) {
        SubCategory subCategory = subCategoryRepository.findById(subCode).orElse(null);
        Brand brand = brandRepository.findById(brandId).orElse(null);
        newCloth.setSubCategory(subCategory);
        newCloth.setBrand(brand);
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
