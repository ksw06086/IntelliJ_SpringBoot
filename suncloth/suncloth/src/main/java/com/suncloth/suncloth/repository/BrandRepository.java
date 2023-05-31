package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    // 브랜드 이름을 기준으로 정보들 가져오기(where brand_name = ?1)
    List<Brand> findByBrandName(String brandName);
}
