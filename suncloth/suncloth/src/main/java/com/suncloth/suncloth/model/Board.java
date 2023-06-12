package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.net.InetAddress;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@Entity
@Table(name="board_tbl")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // => 자동증가
    private long num;               // 상품 식별번호
    private String contentState;           // 상품명
    private String pwd;       // 상품 설명
    private String subject;                // 아이콘명
    private String content = " ";
    private String textType;
    private long ref = 0;               // 참조하는 글의 번호
    private long refStep = 0;           // 답변이 몇번째 답변인지 확인(참조글의 max(Step) +1 해줌)
    private long refLevel = 0;          // 몇번째 글의 답변인지 확인(참조글 Level에 +1 해줌)
    private String ip;
    private String writeState = "답변대기";
    private String  boardState;
    @CreationTimestamp                  // insert 될 때 현재 시간을 넣어줌
    private Date regDate;               // 상품 등록 날짜

    // User 와 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User boardUser;


    // Cloth 와 연결된 File(이미지) 들
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BoardFile> boardFiles = new ArrayList<>();

}
