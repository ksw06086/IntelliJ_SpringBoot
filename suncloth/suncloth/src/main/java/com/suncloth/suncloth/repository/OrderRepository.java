package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Cart;
import com.suncloth.suncloth.model.Order;
import com.suncloth.suncloth.model.Stock;
import com.suncloth.suncloth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

/*
 * 최초 작성자 : 김선우
 * 최초 작성일 : 2023.06.27
 * 최초 변경일 : 2023.06.27
 * 목적 : 쇼핑몰 주문 관련 Repository
 * 개정 이력 : 김선우 - 2023.06.27, 주문 Repository 생성
 * 저작권 : 김선우
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 날짜가 포함된 주문 정보들 가져오기(where reg_date between ?1 and ?2)
    List<Order> findByRegDateBetween(Date firstDay, Date lastDay);

    // Stock 를 기준으로 정보들 가져오기(where stock_id = ?1)
    List<Order> findByOrderStock(Stock stock);

    // User 를 기준으로 정보들 가져오기(where user_id = ?1)
    List<Order> findByOrderUser(User user);
    
    // Stock과 User를 기준으로 정보들 가져오기(where stock_id = ?1 and user_id = ?2)
    Order findByOrderStockAndOrderUser(Stock stock, User user);
}
