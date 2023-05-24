package com.suncloth.suncloth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

@Controller
public class HomeController {
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
