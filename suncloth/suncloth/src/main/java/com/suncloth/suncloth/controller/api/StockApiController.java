package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.Color;
import com.suncloth.suncloth.model.Size;
import com.suncloth.suncloth.model.Stock;
import com.suncloth.suncloth.repository.*;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class StockApiController {
    @Autowired
    private final ColorRepository colorRepository;
    @Autowired
    private final SizeRepository sizeRepository;
    @Autowired
    private final ClothRepository clothRepository;
    @Autowired
    private final StockRepository stockRepository;

    StockApiController(ColorRepository colorRepository
            , SizeRepository sizeRepository
            , ClothRepository clothRepository
            , StockRepository stockRepository) {
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.clothRepository = clothRepository;
        this.stockRepository = stockRepository;
    }

    // GET : Stock 테이블 정보 가져오기
    @GetMapping("/stocks")
    List<Stock> all(@RequestParam(required = false, defaultValue = "") String state) {
        if(StringUtils.isEmpty(state)){
            return stockRepository.findAll();
        } else {
            return stockRepository.findByState(state);
        }
    }
    // end::get-aggregate-root[]

    // POST : Stock 테이블에 정보 삽입하기
    @PostMapping("/stock")
    Stock newStock(Stock newStock, Long colorCode, Long sizeCode, Long clothId) {
        Color color = colorRepository.findById(colorCode).orElse(null);
        Size size = sizeRepository.findById(sizeCode).orElse(null);
        Cloth cloth = clothRepository.findById(clothId).orElse(null);

        newStock.setStockColor(color);
        newStock.setStockSize(size);
        newStock.setStockCloth(cloth);

        System.out.println("stock : " + newStock);
        return stockRepository.save(newStock);
    }

    // Single item

    // GET : clothId 에 맞게 Stock 정보 가져오기
    @GetMapping("/stocks/cloth/{clothId}")
    List<Stock> oneClothAndMany(@PathVariable Long clothId) {
        Cloth cloth = clothRepository.findById(clothId).orElse(null);
        return stockRepository.findByStockCloth(cloth);
    }

    // GET : colorCode 에 맞게 Stock 정보 가져오기
    @GetMapping("/stocks/color/{colorCode}")
    List<Stock> oneColorAndMany(@PathVariable Long colorCode) {
        Color color = colorRepository.findById(colorCode).orElse(null);
        return stockRepository.findByStockColor(color);
    }

    // GET : sizeCode 에 맞게 Stock 정보 가져오기
    @GetMapping("/stocks/size/{sizeCode}")
    List<Stock> oneSizeAndMany(@PathVariable Long sizeCode) {
        Size size = sizeRepository.findById(sizeCode).orElse(null);
        return stockRepository.findByStockSize(size);
    }

    // GET : Id에 맞게 한가지 Stock 정보만 가져오기
    @GetMapping("/stock/{stockId}")
    Stock one(@PathVariable Long stockId) {
        return stockRepository.findById(stockId).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Stock 정보만 갱신
    @PutMapping("/stock/{stockId}")
    Stock replaceStock(@RequestBody Stock newStock, @PathVariable Long stockId) {

        return stockRepository.findById(stockId)
                .map(stock -> {
                    stock.setStockCount(newStock.getStockCount());
                    stock.setStockMaxCount(newStock.getStockMaxCount());
                    stock.setBuyPrice(newStock.getBuyPrice());
                    stock.setSalePrice(newStock.getSalePrice());
                    stock.setDeliDay(newStock.getDeliDay());
                    stock.setDeliPrice(newStock.getDeliPrice());
                    stock.setPlus(newStock.getPlus());
                    stock.setTex(newStock.getTex());
                    stock.setState(newStock.getState());
                    return stockRepository.save(stock);
                })
                .orElseGet(() -> {
                    newStock.setStockId(stockId);
                    return stockRepository.save(newStock);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Stock 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/stocks/{stockId}")
    void deleteStock(@PathVariable Long stockId) {
        stockRepository.deleteById(stockId);
    }

}
