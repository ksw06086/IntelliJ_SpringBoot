package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.*;
import com.suncloth.suncloth.repository.*;
import com.suncloth.suncloth.repository.querydsl.BoardRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class BoardApiController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ClothRepository clothRepository;
    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private final BoardRepositoryImpl boardRepositoyImpl;
    @Autowired
    private final BoardFileRepository boardFileRepository;

    BoardApiController(UserRepository userRepository
            , BoardRepository boardRepository
            , BoardRepositoryImpl boardRepositoyImpl
            , BoardFileRepository boardFileRepository
            , ClothRepository clothRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.boardRepositoyImpl = boardRepositoyImpl;
        this.boardFileRepository = boardFileRepository;
        this.clothRepository = clothRepository;
    }

    // GET : Board 테이블 정보 가져오기
    @GetMapping("/boards")
    Map<String, Object> all(@RequestParam(required = false) String searchType       // 검색 타입
            , @RequestParam(required = false) String searchInput                    // 검색 TextInput value
            , @RequestParam(required = false) String writeState                     // 답변상태
            , @RequestParam(required = false) String contentState                   // 문의구분 및 분류
            , @RequestParam(required = false) String firstDay                       // 시작 날짜
            , @RequestParam(required = false) String lastDay                        // 끝 날짜
            , @RequestParam(required = false) String boardState                     // 페이지 이름
            , @RequestParam(required = false) Long clothId                          // 상품 ID
            , @PageableDefault(size = 10) Pageable pageable) {                      // 페이지 객체
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        User user = null;
        Cloth cloth = null;
        if(boardState == "NOTICE" || boardState == "FAQ") {
            user = userRepository.findByUsername("host");
        } else if(boardState == "MY BOARD"){
            user = userRepository.findByUsername(username);
        }
        if(clothId != null && clothId != 0){
            cloth = clothRepository.findById(clothId).orElse(null);
        }

        Page<Board> boardList = boardRepositoyImpl.findSearchAll(searchType, searchInput, user, writeState, contentState, firstDay, lastDay, boardState, cloth, pageable);
        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, boardList.getPageable().getPageNumber()-1);
        int endPage = Math.min(boardList.getTotalPages(), boardList.getPageable().getPageNumber()+4);

        // 각 프로퍼티 결과 출력
        log.info("searchType : {}", searchType);
        log.info("searchInput : {}", searchInput);
        log.info("user : {}", user);
        log.info("writeState : {}", writeState);
        log.info("contentState : {}", contentState);
        log.info("firstDay : {}, lastDay : {}", firstDay, lastDay);
        log.info("pageSize : {}", pageable.getPageSize());
        log.info("pageTotal : {}", boardList.getTotalPages());
        log.info("boardPageList.size() : {}", boardList.getTotalElements());
        log.info("boardPageList.getElements() : {}", boardList.getNumberOfElements());
        log.info("boardState : {}", boardState);
        log.info("startPage : {}, endPage : {}", startPage, endPage);

        List<Map<String, Object>> boardMap = new ArrayList<>();
        for (Board board : boardList) {
            boardMap.add(new HashMap<>());
            boardMap.get(boardMap.size()-1).put("board", board);
            List<BoardFile> boardFileList = boardFileRepository.findByBoard(board);
            boardMap.get(boardMap.size()-1).put("boardFileList", boardFileList);
            boardMap.get(boardMap.size()-1).put("boardCloth", board.getBoardCloth());
            boardMap.get(boardMap.size()-1).put("user", board.getBoardUser());
        }

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("contentList", boardMap);         // board와 user의 내용을 쓰기 위한 것
        pageMap.put("pageObject", boardList);         // page 관련 함수를 쓰기 위한 것
        pageMap.put("startPage", startPage);
        pageMap.put("endPage", endPage);
        return pageMap;
    }
    // end::get-aggregate-root[]

    // POST : Board 테이블에 정보 삽입하기
    @PostMapping("/board")
    Board newBoard(Board newBoard
            , @RequestParam(required = false) Long clothId
            , @RequestParam(required = false) Long refBoardNum) throws UnknownHostException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        newBoard.setBoardUser(user);
        newBoard.setIp(InetAddress.getLocalHost().getHostAddress());
        if(newBoard.getNum() > 0) {
            newBoard.setRef(boardRepository.findByMaxRef() + 1);
        }

        if(clothId != null) {
            Cloth cloth = clothRepository.findById(clothId).orElse(null);
            newBoard.setBoardCloth(cloth);
        }
        if(refBoardNum != null && refBoardNum != 0) {
            int updateResult = boardRepository.writeStateUpdateById(refBoardNum);
            Long ref = boardRepository.refById(refBoardNum);
            Long maxRefStep = boardRepository.findByMaxRefStep(refBoardNum);
            newBoard.setRef(ref);
            newBoard.setRefStep(maxRefStep + 1);
            newBoard.setWriteState(" ");
            log.info("updateResult : {}", updateResult);
            log.info("maxRefStep : {}", maxRefStep);
        }

        log.info("newBoard : {}", newBoard.toString());
        log.info("username : {}", username);
        log.info("user : {}", user);
        log.info("refBoardNum : {}", refBoardNum);
        return boardRepository.save(newBoard);
    }

    // Single item

    // GET : userId 에 맞게 Board 정보 가져오기
    @GetMapping("/boards/brand/{userId}")
    List<Board> oneUserAndMany(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return boardRepository.findByBoardUser(user);
    }

    // GET : Id에 맞게 한가지 Board 정보만 가져오기
    @GetMapping("/board/{num}")
    Board one(@PathVariable Long num) {
        return boardRepository.findById(num).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Board 정보만 갱신
    @PutMapping("/board/{num}")
    Board replaceBoard(Board newBoard, @PathVariable Long num) {
        log.info("newBoard : {}, {}, {}, {}, {}, {}, {}, {}", newBoard.getNum(), newBoard.getBoardUser()
                , newBoard.getBoardState(), newBoard.getRef(), newBoard.getRegDate(), newBoard.getSubject(), newBoard.getContent(), newBoard.getContentState());
        return boardRepository.findById(num)
                .map(board -> {
                    log.info("board : {}, {}, {}, {}, {}, {}, {}, {}", board.getNum(), board.getBoardUser()
                            , board.getBoardState(), board.getRef(), board.getRegDate(), board.getSubject(), board.getContent(), board.getContentState());
                    board.setSubject(newBoard.getSubject());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setNum(num);
                    return boardRepository.save(newBoard);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Board 만 삭제
    @DeleteMapping("/boards/{num}")
    void deleteBoard(@PathVariable Long num) {
        boardRepository.deleteById(num);
    }

}
