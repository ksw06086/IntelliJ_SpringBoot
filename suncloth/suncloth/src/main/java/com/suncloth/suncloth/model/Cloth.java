package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name="cloth_tbl")
public class Cloth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long clothId;               // 상품 식별번호
    private String clothName;           // 상품명
    private String content = " ";       // 상품 설명
    private String icon;                // 아이콘명
    private String tex;                 // 관세,비관세 여부
    private long deliDay;               // 배달소요날짜
    private long deliPrice;             // 배달비 금액
    private long basePrice;             // 상품기본 금액
    private String withItemIds = " ";   // 관련 상품들의 Id
    @CreationTimestamp                  // insert 될 때 현재 시간을 넣어줌
    private Date regDate;               // 상품 등록 날짜

    // SubCategory 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "sub_code")
    @JsonIgnore
    private SubCategory subCategory;

    // Brand 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    private Brand brand;

    // Cloth 와 연결된 File(이미지) 들
    @OneToMany(mappedBy = "cloth", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<File> files = new ArrayList<>();

    // Cloth 와 연결된 Stock(재고) 들
    @OneToMany(mappedBy = "stockCloth", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stocks = new ArrayList<>();

    // Cloth 와 연결된 Board(게시판) 들
    @OneToMany(mappedBy = "boardCloth", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();
}
