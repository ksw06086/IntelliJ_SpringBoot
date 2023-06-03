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
@Table(name="color_tbl")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long colorCode;               // Color 식별번호
    private String colorName;             // Color Name

    // Color 와 연결된 Stock 들
    @OneToMany(mappedBy = "stockColor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList = new ArrayList<>();
}
