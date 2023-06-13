package com.suncloth.suncloth.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.suncloth.suncloth.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ClothRepository extends JpaRepository<Cloth, Long> {
    // 상품명을 기준으로 정보들 가져오기(where cloth_name = ?1)
    List<Cloth> findByClothName(String clothName);

    // 상품 이름에 검색한 Text가 포함된 상품 정보들 가져오기(where cloth_name like %?1%)
    List<Cloth> findByClothNameContains(String searchText);

    // Sub Category 를 기준으로 정보들 가져오기(where sub_code = ?1)
    List<Cloth> findBySubCategory(SubCategory subCategory);

    // Brand 를 기준으로 정보들 가져오기(where brand_id = ?1)
    List<Cloth> findByBrand(Brand brand);

    // icon을 기준으로 상품 정보들 가져오기(where icon = ?1)
    List<Cloth> findByIcon(String icon);

}
