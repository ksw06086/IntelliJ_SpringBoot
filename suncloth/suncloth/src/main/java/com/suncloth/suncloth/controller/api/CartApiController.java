package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.*;
import com.suncloth.suncloth.repository.*;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class CartApiController {
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
    private final CartRepository cartRepository;

    CartApiController(ColorRepository colorRepository
            , SizeRepository sizeRepository
            , ClothRepository clothRepository
            , StockRepository stockRepository
            , UserRepository userRepository
            , CartRepository cartRepository) {
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.clothRepository = clothRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    // GET : Cart 테이블 정보 가져오기
    @GetMapping("/carts")
    List<Cart> all(@RequestParam(required = false, defaultValue = "") String state) {
        return cartRepository.findAll();
    }
    // end::get-aggregate-root[]

    // POST : Cart 테이블에 정보 삽입하기
    @PostMapping("/cart")
    Cart newCart(Cart newCart, Long stockId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);
        Stock stock = stockRepository.findById(stockId).orElse(null);

        newCart.setCartUser(user);
        newCart.setCartStock(stock);

        System.out.println("cart : " + newCart.toString());
        return cartRepository.save(newCart);
    }

    // POST : Id에 맞게 한가지 Cart 정보만 갱신
    @PostMapping("/newCart")
    Cart updateCart(Cart newCart, Long stockId, String username) {

        return cartRepository.findById(newCart.getCartNum())
                .map(cart -> {
                    Stock stock = stockRepository.findById(stockId).orElse(null);
                    User user = userRepository.findByUsername(username);
                    cart.setCartStock(stock);
                    cart.setCartUser(user);
                    cart.setCount(newCart.getCount());
                    return cartRepository.save(cart);
                })
                .orElseGet(() -> {
                    return cartRepository.save(newCart);
                });
    }

    // Single item

    // GET : stockId 에 맞게 Cart 정보 가져오기
    @GetMapping("/carts/stock/{stockId}")
    List<Cart> oneStockAndMany(@PathVariable Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElse(null);
        return cartRepository.findByCartStock(stock);
    }

    // GET : username 에 맞게 Cart 정보 가져오기
    @GetMapping("/carts/user")
    List<Cart> oneUserAndMany(@RequestParam(required = false) String username) {
        User user = userRepository.findByUsername(username);

        return cartRepository.findByCartUser(user);
    }

    // PUT : Id에 맞게 한가지 Cart 정보만 갱신
    @PutMapping("/cart/{cartNum}")
    Cart replaceCart(@RequestBody Cart newCart,@RequestBody Long stockId,@RequestBody String username, @PathVariable Long cartNum) {

        return cartRepository.findById(cartNum)
                .map(cart -> {
                    Stock stock = stockRepository.findById(stockId).orElse(null);
                    User user = userRepository.findByUsername(username);
                    cart.setCartStock(stock);
                    cart.setCartUser(user);
                    cart.setCount(newCart.getCount());
                    return cartRepository.save(cart);
                })
                .orElseGet(() -> {
                    newCart.setCartNum(cartNum);
                    return cartRepository.save(newCart);
                });
    }

    // 로그인 된 사용자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Cart 만 삭제
    @Secured("ROLE_USER")
    @DeleteMapping("/carts/{cartNum}")
    void deleteCart(@PathVariable Long cartNum) {
        cartRepository.deleteById(cartNum);
    }

}
