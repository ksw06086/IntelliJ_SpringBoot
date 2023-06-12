package com.suncloth.suncloth.service;

import com.suncloth.suncloth.model.BoardFile;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import com.suncloth.suncloth.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

// 파일은 최대한 절대경로를 선호(C, D 드라이브)
// 이유 : 보안이 좋고 재배포시 파일 유실이 안되기 때문
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    private final ResourceLoader resourceLoader;

    private final FileRepository fileRepository;

    // 상품 이미지 업로드
    public File saveFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        File file = File.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new java.io.File(savedPath));

        return file;
    }

    // 게시판 이미지 업로드
    public BoardFile saveBoardFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        BoardFile boardFile = BoardFile.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new java.io.File(savedPath));

        return boardFile;
    }
}
