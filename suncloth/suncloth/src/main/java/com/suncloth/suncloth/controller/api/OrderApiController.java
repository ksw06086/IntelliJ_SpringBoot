package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Order;
import com.suncloth.suncloth.model.Stock;
import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    OrderApiController(ColorRepository colorRepository
            , SizeRepository sizeRepository
            , ClothRepository clothRepository
            , StockRepository stockRepository
            , UserRepository userRepository
            , OrderRepository orderRepository) {
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.clothRepository = clothRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // GET : Order 테이블 정보 가져오기
    @GetMapping("/orders")
    List<Order> all(@RequestParam(required = false, defaultValue = "") String state) {
        return orderRepository.findAll();
    }
    // end::get-aggregate-root[]

    // POST : Order 테이블에 정보 삽입하기
    @PostMapping("/order")
    Order newOrder(Order newOrder, Long stockId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);
        Stock stock = stockRepository.findById(stockId).orElse(null);

        newOrder.setOrderUser(user);
        newOrder.setOrderStock(stock);

        System.out.println("order : " + newOrder.toString());
        return orderRepository.save(newOrder);
    }

    // POST : Id에 맞게 한가지 Order 정보만 갱신
    @PostMapping("/newOrder")
    Order updateOrder(Order newOrder, Long stockId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        return orderRepository.findById(newOrder.getOrderId())
                .map(order -> {
                    Stock stock = stockRepository.findById(stockId).orElse(null);
                    User user = userRepository.findByUsername(username);
                    order.setOrderStock(stock);
                    order.setOrderUser(user);
                    order.setCount(newOrder.getCount());
                    return orderRepository.save(order);
                })
                .orElseGet(() -> {
                    return orderRepository.save(newOrder);
                });
    }

    // Single item

    // GET : stockId 에 맞게 Order 정보 가져오기
    @GetMapping("/orders/stock/{stockId}")
    List<Order> oneStockAndMany(@PathVariable Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElse(null);
        return orderRepository.findByOrderStock(stock);
    }

    // GET : username 에 맞게 Order 정보 가져오기
    @GetMapping("/orders/user")
    List<Order> oneUserAndMany(@RequestParam(required = false) String username) {
        User user = userRepository.findByUsername(username);

        return orderRepository.findByOrderUser(user);
    }

    // PUT : Id에 맞게 한가지 Order 정보만 갱신
    @PutMapping("/order/{orderId}")
    Order replaceOrder(@RequestBody Order newOrder,@RequestBody Long stockId,@RequestBody String username, @PathVariable Long orderId) {

        return orderRepository.findById(orderId)
                .map(order -> {
                    Stock stock = stockRepository.findById(stockId).orElse(null);
                    User user = userRepository.findByUsername(username);
                    order.setOrderStock(stock);
                    order.setOrderUser(user);
                    order.setCount(newOrder.getCount());
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
