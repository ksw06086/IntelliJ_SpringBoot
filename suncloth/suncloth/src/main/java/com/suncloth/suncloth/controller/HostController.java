package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.*;
import com.suncloth.suncloth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    // *** Repository 목록들 ***//
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    MainCategoryRepository mainCategoryRepository;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    ClothRepository clothRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardFileRepository boardFileRepository;

    /* 상품 */
    // 상품 리스트
    @GetMapping("/productList")
    public String productList(Model model
            , @PageableDefault(size = 3)Pageable pageable) {
        List<Brand> brands = brandRepository.findAll();
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        Page<Cloth> cloths = clothRepository.findAll(pageable);

        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, cloths.getPageable().getPageNumber()-1);
        int endPage = Math.min(cloths.getTotalPages(), cloths.getPageable().getPageNumber()+4);

        model.addAttribute("brandList", brands);
        model.addAttribute("mainCategoryList", mainCategories);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("clothList", cloths);

        return "/host/product/host_productList";
    }
    // 상품 등록
    @GetMapping("/productInput")
    public String productInput(Model model
            , @RequestParam(required = false) Long clothId) {
        List<Brand> brands = brandRepository.findAll();
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        Cloth cloth = new Cloth();
        List<File> mainFile = null;
        List<File> subFile = new ArrayList<>();
        List<SubCategory> subCategoryList = new ArrayList<>();
        if(clothId != null) {
            cloth = clothRepository.findById(clothId).orElse(null);
            mainFile = fileRepository.findByClothAndFileType(cloth, "main");
            subFile = fileRepository.findByClothAndFileType(cloth, "sub");
            subCategoryList = subCategoryRepository.findByMainCategory((cloth != null)?cloth.getSubCategory().getMainCategory():null);
        }

        model.addAttribute("brandList", brands);
        model.addAttribute("mainCategoryList", mainCategories);
        model.addAttribute("cloth", cloth);
        model.addAttribute("mainFile", mainFile);
        model.addAttribute("subFile", subFile);
        model.addAttribute("subCategoryList", subCategoryList);

        return "/host/product/host_productInput";
    }

    /* 재고 */
    // 재고 리스트
    @GetMapping("/stockList")
    public String stockList(Model model
            , @RequestParam(required = false) long clothId) {
        Cloth cloth = clothRepository.findById(clothId).orElse(null);
        List<Stock> stocks = stockRepository.findByStockCloth(cloth);
        List<Color> colors = colorRepository.findAll();
        List<Size> sizes = sizeRepository.findAll();

        model.addAttribute("cloth", cloth);
        model.addAttribute("stockList", stocks);
        model.addAttribute("colorList", colors);
        model.addAttribute("sizeList", sizes);

        return "/host/product/host_stockList";
    }
    // 재고 등록
    @GetMapping("/stockInput")
    public String stockInput(Model model
            , @RequestParam(required = false) Long clothId
            , @RequestParam(required = false) Long stockId) {
        List<Color> colors = colorRepository.findAll();
        List<Size> sizes = sizeRepository.findAll();
        Cloth cloth = clothRepository.findById(clothId).orElse(null);
        Stock stock = new Stock();
        if(stockId != null) {
            stock = stockRepository.findById(stockId).orElse(null);
        }
        model.addAttribute("stock", stock);
        model.addAttribute("colorList", colors);
        model.addAttribute("sizeList", sizes);
        model.addAttribute("cloth", cloth);

        return "/host/product/host_stockInput";
    }

    /* 브랜드 */
    // 브랜드 목록
    @GetMapping("/brandList")
    public String brandList(Model model
            , @PageableDefault(size = 3)Pageable pageable) {
        Page<Brand> brandList = brandRepository.findAll(pageable);
        // 현재 아래 바를 1~5까지 보여주게 하기 위해서 끝에 4를 빼고 더해준 것
        int startPage = Math.max(1, brandList.getPageable().getPageNumber()-4);
        int endPage = Math.min(brandList.getTotalPages(), brandList.getPageable().getPageNumber()+4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("brandList", brandList);
        return "/host/product/host_brandList";
    }
    // 브랜드 등록/수정
    @GetMapping("/brandInput")
    public String brandInput(Model model
            , @RequestParam(required = false) String name
            , @RequestParam(required = false) Long brandId) {
        Brand brand;
        if(brandId == null) { brand = new Brand(); }
        else { brand = brandRepository.findById(brandId).orElse(null); }
        model.addAttribute("brand", brand);
        model.addAttribute("name", name);
        return "/host/product/host_brandInput";
    }

    /* 주문 */
    // 주문 목록
    @GetMapping("/orderList")
    public String orderList() {
        return "/host/order/host_orderList";
    }

    /* 회원 */
    // 회원 목록
    @GetMapping("/memberList")
    public String memberList(Model model) {
        List<User> userList = userRepository.findByUsernameNot("host");
        model.addAttribute("userList", userList);
        return "/host/member/host_memberList";
    }
    // 회원 수정
    @GetMapping("/memberView")
    public String memberView(Model model
            , @RequestParam(required = false) String username) {
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "/host/member/host_memberView";
    }

    /* 운영 */
    // 모든 게시판 리스트
    @GetMapping("/boardAllList")
    public String boardAllList(Model model) {
        // 오늘 날짜
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        Date today = new Date(calendar.get(Calendar.YEAR)-1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        // 오늘 추가된 게시물 개수
        model.addAttribute("noticeNewCnt", boardRepository.countByBoardStateAndRegDateAfter("NOTICE", today));
        model.addAttribute("FAQNewCnt", boardRepository.countByBoardStateAndRegDateAfter("FAQ", today));
        model.addAttribute("QnANewCnt", boardRepository.countByBoardStateAndRegDateAfter("Q&A", today));
        model.addAttribute("reviewNewCnt", boardRepository.countByBoardStateAndRegDateAfter("REVIEW", today));

        // 총 게시물 개수
        model.addAttribute("noticeCnt", boardRepository.countByBoardState("NOTICE"));
        model.addAttribute("FAQCnt", boardRepository.countByBoardState("FAQ"));
        model.addAttribute("QnACnt", boardRepository.countByBoardState("Q&A"));
        model.addAttribute("reviewCnt", boardRepository.countByBoardState("REVIEW"));

        return "/host/board/host_boardAllList";
    }
    // 게시판 리스트
    @GetMapping("/boardList")
    public String boardList(Model model
            , @RequestParam(required = false) String name
            , @PageableDefault(size = 3) Pageable pageable) {
        Page<Board> boardList = boardRepository.findByBoardStateOrderByNum(name, pageable);
        model.addAttribute("name", name);
        model.addAttribute("boardList", boardList);
        return "/host/board/host_boardList";
    }
    // 게시판 등록/수정
    @GetMapping("/boardInput")
    public String boardInput(Model model
            , @RequestParam(required = false) String name
            , @RequestParam(required = false) Long boardNum
            , @RequestParam(required = false) Long userBoardNum) {
        Board board = new Board();
        List<BoardFile> files = new ArrayList<>();
        if(boardNum != null) {
            board = boardRepository.findById(boardNum).orElse(null);
            files = boardFileRepository.findByBoard(board);
        }
        if(board.getRef() != 0 || userBoardNum != null){
            Board userBoard = boardRepository.findById(userBoardNum).orElse(null);
            model.addAttribute("userBoard", userBoard);
        }

        model.addAttribute("board", board);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        return "/host/board/host_boardInput";
    }

    /* 통계 */
    // 방문자 횟수 분석
    @GetMapping("/clickTotal")
    public String clickTotal() {
        return "/host/total/host_clickTotal";
    }
    // 신규회원 분석
    @GetMapping("/newMemberTotal")
    public String newMemberTotal() {
        return "/host/total/host_newMemberTotal";
    }
    // 회원 적립금 분석
    @GetMapping("/memberPlusPay")
    public String memberPlusPay() {
        return "/host/total/host_memberPlusPay";
    }
    // 카테고리 분석
    @GetMapping("/category")
    public String category() {
        return "/host/total/host_category";
    }
    // 판매순위 분석
    @GetMapping("/saleRank")
    public String saleRank() {
        return "/host/total/host_saleRank";
    }
    // 주문 통계
    @GetMapping("/orderTotal")
    public String orderTotal() {
        return "/host/total/host_orderTotal";
    }
    // 매출 통계
    @GetMapping("/saleTotal")
    public String saleTotal() {
        return "/host/total/host_saleTotal";
    }
}
