package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="refundAcc_tbl")
public class RefundAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long id;                        // Refund Account 식별번호
    private String accHost = null;         // Account Host
    private String bankName = null;        // Bank Name
    private String accNumber = null;       // Account Number

    // User 테이블과 조인
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
