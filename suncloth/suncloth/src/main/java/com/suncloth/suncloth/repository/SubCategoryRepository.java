package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

}
