package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.*;
import com.suncloth.suncloth.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * 최초 작성자 : 김선우
 * 최초 작성일 : 2023.06.27
 * 최초 변경일 : 2023.06.27
 * 목적 : 쇼핑몰 주문 관련 RestAPI 컨트롤러
 * 개정 이력 : 김선우 - 2023.06.03, 주문 RestAPI 컨트롤러 생성
 * 저작권 : 김선우
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class OrderApiController {
    @Autowired
    private final ColorRepository colorRepository;
    @Autowired
    private final SizeRepository sizeRepository;
    @Autowired
    private final ClothRepository clothRepository;
    @Autowired
    private final StockRepository stockRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderStockRepository orderStockRepository;

    OrderApiController(ColorRepository colorRepository
            , SizeRepository sizeRepository
            , ClothRepository clothRepository
            , StockRepository stockRepository
            , UserRepository userRepository
            , OrderRepository orderRepository
            , OrderStockRepository orderStockRepository) {
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.clothRepository = clothRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderStockRepository = orderStockRepository;
    }

    // GET : Order 테이블 정보 가져오기
    @GetMapping("/orders")
    List<Order> all(@RequestParam(required = false, defaultValue = "") String state) {
        return orderRepository.findAll();
    }
    // end::get-aggregate-root[]

    /*
     * 목적 : POST - Order 테이블에 정보 삽입하기(Insert)
     * 매개변수 : newOrder(주문 Model)
     *           stockId(재고 아이디)
     * 반환 값 : Order(주문 Model)
     * 변경 이력 : 김선우, 2023.06.27(ver. 01)
     */
    @PostMapping("/order")
    Order newOrder(Order newOrder
            , @RequestParam(required = false, defaultValue = "") String imp_uid
            , @RequestParam(required = false, defaultValue = "") List<Long> stockIdList
            , @RequestParam(required = false, defaultValue = "") List<Long> stockCountList) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);
        newOrder.setOrderUser(user);

        for (int i = 0; i < stockIdList.size(); i++) {
            Stock stock = stockRepository.findById(stockIdList.get(i)).orElse(null);
            newOrder.getOrderStockList().add(stock);
        }

        log.info("stockIds : {}", stockIdList.size());
        log.info("imp_uid : {}", imp_uid);
        log.info("주문번호 : {}", newOrder.getMerchantUid());
        log.info("주문상태 : {}", newOrder.getOrderState());

        List<OrderStock> orderStockList = orderStockRepository.findByOrderId(6L);
        log.info("orderStockList.size : {}", orderStockList.size());


        return orderRepository.save(newOrder);

        /*Stock stock = stockRepository.findById(stockId).orElse(null);

        newOrder.setOrderUser(user);
        newOrder.setOrderStock(stock);

        System.out.println("order : " + newOrder.toString());
        return orderRepository.save(newOrder);*/
    }

    // POST : Id에 맞게 한가지 Order 정보만 갱신(Update)
    @PostMapping("/newOrder")
    Order updateOrder(Order newOrder) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        return orderRepository.findById(newOrder.getOrderId())
                .map(order -> {
                    User user = userRepository.findByUsername(username);
                    order.setOrderUser(user);
                    return orderRepository.save(order);
                })
                .orElseGet(() -> {
                    return orderRepository.save(newOrder);
                });
    }

    /*
     * 목적 : POST - Order 결제 금액이 DB와 맞는지 확인하기 위함
     * 매개변수 : stockIdList(stock 아이디 목록)
     *           totalOrderPriceView(orderForm.html 의 주문 총 예정 금액)
     *           useMileage(orderForm.html 의 고객이 사용할 적립금)
     * 반환 값 : String(정상/위조된 결제시도)
     * 변경 이력 : 김선우, 2023.06.28(ver. 01)
     */
    @PostMapping("/order/payments_verification")
    String paymentsVerification(@RequestParam(required = false, defaultValue = "") List<Long> stockIdList
            , @RequestParam(required = false, defaultValue = "") List<Long> countList
            , @RequestParam(required = false, defaultValue = "") Long finishTotalPriceView
            , @RequestParam(required = false, defaultValue = "") Long useMileage) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);

        long stockTotalSalePrice = 0;       // DB Stock 테이블의 totalSalePrice
        for (int i = 0; i < stockIdList.size(); i++) {
            Stock stock = stockRepository.findById(stockIdList.get(i)).orElse(null);
            stockTotalSalePrice += (stock.getSalePrice() * countList.get(i) + stock.getStockCloth().getDeliPrice());
        }

        log.info("orderForm 사용 적립금 : {}", useMileage);
        log.info("사용 가능 적립금 : {}", user.getUsablePlus());
        log.info("orderForm 주문 최종 금액 : {}", finishTotalPriceView);
        log.info("주문 최종 금액 : {}", (stockTotalSalePrice - useMileage));

        if(useMileage > user.getUsablePlus()){
            return "사용 적립금 위조된 결제시도";
        } else if(finishTotalPriceView != (stockTotalSalePrice - useMileage)){
            return "주문 최종금액 위조된 결제시도";
        } else {
            return "정상 결제시도";
        }
    }

    // Single item

    // GET : stockId 에 맞게 Order 정보 가져오기
    /*@GetMapping("/orders/stock/{stockId}")
    List<Order> oneStockAndMany(@PathVariable Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElse(null);
        return orderRepository.findByOrderStock(stock);
    }*/

    // GET : username 에 맞게 Order 정보 가져오기
    @GetMapping("/orders/user")
    List<Order> oneUserAndMany(@RequestParam(required = false) String username) {
        User user = userRepository.findByUsername(username);

        return orderRepository.findByOrderUser(user);
    }

    // GET : Id 중 가장 높은 값 가져오기
    @GetMapping("/order/maxId")
    Long oneOrderMaxId() {
        Long maxId = orderRepository.findByOrderMaxId();
        log.info("{}", maxId);
        return maxId;
    }

    // PUT : Id에 맞게 한가지 Order 정보만 갱신
    @PutMapping("/order/{orderId}")
    Order replaceOrder(@RequestBody Order newOrder,@RequestBody String username, @PathVariable Long orderId) {

        return orderRepository.findById(orderId)
                .map(order -> {
                    User user = userRepository.findByUsername(username);
                    order.setOrderUser(user);
                    return orderRepository.save(order);
                })
                .orElseGet(() -> {
                    newOrder.setOrderId(orderId);
                    return orderRepository.save(newOrder);
                });
    }

    // 로그인 된 사용자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Order 만 삭제
    @DeleteMapping("/orders/{orderId}")
    void deleteOrder(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
    }

}
