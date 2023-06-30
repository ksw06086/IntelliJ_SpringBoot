package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Order;
import com.suncloth.suncloth.model.OrderStock;
import com.suncloth.suncloth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
public interface OrderStockRepository extends JpaRepository<OrderStock, Long> {
    // order_id와 관련된 정보들 가져오기
    List<OrderStock> findByOrderId(Long orderId);
}
