package com.suncloth.suncloth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "board_file_tbl")
@Entity
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;        // 이미지파일 식별변호
    private String orgNm;       // 기존 파일 이름
    private String savedNm;     // 저장 파일 이름
    private String savedPath;   // 파일 저장 경로

    // Cloth 테이블과 연결
    // name : 나의 외래키 컬럼, referencedColumnName(생략가능) : 상대의 primary 컬럼
    @ManyToOne
    @JoinColumn(name = "board_num")
    @JsonIgnore
    private Board board;

    // 생성자
    @Builder
    public BoardFile(Long fileId, String orgNm, String savedNm, String savedPath, Board board) {
        this.fileId = fileId;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
        this.board = board;
    }
}
