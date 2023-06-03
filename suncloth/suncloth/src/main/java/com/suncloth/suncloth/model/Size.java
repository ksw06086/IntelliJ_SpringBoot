package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="size_tbl")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long sizeCode;               // Size 식별번호
    private String sizeName;             // Size Name

    // Size 와 연결된 Stock 들
    @OneToMany(mappedBy = "stockSize", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList = new ArrayList<>();
}
