package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.repository.BrandRepository;
import com.suncloth.suncloth.repository.BrandRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class BrandApiController {
    @Autowired
    private final BrandRepository repository;

    BrandApiController(BrandRepository repository) {
        this.repository = repository;
    }

    // GET : Brand 테이블 정보 가져오기
    @GetMapping("/brands")
    List<Brand> all(@RequestParam(required = false, defaultValue = "") String brandName) {
        if(StringUtils.isEmpty(brandName)){
            return repository.findAll();
        } else {
            return repository.findByBrandName(brandName);
        }
    }
    // end::get-aggregate-root[]

    // POST : Brand 테이블에 정보 삽입하기
    @PostMapping("/brand")
    Brand newBrand(@RequestBody Brand newBrand) {
        System.out.println(newBrand);
        return repository.save(newBrand);
    }

    // Single item

    // GET : Id에 맞게 한가지 Brand 정보만 가져오기
    @GetMapping("/brands/{brandId}")
    Brand one(@PathVariable Long brandId) {

        return repository.findById(brandId).orElse(null);
    }

    // PUT : Id에 맞게 한가지 brand 정보만 갱신
    @PutMapping("/brands/{brandId}")
    Brand replaceBrand(@RequestBody Brand newBrand, @PathVariable Long brandId) {

        return repository.findById(brandId)
                .map(brand -> {
                    brand.setBrandName(newBrand.getBrandName());
                    return repository.save(brand);
                })
                .orElseGet(() -> {
                    newBrand.setBrandId(brandId);
                    return repository.save(newBrand);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Brand 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/brands/{brandId}")
    void deleteBrand(@PathVariable Long brandId) {
        repository.deleteById(brandId);
    }
}
