package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="stock_tbl")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long stockId;               // 재고 식별번호
    private String state;               // 재고 상태
    private long stockCount;            // 재고 수량
    private long stockMaxCount;         // 최대 구매 가능 수량
    private long plus;                  // 적립금
    private long salePrice;             // 판매금액
    private long buyPrice;              // 원가금액
    @CreationTimestamp                  // insert 될 때 현재 시간을 넣어줌
    private Date regDate;               // 상품 등록 날짜

    // SubCategory 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "cloth_id")
    @JsonIgnore
    private Cloth stockCloth;

    // Color 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "color_code")
    @JsonIgnore
    private Color stockColor;

    // Size 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "size_code")
    @JsonIgnore
    private Size stockSize;
}
