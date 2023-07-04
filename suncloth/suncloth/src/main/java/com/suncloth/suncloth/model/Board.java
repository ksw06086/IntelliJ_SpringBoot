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
    private long num;                       // 게시글 식별번호
    private String contentState;            // 게시글 카테고리
    private String pwd;                     // Q&A : 문의 관련 상태 표시
    private String subject;                 // 게시글 제목
    private String content = " ";           // 게시글 내용
    private String textType;                // Q&A : 비밀글 여부
    private long ref = 0;                   // 참조하는 글의 번호
    private long refStep = 0;               // 답변이 몇번째 답변인지 확인(참조글의 max(Step) +1 해줌)
    private long refLevel = 0;              // 몇번째 글의 답변인지 확인(참조글 Level에 +1 해줌)
    private String ip;                      // 게시글 작성 IP 주소
    private String writeState = "답변대기";  // Q&A : 답변 상태
    private String boardState;              // 게시글 상태
    private long readCnt = 0;               // 방문횟수
    @CreationTimestamp                      // insert 될 때 현재 시간을 넣어줌
    private Date regDate;                   // 게시글 등록 날짜

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
