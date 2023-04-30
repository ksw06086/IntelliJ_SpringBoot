package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 제목기준으로 정보들 가져오기(where title = ?1)
    List<Board> findByTitle(String title);

    // 제목 또는 내용 기준으로 정보들 가져오기(where title = ?1 or content = ?2)
    List<Board> findByTitleOrContent(String title, String content);

    // 해당 text가 제목 또는 내용에 포함되는 Board 가져오기 
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

}
