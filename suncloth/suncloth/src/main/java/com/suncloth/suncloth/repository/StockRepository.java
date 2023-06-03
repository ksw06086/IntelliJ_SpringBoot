package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // state 를 기준으로 정보들 가져오기(where state = ?1)
    List<Stock> findByState(String state);

    // Cloth 를 기준으로 정보들 가져오기(where cloth_id = ?1)
    List<Stock> findByStockCloth(Cloth cloth);

    // Color 를 기준으로 정보들 가져오기(where color_code = ?1)
    List<Stock> findByStockColor(Color color);

    // Size 를 기준으로 정보들 가져오기(where size_code = ?1)
    List<Stock> findByStockSize(Size size);
}
