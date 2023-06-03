package com.suncloth.suncloth.controller.api;

import com.suncloth.suncloth.model.Size;
import com.suncloth.suncloth.repository.SizeRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class SizeApiController {
    @Autowired
    private final SizeRepository repository;

    SizeApiController(SizeRepository repository) {
        this.repository = repository;
    }

    // GET : Size 테이블 정보 가져오기
    @GetMapping("/sizes")
    List<Size> all(@RequestParam(required = false, defaultValue = "") String sizeName) {
        if(StringUtils.isEmpty(sizeName)){
            return repository.findAll();
        } else {
            return repository.findBySizeName(sizeName);
        }
    }
    // end::get-aggregate-root[]

    // POST : Size 테이블에 정보 삽입하기
    @PostMapping("/size")
    Size newSize(@RequestBody Size newSize) {
        System.out.println(newSize);
        return repository.save(newSize);
    }

    // Single item

    // GET : Id에 맞게 한가지 Size 정보만 가져오기
    @GetMapping("/sizes/{sizeCode}")
    Size one(@PathVariable Long sizeCode) {

        return repository.findById(sizeCode).orElse(null);
    }

    // PUT : Id에 맞게 한가지 Size 정보만 갱신
    @PutMapping("/sizes/{sizeCode}")
    Size replaceSize(@RequestBody Size newSize, @PathVariable Long sizeCode) {

        return repository.findById(sizeCode)
                .map(size -> {
                    size.setSizeName(newSize.getSizeName());
                    return repository.save(size);
                })
                .orElseGet(() -> {
                    newSize.setSizeCode(sizeCode);
                    return repository.save(newSize);
                });
    }

    // 관리자일 경우에만 삭제가 가능하고, ID에 맞는 한가지의 Size 만 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/sizes/{sizeCode}")
    void deleteSize(@PathVariable Long sizeCode) {
        repository.deleteById(sizeCode);
    }
}
