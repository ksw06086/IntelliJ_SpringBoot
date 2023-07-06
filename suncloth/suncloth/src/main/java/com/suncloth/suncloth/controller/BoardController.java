package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.Board;
import com.suncloth.suncloth.model.BoardFile;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.repository.BoardFileRepository;
import com.suncloth.suncloth.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardFileRepository boardFileRepository;

    @GetMapping("/boardList")
    public String boardList(Model model
            , @RequestParam(required = false) String name
            , @PageableDefault(size = 10) Pageable pageable) {
        Page<Board> boardList = boardRepository.findByBoardState(name, pageable);

        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, boardList.getPageable().getPageNumber()-1);
        int endPage = Math.min(boardList.getTotalPages(), boardList.getPageable().getPageNumber()+4);



        model.addAttribute("boardList", boardList);
        model.addAttribute("name", name);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/guest_boardList";
    }

    @GetMapping("/boardView")
    public String boardView(Model model
            , @RequestParam(required = false) String name
            , @RequestParam(required = false) Long num) {
        Board board = boardRepository.findById(num).orElse(null);
        List<BoardFile> boardFileList = boardFileRepository.findByBoard(board);
        model.addAttribute("boardFileList", boardFileList);
        model.addAttribute("board", board);
        model.addAttribute("name", name);
        return "/board/guest_boardView";
    }

    @GetMapping("/boardWrite")
    public String boardWrite(Model model
            , @RequestParam(required = false) Long boardNum
            , @RequestParam(required = false) String name) {
        Board board = new Board();
        if(name.equals("REVIEW")) {
            board.setContent("※리뷰작성 시 최대 적립금 2,000원 지급!(50자 이상 리뷰 작성시)\n" +
                    "(일반리뷰 500원 / 상품포토리뷰 1000 / 상품착용포토리뷰(키,몸무게,구매사이즈(컬러) '작성필수') 2,000원)\n\n" +
                    "키(cm):\n" + "몸무게(kg):\n" + "구매사이즈(컬러):\n" + "상품리뷰:");
        }
        List<BoardFile> files = new ArrayList<>();
        if(boardNum != null) {
            board = boardRepository.findById(boardNum).orElse(null);
            files = boardFileRepository.findByBoard(board);
        }

        model.addAttribute("board", board);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        return "/board/guest_boardWrite";
    }
}
