package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.RefundAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    // Main Category 이름을 기준으로 정보들 가져오기(where main_name = ?1)
    MainCategory findByMainName(String mainName);
}
