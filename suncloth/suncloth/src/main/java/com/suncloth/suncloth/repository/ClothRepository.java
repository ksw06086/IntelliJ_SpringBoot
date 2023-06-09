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

    // Sub Category 를 기준으로 정보들 가져오기(where sub_code = ?1)
    List<Cloth> findBySubCategory(SubCategory subCategory);

    // Brand 를 기준으로 정보들 가져오기(where brand_id = ?1)
    List<Cloth> findByBrand(Brand brand);

    @Query("select c from Cloth c where " +
            "c.clothName like %?1% and c.brand = nvl(?2, c.brand) " +
            "and c.subCategory.mainCategory = nvl(?3, c.subCategory.mainCategory) and c.subCategory = nvl(?4, c.subCategory) " +
            "and c.icon in (select DISTINCT nvl(max(c_cp.icon), c.icon) from Cloth c_cp where c_cp.icon in ?5)")
    List<Cloth> findByQuery1(String clothName, Long brandId, Long mainCategoryId, Long subCategoryId, List<String> icons);

    // Cloth 검색
    @Query("select u from User u where u.username like %?1%")
    List<Cloth> findBySearchQuery(long mainCategoryId
            , long subCategoryId
            , long brandId
            , Date startDay
            , Date endDay
            , String icon
            , String clothName
            , long clothId);

}
