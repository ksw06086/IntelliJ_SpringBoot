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
    private final BoardRepository boardRepository;
    @Autowired
    private final BoardRepositoryImpl boardRepositoyImpl;

    BoardApiController(UserRepository userRepository
            , BoardRepository boardRepository
            , BoardRepositoryImpl boardRepositoyImpl) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.boardRepositoyImpl = boardRepositoyImpl;
    }

    // GET : Board 테이블 정보 가져오기
    @GetMapping("/boards")
    Map<String, Object> all(@RequestParam(required = false) String searchType       // 검색 타입
            , @RequestParam(required = false) String searchInput            // 검색 TextInput value
            , @RequestParam(required = false) Long userId                   // 유저 식별자
            , @RequestParam(required = false) String firstDay               // 시작 날짜
            , @RequestParam(required = false) String lastDay                // 끝 날짜
            , @PageableDefault(size = 3) Pageable pageable) {               // 페이지 객체
        User user = userRepository.findById(userId).orElse(null);

        Page<Board> boardList = boardRepositoyImpl.findSearchAll(searchType, searchInput, user, firstDay, lastDay, pageable);
        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, boardList.getPageable().getPageNumber()-4);
        int endPage = Math.min(boardList.getTotalPages(), boardList.getPageable().getPageNumber()+4);

        // 각 프로퍼티 결과 출력
        System.out.println("searchType : " + searchType + ", searchInput : " + searchInput +
                ", user : " + user + ", firstDay : " + firstDay + ", lastDay : " + lastDay + ", pageable : " + pageable);

        List<Map<String, Object>> boardMap = new ArrayList<>();
        for (Board board : boardList) {
            boardMap.add(new HashMap<>());
            boardMap.get(boardMap.size()-1).put("board", board);
            boardMap.get(boardMap.size()-1).put("user", board.getBoardUser());
        }

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("contentList", boardMap);
        pageMap.put("pageObject", boardList);
        pageMap.put("startPage", startPage);
        pageMap.put("endPage", endPage);
        return pageMap;
    }
    // end::get-aggregate-root[]

    // POST : Board 테이블에 정보 삽입하기
    @PostMapping("/board")
    Board newBoard(Board newBoard, String username
            , @RequestParam(required = false) Long refBoardNum) throws UnknownHostException {
        User user = userRepository.findByUsername(username);
        newBoard.setBoardUser(user);
        newBoard.setIp(InetAddress.getLocalHost().getHostAddress());

        if(refBoardNum != null && refBoardNum != 0) {
            Long maxRefStep = boardRepository.findByMaxRefStep(refBoardNum);
            newBoard.setRefStep(maxRefStep + 1);
            System.out.println("maxRefStep : " + maxRefStep);
        }

        System.out.println("newBoard : " + newBoard);
        System.out.println("username : " + username);
        System.out.println("user : " + user);
        System.out.println("refBoardNum : " + refBoardNum);
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
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long num) {

        return boardRepository.findById(num)
                .map(board -> {
                    board.setSubject(newBoard.getSubject());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setNum(num);
                    return boardRepository.save(newBoard);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Board 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/boards/{num}")
    void deleteBoard(@PathVariable Long num) {
        boardRepository.deleteById(num);
    }

}
