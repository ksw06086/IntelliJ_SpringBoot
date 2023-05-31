package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    // Sub Category 이름을 기준으로 정보들 가져오기(where sub_name = ?1)
    List<SubCategory> findBySubName(String subName);

    // Main Category 를 기준으로 정보들 가져오기(where main_code = ?1)
    List<SubCategory> findByMainCategory(MainCategory mainCategory);
}
