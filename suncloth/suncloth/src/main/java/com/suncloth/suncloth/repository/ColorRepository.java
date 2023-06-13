package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Color;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    // Color 이름을 기준으로 정보들 가져오기(where color_name = ?1)
    List<Color> findByColorName(String colorName);
}
