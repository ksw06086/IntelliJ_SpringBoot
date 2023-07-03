package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.Board;
import com.suncloth.suncloth.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardRepository boardRepository;

    @GetMapping("/boardList")
    public String boardList(Model model
            , @RequestParam(required = false) String name) {
        List<Board> boardList = boardRepository.findByBoardState(name);
        model.addAttribute("boardList", boardList);
        model.addAttribute("name", name);
        return "/board/guest_boardList";
    }

    @GetMapping("/boardView")
    public String boardView(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/board/guest_boardView";
    }

    @GetMapping("/boardWrite")
    public String boardWrite(Model model
            , @RequestParam(required = false) String name) {
        model.addAttribute("name", name);
        return "/board/guest_boardWrite";
    }
}
