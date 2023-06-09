package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oracle.sql.TIMESTAMP;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.spi.CalendarDataProvider;

/*
 * 최초 작성자 : 김선우
 * 최초 작성일 : 2023.04.02
 * 최초 변경일 : 2023.06.27
 * 목적 : 쇼핑몰 사용자 내역 저장
 * 개정 이력 : 김선우 - 2023.04.02, 사용자 Model 생성
 *                   2023.06.27, 주문 Model 연결
 * 저작권 : 김선우
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user_tbl")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long id;                // Guest 식별번호
    private String username;        // ID
    private String password;        // Password
    private String name;            // Name
    private String addressNum;      // Address Num
    private String addressSub;      // Address Sub
    private String addressDetail;   // Address Detail
    private String hp;              // HP
    private String emailIdName;     // Email idName
    private String emailUrlCode;    // Email
    @CreationTimestamp              // insert 될 때 현재 시간을 넣어줌
    private Date regDate;           // Register Date
    private Date birthDay = null;   // BirthDay
    private String birthType = null;// BirthType
    private int usablePlus;         // 현재 User가 사용가능한 적립금
    private int visitCnt;           // 방문 횟수
    private String hostMemo;        // 관리자 메모
    private int enabled;            // User 권한

    // 권한 테이블과 조인함
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    // cascade = user가 삭제되면 RefundAccount에서도 해당 user의 데이터 다 삭제됨
    /* orphanRemoval = true : orphan은 고아라는 뜻인데 부모 없는 값은 다 지움,
       곧 Board 클래스에 부모가 없으면 다 삭제하라는 것인데 부모가 clear를 해주어서 부모가 없는 값이 됨
       user.setBoards(newUser.getBoards());
       => user.getBoards().clear();
          user.getBoards().addAll(newUser.getBoards()); */
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Board클래스의 user 변수 사용하겠다.
    /* *** FetchType ***
       + EAGER : 사용자 정보 가져올 때 board까지 가져옴(@OneToOne, @ManyToOne)
       + LAZY : board를 사용할 때만 데이터 조회가 됨(@OneToMany, @ManyToMany) */
    // 환불 Account 테이블과 조인
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private RefundAccount refundAccounts;

    // 게시판 Board 테이블과 조인
    @OneToMany(mappedBy = "boardUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();

    // 장바구니 Cart 테이블과 조인
    @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    // 주문 Order 테이블과 조인
    @OneToMany(mappedBy = "orderUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();
}
