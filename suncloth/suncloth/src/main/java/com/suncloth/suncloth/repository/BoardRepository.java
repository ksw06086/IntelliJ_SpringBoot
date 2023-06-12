package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.*;
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
    List<Board> findByBoardState(String boardState);

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
