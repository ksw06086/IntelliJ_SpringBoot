package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import oracle.sql.TIMESTAMP;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.spi.CalendarDataProvider;

@Data
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
}
