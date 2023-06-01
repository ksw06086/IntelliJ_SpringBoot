package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    // 기존 파일 명을 기준으로 정보들 가져오기(where org_nm = ?1)
    List<File> findByOrgNm(String orgNm);

    // cloth_id를 기준으로 정보들 가져오기(where cloth_id = ?1)
    List<File> findByCloth(Cloth cloth);
}
