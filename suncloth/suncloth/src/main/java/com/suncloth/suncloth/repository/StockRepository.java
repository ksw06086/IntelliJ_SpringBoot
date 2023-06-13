package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    // Size 를 기준으로 정보들 가져오기(where size_code = ?1)
    Stock findByStockSizeAndStockColor(Size size, Color color);

    // Cloth 를 기준으로 관련 Color 정보들 Distinct 해서 가져오기
    @Query("select distinct s0.stockColor from Stock s0 where s0.stockCloth = :cloth")
    List<Color> findColorByStockCloth(Cloth cloth);

    // Cloth 와 Color 를 기준으로 관련 Stock 의 Size 정보들 가져오기
    @Query("select s0.stockSize from Stock s0 where s0.stockColor = :color and s0.stockCloth = :cloth")
    List<Size> findSizeByStockColorAndStockCloth(Color color, Cloth cloth);

}
