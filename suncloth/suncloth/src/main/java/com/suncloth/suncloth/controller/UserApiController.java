package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {
    @Autowired
    private final UserRepository repository;

    public UserApiController(UserRepository repository) {
        this.repository = repository;
    }

    // 모든 Guest 정보 불러오기
    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method,
                       @RequestParam(required = false, defaultValue = "") String text) {
        // return repository.findAll();
        // : 사용자를 먼저 조회, 사용자에 해당하는 board들을 조회함 => user 100개 있으면 101개의 sql이 조회가 될 것임
        Iterable<User> users = null;
        // nativeQuery  : users = repository.findByUsernameNativeQuery(text);
        // mybatis      : users = userMapper.getUsers(text);
        users = repository.findAll(); // => 이건 board를 지금 사용안해서 조회 안함
        return users;
    }
    // end::get-aggregate-root[]

    // Guest 추가
    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    // Guest 1명 데이터 불러오기
    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    // Guest 1명 ID명으로 조회하기
    @GetMapping("/userCheck/{username}")
    User idCheck(@PathVariable String username) {

        return repository.findByUsername(username);
    }

    // Guest 1명 ID명으로 수정하기
    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    /*user.setTitle(newUser.getTitle());
                    user.setContent(newUser.getContent());*/
                    /*user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());*/
                    user = newUser;
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    // Guest 1명 삭제하기
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
