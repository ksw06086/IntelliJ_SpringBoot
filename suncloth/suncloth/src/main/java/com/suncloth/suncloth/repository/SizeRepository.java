package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    // Size 이름을 기준으로 정보들 가져오기(where size_name = ?1)
    List<Size> findBySizeName(String sizeName);
}
