package com.suncloth.suncloth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;

/*
* 최초 작성자 : 김선우
* 최초 작성일 : 2023.06.27
* 최초 변경일 : 2023.06.27
* 목적 : 쇼핑몰 주문 내역 저장
* 개정 이력 : 김선우 - 2023.06.27, 주문 Model 생성
* 저작권 : 김선우
*/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderStockPK implements Serializable {
    private long orderId;               // 주문 목록 식별번호
    private long stockId;               // 재고 목록 식별번호
}
