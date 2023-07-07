package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시글 제목을 기준으로 정보들 가져오기(where subject = ?1)
    List<Board> findBySubject(String subject);

    // User 를 기준으로 정보들 가져오기(where user_id = ?1)
    List<Board> findByBoardUser(User user);

    // BoardState 를 기준으로 정보들 가져오기(where board_state = ?1)
    Page<Board> findByBoardStateOrderByNum(String boardState, Pageable pageable);

    // BoardState 를 기준으로 정보들 가져오기(where board_state = ?1)
    Page<Board> findByBoardClothAndBoardState(Cloth cloth, String boardState, Pageable pageable);

    // Board 이전글의 대한 정보 가져오기
    @Query(value = "SELECT * FROM board_tbl " +
            "WHERE num = (SELECT prev_no FROM (SELECT num, LAG(num, 1, -1) OVER(ORDER BY num) AS prev_no FROM board_tbl " +
            "WHERE board_state = :boardState) B " +
            "WHERE num = :num)", nativeQuery = true)
    Board findByBeforeBoard(Long num, String boardState);

    // Board 다음글의 대한 정보 가져오기
    @Query(value = "SELECT * FROM board_tbl " +
            "WHERE num = (SELECT prev_no FROM (SELECT num, LEAD(num, 1, -1) OVER(ORDER BY num) AS prev_no FROM board_tbl " +
            "WHERE board_state = :boardState) B " +
            "WHERE num = :num)", nativeQuery = true)
    Board findByAfterBoard(Long num, String boardState);

    // Q&A : 해당 글의 refStep Max값 가져오기
    @Query("""
    select nvl(max(b.refStep),0)
    from Board b
    where b.ref = :num
    """)
    Long findByMaxRefStep(Long num);

    Long countByBoardState(String boardState);

    Long countByBoardStateAndRegDateAfter(String boardState, Date regDate);

}
