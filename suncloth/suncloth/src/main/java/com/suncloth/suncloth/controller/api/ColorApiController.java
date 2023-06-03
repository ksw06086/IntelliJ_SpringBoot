package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Color;
import com.suncloth.suncloth.repository.ColorRepository;
import com.suncloth.suncloth.repository.ColorRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ColorApiController {
    @Autowired
    private final ColorRepository repository;

    ColorApiController(ColorRepository repository) {
        this.repository = repository;
    }

    // GET : Color 테이블 정보 가져오기
    @GetMapping("/colors")
    List<Color> all(@RequestParam(required = false, defaultValue = "") String colorName) {
        if(StringUtils.isEmpty(colorName)){
            return repository.findAll();
        } else {
            return repository.findByColorName(colorName);
        }
    }
    // end::get-aggregate-root[]

    // POST : Color 테이블에 정보 삽입하기
    @PostMapping("/color")
    Color newColor(@RequestBody Color newColor) {
        System.out.println(newColor);
        return repository.save(newColor);
    }

    // Single item

    // GET : Id에 맞게 한가지 Color 정보만 가져오기
    @GetMapping("/colors/{colorCode}")
    Color one(@PathVariable Long colorCode) {

        return repository.findById(colorCode).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Color 정보만 갱신
    @PutMapping("/colors/{colorCode}")
    Color replaceColor(@RequestBody Color newColor, @PathVariable Long colorCode) {

        return repository.findById(colorCode)
                .map(color -> {
                    color.setColorName(newColor.getColorName());
                    return repository.save(color);
                })
                .orElseGet(() -> {
                    newColor.setColorCode(colorCode);
                    return repository.save(newColor);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Color 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/colors/{colorCode}")
    void deleteColor(@PathVariable Long colorCode) {
        repository.deleteById(colorCode);
    }
}
