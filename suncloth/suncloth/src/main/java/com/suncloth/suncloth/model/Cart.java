package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@Entity
@Table(name="cart_tbl")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long cartId;                // 장바구니 목록 식별번호
    private long count;                 // 장바구니에 추가한 재고 수
    @CreationTimestamp                  // insert 될 때 현재 시간을 넣어줌
    private Date regDate;               // 장바구니 등록 날짜

    // SubCategory 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User cartUser;

    // Brand 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "stock_id")
    @JsonIgnore
    private Stock cartStock;
}
