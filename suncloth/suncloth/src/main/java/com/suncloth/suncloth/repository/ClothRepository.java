package com.suncloth.suncloth.repository;

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

    @Query("select c from Cloth c where c.clothName like %?1% and c.icon like %?2% and c.clothId = ?3 and c.regDate between ?4 and ?5 and c.brand = case when ?6 is null then c.brand else ?6 end")
    List<Cloth> findByQuery1(String clothName, String icon);

    @Query("select c from Cloth c where c.brand = nvl(?1,c.brand)")
    List<Cloth> findByQuery2(long brandId);

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
