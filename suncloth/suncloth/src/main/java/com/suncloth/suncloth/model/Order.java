package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Date;

/*
* 최초 작성자 : 김선우
* 최초 작성일 : 2023.06.27
* 최초 변경일 : 2023.06.27
* 목적 : 쇼핑몰 주문 내역 저장
* 개정 이력 : 김선우 - 2023.06.27, 주문 Model 생성
* 저작권 : 김선우
*/
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@Entity
@Table(name="order_tbl")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long orderId;               // 주문 목록 식별번호
    private String merchantUid;         // 주문 번호
    private long usePlus;               // 주문할 때 사용한 적립금
    private long realPrice;             // 주문 최종 금액
    private String orderState;          // 주문 상태
    // 배송 관련
    private String addressNum;          // 우편번호
    private String addressSub;          // 주소 Main 텍스트
    private String addressDetail;       // 주소 Detail 텍스트
    private String hp;                  // 핸드폰 번호
    private String reserveHp;           // 서브 전화번호(집, 회사 등등)
    private String emailIdName;         // 이메일 아이디 명
    private String emailUrlCode;        // 이메일 URL 명
    private String userMessage;         // 고객 메세지
    // 결제방식 관련
    private String depositName;         // 계좌번호
    private String depositAccount;      // 은행명
    private String payOption;           // 결제방식
    @CreationTimestamp                  // insert 될 때 현재 시간을 넣어줌
    private Date regDate;               // 주문 등록 날짜
    private long count;                 // 주문에 추가한 재고 수

    // User 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User orderUser;

    // Stock 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "stock_id")
    @JsonIgnore
    private Stock orderStock;
}
