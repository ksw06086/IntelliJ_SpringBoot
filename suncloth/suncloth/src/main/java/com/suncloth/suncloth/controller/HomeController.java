package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.BoardFile;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.File;
import com.suncloth.suncloth.repository.BoardFileRepository;
import com.suncloth.suncloth.repository.BoardRepository;
import com.suncloth.suncloth.repository.ClothRepository;
import com.suncloth.suncloth.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {


    @Autowired
    ClothRepository clothRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    BoardFileRepository boardFileRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    /* 여기가 로그인 후에 들어오는 곳이기에 여기서
    ADMIN과 USER를 나누어서 페이지를 보내주어야 함 */
    @GetMapping("/main")
    public String main() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증된 사용자의 권한 목록을 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();
            // 권한명에 따른 처리 로직 작성
            if (roleName.equals("ROLE_ADMIN")) {
                // ADMIN 권한에 해당하는 동작 수행
                return "host_main";
            } else if (roleName.equals("ROLE_USER")) {
                // USER 권한에 해당하는 동작 수행
                return "main";
            }
        }

        // 기본 페이지 반환 (권한이 없는 경우 등)
        return "main";
    }

    // 사용자 메인 페이지
    @GetMapping("/userMain")
    public String userMain() {
        return "main";
    }

    // 관리자 메인 페이지
    @GetMapping("/hostMain")
    public String hostMain() {
        return "host_main";
    }

    // clothId로 메인 이미지 출력
    @GetMapping("/uploadMainImageView/{clothId}")
    @ResponseBody
    public Resource uploadMainImage(@PathVariable("clothId") Long id) throws IOException {
        Cloth cloth = clothRepository.findById(id).orElse(null);
        List<File> file = fileRepository.findByClothAndFileType(cloth, "main");
        return new UrlResource("file:" + file.get(0).getSavedPath());
    }

    // fileId로 이미지 출력
    @GetMapping("/uploadImageView/{fileId}")
    @ResponseBody
    public Resource uploadImage(@PathVariable("fileId") Long id) throws IOException {
        File file = fileRepository.findById(id).orElse(null);
        return new UrlResource("file:" + file.getSavedPath());
    }


    // fileId로 게시판 이미지 출력
    @GetMapping("/uploadBoardImageView/{fileId}")
    @ResponseBody
    public Resource uploadBoardImage(@PathVariable("fileId") Long id) throws IOException {
        BoardFile boardFile = boardFileRepository.findById(id).orElse(null);
        return new UrlResource("file:" + boardFile.getSavedPath());
    }


    // sc5Form
//    @GetMapping("/sc5Form")
//    public String sc5Form(Model model) {
//        model.addAttribute("bookDTO", new BookDTO());
//        return "/post/sc5Form";
//    }
//    // sc5View
//    @PostMapping("/sc5View")
//    public String sc5View(BookDTO bookDTO, Model model) {
//        // model에 데이터 추가
//        model.addAttribute("heading", "Book Information");
//
//        // userDTO 객체를 통해서 출력
//        System.out.println("---------------------------------");
//        System.out.println( bookDTO.getTitle() );
//        System.out.println( bookDTO.getAuthor() );
//        System.out.println( bookDTO.getPublisher() );
//        System.out.println( bookDTO.getDate() );
//        System.out.println("---------------------------------");
//        return "/post/sc5View";
//    }
}
