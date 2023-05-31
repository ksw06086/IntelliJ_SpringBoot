package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import com.suncloth.suncloth.repository.SubCategoryRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class SubCategoryApiController {
    @Autowired
    private final SubCategoryRepository repository;

    @Autowired
    private final MainCategoryRepository mainCategoryRepository;

    SubCategoryApiController(SubCategoryRepository repository, MainCategoryRepository mainCategoryRepository) {
        this.repository = repository;
        this.mainCategoryRepository = mainCategoryRepository;
    }

    // GET : SubCategory 테이블 정보 가져오기
    @GetMapping("/subCategories")
    List<SubCategory> all(@RequestParam(required = false, defaultValue = "") String subName) {
        if(StringUtils.isEmpty(subName)){
            return repository.findAll();
        } else {
            return repository.findBySubName(subName);
        }
    }
    // end::get-aggregate-root[]

    // POST : SubCategory 테이블에 정보 삽입하기
    @PostMapping("/subCategory")
    SubCategory newSubCategory(@RequestBody SubCategory newSubCategory) {
        System.out.println(newSubCategory);
        return repository.save(newSubCategory);
    }

    // PUT : MainCode에 맞게 한가지 SubCategory 정보 삽입하기
    @PutMapping("/subCategory/{mainCode}")
    SubCategory insertSubCategory(@RequestBody SubCategory newSubCategory, @PathVariable Long mainCode) {
        MainCategory mainCategory = mainCategoryRepository.findById(mainCode).orElse(null);
        newSubCategory.setMainCategory(mainCategory);
        return repository.save(newSubCategory);
    }

    // Single item

    // GET : mainCode 에 맞게 한가지 Sub Category 정보만 가져오기
    @GetMapping("/subCategories/{mainCode}")
    List<SubCategory> oneAndMany(@PathVariable Long mainCode) {
        MainCategory mainCategory = mainCategoryRepository.findById(mainCode).orElse(null);
        return repository.findByMainCategory(mainCategory);
    }

    // GET : Id에 맞게 한가지 Sub Category 정보만 가져오기
    @GetMapping("/subCategory/{subCode}")
    SubCategory one(@PathVariable Long subCode) {
        return repository.findById(subCode).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Sub Categroy 정보만 갱신
    @PutMapping("/subCategories/{subCode}")
    SubCategory replaceSubCategory(@RequestBody SubCategory newSubCategory, @PathVariable Long subCode) {

        return repository.findById(subCode)
                .map(subCategory -> {
                    subCategory.setSubName(newSubCategory.getSubName());
                    return repository.save(subCategory);
                })
                .orElseGet(() -> {
                    newSubCategory.setSubCode(subCode);
                    return repository.save(newSubCategory);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 SubCategory 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/subCategories/{subCode}")
    void deleteSubCategory(@PathVariable Long subCode) {
        repository.deleteById(subCode);
    }
}
