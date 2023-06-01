package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Brand;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothRepository extends JpaRepository<Cloth, Long> {
    // 상품명을 기준으로 정보들 가져오기(where cloth_name = ?1)
    List<Cloth> findByClothName(String clothName);

    // Sub Category 를 기준으로 정보들 가져오기(where sub_code = ?1)
    List<Cloth> findBySubCategory(SubCategory subCategory);

    // Brand 를 기준으로 정보들 가져오기(where brand_id = ?1)
    List<Cloth> findByBrand(Brand brand);
}
