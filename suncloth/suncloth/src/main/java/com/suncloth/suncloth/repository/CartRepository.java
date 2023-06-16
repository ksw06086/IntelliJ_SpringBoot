package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 날짜가 포함된 장바구니 정보들 가져오기(where reg_date between ?1 and ?2)
    List<Cart> findByRegDateBetween(Date firstDay, Date lastDay);

    // Stock 를 기준으로 정보들 가져오기(where stock_id = ?1)
    List<Cart> findByCartStock(Stock stock);

    // User 를 기준으로 정보들 가져오기(where user_id = ?1)
    List<Cart> findByCartUser(User user);
    
    // Stock과 User를 기준으로 정보들 가져오기(where stock_id = ?1 and user_id = ?2)
    Cart findByCartStockAndCartUser(Stock stock, User user);
}
