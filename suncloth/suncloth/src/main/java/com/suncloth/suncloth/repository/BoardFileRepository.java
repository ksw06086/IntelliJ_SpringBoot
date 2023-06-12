package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Board;
import com.suncloth.suncloth.model.BoardFile;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    // 기존 파일 명을 기준으로 정보들 가져오기(where org_nm = ?1)
    List<BoardFile> findByOrgNm(String orgNm);

    // cloth_id를 기준으로 정보들 가져오기(where cloth_id = ?1)
    List<BoardFile> findByBoard(Board board);
}
