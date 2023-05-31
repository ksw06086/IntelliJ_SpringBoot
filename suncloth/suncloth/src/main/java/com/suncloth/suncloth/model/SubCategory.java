package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="sub_category_tbl")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long subCode;                      // Sub Category 식별번호
    private String subName;                    // Sub Category Name

    // MainCategory 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "main_code")
    @JsonIgnore
    private MainCategory mainCategory;
}
