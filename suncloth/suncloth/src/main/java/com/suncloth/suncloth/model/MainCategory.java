package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="main_category_tbl")
public class MainCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long mainCode;               // Sub Category 식별번호
    private String mainName;             // Sub Category Name

    // MainCategory 와 연결된 SubCategory 들
    @OneToMany(mappedBy = "mainCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SubCategory> subCategoryList = new ArrayList<>();
}
