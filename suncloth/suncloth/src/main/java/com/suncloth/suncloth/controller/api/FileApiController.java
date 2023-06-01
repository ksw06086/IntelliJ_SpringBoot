package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import com.suncloth.suncloth.model.MainCategory;
import com.suncloth.suncloth.model.SubCategory;
import com.suncloth.suncloth.repository.ClothRepository;
import com.suncloth.suncloth.repository.FileRepository;
import com.suncloth.suncloth.repository.MainCategoryRepository;
import com.suncloth.suncloth.repository.SubCategoryRepository;
import com.suncloth.suncloth.service.FileService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileApiController {
    @Autowired
    private final FileService fileService;

    @Autowired
    private final FileRepository fileRepository;

    @Autowired
    private final ClothRepository clothRepository;

    FileApiController(FileRepository fileRepository, ClothRepository clothRepository, FileService fileService) {
        this.fileRepository = fileRepository;
        this.clothRepository = clothRepository;
        this.fileService = fileService;
    }

    // GET : File 테이블 정보 가져오기
    @GetMapping("/files")
    List<File> all(@RequestParam(required = false, defaultValue = "") String orgNm) {
        if(StringUtils.isEmpty(orgNm)){
            return fileRepository.findAll();
        } else {
            return fileRepository.findByOrgNm(orgNm);
        }
    }
    // end::get-aggregate-root[]

    /*
    // 트랜잭션 어노테이션 -> 예외 발생시 함수의 모든 데이터 SQL 반영 안됨
    // 제약이 있음 - 어노테이션 사용시 주의점
    // 1. 동일한 클래스의 트랜잭션 메소드를 호출시 트랜잭션 적용이 안됨
    // 2. public 메소드에만 적용됨
    // 3. runtimeException만 rollback이 됨, 다른 에러가 나도 rollback이 되게 하고 싶으면 rollbackFor, rollbackForClassName = {}에 해당 예외클래스 추가
    // https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
    // * propagation : 해당 함수 내 트랜잭션 메소드를 호출했을 경우 범위 지정
    // + propagation.require : 이전에 트랜잭션 없었으면 새로 시작하고 있으면 이전거 그대로 가게 함
    // * isolation : 여러명이 같이 사용할 때 데이터베이스의 값을 얼마나 안전하게 보장해줄 것인가?
    // + .DEFAULT - database에 기본값 따라감

    A table - id, age : 1, 10
    <.READ_UNCOMMITTED>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> rollback
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-20 -> select: id-1, age-10
    <.READ COMMITTED>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> commit
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-10 -> select: id-1, age-20
    <.REPEATABLE READ>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> commit
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-10 -> select: id-1, age-10

    // TIMEOUT : SQL이 일정시간안에 처리가 안되면 에러 발생
    */
    // POST : File 들을 테이블에 정보 삽입하기
    @Transactional
    @PostMapping("/files")
    public String newFiles(@RequestParam("mainImage") MultipartFile file
            , @RequestParam(value = "subImages", required = false) List<MultipartFile> files) {
        try {
            fileService.saveFile(file, "main");
            System.out.println(file);

            if(files != null) {
                for (MultipartFile multipartFile : files) {
                    fileService.saveFile(multipartFile, "sub");
                    System.out.println(multipartFile);
                }
            }
            return "성공";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // PUT : cloth_id에 맞게 한가지 file 정보 삽입하기
    @PutMapping("/file/{cloth_id}")
    File insertFile(@RequestBody File newFile, @PathVariable Long cloth_id) {
        Cloth cloth = clothRepository.findById(cloth_id).orElse(null);
        newFile.setCloth(cloth);
        return fileRepository.save(newFile);
    }

    // Single item

    // GET : cloth_id 에 맞는 File들 정보만 가져오기
    @GetMapping("/files/{cloth_id}")
    List<File> oneAndMany(@PathVariable Long cloth_id) {
        Cloth cloth = clothRepository.findById(cloth_id).orElse(null);
        return fileRepository.findByCloth(cloth);
    }

    // GET : Id에 맞게 한가지 File 정보만 가져오기
    @GetMapping("/file/{fileId}")
    File one(@PathVariable Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }

    // PUT : Id에 맞게 한가지 File 정보만 갱신
    @PutMapping("/files/{fileId}")
    File replaceFile(@RequestBody File newFile, @PathVariable Long fileId) {

        return fileRepository.findById(fileId)
                .map(file -> {
                    file.setOrgNm(newFile.getOrgNm());
                    file.setSavedNm(newFile.getSavedNm());
                    file.setSavedPath(newFile.getSavedPath());
                    return fileRepository.save(file);
                })
                .orElseGet(() -> {
                    newFile.setFileId(fileId);
                    return fileRepository.save(newFile);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 File 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/files/{fileId}")
    void deleteFile(@PathVariable Long fileId) {
        fileRepository.deleteById(fileId);
    }
}
