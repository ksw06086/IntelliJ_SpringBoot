package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Board;
import com.suncloth.suncloth.model.BoardFile;
import com.suncloth.suncloth.repository.BoardRepository;
import com.suncloth.suncloth.repository.BoardFileRepository;
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
public class BoardFileApiController {
    @Autowired
    private final FileService fileService;

    @Autowired
    private final BoardFileRepository boardFileRepository;

    @Autowired
    private final BoardRepository boardRepository;

    BoardFileApiController(BoardFileRepository boardFileRepository, BoardRepository boardRepository, FileService fileService) {
        this.boardFileRepository = boardFileRepository;
        this.boardRepository = boardRepository;
        this.fileService = fileService;
    }

    // GET : BoardFile 테이블 정보 가져오기
    @GetMapping("/boardFiles")
    List<BoardFile> all(@RequestParam(required = false, defaultValue = "") String orgNm) {
        if(StringUtils.isEmpty(orgNm)){
            return boardFileRepository.findAll();
        } else {
            return boardFileRepository.findByOrgNm(orgNm);
        }
    }
    // GET : board_num 에 맞는 BoardFile들 정보만 가져오기
    @GetMapping("/boardFiles/{board_num}")
    List<BoardFile> oneAndMany(@PathVariable Long board_num) {
        Board board = boardRepository.findById(board_num).orElse(null);
        return boardFileRepository.findByBoard(board);
    }
    // end::get-aggregate-root[]

    // POST : BoardFile 들을 테이블에 정보 삽입하기
    @Transactional
    @PostMapping("/boardFiles")
    public String newBoardFiles(@RequestParam(value = "boardImages", required = false) List<MultipartFile> boardFiles
            , @RequestParam(value = "boardNum", required = false) long boardNum) {
        try {
            Board board = boardRepository.findById(boardNum).orElse(null);

            if (boardFiles != null) {
                for (MultipartFile multipartFile : boardFiles) {
                    BoardFile subBoardFile = fileService.saveBoardFile(multipartFile);
                    subBoardFile.setBoard(board);
                    boardFileRepository.save(subBoardFile);
                }
            }
            return "성공";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // PUT : board_num에 맞게 한가지 boardFile 정보 삽입하기
    @PutMapping("/boardFile/{board_num}")
    BoardFile insertBoardFile(@RequestBody BoardFile newBoardFile, @PathVariable Long board_num) {
        Board board = boardRepository.findById(board_num).orElse(null);
        newBoardFile.setBoard(board);
        return boardFileRepository.save(newBoardFile);
    }

    // Single item

    // GET : Id에 맞게 한가지 BoardFile 정보만 가져오기
    @GetMapping("/boardFile/{boardFileId}")
    BoardFile one(@PathVariable Long boardFileId) {
        return boardFileRepository.findById(boardFileId).orElse(null);
    }

    // PUT : Id에 맞게 한가지 BoardFile 정보만 갱신
    @PutMapping("/boardFiles/{boardFileId}")
    BoardFile replaceBoardFile(@RequestBody BoardFile newBoardFile, @PathVariable Long boardFileId) {

        return boardFileRepository.findById(boardFileId)
                .map(boardFile -> {
                    boardFile.setOrgNm(newBoardFile.getOrgNm());
                    boardFile.setSavedNm(newBoardFile.getSavedNm());
                    boardFile.setSavedPath(newBoardFile.getSavedPath());
                    return boardFileRepository.save(boardFile);
                })
                .orElseGet(() -> {
                    newBoardFile.setFileId(boardFileId);
                    return boardFileRepository.save(newBoardFile);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 BoardFile 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/boardFiles/{boardFileId}")
    void deleteBoardFile(@PathVariable Long boardFileId) {
        boardFileRepository.deleteById(boardFileId);
    }
}
