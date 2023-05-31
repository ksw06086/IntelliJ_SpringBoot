package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import com.suncloth.suncloth.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainCategoryApiController {
    @Autowired
    private final MainCategoryRepository repository;

    MainCategoryApiController(MainCategoryRepository repository) {
        this.repository = repository;
    }

    // GET : MainCategory 테이블 정보 가져오기
    @GetMapping("/mainCategories")
    List<MainCategory> all(@RequestParam(required = false, defaultValue = "") String mainName) {
        if(StringUtils.isEmpty(mainName)){
            return repository.findAll();
        } else {
            return repository.findByMainName(mainName);
        }
    }
    // end::get-aggregate-root[]

    // POST : MainCategory 테이블에 정보 삽입하기
    @PostMapping("/mainCategory")
    MainCategory newMainCategory(@RequestBody MainCategory newMainCategory) {
        System.out.println(newMainCategory);
        return repository.save(newMainCategory);
    }

    // Single item

    // GET : Id에 맞게 한가지 Main Category 정보만 가져오기
    @GetMapping("/mainCategories/{mainCode}")
    MainCategory one(@PathVariable Long mainCode) {

        return repository.findById(mainCode).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Main Categroy 정보만 갱신
    @PutMapping("/mainCategories/{mainCode}")
    MainCategory replaceMainCategory(@RequestBody MainCategory newMainCategory, @PathVariable Long mainCode) {

        return repository.findById(mainCode)
                .map(mainCategory -> {
                    mainCategory.setMainName(newMainCategory.getMainName());
                    return repository.save(mainCategory);
                })
                .orElseGet(() -> {
                    newMainCategory.setMainCode(mainCode);
                    return repository.save(newMainCategory);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 MainCategory 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/mainCategories/{mainCode}")
    void deleteMainCategory(@PathVariable Long mainCode) {
        repository.deleteById(mainCode);
    }
}
